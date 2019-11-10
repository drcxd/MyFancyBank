package Data;

public class LoanAccount extends MoneyAccount {

    protected LoanAccount(int id, String userName, Money money) {
        super(id, userName);
        super.save(money);
    }

    @Override
    public void save(final Money money) {
        for (Money.Currency c : Money.Currency.values()) {
            if (deposit.query(c) != null) {
                deposit.query(c).add(money.toCurrency(c));
                if (deposit.query(c).amount > 0) {
                    deposit.query(c).amount = 0;
                }
                break;
            }
        }
    }

    @Override
    public boolean transact(Money money, MoneyAccount account, Msg err) {
        err.msg = "No transaction allowed for a loan account!";
        return false;
    }

    @Override
    public boolean withdraw(Money money, Money fee, Msg err) {
        err.msg = "No withdrawl allowed for a loan account!";
        return false;
    }

    @Override
    public boolean destroy(Msg err) {
        if (!isClear()) {
            err.msg = "You should pay the loan before you destroy it!";
            return false;
        }
        err.msg = "Destruction succeeded!";
        return true;
    }

    @Override
    public boolean payInterest() {
        deposit.payInterest(interestRate);
        return true;
    }

    public boolean isClear() {
        boolean clear = true;
        for (Money.Currency c : Money.Currency.values()) {
            if (deposit.query(c) != null && deposit.query(c).amount < 0) {
                    clear = false;
                    break;
            }
        }
        return clear;
    }
}