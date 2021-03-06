package com.hard.views;

import com.hard.controllers.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class FrameView extends View {
    private JTextArea messagesInputTextArea;
    private JTextArea messagesOutputTextArea;
    private JTextArea usersOutputTextArea;

    private JScrollPane messagesInputScrollPane;
    private JScrollPane messagesOutputScrollPane;
    private JScrollPane usersOutputScrollPane;

    public FrameView(ClientController clientController) {
        super(clientController);
    }

    @Override
    public void run() {
        createGui();

        connect();
    }

    public void createGui() {
        JFrame frame = new JFrame("Chat");

        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setLayout(new GridBagLayout());

        /**
         * components
         */

        messagesInputTextArea = new JTextArea(5, 50);

        messagesOutputTextArea = new JTextArea(20, 50);
        messagesOutputTextArea.setEditable(false);

        usersOutputTextArea = new JTextArea(20, 20);
        usersOutputTextArea.setEditable(false);

        messagesInputScrollPane = new JScrollPane(messagesInputTextArea);
        messagesOutputScrollPane = new JScrollPane(messagesOutputTextArea);
        usersOutputScrollPane = new JScrollPane(usersOutputTextArea);

        JButton sendButton = new JButton("send");

        frame.add(messagesOutputScrollPane,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5),
                        0, 0
                )
        );
        frame.add(usersOutputScrollPane,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5),
                        0, 0
                )
        );
        frame.add(
                messagesInputScrollPane,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5),
                        0, 0
                )
        );

        frame.add(
                sendButton,
                new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5),
                        0, 55
                ));

        frame.pack();
        frame.setVisible(true);

        messagesInputTextArea.requestFocus();

        /**
         * add listeners
         */

        messagesInputTextArea.addKeyListener(new SendMessageKeyListener1());
        sendButton.addActionListener(new SendMessageActionListener());
        frame.addWindowListener(new CloseWindowListener());
    }

    @Override
    public void getMessage(String str) {
        messagesOutputTextArea.append(str + "\n");

        // scroll to bottom
        JScrollBar verticalScrollBar = messagesInputScrollPane.getVerticalScrollBar();
        int maximum = verticalScrollBar.getMaximum();
        verticalScrollBar.setValue(maximum);
    }

    @Override
    public void sendMessage(String str) {
        clientController.sendMessage(str);

        messagesInputTextArea.requestFocus();
        messagesInputTextArea.setText(null);
    }

    public void connect() {
        clientController.connect("localhost", 9999);

        clientController.sendMessage("\r\n\r\n"); // handshake

        while (true) {
            String str = clientController.getMessage();

            clientController.notifyAllViews(str);
        }
    }

    /**
     * listeners
     */

    private class SendMessageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = messagesInputTextArea.getText();
            if (str.equals(""))
                return;

            sendMessage(str);
        }
    }

    /**
     * Enter: send message
     * Ctrl + Enter: return to new line
     */
    private class SendMessageKeyListener1 implements KeyListener {
        private final Set<Integer> pressedKeys = new HashSet<>();

        @Override
        public synchronized void keyTyped(KeyEvent e) {

        }

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            pressedKeys.add(keyCode);

            if (pressedKeys.size() == 1) {
                if (pressedKeys.contains(KeyEvent.VK_ENTER)) {
                    e.consume();

                    String str = messagesInputTextArea.getText();
                    if (str.trim().equals(""))
                        return;

                    sendMessage(str);
                }
            }

            if (pressedKeys.size() == 2) {
                if (pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_ENTER)) {
                    messagesInputTextArea.append("\n");
                }
            }
        }

        @Override
        public synchronized void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            pressedKeys.remove(keyCode);
        }
    }

    /**
     * Ctrl + Enter: send message
     * Enter: return to new line
     */
    private class SendMessageKeyListener2 implements KeyListener {
        private final Set<Integer> pressedKeys = new HashSet<>();

        @Override
        public synchronized void keyTyped(KeyEvent e) {

        }

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            pressedKeys.add(keyCode);

            if (pressedKeys.size() == 2) {
                if (pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_ENTER)) {
                    String str = messagesInputTextArea.getText();
                    if (str.equals(""))
                        return;

                    sendMessage(str);
                }
            }
        }

        @Override
        public synchronized void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            pressedKeys.remove(keyCode);
        }
    }

    private class CloseWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            clientController.disconnect();
        }
    }
}
