package com.hard.controllers;

import com.hard.models.Client;
import com.hard.views.View;

import java.util.Collection;

public class ClientController {
    private Client client;
    private Collection<View> views;

    public ClientController(Client client, Collection<View> views) {
        this.client = client;
        this.views = views;
    }

    public void connect(String host, int port) {
        client.connect(host, port);
    }

    public void disconnect() {
        client.write("/exit");
//        client.disconnect(); // Exception
    }

    public String getMessage() {
        return client.read();
    }

    public void sendMessage(String str) {
        client.write(str);
    }

    public void notifyAllViews(String str) {
        for (View view : views)
            view.getMessage(str);
    }
}
