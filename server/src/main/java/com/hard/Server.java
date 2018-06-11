package com.hard;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    private Client client;

    public void run() {
        init();
        System.out.println("1. launched");

        Socket socket = null;
        try {
            System.out.println("2. listening...");
            socket = serverSocket.accept();
            System.out.println("3. accepted client");
        } catch (IOException e) {
            e.printStackTrace();
        }

        client = new Client(socket);
        client.run();

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
        client.stop();

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Client {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        initStreams();

        String str = readData();
        write(str);
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
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readData() {
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
