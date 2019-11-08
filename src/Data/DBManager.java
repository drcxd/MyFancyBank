package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.UserReader;
import DBUser.DBUser;
import db.StockReader;
import DBStock.DBStock;

public class DBManager {
    private UserReader userReader = new UserReader();

    private StockReader stockReader = new StockReader();

    void createUser(User user) {
        DBUser u = new DBUser.Builder().setName(user.getName()).build();
        userReader.insert(u);
    }

    void updateMoneyAccount(User user, MoneyAccount moneyAccount) {
        String name = user.getName();
        int id = moneyAccount.getAccountID();
        int accountType = Account.AccountType.valueOf(moneyAccount.getClass().getSimpleName()).ordinal();
        for (Money.Currency currency : Money.Currency.values()) {
            Money m = moneyAccount.query(currency);
            if (m == null) {
                continue;
            }
            int currencyType = m.currency.ordinal();
            double amount = m.amount;
            double interestRate = moneyAccount.getInterestRate();
            DBUser u = new DBUser.Builder().setName(name).setAcc_id(id).setAcc_type(accountType).setCurr_type(currencyType).setMoney(amount).setInterest(interestRate).build();
            userReader.insert(u);
        }
    }

    void updateStockAccount(User user, StockAccount stockAccount) {
        String name = user.getName();
        int id = stockAccount.getAccountID();
        int accountType = Account.AccountType.valueOf(stockAccount.getClass().getSimpleName()).ordinal();
        ArrayList<UserStock> stocks = stockAccount.getAllStock();
        for (UserStock stock : stocks) {
            int stockID = stock.stockID;
            int stockNum = stock.number;
            DBUser u = new DBUser.Builder().setName(name).setAcc_id(id).setAcc_type(accountType).setStock_id(stockID).setStock_amount(stockNum).build();
            userReader.insert(u);
        }
    }

    public void loadAllStock(HashMap<Integer, Stock> id2Stock) {
        List<Integer> allStockID = stockReader.getAllIds();
        for (Integer it : allStockID) {
            DBStock dbStock = stockReader.getById(it.intValue());
            Stock stock = new Stock(dbStock.getId(), dbStock.getName(), new Money(Money.Currency.USD, dbStock.getPrice()));
            id2Stock.put(it, stock);
        }
    }

    public void loadAllUser(HashMap<String, User> name2User,
                            HashMap<Integer, Account> id2Account,
                            HashMap<Integer, MoneyAccount> id2MoneyAccount,
                            HashMap<Integer, StockAccount> id2StockAccount) {
        int maxAccountID = 0;
        // read all user name
        ArrayList<String> userNames = userReader.getAllUser();
        for (String name : userNames) {
            User user = new User(name);
            name2User.put(name, user);
            Log.createUserLog(user);

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
                if (type == Account.AccountType.Stock) {
                    StockAccount stockAccount = user.createStockAccount(type, accountID.intValue());
                    account = stockAccount;
                    id2StockAccount.put(accountID.intValue(), stockAccount);
                    for (DBUser entry : content) {
                        if (entry.getStock_id() < 0) {
                            continue;
                        }
                        stockAccount.addStock(entry.getStock_id(),
                                              entry.getStock_amount(),
                                              new Money(Money.Currency.USD, 0)); // TODO remove hardcode 0
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
    }
}