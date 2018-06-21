package com.hard.controllers;

import com.hard.models.Client;
import com.hard.views.ConsoleView;
import com.hard.views.FrameView;
import com.hard.views.View;

import java.util.ArrayList;
import java.util.Collection;

public class ClientController {
    private Collection<View> views;

    private Client client;

    public ClientController(Client client) {
        views = new ArrayList<>();

        views.add(new ConsoleView(this));
        views.add(new FrameView(this));

        this.client = client;
    }

    public void run() {
        for (View view : views)
            view.run();
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
