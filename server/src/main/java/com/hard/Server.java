package com.hard;

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

            Client client = new Client(this, socket);
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
}

class Client implements Runnable {
    private Server server;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Client(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        initStreams();

        while (true) {
            String str = read();

            if (str.equalsIgnoreCase("/exit"))
                break;

            write(str);
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

    private void write(String str) {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            dataOutputStream.writeUTF(str);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
