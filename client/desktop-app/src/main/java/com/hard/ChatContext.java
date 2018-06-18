package com.hard;

import com.hard.controllers.ChatController;
import com.hard.views.ConsoleView;
import com.hard.views.FrameView;
import com.hard.views.View;

import java.util.ArrayList;
import java.util.Collection;

public class ChatContext {
    private ChatController chatController;
    private Collection<View> views;

    public ChatContext() {
        chatController = new ChatController(this);

        views = new ArrayList<>();

        views.add(new ConsoleView(chatController));
        views.add(new FrameView(chatController));
    }

    public void run() {
        for (View view : views)
            view.run();
    }

    public void notifyAllViews(String str) {
        for (View view : views)
            view.getMessage(str);
    }
}
