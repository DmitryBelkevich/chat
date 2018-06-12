package com.hard.views;

import com.hard.Client;

public class ConsoleView extends View {
    public ConsoleView(Client client) {
        super(client);
    }

    @Override
    public void run() {

    }

    @Override
    public void getMessage(String str) {
        System.out.println(str);
    }

    @Override
    public void sendMessage(String str) {
        client.write(str);
    }
}
