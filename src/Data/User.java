package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class User {
    private String name;

    private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public MoneyAccount createMoneyAccount(Account.AccountType type, Money initialDeposit, int accountID) {
        MoneyAccount account = MoneyAccount.createAccount(type, accountID, name, initialDeposit);
        accounts.put(Integer.valueOf(accountID), account);
        return account;
    }

    public StockAccount createStockAccount(Account.AccountType type, int accountID) {
        StockAccount account = StockAccount.createAccount(type, accountID, name);
        accounts.put(Integer.valueOf(accountID), account);
        return account;
    }

    public boolean destroyAccount(int accountID, Msg msg) {
        Integer id = Integer.valueOf(accountID);
        if (!accounts.containsKey(id)) {
            msg.msg = "No such account!";
            return false;
        }
        Account account = accounts.get(id);
        if (!account.destroy(msg)) {
            return false;
        }
        accounts.remove(id);
        return true;
    }

    public Money getUserNetWorthInCurrency(Money.Currency currency) {
        Money total = new Money(currency, 0);
        for (Entry<Integer, Account> it : accounts.entrySet()) {
            total.add(it.getValue().getNetWorth(currency));
        }
        return total;
    }

    public ArrayList<Account.AccountInfo> getUserAccountInfo() {
        ArrayList<Account.AccountInfo> info = new ArrayList<Account.AccountInfo>();
        for (Entry<Integer, Account> it : accounts.entrySet()) {
            info.add(it.getValue().getAccountInfo());
        }
        return info;
    }

    public UserInfo getUserInfo() {
        UserInfo info = new UserInfo();
        info.name = new String(name);
        return info;
    }

    public boolean hasAuthOnAccount(int id) {
        Integer key = Integer.valueOf(id);
        return accounts.containsKey(key);
    }

    public class UserInfo {
        public String name;
    }
}
