package Data;

public class Stock {
    public final int id;

    public Money price;

    public String name;

    public Stock(int id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Stock() {
        this(0, "", new Money(Money.Currency.USD, Bank.MIN_STOCK_PRICE));
    }

    public StockInfo getInfo() {
        Money.MoneyInfo moneyInfo = price.getMoneyInfo();
        return new StockInfo("" + id, new String(name), moneyInfo);
    }

    public class StockInfo {
        public String id;
        public String name;
        public String currency;
        public String amount;

        public StockInfo(String id, String name, Money.MoneyInfo moneyInfo) {
            this.id = id;
            this.name = name;
            this.currency = moneyInfo.type;
            this.amount = moneyInfo.amount;
        }
    }
}