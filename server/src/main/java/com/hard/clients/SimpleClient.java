package com.hard.clients;

import com.hard.Server;
import com.hard.models.User;

import java.io.*;
import java.net.Socket;

public class SimpleClient extends Client {
    public SimpleClient(Server server, Socket socket) {
        super(server, socket);
    }

    public String read() {
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
