package com.hard.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Handshaker {
    private static final String CRLF = "\r\n";

    public static void handshake(InputStream inputStream, OutputStream outputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter(CRLF + CRLF);

        String requestHeaders = scanner.next();
        String responseHeaders = getResponseHeaders(requestHeaders);

        byte[] responseHeadersBytes = new byte[0];
        try {
            responseHeadersBytes = responseHeaders.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            outputStream.write(responseHeadersBytes, 0, responseHeadersBytes.length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getResponseHeaders(String requestHeaders) {
        Matcher getMatcher = Pattern.compile("^GET").matcher(requestHeaders);

        if (!getMatcher.find())
            return null;

        Matcher secWebSocketKeyMatcher = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(requestHeaders);
        secWebSocketKeyMatcher.find();

        String secWebSocketKey = secWebSocketKeyMatcher.group(1);
        String secWebSocketAccept = evaluateSecWebSocketAccept(secWebSocketKey);

        String responseHeaders = "HTTP/1.1 101 Switching Protocols" + CRLF
                + "Connection: Upgrade" + CRLF
                + "Upgrade: websocket" + CRLF
                + "Sec-WebSocket-Accept: " + secWebSocketAccept + CRLF
                + CRLF;

        return responseHeaders;
    }

    private static String evaluateSecWebSocketAccept(String secWebSocketKey) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String concatenatedString = secWebSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

        byte[] concatenatedStringBytes = new byte[0];
        try {
            concatenatedStringBytes = concatenatedString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] sha1Bytes = messageDigest.digest(concatenatedStringBytes);

        Base64.Encoder encoder = Base64.getEncoder();

        byte[] secWebSocketAcceptBytes = encoder.encode(sha1Bytes);

        return new String(secWebSocketAcceptBytes);
    }
}
