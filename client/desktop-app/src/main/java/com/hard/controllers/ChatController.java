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

    public void connect() {
        chatService.init();
    }

    public void disconnect() {
        chatService.write("/exit");
//        chatService.close(); // Exception
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
