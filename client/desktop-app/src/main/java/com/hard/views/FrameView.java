package com.hard.views;

import com.hard.controllers.Client;

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

        frame.setLayout(new GridBagLayout());

//        JPanel panel = new JPanel();
//        panel.setPreferredSize(new Dimension(640 - 10, 480));
//        frame.setContentPane(panel);

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

        ActionListener actionListener = new SendActionListener();
        messagesInputTextArea.addKeyListener(new KeyListener1());
        sendButton.addActionListener(actionListener);
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
        client.sendMessage(str);

        messagesInputTextArea.requestFocus();
        messagesInputTextArea.setText(null);
    }

    /**
     * listeners
     */

    private class SendActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = messagesInputTextArea.getText();
            if (str.equals(""))
                return;

            sendMessage(str);
        }
    }

    private class KeyListener1 implements KeyListener {
        private final Set<Integer> pressed = new HashSet<>();

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public synchronized void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            pressed.add(keyCode);

            if (pressed.size() > 1) {
                if (pressed.contains(KeyEvent.VK_CONTROL) && pressed.contains(KeyEvent.VK_ENTER)) {
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
            pressed.remove(keyCode);
        }
    }

    private class CloseWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            client.disconnect();
        }
    }
}
