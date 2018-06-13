package com.hard;

import com.hard.services.ClientService;
import com.hard.views.ConsoleView;
import com.hard.views.FrameView;
import com.hard.views.View;

import java.util.ArrayList;
import java.util.Collection;

public class Client {
    private Collection<View> views;
    private ClientService clientService;

    public Client() {
        views = new ArrayList<>();

        views.add(new ConsoleView(this));
        views.add(new FrameView(this));

        clientService = new ClientService();
    }

    public void run() {
        for (View view : views)
            view.run();

        clientService.init();
        clientService.initStreams();

        while (true) {
            String str = read();

            notifyAllViews(str);
        }
    }

    public void stop() {
        clientService.stop();
    }

    private String read() {
        return clientService.read();
    }

    public void write(String str) {
        clientService.write(str);
    }

    public void notifyAllViews(String str) {
        for (View view : views)
            view.getMessage(str);
    }
}
