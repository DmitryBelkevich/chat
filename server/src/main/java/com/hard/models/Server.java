package com.hard.models;

import com.hard.models.clients.Client;
import com.hard.models.clients.SimpleClient;
import com.hard.models.clients.WebClient;
import com.hard.services.HandshakeService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class Server {
    private ServerSocket serverSocket;

    private Collection<Client> clients;

    private HandshakeService handshakeService;

    public Server() {
        this.clients = new LinkedList<>();
        handshakeService = new HandshakeService();
    }

    public void run() {
        init();
        System.out.println("1. launched");

        while (true) {
            Socket socket = null;
            try {
                System.out.println("2. listening...");
                socket = serverSocket.accept();
                System.out.println("3. accepted client");
            } catch (IOException e) {
                e.printStackTrace();
            }

            int type = 1;
            try {
                handshakeService.handshake(socket.getInputStream(), socket.getOutputStream());
                type = 2;
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Client client = null;

            switch (type) {
                case 1:
                    client = new SimpleClient(this, socket);
                    break;
                case 2:
                    client = new WebClient(this, socket);
                    break;
            }

            clients.add(client);
            System.out.println("clients: " + clients.size() + " " + clients);
            new Thread(client).start();

            if (false)
                break;
        }

        stop();
        System.out.println("4. stopped");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        for (Client client : clients)
            client.stop();

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(Client client) {
        clients.remove(client);
        System.out.println(client + " removed. Total clients: " + clients.size());
    }

    public void notifyAllClients(String str) {
        for (Client client : clients)
            client.write(str);
    }
}
