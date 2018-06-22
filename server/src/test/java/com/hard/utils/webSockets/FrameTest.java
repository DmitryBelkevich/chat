package com.hard.utils.webSockets;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FrameTest {
    public static class Server_to_Client {
        /**
         * payLoadLength < 126
         * from 0 to 125
         *
         * mask = false
         * masking = null
         */
        @Test
        public void test1() {
            StringBuilder stringBuilder = new StringBuilder();

            int n = 125;

            for (int i = 0; i < n; i++) {
                stringBuilder.append(i % 10);
            }

            String str = stringBuilder.toString();

            Frame frame = new Frame();

            frame.setFin(true);
            frame.setRsv1(false);
            frame.setRsv2(false);
            frame.setRsv3(false);
            frame.setOpCode((byte) 0x1);
            frame.setMask(false);
            frame.setPayLoadLength(str.getBytes().length);
            frame.setMaskingKey(null);
            frame.setPayLoad(str.getBytes());

            byte[] bytes = frame.asBytes();

            Assert.assertEquals(n, str.length());
            Assert.assertEquals(2 + str.length(), bytes.length);

            Frame frame2 = Frame.parse(bytes);

            Assert.assertEquals(true, frame2.isFin());
            Assert.assertEquals(false, frame2.isRsv1());
            Assert.assertEquals(false, frame2.isRsv2());
            Assert.assertEquals(false, frame2.isRsv3());
            Assert.assertEquals(0x1, frame2.getOpCode());
            Assert.assertEquals(false, frame2.isMask());
            Assert.assertEquals(str.length(), frame2.getPayLoadLength());
            Assert.assertEquals(null, frame2.getMaskingKey());
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2, bytes.length), frame2.getPayLoad()));
        }

        /**
         * payLoadLength == 126
         * from 126 to (2^(2*8))
         *
         * mask = false
         * masking = null
         */
        @Test
        public void test2() {
            StringBuilder stringBuilder = new StringBuilder();

            int n = 126;

            for (int i = 0; i < n; i++) {
                stringBuilder.append(i % 10);
            }

            String str = stringBuilder.toString();

            Frame frame = new Frame();

            frame.setFin(true);
            frame.setRsv1(false);
            frame.setRsv2(false);
            frame.setRsv3(false);
            frame.setOpCode((byte) 0x1);
            frame.setMask(false);
            frame.setPayLoadLength(str.getBytes().length);
            frame.setMaskingKey(null);
            frame.setPayLoad(str.getBytes());

            byte[] bytes = frame.asBytes();

            Assert.assertEquals(n, str.length());
            Assert.assertEquals(2 + 2 + str.length(), bytes.length);

            Frame frame2 = Frame.parse(bytes);

            Assert.assertEquals(true, frame2.isFin());
            Assert.assertEquals(false, frame2.isRsv1());
            Assert.assertEquals(false, frame2.isRsv2());
            Assert.assertEquals(false, frame2.isRsv3());
            Assert.assertEquals(0x1, frame2.getOpCode());
            Assert.assertEquals(false, frame2.isMask());
            Assert.assertEquals(str.length(), frame2.getPayLoadLength());
            Assert.assertEquals(null, frame2.getMaskingKey());
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2 + 2, bytes.length), frame2.getPayLoad()));
        }

        /**
         * payLoadLength == 127
         * from (2^(2*8)) to (2^(8*8))
         */
        @Test
        public void test3() {

        }
    }

    public static class Client_to_Server {
        /**
         * payLoadLength < 126
         * from 0 to 125
         */
        @Test
        public void test1() {
            StringBuilder stringBuilder = new StringBuilder();

            int n = 125;

            for (int i = 0; i < n; i++) {
                stringBuilder.append(i % 10);
            }

            String str = stringBuilder.toString();

            byte[] maskingKey = {-18, 44, -30, -115,};
            byte[] payLoad = Encoder.encode(str.getBytes(), maskingKey);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                // fin, rsv1, rsv2, rsv3, opCode
                byteArrayOutputStream.write(-127);

                // mask, payLoad length
                byteArrayOutputStream.write(1 << 7 | payLoad.length); // -28

                // maskingKey
                byteArrayOutputStream.write(maskingKey);

                // payLoad
                byteArrayOutputStream.write(payLoad);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] bytes = byteArrayOutputStream.toByteArray();

            Assert.assertEquals(n, str.length());
            Assert.assertEquals(2 + 4 + str.length(), bytes.length);

            Frame frame = Frame.parse(bytes);

            Assert.assertEquals(true, frame.isFin());
            Assert.assertEquals(false, frame.isRsv1());
            Assert.assertEquals(false, frame.isRsv2());
            Assert.assertEquals(false, frame.isRsv3());
            Assert.assertEquals(0x1, frame.getOpCode());
            Assert.assertEquals(true, frame.isMask());
            Assert.assertEquals(str.length(), frame.getPayLoadLength());
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2, 2 + 4), frame.getMaskingKey()));
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2 + 4, bytes.length), frame.getPayLoad()));
        }

        /**
         * payLoadLength == 126
         * from 126 to (2^(2*8))
         */
        @Test
        public void test2_255() {
            StringBuilder stringBuilder = new StringBuilder();

            int n = 255;

            for (int i = 0; i < n; i++) {
                stringBuilder.append(i % 10);
            }

            String str = stringBuilder.toString();

            byte[] maskingKey = {-79, -71, -121, 123,};
            byte[] payLoad = Encoder.encode(str.getBytes(), maskingKey);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                // fin, rsv1, rsv2, rsv3, opCode
                byteArrayOutputStream.write(-127);

                // mask, payLoad length
                byteArrayOutputStream.write(1 << 7 | 126);    // -2

                // extended payLoadLength
                byte[] payLoadLength = splitToBytes(str.length(), 2);

                byteArrayOutputStream.write(payLoadLength);// 0, -56

                // maskingKey
                byteArrayOutputStream.write(maskingKey);

                // payLoad
                byteArrayOutputStream.write(payLoad);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] bytes = byteArrayOutputStream.toByteArray();

            Assert.assertEquals(n, str.length());
            Assert.assertEquals(2 + 2 + 4 + str.length(), bytes.length);

            Frame frame = Frame.parse(bytes);

            Assert.assertEquals(true, frame.isFin());
            Assert.assertEquals(false, frame.isRsv1());
            Assert.assertEquals(false, frame.isRsv2());
            Assert.assertEquals(false, frame.isRsv3());
            Assert.assertEquals(0x1, frame.getOpCode());
            Assert.assertEquals(true, frame.isMask());
            Assert.assertEquals(str.length(), frame.getPayLoadLength());
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2 + 2, 2 + 2 + 4), frame.getMaskingKey()));
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2 + 2 + 4, bytes.length), frame.getPayLoad()));
        }


        @Test
        public void test2_256() {
            StringBuilder stringBuilder = new StringBuilder();

            int n = 256;

            for (int i = 0; i < n; i++) {
                stringBuilder.append(i % 10);
            }

            String str = stringBuilder.toString();

            byte[] maskingKey = {-79, -71, -121, 123,};
            byte[] payLoad = Encoder.encode(str.getBytes(), maskingKey);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                // fin, rsv1, rsv2, rsv3, opCode
                byteArrayOutputStream.write(-127);

                // mask, payLoad length
                byteArrayOutputStream.write(1 << 7 | 126);    // -2

                // extended payLoadLength
                byte[] payLoadLength = splitToBytes(str.length(), 2);

                byteArrayOutputStream.write(payLoadLength);// 0, -56

                // maskingKey
                byteArrayOutputStream.write(maskingKey);

                // payLoad
                byteArrayOutputStream.write(payLoad);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] bytes = byteArrayOutputStream.toByteArray();

            Assert.assertEquals(n, str.length());
            Assert.assertEquals(2 + 2 + 4 + str.length(), bytes.length);

            Frame frame = Frame.parse(bytes);

            Assert.assertEquals(true, frame.isFin());
            Assert.assertEquals(false, frame.isRsv1());
            Assert.assertEquals(false, frame.isRsv2());
            Assert.assertEquals(false, frame.isRsv3());
            Assert.assertEquals(0x1, frame.getOpCode());
            Assert.assertEquals(true, frame.isMask());
            Assert.assertEquals(str.length(), frame.getPayLoadLength());
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2 + 2, 2 + 2 + 4), frame.getMaskingKey()));
            Assert.assertTrue(Arrays.equals(Arrays.copyOfRange(bytes, 2 + 2 + 4, bytes.length), frame.getPayLoad()));
        }

        /**
         * payLoadLength == 127
         * from (2^(2*8)) to (2^(8*8))
         */
        @Test
        public void test3() {

        }

        public byte[] splitToBytes(int value, int n) {
            byte[] bytes = new byte[n];

            for (int i = 0; i < n; i++) {
                bytes[n - 1 - i] = (byte) (value >> 8 * i & 0xFF);
            }

            return bytes;
        }
    }
}
