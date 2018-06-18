package com.hard.utils;

public class Decoder {
    public static byte[] decode(byte[] payload, byte[] key) {
        byte[] decoded = new byte[payload.length];

        for (int i = 0; i < payload.length; i++) {
            decoded[i] = (byte) (payload[i] ^ key[i % key.length]);
        }

        return decoded;
    }
}
