package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Data.Account;
import Data.Money;

public class AccountInfoPanel extends JPanel {
    public AccountInfoPanel(Account.AccountInfo info) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        info.reset();

        String strID = info.getNextField();
        String strType = info.getNextField();
        String strInterestRate = info.getNextField();
        int currencyTypeNum = Integer.parseInt(info.getNextField());
        String[] types = new String[currencyTypeNum];
        String[] amounts = new String[currencyTypeNum];
        for (int i = 0; i < currencyTypeNum; ++i) {
            types[i] = info.getNextField();
            amounts[i] = info.getNextField();
        }

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
        moneyPanel.setLayout(new GridLayout(2, currencyTypeNum, 10, 5));
        for (String it : types) {
            moneyPanel.add(new JLabel(it));
        }
        for (String it : amounts) {
            moneyPanel.add(new JLabel(it));
        }
        add(moneyPanel);
    }
}