package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Data.Money;

public class MoneyInputer extends BankPanel {
    private JTextField txtAmount;
    private JComboBox cbxCurrencyType;

    public MoneyInputer(DlgBank dlgBank) {
        super(dlgBank);
        txtAmount = new JTextField(10);
        add(txtAmount);

        add(new JLabel("Currency Type"));
        cbxCurrencyType = new JComboBox<Money.Currency>(Money.Currency.values());
        add(cbxCurrencyType);
    }

    public double getAmount() {
        double amount = -1;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException ex) {
            new MessageWindow("Please input valid number!", dlgBank);
        }
        return amount;
    }

    public Money.Currency getCurrency() {
        return (Money.Currency)cbxCurrencyType.getSelectedItem();
    }
}