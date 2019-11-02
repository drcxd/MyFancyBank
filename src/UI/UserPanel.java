package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Data.MoneyAccount;
import Data.Account;
import Data.Deposit;
import Data.Money;

public class UserPanel extends BankPanel {
    private JLabel lblUserName = new JLabel();
    private JPanel pnlAccounts = new JPanel();
    private JScrollPane scrPnl;

    public UserPanel(final DlgBank dlgBank) {
        super(dlgBank);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(lblUserName);
        add(new JLabel("Account List"));
        scrPnl = new JScrollPane(pnlAccounts);
        pnlAccounts.setLayout(new BoxLayout(pnlAccounts, BoxLayout.Y_AXIS));
        // add(pnlAccounts);
        add(scrPnl);
        JButton btnCreateAccount = new JButton("Create New Account");
        btnCreateAccount.addActionListener(new CreateAccountListener());
        add(btnCreateAccount);
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new LogoutListener());
        add(btnLogout);
        JButton btnLog = new JButton("Log");
        btnLog.addActionListener(new LogListener());
        add(btnLog);
    }

    public void update(String name, ArrayList<Account.AccountInfo> accountsInfo) {
        lblUserName.setText("Hello, " + name);
        pnlAccounts.removeAll();
        for (Account.AccountInfo it : accountsInfo) {
            if (it instanceof MoneyAccount.MoneyAccountInfo) {
                MoneyAccountItem item = new MoneyAccountItem(dlgBank, it);
                pnlAccounts.add(item);
            } else {
                //
            }
        }
    }

    private class CreateAccountListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.switchCreateAccountPanel();
        }
    }

    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.userLogout();
        }
    }

    private class LogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dlgBank.showUserLog();
        }
    }
}