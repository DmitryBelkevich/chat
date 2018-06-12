package com.hard;

import com.hard.views.ConsoleView;
import com.hard.views.FrameView;
import com.hard.views.View;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Client {
    // views
    private Collection<View> views;

    private String host = "localhost";//"18.219.167.139";
    private int port = 9999;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Client() {
        views = new ArrayList<>();

        views.add(new ConsoleView(this));
        views.add(new FrameView(this));
    }

    public void run() {
        for (View view : views)
            view.run();

        init();
        initStreams();

        while (true) {
            try {
                if (inputStream.available() > 0) {
                    String str = read();

                    notifyAllViews(str);
                }

                if (false)
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void notifyAllViews(String str) {
        for (View view : views)
            view.getMessage(str);
    }
}
