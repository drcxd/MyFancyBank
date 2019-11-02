package Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class User {
    private String name;

    private int id;

    private ArrayList<Account> accounts = new ArrayList<Account>();

    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }

    public Account createAccount(Account.AccountType type, Money initialDeposit, int accountID) {
        Account account = Account.createAccount(type, accountID, initialDeposit);
        accounts.add(account);
        return account;
    }

    public boolean destroyAccount(Account account, Msg msg) {
        if (!account.destroy(msg)) {
            return false;
        }
        accounts.remove(account);
        return true;
    }

    public Money getUserTotalMoneyInCurrency(Money.Currency currency) {
        Money total = new Money(currency, 0);
        for (Account account : accounts) {
            total.add(account.getNetWorth(currency));
        }
        return total;
    }

    public ArrayList<Account.AccountInfo> getUserAccountInfo() {
        ArrayList<Account.AccountInfo> info = new ArrayList<Account.AccountInfo>();
        for (Account account : accounts) {
            info.add(account.getAccountInfo());
        }
        return info;
    }

    public UserInfo getUserInfo() {
        UserInfo info = new UserInfo();
        info.name = new String(name);
        return info;
    }

    public class UserInfo {
        public String name;
    }
}
