package com.hard.views;

import com.hard.controllers.ChatController;

public abstract class View {
    protected ChatController chatController;

    public View(ChatController chatController) {
        this.chatController = chatController;
    }

    public abstract void run();

    public abstract void getMessage(String str);

    public abstract void sendMessage(String str);
}
