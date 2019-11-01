package Data;

import java.util.HashMap;

public class Money {

    public enum Currency {
        RMB,
        USD,
        EUR,
        JPY,
        GBP,
        CHF,
    }

    private static final double exchangeRate[][] = {
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
    };

    public final Currency currency;

    public double amount;

    public Money(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Money(double amount) {
        this(Currency.USD, amount);
    }

    public Money() {
        this(Currency.USD, 0);
    }

    public Money(final Money in) {
        this.currency = in.currency;
        this.amount = in.amount;
    }

    public Money toCurrency(Currency targetCurrency) {
        return new Money(targetCurrency, amount * exchangeRate[currency.ordinal()][targetCurrency.ordinal()]);
    }

    public boolean lessThan(final Money rhs) {
        return this.amount < rhs.toCurrency(currency).amount;
    }

    public boolean moreThan(final Money rhs) {
        return this.amount > rhs.toCurrency(currency).amount;
    }

    public boolean equals(final Money rhs) {
        return this.amount == rhs.toCurrency(currency).amount;
    }

    public Money add(final Money money) {
        Money temp = money.toCurrency(currency);
        amount += temp.amount;
        return this;
    }

    public Money sub(final Money money) {
        Money temp = money.toCurrency(currency);
        amount -= temp.amount;
        return this;
    }

    public class MoneyInfo {
        public MoneyInfo(String type, String amount) {
            this.type = type;
            this.amount = amount;
        }
        public String type;
        public String amount;
    }

    public MoneyInfo getMoneyInfo() {
        return new MoneyInfo(currency.toString(), Double.toString(amount));
    }
}