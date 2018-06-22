package com.hard.utils.webSockets;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FrameTest {
    /**
     * Server to Client
     */

    // payLoadLength < 126
    // from 0 to 125
    @Test
    public void test1() {

    }

    // payLoadLength == 126
    // from 126 to (2^(2*8))

    @Test
    public void test2() {

    }

    // payLoadLength == 127
    // from (2^(2*8)) to (2^(8*8))

    @Test
    public void test3() {

    }

    /**
     * Client to Server
     */

    // payLoadLength < 126
    // from 0 to 125
    @Test
    public void test4() {
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

    // payLoadLength == 126
    // from 126 to (2^(2*8))

    @Test
    public void test5_255() {
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
    public void test5_256() {
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

    // payLoadLength == 127
    // from (2^(2*8)) to (2^(8*8))

    @Test
    public void test6() {

    }

    public byte[] splitToBytes(int value, int n) {
        byte[] bytes = new byte[n];

        for (int i = 0; i < n; i++) {
            bytes[n - 1 - i] = (byte) (value >> 8 * i & 0xFF);
        }

        return bytes;
    }
}
