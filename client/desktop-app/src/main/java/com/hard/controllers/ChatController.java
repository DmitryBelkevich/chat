package com.hard.controllers;

import com.hard.ChatContext;
import com.hard.services.ChatService;

public class ChatController {
    private ChatContext chatContext;
    private ChatService chatService;

    public ChatController(ChatContext chatContext) {
        this.chatContext = chatContext;
        chatService = new ChatService();
    }

    public void connect(String host, int port) {
        chatService.connect(host, port);
    }

    public void disconnect() {
        chatService.write("/exit");
//        chatService.disconnect(); // Exception
    }

    public String getMessage() {
        return chatService.read();
    }

    public void sendMessage(String str) {
        chatService.write(str);
    }

    public void notifyAllViews(String str) {
        chatContext.notifyAllViews(str);
    }
}
