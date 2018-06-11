package com.hard;

import com.hard.views.View;

import java.io.*;
import java.net.Socket;

public class Client {
    private View view = new View();

    private String host = "localhost";//"18.219.167.139";
    private int port = 9999;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void run() {
        view.run();

        init();
        initStreams();

        write("Hello World");

        String str = readData();
        System.out.println(str);

        stop();
    }

    private void init() {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStreams() {
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

    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readData() {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String result = null;
        try {
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void write(String str) {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeUTF(str);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
