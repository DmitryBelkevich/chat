package com.hard;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Server server;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private String username;

    public Client(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        initStreams();

        username = this.toString().substring(getClass().getName().length() + 1, this.toString().length());
        server.notifyAllClients("[" + username + "] has joined to the chat");

        while (true) {
            String str = read();

            System.out.println("[" + username + "]:" + str);

            if (str.equalsIgnoreCase("/exit"))
                break;

            server.notifyAllClients("[" + username + "]:" + str);
        }

        stop();
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

        server.remove(this);
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
            stop();
        }
    }
}
