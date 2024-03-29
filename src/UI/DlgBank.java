package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Data.Bank;
import Data.Money;
import Data.Account;
import Data.Msg;
import Data.Stock;

public class DlgBank extends JFrame {
    private final WelcomePanel welcomePanel = new WelcomePanel(this);
    private final UserLoginPanel userLoginPanel = new UserLoginPanel(this);
    private final UserPanel userPanel = new UserPanel(this);
    private final CreateAccountPanel createAccountPanel = new CreateAccountPanel(this);
    private final ManagerPanel managerPanel = new ManagerPanel(this);
    private final Bank bank;

    public DlgBank(Bank bank) {
        this.bank = bank;
        add(welcomePanel);
        setTitle("Fancy Bank");
        setSize(500, 500);
        setLocation(200, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public boolean userExists(final String userName) {
        return bank.userExists(userName);
    }

    public void managerLogin() {
        System.out.println("Manager Login!");
    }

    public void userLogin(final String userName) {
        bank.userLogin(userName);
        switchUserPanel();
    }

    public void userLogout() {
        bank.userLogout();
        switchWelcomePanel();
    }

    public void createUser(final String userName) {
        bank.createUser(userName);
    }

    public boolean tryCreateAccount(Account.AccountType type, Money initialDeposit) {
        Msg err = new Msg();
        boolean res = bank.tryCreateAccount(type, initialDeposit, err);
        new MessageWindow(err.msg, this);
        return res;
    }

    public boolean tryDestroyAccount(int id) {
        Msg err = new Msg();
        boolean res = bank.tryDestroyAccount(id, err);
        new MessageWindow(err.msg, this);
        return res;
    }

    public void saveMoneyToAccount(final Money money, int accountID) {
        bank.saveMoneyToAccount(money, accountID);
    }

    public void withdrawMoneyFromAccount(final Money money, int accountID) {
        Msg err = new Msg();
        if (!bank.withdrawMoneyFromAccount(money, accountID, err)) {
            new MessageWindow(err.msg, this);
        }
    }

    public void transactMoney(final Money money, int fromID, int toID) {
        Msg err = new Msg();
        if (!bank.transactMoney(money, fromID, toID, err)) {
            new MessageWindow(err.msg, this);
        }
    }

    public void payInterest() {
        bank.payInterest();
    }

    public void showLogAll() {
        new DlgLog(bank.getLogAll(), this);
    }

    public void showLogUpdate() {
        new DlgLog(bank.getLogUpdate(), this);
    }

    public void showUserLog() {
        new DlgLog(bank.getActiveUserLog(), this);
    }

    public ArrayList<Account.AccountInfo> getUserAccountInfo(String name) {
        return bank.getUserAccountInfo(name);
    }

    public void setTransFee(Money newTransFee) {
        bank.setTransFee(newTransFee);
    }

    public void setHighBalance(Money highBalance) {
        bank.setHighBalance(highBalance);
    }

    public void setLoanInterest(double interestRate) {
        bank.setLoanInterest(interestRate);
    }

    public void setShareThreshold(Money threshold) {
        bank.setShareThreshold(threshold);
    }

    public boolean tryCreateNewStock(int id, String name, Msg err) {
        return bank.tryCreateNewStock(id, name, err);
    }

    public boolean tryRemoveStock(int id, Msg err) {
        return bank.tryRemoveStock(id, err);
    }

    public boolean tryBuyStock(int accountID, int assoAccntID, int stockID, int stockNum, Msg err) {
        return bank.tryBuyStock(accountID, assoAccntID, stockID, stockNum, err);
    }

    public boolean trySellStock(int accountID, int assoAccntID, int stockID, int stockNum, Msg err) {
        return bank.trySellStock(accountID, assoAccntID, stockID, stockNum, err);
    }

    public ArrayList<Stock.StockInfo> getStockInfo() {
        return bank.getStockInfo();
    }
    public void switchUserLoginPanel() {
        getContentPane().removeAll();
        add(userLoginPanel);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void switchCreateAccountPanel() {
        getContentPane().removeAll();
        add(createAccountPanel);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void switchUserPanel() {
        getContentPane().removeAll();
        add(userPanel);
        userPanel.update(bank.getActiveUserName(), bank.getActiveUserAccountsInfo());
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void switchWelcomePanel() {
        getContentPane().removeAll();
        add(welcomePanel);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void switchManagerPanel() {
        getContentPane().removeAll();
        managerPanel.update(bank.getUserInfo(), bank.getStockInfo());
        add(managerPanel);
        SwingUtilities.updateComponentTreeUI(this);
    }
}
