package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import UI.DlgBank;

public class Bank {
    public static final Money CREATE_ACCOUNT_FEE = new Money(Money.Currency.USD, 10);

    public static final Money WITHDRAW_FEE = new Money(Money.Currency.USD, 1);

    public static Money TRANSANCT_FEE = new Money(Money.Currency.USD, 1);

    public static final Money DESTROY_ACCOUNT_FEE = new Money(Money.Currency.USD, 5);

    public static final double INTEREST_RATE = 1.01;

    public static final double LOAN_RATE = 1.05;

    public static final int LOAN_COLLATERAL_MULTIPLIER = 5;

    public static Money HIGH_BALANCE = new Money(Money.Currency.USD, 100);

    public static Money SHARE_ACCOUNT_THRESHOLD = new Money(Money.Currency.USD, 100);

    private static Random r = new Random();

    public static final double MIN_STOCK_PRICE = 10.0;

    public static final double MAX_STOCK_PRICE = 300.0;

    private HashMap<String, User> name2User = new HashMap<String, User>();

    private HashMap<Integer, Account> id2Account = new HashMap<Integer, Account>();

    private HashMap<Integer, MoneyAccount> id2MoneyAccount = new HashMap<Integer, MoneyAccount>();

    private HashMap<Integer, StockAccount> id2StockAccount = new HashMap<Integer, StockAccount>();

    private static HashMap<Integer, Stock> id2Stock = new HashMap<Integer, Stock>();

    private DlgBank dlgBank;

    private int globalAccountID = 10001;

    private User activeUser;

    private DBManager dbManager = new DBManager();

    public MoneyAccount bankerAccount = new SavingAccount(10000, "BANKMANAGER");

    public Bank() {

        globalAccountID = loadAllUser() + 1;

        loadAllStock();

        dlgBank = new DlgBank(this);
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
    }

    private int loadAllUser() {
        return dbManager.loadAllUser(name2User, id2Account, id2MoneyAccount, id2StockAccount);
    }

    private void loadAllStock() {
        dbManager.loadAllStock(id2Stock);
    }

    public boolean userExists(final String name) {
        return name2User.containsKey(name);
    }

    public void createUser(final String name) {
        User user = new User(name);
        name2User.put(name, user);
        Log.createUserLog(user);
        Log.globalLogCreateUser(user, name);
        dbManager.createUser(user);
    }

    public void userLogin(final String name) {
        activeUser = name2User.get(name);
    }

    public void userLogout() {
        activeUser = null;
    }

    public String getActiveUserName() {
        return activeUser.getName();
    }

    public ArrayList<Account.AccountInfo> getActiveUserAccountsInfo() {
        return activeUser.getUserAccountInfo();
    }

    public ArrayList<Account.AccountInfo> getUserAccountInfo(String name) {
        return name2User.get(name).getUserAccountInfo();
    }

    private MoneyAccount tryCreateMoneyAccount(Account.AccountType type, Money money, Msg err) {
        if (type == Account.AccountType.LoanAccount) {
            if (money.amount > activeUser.getUserNetWorthInCurrency(money.currency).amount * LOAN_COLLATERAL_MULTIPLIER) {
                err.msg = "You need more deposit to take the loan!";
                return null;
            }
            money.amount *= -1;
        } else {
            if (money.lessThan(CREATE_ACCOUNT_FEE)) {
                err.msg = "You need to deposit at least " + CREATE_ACCOUNT_FEE.currency + " " + CREATE_ACCOUNT_FEE.amount + "!";
                return null;
            }
            collectFee(CREATE_ACCOUNT_FEE);
            money.sub(CREATE_ACCOUNT_FEE);
        }

        MoneyAccount account = activeUser.createMoneyAccount(type, money, globalAccountID);

        if (type == Account.AccountType.LoanAccount) {
            account.setInterestRate(LOAN_RATE);
        } else if (type == Account.AccountType.CheckingAccount) {
            account.setInterestRate(INTEREST_RATE);
        }

        id2MoneyAccount.put(globalAccountID, account);
        Log.globalLogCreateAccount(activeUser, globalAccountID, activeUser.getName(), type, money);
        dbManager.createMoneyAccount(account, money.currency);
        err.msg = "Account Created!";
        return account;
    }

    private StockAccount tryCreateStockAccount(Account.AccountType type, Msg err) {
        if (activeUser.getUserNetWorthInCurrency(Money.Currency.USD).lessThan(SHARE_ACCOUNT_THRESHOLD)) {
            err.msg = "You have no enough net worth to create a stock account!";
            return null;
        }
        StockAccount account = activeUser.createStockAccount(type, globalAccountID);
        id2StockAccount.put(globalAccountID, account);
        dbManager.createStockAccount(account);
        err.msg = "Account Created!";
        return account;
    }

