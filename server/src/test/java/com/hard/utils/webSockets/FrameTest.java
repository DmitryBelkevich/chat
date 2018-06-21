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

        int n = 100;
        String s = "1234567890";
        for (int i = 0; i < n / s.length(); i++) {
            stringBuilder.append(s);
        }

        String str = stringBuilder.toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // fin, rsv1, rsv2, rsv3, opCode
            byteArrayOutputStream.write(-127);

            // mask, payLoad length
            byteArrayOutputStream.write(-28);

            // maskingKey
            byteArrayOutputStream.write(new byte[]{
                    -18, 44, -30, -115,
            });

            // payLoad
            byteArrayOutputStream.write(new byte[]{
                    -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                    -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                    -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                    -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                    -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
            });
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
    public void test5() {
        StringBuilder stringBuilder = new StringBuilder();

        int n = 200;
        String s = "1234567890";
        for (int i = 0; i < n / s.length(); i++) {
            stringBuilder.append(s);
        }

        String str = stringBuilder.toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // fin, rsv1, rsv2, rsv3, opCode
            byteArrayOutputStream.write(-127);

            // mask, payLoad length
            byteArrayOutputStream.write(-2);

            // extended payLoadLength
            byteArrayOutputStream.write(new byte[]{0, -56});

            // maskingKey
            byteArrayOutputStream.write(new byte[]{
                    -79, -71, -121, 123,
            });

            // payLoad
            byteArrayOutputStream.write(new byte[]{
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
                    -128, -117, -76, 79, -124, -113, -80, 67, -120, -119, -74, 73, -126, -115, -78, 77, -122, -127, -66, 75,
            });
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
}
