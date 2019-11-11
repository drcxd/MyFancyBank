package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.UserReader;
import DBUser.DBUser;
import db.StockReader;
import DBStock.DBStock;
import db.LogReader;

public class DBManager {
    private UserReader userReader = new UserReader();

    private StockReader stockReader = new StockReader();

    private static LogReader logReader = new LogReader();

    public void createUser(User user) {
        DBUser u = new DBUser.Builder().setName(user.getName()).build();
        userReader.insert(u);
    }

    public void createMoneyAccount(MoneyAccount moneyAccount, Money.Currency currency) {
        String name = moneyAccount.getAccountUserName();
        int id = moneyAccount.getAccountID();
        int accountType = Account.AccountType.valueOf(moneyAccount.getClass().getSimpleName()).ordinal();
        Money m = moneyAccount.query(currency);
        if (m == null) {
            return;
        }
        int currencyType = m.currency.ordinal();
        double amount = m.amount;
        double interestRate = moneyAccount.getInterestRate();
        DBUser u = new DBUser.Builder().setName(name).setAcc_id(id).setAcc_type(accountType).setCurr_type(currencyType).setMoney(amount).setInterest(interestRate).build();
        userReader.insert(u);
    }

    public void createStockAccount(StockAccount stockAccount) {
        String name = stockAccount.getAccountUserName();
        int id = stockAccount.getAccountID();
        int accountType = Account.AccountType.valueOf(stockAccount.getClass().getSimpleName()).ordinal();
        DBUser u = new DBUser.Builder().setName(name).setAcc_id(id).setAcc_type(accountType).build();
        userReader.insert(u);
    }

    public void deleteAccount(int accountID) {
        userReader.delete_account(accountID);
    }

    public void updateMoneyAccount(MoneyAccount account, Money.Currency currency) {
        Money m = account.query(currency);
        if (m == null) {
            return;
        }
        String name = account.getAccountUserName();
        int id = account.getAccountID();
        int accountType = Account.AccountType.valueOf(account.getClass().getSimpleName()).ordinal();
        int currencyType = m.currency.ordinal();
        double amount = m.amount;
        double interestRate = account.getInterestRate();
        DBUser u = new DBUser.Builder().setName(name).setAcc_id(id).setAcc_type(accountType).setCurr_type(currencyType).setMoney(amount).setInterest(interestRate).build();
        userReader.insert(u);
    }

    public void createStock(Stock stock) {
        DBStock s = new DBStock.Builder().setId(stock.id).setName(stock.name).setPrice(stock.price.amount).build();
        stockReader.insert(s);
    }

    public void deleteStock(int id) {
        stockReader.delete(id);
    }

    public void updateStockAccount(StockAccount account, int stockID) {
        UserStock userStock = account.queryStock(stockID);
        if (userStock == null) {
            return;
        }
        String name = account.getAccountUserName();
        int id = account.getAccountID();
        int accountType = Account.AccountType.valueOf(account.getClass().getSimpleName()).ordinal();
        DBUser u = new DBUser.Builder().setName(name).setAcc_id(id).setAcc_type(accountType).setStock_id(stockID).setStock_amount(userStock.number).setPurchased_price_of_stock(userStock.price.amount).build();
        userReader.insert(u);
    }

    public void updateInterestRate(MoneyAccount account, double interestRate) {
        userReader.update_interest_rate(account.getAccountID(), interestRate);
    }

    public void loadAllStock(HashMap<Integer, Stock> id2Stock) {
        List<Integer> allStockID = stockReader.getAllIds();
        for (Integer it : allStockID) {
            DBStock dbStock = stockReader.getById(it.intValue());
            Stock stock = new Stock(dbStock.getId(), dbStock.getName(), new Money(Money.Currency.USD, dbStock.getPrice()));
            id2Stock.put(it, stock);
        }
    }

    public int loadAllUser(HashMap<String, User> name2User,
                            HashMap<Integer, Account> id2Account,
                            HashMap<Integer, MoneyAccount> id2MoneyAccount,
                            HashMap<Integer, StockAccount> id2StockAccount) {
        Log.loadGlobalLog(logReader.getLog_by_Name(Log.getGlobalLogName()));
        int maxAccountID = 10000;
        // read all user name
        ArrayList<String> userNames = userReader.getAllUser();
        for (String name : userNames) {
            User user = new User(name);
            name2User.put(name, user);
            Log.createUserLog(user);
            Log.loadUserLog(user, logReader.getLog_by_Name(user.getName()));

            // read all user accounts
            ArrayList<Integer> allAccountID = userReader.getAccountId(name);
            for (Integer accountID : allAccountID) {
                if (accountID.intValue() == 0) {
                    continue;
                }
                if (accountID.intValue() > maxAccountID) {
                    maxAccountID = accountID.intValue();
                }
                ArrayList<DBUser> content = userReader.getByAccountId(accountID.intValue());
                Account.AccountType type = Account.AccountType.values()[content.get(0).getAccount_type()];
                Account account = null;
                if (type == Account.AccountType.StockAccount) {
                    StockAccount stockAccount = user.createStockAccount(type, accountID.intValue());
                    account = stockAccount;
                    id2StockAccount.put(accountID.intValue(), stockAccount);
                    for (DBUser entry : content) {
                        if (entry.getStock_id() < 0) {
                            continue;
                        }
                        stockAccount.addStock(entry.getStock_id(),
                                              entry.getStock_amount(),
                                              new Money(Money.Currency.USD, entry.getPurchased_price_of_stock()));
                    }
                } else {
                    Money first = new Money(Money.Currency.values()[content.get(0).getCurrency_type()],
                                            content.get(0).getMoney());
                    MoneyAccount moneyAccount = user.createMoneyAccount(type, first, accountID.intValue());
                    account = moneyAccount;
                    id2MoneyAccount.put(accountID.intValue(), moneyAccount);
                    moneyAccount.setInterestRate(content.get(0).getInterest());
                    for (int i = 1; i < content.size(); ++i) {
                        moneyAccount.save(new Money(Money.Currency.values()[content.get(i).getCurrency_type()],
                                                    content.get(i).getMoney()));
                    }
                }
                id2Account.put(accountID.intValue(), account);
            }
        }
        return maxAccountID;
    }

    public static void saveLog(String userName, String log) {
        logReader.insert(userName, log);
    }
}