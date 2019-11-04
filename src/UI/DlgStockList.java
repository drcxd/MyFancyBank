package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import Data.Stock;

public class DlgStockList extends JFrame {
    public DlgStockList(ArrayList<Stock.StockInfo> stockInfo, Component parent) {
        setTitle("Stock List");
        setLocationRelativeTo(parent);
        JPanel panel = new JPanel();
        JScrollPane scrlList = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (Stock.StockInfo it : stockInfo) {
            panel.add(new StockInfoPanel(it));
        }
        add(scrlList);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
}