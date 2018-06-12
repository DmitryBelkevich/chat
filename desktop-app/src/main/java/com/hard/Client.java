package com.hard;

import com.hard.views.FrameView;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private FrameView frameView;

    private String host = "localhost";//"18.219.167.139";
    private int port = 9999;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Client() {
        frameView = new FrameView(this);
    }

    public void run() {
        frameView.run();

        init();
        initStreams();

        new Thread(() -> {
            while (true) {
                try {
                    if (inputStream.available() > 0) {System.out.println("x");
                        String str = read();

                        System.out.println(str);
                        frameView.getMessage(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            write(str);

            if (str.equalsIgnoreCase("/exit"))
                break;
        }

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

    private String read() {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String result = null;
        try {
            result = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
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
}
