package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Data.User;

public class ManagerPanel extends BankPanel {
    JPanel userPanel = new JPanel();
    public ManagerPanel(final DlgBank dlgBank) {
        super(dlgBank);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel ctrlPanel = new JPanel();
        JButton btnPayInterest = new JButton("Pay Interest");
        btnPayInterest.addActionListener(new PayInterestListener());
        ctrlPanel.add(btnPayInterest);

        JButton btnLogAll = new JButton("Log All");
        btnLogAll.addActionListener(new LogAllListener());
        ctrlPanel.add(btnLogAll);

        JButton btnLogUpdate = new JButton("Log Update");
        btnLogUpdate.addActionListener(new LogUpdateListener());
        ctrlPanel.add(btnLogUpdate);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new LogoutListener());
        ctrlPanel.add(btnLogout);
        add(ctrlPanel);

        add(new JLabel("User List"));

        JScrollPane scrlUserList = new JScrollPane(userPanel);
        add(scrlUserList);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
    }

    public void update(ArrayList<User.UserInfo> userInfo) {
        userPanel.removeAll();
        for (User.UserInfo it : userInfo) {
            JButton btnUser = new JButton(it.name);
            btnUser.setActionCommand(it.name);
            btnUser.addActionListener(new UserCheckListener());
            userPanel.add(btnUser);
        }
    }

    private class PayInterestListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.payInterest();
        }
    }

    private class LogAllListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.showLogAll();
        }
    }

    private class LogUpdateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.showLogUpdate();
        }
    }

    private class UserCheckListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            new DlgUser(name, dlgBank.getUserAccountInfo(name), dlgBank);
        }
    }

    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.switchWelcomePanel();
        }
    }
}