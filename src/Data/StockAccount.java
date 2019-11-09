package Data;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;

public class StockAccount extends Account {

    protected HashMap<Integer/* StockID */, UserStock> id2Stock = new HashMap<Integer, UserStock>();

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
        for (Entry<Integer, UserStock> it : id2Stock.entrySet()) {
            stockInfo.add(new AccountStockInfo(Bank.queryStockInfo(it.getKey().intValue()),
                                               it.getValue().number,
                                               new Money(it.getValue().price)));
        }
        return new StockAccountInfo(getClass().getSimpleName(),
                                    Integer.toString(id),
                                    stockInfo,
                                    getNetWorth(Money.Currency.USD).getMoneyInfo());
    }

    @Override
    public Money getNetWorth(Money.Currency currency) {
        Money netWorth = new Money(currency, 0);
        for (Entry<Integer, UserStock> it : id2Stock.entrySet()) {
            netWorth.add(Bank.queryStockPrice(it.getKey().intValue()).mul(it.getValue().number));
        }
        return netWorth;
    }

    public void addStock(int stockID, int stockNum, Money price) {
        Integer key = Integer.valueOf(stockID);
        if (id2Stock.containsKey(key)) {
            UserStock userStock = id2Stock.get(key);
            double newPrice = (userStock.price.amount * userStock.number + price.amount * stockNum) / (userStock.number + stockNum);
            userStock.number += stockNum;
            userStock.price.amount = newPrice;
        } else {
            id2Stock.put(key, new UserStock(stockID, stockNum, price));
        }
    }

    public boolean removeStock(int stockID, int stockNum, Msg err) {
        Integer key = Integer.valueOf(stockID);
        if (!id2Stock.containsKey(key) || id2Stock.get(key).number < stockNum) {
            err.msg = "No enough stock!";
            return false;
        }
        if (id2Stock.get(key).number == stockNum) {
            id2Stock.remove(key);
        } else {
            id2Stock.get(key).number -= stockNum;
        }
        return true;
    }

    public ArrayList<UserStock> getAllStock() {
        ArrayList<UserStock> stocks = new ArrayList<UserStock>();
        for (Entry<Integer, UserStock> it : id2Stock.entrySet()) {
            stocks.add(it.getValue());
        }
        return stocks;
    }

    public UserStock queryStock(int id) {
        Integer key = Integer.valueOf(id);
        if (id2Stock.containsKey(key)) {
            return id2Stock.get(key);
        }
        return null;
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
        public Money price;

        public AccountStockInfo(Stock.StockInfo stockInfo, int number, Money price) {
            this.stockInfo = stockInfo;
            this.number = number;
            this.price= price;
        }
    }
}