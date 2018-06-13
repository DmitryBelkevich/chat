package com.hard.services;

import java.io.*;
import java.net.Socket;

public class ClientService {
    private String host = "localhost";//"18.219.167.139";
    private int port = 9999;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void init() {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initStreams() {
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String result = null;

        while (true) {
            try {
                if (inputStream.available() > 0) {
                    try {
                        result = dataInputStream.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String str) {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeUTF(str);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
