package com.hard;

import com.hard.models.User;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    private Server server;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private User user;

    public Client(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        user = new User();
    }

    @Override
    public void run() {
        initStreams();

        user.setUsername(this.toString().substring(getClass().getName().length() + 1, this.toString().length()));

        server.notifyAllClients("[" + user.getUsername() + "] has joined to the chat");

        while (true) {
            String str = read();

            System.out.println("[" + user.getUsername() + "]:" + str);

            if (str.equalsIgnoreCase("/exit")) {
                server.notifyAllClients("[" + user.getUsername() + "] has left the chat");
                break;
            }

            server.notifyAllClients("[" + user.getUsername() + "]:" + str);
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
