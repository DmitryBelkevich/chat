package com.hard;

import com.hard.models.Entity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Entity entity = new Entity(100, 100, 100, 100);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket socket = null;
        try {
            System.out.println("listening...");
            socket = serverSocket.accept();
            System.out.println("accepted");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send entity

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
