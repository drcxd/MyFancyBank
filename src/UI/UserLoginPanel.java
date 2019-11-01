package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserLoginPanel extends BankPanel {
    private final JTextField txtUserName;

    public UserLoginPanel(final DlgBank dlgBank) {
        super(dlgBank);
        setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.add(new JLabel("User Name"));
        txtUserName = new JTextField(10);
        panel.add(txtUserName);
        JButton btnLogin = new JButton("Login");
        JButton btnCreate = new JButton("Create");
        panel.add(btnLogin);
        panel.add(btnCreate);
        btnLogin.addActionListener(new UserLoginListener());
        btnCreate.addActionListener(new UserCreateListener());
        add(panel);
    }

    private class UserLoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userName = txtUserName.getText();
            if (userName.length() > 10) {
                new MessageWindow("Length of user name must less or equal than 10 characters!", dlgBank);
            } else if (dlgBank.userExists(userName)) {
                dlgBank.userLogin(userName);
            } else {
                new MessageWindow("User does not exist! Please chek your user name or create a new one!", dlgBank);
            }
        }
    }

    private class UserCreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String userName = txtUserName.getText();
            if (userName.length() > 10) {
                 new MessageWindow("Length of user name must less or equal than 10 characters!", dlgBank);
            } else if (dlgBank.userExists(userName)) {
                new MessageWindow("User name already been used! Try another!", dlgBank);
            } else {
                dlgBank.createUser(userName);
                dlgBank.userLogin(userName);
            }
        }
    }
}