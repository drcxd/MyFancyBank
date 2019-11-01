package Data;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;

public class Deposit {
    private HashMap<Money.Currency, Money> currency2Money = new HashMap<Money.Currency, Money>();

    public void addMoney(final Money money) {
        if (currency2Money.containsKey(money.currency)) {
            currency2Money.get(money.currency).add(money);
        } else {
            Money deposit = new Money(money);
            currency2Money.put(deposit.currency, deposit);
        }
    }

    public boolean takeOutMoney(final Money money, final Msg err, final Money fee) {
        if (!currency2Money.containsKey(money.currency)) {
            err.msg = "You have no much money to withdraw!";
            return false;
        }
        Money depositMoney = currency2Money.get(money.currency);
        if (fee != null) {
            money.add(fee);
        }
        if (depositMoney.lessThan(money)) {
            err.msg = "You have no much money to withdraw!";
            return false;
        }
        depositMoney.sub(money);
        if (fee != null) {
            money.sub(fee);
        }
        return true;
    }

    public final Money query(Money.Currency currency) {
        if (currency2Money.containsKey(currency)) {
            return currency2Money.get(currency);
        }
        return null;
    }

    public boolean destroy(Money fee, Msg err) {
        Money total = new Money(fee.currency, 0);
        for (Entry<Money.Currency, Money> it : currency2Money.entrySet()) {
            total.add(it.getValue());
        }
        if (total.lessThan(fee)) {
            err.msg = "You have no enough money to destroy!";
            return false;
        }
        err.msg = "Destruction succeeded!";
        return true;
    }

    public boolean payInterest(double rate) {
        for (Entry<Money.Currency, Money> it : currency2Money.entrySet()) {
            it.getValue().amount *= rate;
        }
        return true;
    }

    public DepositInfo getDepositInfo() {
        DepositInfo info = new DepositInfo();
        for (Entry<Money.Currency, Money> it : currency2Money.entrySet()) {
            info.moneyInfo.add(it.getValue().getMoneyInfo());
        }
        return info;
    }

    public class DepositInfo {
        public ArrayList<Money.MoneyInfo> moneyInfo = new ArrayList<Money.MoneyInfo>();
    }
}