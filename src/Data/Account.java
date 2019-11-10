package Data;

import java.util.HashMap;
import java.util.ArrayList;

abstract public class Account {

    public enum AccountType {
        SavingAccount,
        CheckingAccount,
        LoanAccount,
        StockAccount,
    };

    protected final int id;

    protected String userName;

    public Account(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public abstract boolean destroy(Msg err);

    public abstract Money getNetWorth(Money.Currency currency);

    public abstract AccountInfo getAccountInfo();

    public int getAccountID() {
        return id;
    }

    public String getAccountUserName() {
        return userName;
    }

    abstract public class AccountInfo {
        protected ArrayList<Object> fields = new ArrayList<Object>();

        private int index = 0;

        public AccountInfo(String type, String id) {
            fields.add(id);
            fields.add(type);
        }

        public Object getNextField() {
            if (index < fields.size()) {
                return fields.get(index++);
            }
            return null;
        }

        public void reset() {
            index = 0;
        }
    }
}
