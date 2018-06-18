package com.hard.views;

import com.hard.controllers.ChatController;

import java.util.Scanner;

public class ConsoleView extends View {
    public ConsoleView(ChatController chatController) {
        super(chatController);
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
        chatController.sendMessage(str);
    }
}
