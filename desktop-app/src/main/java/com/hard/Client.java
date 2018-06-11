package com.hard;

import javax.swing.*;
import java.awt.*;

public class Client {
    public void run() {
        JFrame frame = new JFrame("Client");

        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(640, 480));
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}
