package com.hard;

import com.hard.controllers.ClientController;
import com.hard.models.Client;

public class DesktopClientLoader {
    public static void main(String[] args) {
        Client client = new Client();

        ClientController clientController = new ClientController(client);

        clientController.run();
    }
}
