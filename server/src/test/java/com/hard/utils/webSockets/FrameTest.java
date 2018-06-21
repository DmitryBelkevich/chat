package com.hard.utils.webSockets;

import org.junit.Assert;
import org.junit.Test;

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
        String str = "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890";

        byte[] bytes = {
                // fin, rsv1, rsv2, rsv3, opCode
                -127,

                // mask, payLoad length
                -28,

                // maskingKey
                -18, 44, -30, -115,

                // payLoad

                -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
                -33, 30, -47, -71, -37, 26, -43, -75, -41, 28, -45, -65, -35, 24, -41, -69, -39, 20, -37, -67,
        };

        Assert.assertEquals(100, str.length());
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
        String str = "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890";

        byte[] bytes = {
                // fin, rsv1, rsv2, rsv3, opCode
                -127,

                // mask, payLoad length
                -2,
                // extended payLoadLength
                0, -56,

                // maskingKey
                -79, -71, -121, 123,

                // payLoad
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
        };

        Assert.assertEquals(200, str.length());
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
