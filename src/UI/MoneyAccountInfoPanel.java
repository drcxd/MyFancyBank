package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Data.Account;
import Data.Money;
import Data.Deposit;

public class MoneyAccountInfoPanel extends JPanel {
    public MoneyAccountInfoPanel(Account.AccountInfo info) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        info.reset();

        String strID = (String)info.getNextField();
        String strType = (String)info.getNextField();
        String strInterestRate = (String)info.getNextField();
        Deposit.DepositInfo depositInfo = (Deposit.DepositInfo)info.getNextField();

        JPanel basicPanel = new JPanel();
        basicPanel.setLayout(new GridLayout(2, 3, 10, 5));

        basicPanel.add(new JLabel("Account Type: "));
        basicPanel.add(new JLabel("Account ID: "));
        basicPanel.add(new JLabel("Account Interest: "));

        basicPanel.add(new JLabel(strType));
        basicPanel.add(new JLabel(strID));
        basicPanel.add(new JLabel(strInterestRate));

        add(basicPanel);

        JPanel moneyPanel = new JPanel();
        moneyPanel.setLayout(new GridLayout(2, depositInfo.moneyInfo.size(), 10, 5));
        for (Money.MoneyInfo it : depositInfo.moneyInfo) {
            moneyPanel.add(new JLabel(it.type));
        }
        for (Money.MoneyInfo it : depositInfo.moneyInfo) {
            moneyPanel.add(new JLabel(it.amount));
        }
        add(moneyPanel);
    }
}