package Data;

public class UserStock {
    public int stockID;
    public int number;
    public Money price;

    public UserStock(int stockID, int number, Money price) {
        this.stockID = stockID;
        this.number = number;
        this.price = price;
    }

    public UserStock() {
        this(0, 0, new Money(Money.Currency.USD, 1));
    }
}