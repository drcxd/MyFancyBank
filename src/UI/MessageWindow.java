package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MessageWindow extends JFrame {
    public MessageWindow(String text, Component parent) {
        JLabel lblMsg = new JLabel(text);
        add(lblMsg);
        setTitle("Warning!");
        // setSize(100, 50);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}