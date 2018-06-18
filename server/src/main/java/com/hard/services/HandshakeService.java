package com.hard.services;

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

public class HandshakeService {
    private static final String CRLF = "\r\n";

    public void handshake(InputStream inputStream, OutputStream outputStream) {
        String requestHeaders = getRequestHeaders(inputStream);

        validation(requestHeaders);

        String secWebSocketKey = getSecWebSocketKey(requestHeaders);
        String secWebSocketAccept = evaluateSecWebSocketAccept(secWebSocketKey);

        String responseHeaders = generateResponseHeaders(secWebSocketAccept);

        try {
            outputStream.write(responseHeaders.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequestHeaders(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter(CRLF + CRLF);

        String requestHeaders = scanner.next();

        return requestHeaders;
    }

    private void validation(String requestHeaders) {
        Matcher getMatcher = Pattern.compile("^GET").matcher(requestHeaders);
        if (!getMatcher.find())
            throw new RuntimeException("Not found GET header");

        Matcher upgradeMatcher = Pattern.compile("Upgrade: (.*)").matcher(requestHeaders);
        if (!upgradeMatcher.find())
            throw new RuntimeException("Not found Upgrade header");

        Matcher connectionMatcher = Pattern.compile("Connection: (.*)").matcher(requestHeaders);
        if (!connectionMatcher.find())
            throw new RuntimeException("Not found Connection header");

        Matcher secWebSocketKeyMatcher = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(requestHeaders);
        if (!secWebSocketKeyMatcher.find())
            throw new RuntimeException("Not found Sec-WebSocket-Key header");
    }

    private String getSecWebSocketKey(String requestHeaders) {
        Matcher secWebSocketKeyMatcher = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(requestHeaders);
        secWebSocketKeyMatcher.find();

        String secWebSocketKey = secWebSocketKeyMatcher.group(1);

        return secWebSocketKey;
    }

    private String generateResponseHeaders(String secWebSocketAccept) {
        String responseHeaders = "HTTP/1.1 101 Switching Protocols" + CRLF
                + "Connection: Upgrade" + CRLF
                + "Upgrade: websocket" + CRLF
                + "Sec-WebSocket-Accept: " + secWebSocketAccept + CRLF
                + CRLF;

        return responseHeaders;
    }

    private String evaluateSecWebSocketAccept(String secWebSocketKey) {
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

        String secWebSocketAccept = Base64.getEncoder().encodeToString(sha1Bytes);

        return secWebSocketAccept;
    }
}
