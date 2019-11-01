package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Data.Money;
import Data.Account;

public class CreateAccountPanel extends BankPanel {

    private MoneyInputer moneyInputer;

    private JComboBox cbxAccntTyp;

    public CreateAccountPanel(final DlgBank dlgBank) {
        super(dlgBank);

        setLayout(new GridBagLayout());

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));

        subPanel.add(new JLabel("Create Account"));

        JPanel typePanel = new JPanel();
        typePanel.add(new JLabel("Account Type"));
        cbxAccntTyp = new JComboBox<Account.AccountType>(Account.AccountType.values());
        typePanel.add(cbxAccntTyp);
        subPanel.add(typePanel);

        JPanel depositPanel = new JPanel();
        depositPanel.add(new JLabel("Initial Deposit"));
        moneyInputer = new MoneyInputer(dlgBank);
        depositPanel.add(moneyInputer);
        subPanel.add(depositPanel);

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(new ConfirmListener());
        subPanel.add(btnConfirm);

        add(subPanel);
    }

    private class ConfirmListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            double amount = moneyInputer.getAmount();
            if (amount <= 0) {
                new MessageWindow("You would like to input a number larger than 0!", dlgBank);
                return;
            }
            Money.Currency currency = moneyInputer.getCurrency();
            Account.AccountType type = (Account.AccountType)cbxAccntTyp.getSelectedItem();
            if (dlgBank.tryCreateAccount(type, new Money(currency, amount))) {
                dlgBank.switchUserPanel();
            }
        }
    }
}
