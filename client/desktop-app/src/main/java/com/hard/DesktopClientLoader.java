package com.hard;

import com.hard.controllers.ClientController;
import com.hard.models.Client;
import com.hard.views.ConsoleView;
import com.hard.views.FrameView;
import com.hard.views.View;

import java.util.ArrayList;
import java.util.Collection;

public class DesktopClientLoader {
    public static void main(String[] args) {
        Client client = new Client();
        Collection<View> views = new ArrayList<>();

        ClientController clientController = new ClientController(client, views);

        views.add(new ConsoleView(clientController));
        views.add(new FrameView(clientController));

        for (View view : views)
            view.run();
    }
}
