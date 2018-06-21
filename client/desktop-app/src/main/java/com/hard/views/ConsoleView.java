package com.hard.views;

import com.hard.controllers.ClientController;

import java.util.Scanner;

public class ConsoleView extends View {
    public ConsoleView(ClientController clientController) {
        super(clientController);
    }

    @Override
    public void run() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                sendMessage(str);

                if (str.equalsIgnoreCase("/exit"))
                    break;
            }
        }).start();
    }

    @Override
    public void getMessage(String str) {
        System.out.println(str);
    }

    @Override
    public void sendMessage(String str) {
        clientController.sendMessage(str);
    }
}
