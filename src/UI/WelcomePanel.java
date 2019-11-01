package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class WelcomePanel extends BankPanel {

    public WelcomePanel(final DlgBank dlgBank) {
        super(dlgBank);
        setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        JButton btnUserLogin = new JButton("User Login");
        JButton btnManagerLogin = new JButton("Manager Login");
        panel.add(btnUserLogin);
        panel.add(btnManagerLogin);
        btnUserLogin.addActionListener(new UserLoginListener());
        btnManagerLogin.addActionListener(new ManagerLoginListener());
        add(panel);
    }

    private class UserLoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.switchUserLoginPanel();
        }
    }

    private class ManagerLoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.switchManagerPanel();
        }
    }
}