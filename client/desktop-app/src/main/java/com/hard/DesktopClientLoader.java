package com.hard;

import com.hard.controllers.ClientController;
import com.hard.models.Client;
import com.hard.views.View;

import java.util.ArrayList;
import java.util.Collection;

public class DesktopClientLoader {
    public static void main(String[] args) {
        Client client = new Client();
        Collection<View> views = new ArrayList<>();

        ClientController clientController = new ClientController(client);

        clientController.run();
    }
}
