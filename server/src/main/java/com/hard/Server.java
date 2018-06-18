package com.hard;

import com.hard.clients.Client;
import com.hard.clients.SimpleClient;
import com.hard.clients.WebClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class Server {
    private ServerSocket serverSocket;

    private Collection<Client> clients;

    public Server() {
        this.clients = new LinkedList<>();
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

            Client client = new WebClient(this, socket);
            clients.add(client);
            System.out.println("Total clients: " + clients.size());
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
