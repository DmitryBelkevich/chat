package com.hard;

import com.hard.models.Entity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    private Socket socket;
    private OutputStream outputStream;

    // entities
    private Entity entity;

    public Server() {
        entity = new Entity("Hello World");
    }

    public void run() {
        init();
        System.out.println("launched");

        try {
            System.out.println("listening...");
            socket = serverSocket.accept();
            System.out.println("accepted client");
        } catch (IOException e) {
            e.printStackTrace();
        }

        initStreams();

        write();

        stop();
        System.out.println("stopped");
    }

    private void init() {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStreams() {
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

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write() {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeUTF(entity.toString());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
