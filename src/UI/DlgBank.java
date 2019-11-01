package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Data.Bank;
import Data.Money;
import Data.Account;
import Data.Msg;

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
        managerPanel.update(bank.getUserInfo());
        add(managerPanel);
        SwingUtilities.updateComponentTreeUI(this);
    }
}
