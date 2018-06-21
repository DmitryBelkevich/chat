package com.hard.views;

import com.hard.controllers.ClientController;

public abstract class View {
    protected ClientController clientController;

    public View(ClientController clientController) {
        this.clientController = clientController;
    }

    public abstract void run();

    public abstract void getMessage(String str);

    public abstract void sendMessage(String str);
}
