package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import Data.Account;
import Data.StockAccount;
import Data.Money;

public class StockAccountInfoPanel extends JPanel {
    public StockAccountInfoPanel(Account.AccountInfo info) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        info.reset();

        String strID = (String)info.getNextField();
        String strType = (String)info.getNextField();
        ArrayList<StockAccount.AccountStockInfo> stockInfo =
            (ArrayList<StockAccount.AccountStockInfo>)info.getNextField();
        Money.MoneyInfo netWorth = (Money.MoneyInfo)info.getNextField();

        add(new JLabel("Account ID: " + strID));
        add(new JLabel("Account Type: " + strType));

        add(new JLabel("Stock List"));
        for (StockAccount.AccountStockInfo it : stockInfo) {
            JPanel stockPanel = new JPanel();
            stockPanel.add(new StockInfoPanel(it.stockInfo));
            stockPanel.add(new JLabel("Number"));
            stockPanel.add(new JLabel("" + it.number));
            add(stockPanel);
        }
        add(new JLabel("Net Worth: " + netWorth.type + " " + netWorth.amount));
    }
}