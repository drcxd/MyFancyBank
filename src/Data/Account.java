package Data;

import java.util.HashMap;
import java.util.ArrayList;

abstract public class Account {

    public enum AccountType {
        Saving,
        Checking,
        Loan,
        Stock,
    };

    protected final int id;

    public Account(int id) {
        this.id = id;
    }

    public abstract boolean destroy(Msg err);

    public abstract Money getNetWorth(Money.Currency currency);

    public abstract AccountInfo getAccountInfo();

    abstract public class AccountInfo {
        protected ArrayList<String> fields = new ArrayList<String>();

        private int index = 0;

        public AccountInfo(String type, String id) {
            fields.add(id);
            fields.add(type);
        }

        public String getNextField() {
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
