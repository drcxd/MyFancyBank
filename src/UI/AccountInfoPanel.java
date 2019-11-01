package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Data.Account;
import Data.Money;

public class AccountInfoPanel extends JPanel {
    public AccountInfoPanel(Account.AccountInfo info) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel basicPanel = new JPanel();
        basicPanel.setLayout(new GridLayout(2, 3, 10, 5));

        basicPanel.add(new JLabel("Account Type: "));
        basicPanel.add(new JLabel("Account ID: "));
        basicPanel.add(new JLabel("Account Interest: "));

        basicPanel.add(new JLabel(info.type));
        basicPanel.add(new JLabel(info.id));
        basicPanel.add(new JLabel(info.interestRate));

        add(basicPanel);

        JPanel moneyPanel = new JPanel();
        moneyPanel.setLayout(new GridLayout(2, info.depositInfo.moneyInfo.size(), 10, 5));
        for (Money.MoneyInfo it : info.depositInfo.moneyInfo) {
            moneyPanel.add(new JLabel(it.type));
        }
        for (Money.MoneyInfo it : info.depositInfo.moneyInfo) {
            moneyPanel.add(new JLabel(it.amount));
        }
        add(moneyPanel);
    }
}