package com.hard.views;

import com.hard.Client;

public abstract class View {
    protected Client client;

    public View(Client client) {
        this.client = client;
    }

    public abstract void run();

    public abstract void getMessage(String str);

    public abstract void sendMessage(String str);
}
