package com.hard.utils.webSockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Frame {
    private boolean fin;
    private boolean rsv1;
    private boolean rsv2;
    private boolean rsv3;
    private byte opCode;
    private boolean mask;
    private int payLoadLength;
    private byte[] maskingKey;
    private byte[] payLoad;

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public boolean isRsv1() {
        return rsv1;
    }

    public void setRsv1(boolean rsv1) {
        this.rsv1 = rsv1;
    }

    public boolean isRsv2() {
        return rsv2;
    }

    public void setRsv2(boolean rsv2) {
        this.rsv2 = rsv2;
    }

    public boolean isRsv3() {
        return rsv3;
    }

    public void setRsv3(boolean rsv3) {
        this.rsv3 = rsv3;
    }

    public byte getOpCode() {
        return opCode;
    }

    public void setOpCode(byte opCode) {
        this.opCode = opCode;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }

    public int getPayLoadLength() {
        return payLoadLength;
    }

    public void setPayLoadLength(int payLoadLength) {
        this.payLoadLength = payLoadLength;
    }

    public byte[] getMaskingKey() {
        return maskingKey;
    }

    public void setMaskingKey(byte[] maskingKey) {
        this.maskingKey = maskingKey;
    }

    public byte[] getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public byte[] asBytes() {
        // fin, rsv1, rsv2, rsv3, opCode

        byte byte1 = (byte) ((fin ? 1 : 0) << 7 | (rsv1 ? 1 : 0) | (rsv2 ? 1 : 0) | (rsv3 ? 1 : 0) | opCode);

        // mask, payLoad length

        if (payLoadLength >= 126 && payLoadLength <= 65535) {
            payLoadLength = 126;
        } else if (payLoadLength >= 65536) {
            payLoadLength = 127;
        }

        byte byte2 = (byte) ((mask ? 1 : 0) << 7 | payLoadLength);

        // extended payLoadLength

        byte[] extendedPayLoadLength = {};

        if (payLoadLength == 126) {
            extendedPayLoadLength = splitToBytes(payLoad.length, 2);
        } else if (payLoadLength == 127) {
            extendedPayLoadLength = splitToBytes(payLoad.length, 8);
        }

        payLoadLength = payLoad.length;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(byte1);
            byteArrayOutputStream.write(byte2);
            byteArrayOutputStream.write(extendedPayLoadLength);
            byteArrayOutputStream.write(payLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static Frame parse(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        Frame frame = new Frame();

        byte currentByte;

        // fin, rsv1, rsv2, rsv3, opCode

        currentByte = buffer.get();

        frame.fin = ((currentByte & 0x80) != 0);
        frame.rsv1 = ((currentByte & 0x40) != 0);
        frame.rsv2 = ((currentByte & 0x20) != 0);
        frame.rsv3 = ((currentByte & 0x10) != 0);
        frame.opCode = (byte) (currentByte & 0x0F);

        // mask, payLoad length

        currentByte = buffer.get();

        frame.mask = ((currentByte & 0x80) != 0);
        frame.payLoadLength = (byte) (currentByte & 0x7F);

        int byteCount = 0;
        if (frame.payLoadLength >= 0x0 && frame.payLoadLength <= 0x7D) { // 0 - 125
            // поле "payLoad length" (7 bits)
            byteCount = 0;
        } else if (frame.payLoadLength == 0x7E) {   // 126
            // следующие 2 байта (16 bits) интерпретируются как 16-битное беззнаковое целое число, содержащее длину тела
            // поле "payLoad length" (7 bits + 2 bytes)
            byteCount = 2;
        } else if (frame.payLoadLength == 0x7F) {    // 127
            // следующие 8 байт (64 bits) интерпретируются как 64-битное беззнаковое целое, содержащее длину.
            // поле "payLoad length" (7 bits + 8 bytes)
            byteCount = 8;
        }

        // Decode Payload Length
        byte[] extendedPayLoadLengthBytes = new byte[byteCount];

        for (int i = 0; i < byteCount; i++) {
            extendedPayLoadLengthBytes[i] = buffer.get();
        }

        int extendedPayLoadLength = 0;
        for (int i = extendedPayLoadLengthBytes.length - 1; i > 0; i--) {
            currentByte = extendedPayLoadLengthBytes[i];

            extendedPayLoadLength |= currentByte & 0xFF << (8 * (extendedPayLoadLengthBytes.length - 1 - i));
        }

        if (frame.payLoadLength == 0x7E || frame.payLoadLength == 0x7F) {
            frame.payLoadLength = extendedPayLoadLength;
        }

        // Masking Key

        if (frame.mask) {
            frame.maskingKey = new byte[4];
            buffer.get(frame.maskingKey, 0, 4);
        }

        // Payload itself
        frame.payLoad = new byte[frame.payLoadLength];
        buffer.get(frame.payLoad, 0, frame.payLoadLength);

        // Demask (if needed)
//        if (frame.mask) {
//            for (int i = 0; i < frame.payLoad.length; i++) {
//                frame.payLoad[i] ^= frame.maskingKey[i % 4];
//            }
//        }

        return frame;
    }

    private byte[] splitToBytes(int value, int n) {
        byte[] bytes = new byte[n];

        for (int i = 0; i < n; i++) {
            bytes[n - 1 - i] = (byte) (value >> 8 * i & 0xFF);
        }

        return bytes;
    }

    @Override
    public String toString() {
        return "Frame{" +
                "fin=" + fin +
                ", rsv1=" + rsv1 +
                ", rsv2=" + rsv2 +
                ", rsv3=" + rsv3 +
                ", opCode=" + opCode +
                ", mask=" + mask +
                ", payLoadLength=" + payLoadLength +
                ", maskingKey=" + Arrays.toString(maskingKey) +
                ", payLoad=" + Arrays.toString(payLoad) +
                '}';
    }
}