    public boolean tryCreateAccount(Account.AccountType type, Money money, Msg err) {
        Account account = null;
        if (type != Account.AccountType.StockAccount) {
            account = tryCreateMoneyAccount(type, money, err);
        } else {
            account = tryCreateStockAccount(type, err);
        }
        if (account != null) {
            id2Account.put(globalAccountID, account);
            ++globalAccountID;
            return true;
        }
        return false;
    }

    public boolean tryDestroyAccount(int accountID, Msg err) {
        Integer id = Integer.valueOf(accountID);
        if (!id2Account.containsKey(id)) {
            err.msg = "No such account to destroy!";
            return false;
        }
        if (!activeUser.destroyAccount(id, err)) {
            return false;
        }
        id2Account.remove(id);
        id2MoneyAccount.remove(id);
        id2StockAccount.remove(id);
        collectFee(DESTROY_ACCOUNT_FEE);
        dbManager.deleteAccount(accountID);
        Log.globalLogDestroyAccount(activeUser, globalAccountID, activeUser.getName());
        return true;
    }

    public void saveMoneyToAccount(final Money money, int accountID) {
        Integer id = Integer.valueOf(accountID);
        if (id2MoneyAccount.containsKey(id)) {
            MoneyAccount account = id2MoneyAccount.get(id);
            account.save(money);
            dbManager.updateMoneyAccount(account, money.currency);
            Log.globalLogSave(activeUser, accountID, activeUser.getName(), money);
        }
    }

    public boolean withdrawMoneyFromAccount(final Money money, int accountID, Msg err) {
        Integer id = Integer.valueOf(accountID);
        if (id2MoneyAccount.containsKey(id)) {
            MoneyAccount account = id2MoneyAccount.get(id);
            if (account.withdraw(money, WITHDRAW_FEE, err)) {
                collectFee(WITHDRAW_FEE);
                dbManager.updateMoneyAccount(account, money.currency);
                Log.globalLogWithdraw(activeUser, id, activeUser.getName(), money);
                return true;
            }
            return false;
        }
        err.msg = "No such account!";
        return false;
    }

    public boolean transactMoney(final Money money, int fromID, int toID, Msg err) {
        Integer fromAccntID = Integer.valueOf(fromID);
        if (!id2MoneyAccount.containsKey(fromAccntID)) {
            err.msg = "No account with this number could be transacted from!";
            return false;
        }
        Integer toAccntID = Integer.valueOf(toID);
        if (!id2MoneyAccount.containsKey(toAccntID)) {
            err.msg = "No account with this number could be transacted to!";
            return false;
        }
        MoneyAccount fromAccount = id2MoneyAccount.get(fromAccntID);
        MoneyAccount toAccount = id2MoneyAccount.get(toAccntID);
        if (fromAccount.transact(money, toAccount, err)) {
            Log.globalLogTransact(activeUser, fromID, toID, activeUser.getName(), money);
            collectFee(TRANSANCT_FEE);
            dbManager.updateMoneyAccount(fromAccount, money.currency);
            dbManager.updateMoneyAccount(toAccount, money.currency);
            return true;
        }
        return false;
    }

    public void payInterest() {
        for (Entry<Integer, MoneyAccount> it : id2MoneyAccount.entrySet()) {
            MoneyAccount account = it.getValue();
            if (account.payInterest()) {
                for (Money.Currency c : Money.Currency.values()) {
                    if (account.query(c) != null) {
                        dbManager.updateMoneyAccount(account, c);
                    }
                }
            }
        }
    }

    public String getLogAll() {
        return Log.getGlobalLog();
    }

    public String getLogUpdate() {
        return Log.getGlobalLogUpdate();
    }

    public ArrayList<User.UserInfo> getUserInfo() {
        ArrayList<User.UserInfo> info = new ArrayList<User.UserInfo>();
        for (Entry<String, User> it : name2User.entrySet()) {
            info.add(it.getValue().getUserInfo());
        }
        return info;
    }

    public String getActiveUserLog() {
        return Log.getUserLog(activeUser);
    }

    public void collectFee(Money fee) {
        bankerAccount.save(fee);
        Log.globalLogCollectFee(fee);
    }

    public void setTransFee(Money newTransFee) {
        TRANSANCT_FEE = newTransFee;
    }

    public void setHighBalance(Money highBalance) {
        HIGH_BALANCE = highBalance;
    }

