package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Data.User;
import Data.Money;

public class ManagerPanel extends BankPanel {

    MoneyInputer transFeeInputer;

    MoneyInputer highBalanceInputer;

    MoneyInputer shareThresholdInputer;

    JTextField txtLoanInterest = new JTextField(10);

    JPanel userPanel = new JPanel();

    public ManagerPanel(final DlgBank dlgBank) {
        super(dlgBank);

        transFeeInputer = new MoneyInputer(dlgBank);
        highBalanceInputer = new  MoneyInputer(dlgBank);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel ctrlPanel = new JPanel();
        ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.Y_AXIS));

        JPanel btnPanel = new JPanel();
        JButton btnPayInterest = new JButton("Pay Interest");
        btnPayInterest.addActionListener(new PayInterestListener());
        btnPanel.add(btnPayInterest);

        JButton btnLogAll = new JButton("Log All");
        btnLogAll.addActionListener(new LogAllListener());
        btnPanel.add(btnLogAll);

        JButton btnLogUpdate = new JButton("Log Update");
        btnLogUpdate.addActionListener(new LogUpdateListener());
        btnPanel.add(btnLogUpdate);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new LogoutListener());
        btnPanel.add(btnLogout);
        ctrlPanel.add(btnPanel);

        JPanel transFeePanel = new JPanel();
        transFeePanel.add(new JLabel("Set Transaction Fee"));
        transFeePanel.add(transFeeInputer);
        JButton btnSetTransFee = new JButton("Confirm");
        btnSetTransFee.addActionListener(new setTransFeeListener());
        transFeePanel.add(btnSetTransFee);
        ctrlPanel.add(transFeePanel);

        JPanel highBalancePanel = new JPanel();
        highBalancePanel.add(new JLabel("Set High Balance"));
        highBalancePanel.add(highBalanceInputer);
        JButton btnSetHighBalance = new JButton("Confirm");
        btnSetHighBalance.addActionListener(new setHighBalanceListener());
        highBalancePanel.add(btnSetHighBalance);
        ctrlPanel.add(highBalancePanel);

        JPanel shareThresholdPanel = new JPanel();
        shareThresholdPanel.add(new JLabel("Set Share Account Threshold"));
        shareThresholdPanel.add(shareThresholdInputer);
        JButton btnSetShareThreshold = new JButton("Confirm");
        btnSetShareThreshold.addActionListener(new setShareThresholdListener());
        shareThresholdPanel.add(btnSetShareThreshold);
        ctrlPanel.add(shareThresholdPanel);

        JPanel loanInterestPanel = new JPanel();
        loanInterestPanel.add(new JLabel("Set Loan Interest"));
        loanInterestPanel.add(txtLoanInterest);
        JButton btnSetLoanInterest = new JButton("Confirm");
        btnSetLoanInterest.addActionListener(new setLoanInterestListener());
        loanInterestPanel.add(btnSetLoanInterest);
        ctrlPanel.add(loanInterestPanel);

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

    private class setTransFeeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = transFeeInputer.getAmount();
            if (amount < 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = transFeeInputer.getCurrency();
            dlgBank.setTransFee(new Money(currency, amount));
            new MessageWindow("Set Transaction Fee Succeeded!", dlgBank);
        }
    }

    private class setHighBalanceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = highBalanceInputer.getAmount();
            if (amount < 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = highBalanceInputer.getCurrency();
            dlgBank.setHighBalance(new Money(currency, amount));
            new MessageWindow("Set High Balance Succeeded!", dlgBank);
        }
    }

    private class setLoanInterestListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double interestRate = 0;
            try {
                interestRate = Double.parseDouble(txtLoanInterest.getText());
            }
            catch (Throwable ex) {
                System.out.println("Error " + ex.getMessage());
                ex.printStackTrace();
            }
            if (interestRate < 1) {
                new MessageWindow("You would like to input a number larger than 1!", dlgBank);
            }
            dlgBank.setLoanInterest(interestRate);
            new MessageWindow("Set Loan Interest Succeeded!", dlgBank);
        }
    }

    private class setShareThresholdListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = shareThresholdInputer.getAmount();
            if (amount < 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = shareThresholdInputer.getCurrency();
            dlgBank.setShareThreshold(new Money(currency, amount));
            new MessageWindow("Set Share Account Threshold Succeeded!", dlgBank);
        }
    }
}