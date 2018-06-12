package com.hard.views;

import com.hard.Client;

public class ConsoleView {
    private Client client;

    public ConsoleView(Client client) {
        this.client = client;
    }

    public void run() {

    }

    public void getMessage(String str) {
        System.out.println(str);
    }
}
