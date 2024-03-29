package Data;

public abstract class MoneyAccount extends Account {
    protected Deposit deposit = new Deposit();

    protected double interestRate = 0;

    public static MoneyAccount createAccount(AccountType type, int id, String userName, Money money) {
        MoneyAccount account = null;
        if (type == AccountType.SavingAccount) {
            account = new SavingAccount(id, userName);
            account.save(money);
        } else if (type == AccountType.CheckingAccount) {
            account = new CheckingAccount(id, userName);
            account.save(money);
        } else if (type == AccountType.LoanAccount) {
            account = new LoanAccount(id, userName, money);
        }
        return account;
    }

    protected MoneyAccount(int id, String userName) {
        super(id, userName);
    }

    @Override
    public boolean destroy(Msg err) {
        return deposit.destroy(Bank.DESTROY_ACCOUNT_FEE, err);
    }

    @Override
    public Money getNetWorth(Money.Currency currency) {
        Money total = new Money(currency, 0);
        for (Money.Currency c : Money.Currency.values()) {
            if (deposit.query(c) != null) {
                total.add(deposit.query(c).toCurrency(currency));
            }
        }
        return total;
    }

    @Override
    public MoneyAccountInfo getAccountInfo() {
        return new MoneyAccountInfo(this.getClass().getSimpleName(),
                                    Integer.toString(id),
                                    Double.toString(interestRate),
                                    deposit.getDepositInfo());
    }

    public void save(final Money money) {
        deposit.addMoney(money);
    }

    public boolean withdraw(Money money, Money fee, Msg err) {
        return deposit.takeOutMoney(money, fee, err);
    }

    public boolean transact(Money money, MoneyAccount account, Msg err) { return false; }

    public boolean payInterest() {
        if (getNetWorth(Money.Currency.USD).moreThan(Bank.HIGH_BALANCE)) {
            deposit.payInterest(interestRate);
            return true;
        }
        return false;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public Money query(Money.Currency currency) {
        return deposit.query(currency);
    }

    public class MoneyAccountInfo extends AccountInfo {
        public MoneyAccountInfo(String type, String id, String interestRate, Deposit.DepositInfo depositInfo) {
            super(type, id);
            fields.add(interestRate);
            fields.add(depositInfo);
        }
    }
}