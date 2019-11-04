package Data;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;

public class StockAccount extends Account {

    protected HashMap<Integer/* StockID */, Integer/* Number of Share */> id2Stock = new HashMap<Integer, Integer>();

    public static StockAccount createAccount(AccountType type, int accountID) {
        return new StockAccount(accountID);
    }

    protected StockAccount(int id) {
        super(id);
    }

    @Override
    public boolean destroy(Msg err) {
        return true;
    }

    @Override
    public AccountInfo getAccountInfo() {
        ArrayList<AccountStockInfo> stockInfo = new ArrayList<AccountStockInfo>();
        for (Entry<Integer, Integer> it : id2Stock.entrySet()) {
            stockInfo.add(new AccountStockInfo(Bank.queryStockInfo(it.getKey().intValue()), it.getValue().intValue()));
        }
        return new StockAccountInfo(getClass().getSimpleName(),
                                    Integer.toString(id),
                                    stockInfo,
                                    getNetWorth(Money.Currency.USD).getMoneyInfo());
    }

    @Override
    public Money getNetWorth(Money.Currency currency) {
        Money netWorth = new Money(currency, 0);
        for (Entry<Integer, Integer> it : id2Stock.entrySet()) {
            netWorth.add(Bank.queryStockPrice(it.getKey().intValue()).mul(it.getValue().intValue()));
        }
        return netWorth;
    }

    public void addStock(int stockID, int stockNum) {
        Integer key = Integer.valueOf(stockID);
        if (id2Stock.containsKey(key)) {
            id2Stock.replace(key, Integer.valueOf(id2Stock.get(key).intValue() + stockNum));
        } else {
            id2Stock.put(key, Integer.valueOf(stockNum));
        }
    }

    public boolean removeStock(int stockID, int stockNum, Msg err) {
        Integer key = Integer.valueOf(stockID);
        if (!id2Stock.containsKey(key) || id2Stock.get(key).intValue() < stockNum) {
            err.msg = "No enough stock!";
            return false;
        }
        if (id2Stock.get(key).intValue() == stockNum) {
            id2Stock.remove(key);
        } else {
            id2Stock.replace(key, Integer.valueOf(id2Stock.get(key).intValue() - stockNum));
        }
        return true;
    }

    public boolean possessStock(int id) {
        return id2Stock.containsKey(Integer.valueOf(id));
    }

    public class StockAccountInfo extends AccountInfo {
        public StockAccountInfo(String type, String id, ArrayList<AccountStockInfo> stockInfo, Money.MoneyInfo netWorth) {
            super(type, id);
            fields.add(stockInfo);
            fields.add(netWorth);
        }
    }

    public class AccountStockInfo {
        public Stock.StockInfo stockInfo;
        public int number;

        public AccountStockInfo(Stock.StockInfo stockInfo, int number) {
            this.stockInfo = stockInfo;
            this.number = number;
        }
    }
}