package com.hard.utils.webSockets;

public class Encoder {
    public static byte[] encode(byte[] payload, byte[] key) {
        byte[] encoded = new byte[payload.length];

        for (int i = 0; i < payload.length; i++) {
            encoded[i] = (byte) (payload[i] ^ key[i % key.length]);
        }

        return encoded;
    }
}
