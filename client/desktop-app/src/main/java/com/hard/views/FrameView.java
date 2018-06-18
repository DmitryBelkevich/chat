package com.hard.views;

import com.hard.controllers.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameView extends View {
    private JTextArea messagesTextArea;
    private JScrollPane scrollPane;
    private JTextField inputTextField;

    public FrameView(Client client) {
        super(client);
    }

    @Override
    public void run() {
        createGui();

        client.connect();

        client.sendMessage("\r\n\r\n"); // handshake

        while (true) {
            String str = client.getMessage();

            client.notifyAllViews(str);
        }
    }

    public void createGui() {
        JFrame frame = new JFrame("Client");

        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(640 - 10, 480));
        frame.setContentPane(panel);

        /**
         * components
         */

        messagesTextArea = new JTextArea(20, 50);
        messagesTextArea.setEditable(false);

        scrollPane = new JScrollPane(messagesTextArea);
        inputTextField = new JTextField(20);
        JButton sendButton = new JButton("send");

        panel.add(scrollPane);
        panel.add(inputTextField);
        panel.add(sendButton);

        frame.pack();
        frame.setVisible(true);

        inputTextField.requestFocus();

        /**
         * add listeners
         */

        ActionListener actionListener = new SendActionListener();
        inputTextField.addActionListener(actionListener);
        sendButton.addActionListener(actionListener);
        frame.addWindowListener(new CloseWindowListener());
    }

    @Override
    public void getMessage(String str) {
        messagesTextArea.append(str + "\n");

        // scroll to bottom
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        int maximum = verticalScrollBar.getMaximum();
        verticalScrollBar.setValue(maximum);
    }

    @Override
    public void sendMessage(String str) {
        client.sendMessage(str);

        inputTextField.requestFocus();
        inputTextField.setText(null);
    }

    /**
     * listeners
     */

    private class SendActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = inputTextField.getText();
            if (str.equals(""))
                return;

            sendMessage(str);
        }
    }

    private class CloseWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            client.disconnect();
        }
    }
}
