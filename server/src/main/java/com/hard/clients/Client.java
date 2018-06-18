package com.hard.clients;

import com.hard.Server;
import com.hard.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Client implements Runnable {
    protected Server server;

    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    // models
    protected User user;

    public Client(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        user = new User();

        initStreams();
    }

    @Override
    public void run() {
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

    public abstract String read();

    public abstract void write(String str);
}
