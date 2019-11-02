package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Data.Account;
import Data.CheckingAccount;
import Data.Deposit;
import Data.Money;

public class MoneyAccountItem extends BankPanel {
    int accountID;
    MoneyInputer saveInputer;
    MoneyInputer withdrawInputer;
    MoneyInputer transactInputer;
    JTextField txtAccountID = new JTextField(10);
    public MoneyAccountItem(DlgBank dlgBank, Account.AccountInfo info) {
        super(dlgBank);

        info.reset();
        String strID = info.getNextField();
        String strType = info.getNextField();
        accountID = Integer.parseInt(strID);

        saveInputer = new MoneyInputer(dlgBank);
        withdrawInputer = new MoneyInputer(dlgBank);
        transactInputer = new MoneyInputer(dlgBank);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new AccountInfoPanel(info));

        JPanel savePanel = new JPanel();
        savePanel.add(saveInputer);
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new SaveListener());
        savePanel.add(btnSave);
        add(savePanel);

        JPanel withdrawPanel = new JPanel();
        withdrawPanel.add(withdrawInputer);
        JButton btnWithdraw = new JButton("Withdraw");
        btnWithdraw.addActionListener(new WithdrawListener());
        withdrawPanel.add(btnWithdraw);
        add(withdrawPanel);

        if (strType.equals(CheckingAccount.class.getSimpleName())) {
            JPanel transactPanel = new JPanel();
            transactPanel.add(transactInputer);
            JButton btnTransact = new JButton("Transact");
            btnTransact.addActionListener(new TransactListener());
            transactPanel.add(new JLabel("Transact Account ID"));
            transactPanel.add(txtAccountID);
            transactPanel.add(btnTransact);
            add(transactPanel);
        }

        JButton btnDestroy = new JButton("Destroy");
        btnDestroy.addActionListener(new DestroyListener());
        add(btnDestroy);
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = saveInputer.getAmount();
            if (amount <= 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = saveInputer.getCurrency();
            // int id = Integer.parseInt(lblAccountID.getText());
            dlgBank.saveMoneyToAccount(new Money(currency, amount), accountID);
            dlgBank.switchUserPanel();
        }
    }

    private class WithdrawListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = withdrawInputer.getAmount();
            if (amount <= 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = withdrawInputer.getCurrency();
            // int id = Integer.parseInt(lblAccountID.getText());
            dlgBank.withdrawMoneyFromAccount(new Money(currency, amount), accountID);
            dlgBank.switchUserPanel();
        }
    }

    private class TransactListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = transactInputer.getAmount();
            if (amount <= 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = transactInputer.getCurrency();
            // int fromID = Integer.parseInt(lblAccountID.getText());
            int fromID = accountID;
            int toID;
            try {
                toID = Integer.parseInt(txtAccountID.getText());
            } catch (NumberFormatException ex) {
                new MessageWindow("Please input valid number!", dlgBank);
                return;
            }
            dlgBank.transactMoney(new Money(currency, amount), fromID, toID);
            dlgBank.switchUserPanel();
        }
    }

    private class DestroyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // int id = Integer.parseInt(lblAccountID.getText());
            dlgBank.tryDestroyAccount(accountID);
            dlgBank.switchUserPanel();
        }
    }
}