    public void setLoanInterest(double interestRate) {
        for (Entry<Integer, MoneyAccount> it : id2MoneyAccount.entrySet()) {
            if (it.getValue() instanceof LoanAccount) {
                it.getValue().setInterestRate(interestRate);
                dbManager.updateInterestRate(it.getValue(), interestRate);
            }
        }
    }

    public void setShareThreshold(Money threshold) {
        SHARE_ACCOUNT_THRESHOLD = threshold;
    }

    public boolean tryCreateNewStock(int id, String name, Msg err) {
        Integer key = Integer.valueOf(id);
        if (id2Stock.containsKey(key)) {
            err.msg = "Duplicate stock ID!";
            return false;
        }
        Stock stock = new Stock(id, name, new Money(Money.Currency.USD,
                                                    MIN_STOCK_PRICE + r.nextDouble() * (MAX_STOCK_PRICE - MIN_STOCK_PRICE)));
        id2Stock.put(key, stock);
        dbManager.createStock(stock);
        err.msg = "New Stock Created!";
        return true;
    }

    public boolean tryRemoveStock(int id, Msg err) {
        boolean isValid = true;
        for (Entry<Integer, StockAccount> it : id2StockAccount.entrySet()) {
            if (it.getValue().possessStock(id)) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            err.msg = "Remove failed! Some account still holds this stock!";
            return false;
        }
        id2Stock.remove(Integer.valueOf(id));
        dbManager.deleteStock(id);
        err.msg = "Remove succeeded!";
        return true;
    }

    public ArrayList<Stock.StockInfo> getStockInfo() {
        ArrayList<Stock.StockInfo> info = new ArrayList<Stock.StockInfo>();
        for (Entry<Integer, Stock> it : id2Stock.entrySet()) {
            info.add(it.getValue().getInfo());
        }
        return info;
    }

    public static Stock.StockInfo queryStockInfo(int id) {
        Integer key = Integer.valueOf(id);
        if (id2Stock.containsKey(key)) {
            return id2Stock.get(key).getInfo();
        }
        return null;
    }

    public static Money queryStockPrice(int id) {
        Integer key = Integer.valueOf(id);
        if (id2Stock.containsKey(key)) {
            return new Money(id2Stock.get(key).price);
        }
        return null;
    }

    private boolean isInputValid(int assoAccntID, int stockID, Msg err) {
        Integer key = Integer.valueOf(assoAccntID);
        if (!id2Account.containsKey(key)) {
            err.msg = "No such associated account!";
            return false;
        }
        if (!id2MoneyAccount.containsKey(key)) {
            err.msg = "Associated account must be a money account!";
            return false;
        }
        if (!activeUser.hasAuthOnAccount(assoAccntID)) {
            err.msg = "You have no authority to operate on this associated account!";
            return false;
        }
        Integer stockKey = Integer.valueOf(stockID);
        if (!id2Stock.containsKey(stockKey)) {
            err.msg = "No such stock ID!";
            return false;
        }
        return true;
    }

    public boolean tryBuyStock(int accountID, int assoAccntID, int stockID, int stockNum, Msg err) {
        Integer key = Integer.valueOf(assoAccntID);
        if (!isInputValid(assoAccntID, stockID, err)) {
            return false;
        }
        MoneyAccount moneyAccount = id2MoneyAccount.get(key);
        Money stockValue = queryStockPrice(stockID).mul(stockNum);
        if (!moneyAccount.withdraw(stockValue, null, err)) {
            return false;
        }
        StockAccount stockAccount = id2StockAccount.get(Integer.valueOf(accountID));
        stockAccount.addStock(stockID, stockNum, queryStockPrice(stockID));
        dbManager.updateStockAccount(stockAccount, stockID);
        dbManager.updateMoneyAccount(moneyAccount, stockValue.currency);
        err.msg = "Buy stock succeeded!";
        return true;
    }

    public boolean trySellStock(int accountID, int assoAccntID, int stockID, int stockNum, Msg err) {
        Integer key = Integer.valueOf(assoAccntID);
        if (!isInputValid(assoAccntID, stockID, err)) {
            return false;
        }
        StockAccount stockAccount = id2StockAccount.get(Integer.valueOf(accountID));
        if (!stockAccount.removeStock(stockID, stockNum, err)) {
            return false;
        }
        Money stockValue = queryStockPrice(stockID).mul(stockNum);
        MoneyAccount moneyAccount = id2MoneyAccount.get(key);
        moneyAccount.save(stockValue);
        dbManager.updateStockAccount(stockAccount, stockID);
        dbManager.updateMoneyAccount(moneyAccount, stockValue.currency);
        err.msg = "Buy stock succeeded!";
        return true;
    }
}