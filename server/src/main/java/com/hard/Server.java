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

    // entities
    private Entity entity;

    public Server() {
        entity = new Entity(100, 100, 100, 100);
    }

    public void run() {
        init();

        try {
            System.out.println("listening...");
            socket = serverSocket.accept();
            System.out.println("accepted");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send entity
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeUTF(entity.toString());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stop();
    }

    private void init() {
        try {
            serverSocket = new ServerSocket(9999);
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
}
