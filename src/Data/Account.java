package Data;

import java.util.HashMap;

abstract public class Account {

    public class AccountInfo {
        public String type;
        public String id;
        public String interestRate;
        public Deposit.DepositInfo depositInfo;

        public AccountInfo(String type, String id, String interestRate, Deposit.DepositInfo depositInfo) {
            this.type = type;
            this.id = id;
            this.interestRate = interestRate;
            this.depositInfo = depositInfo;
        }
    }

    public enum AccountType {
        Saving,
        Checking,
        Loan,
    };

    protected Deposit deposit = new Deposit();

    protected double interestRate;

    protected final int id;

    public static Account createAccount(AccountType type, int id, Money money) {
        Account account = null;
        if (type == AccountType.Saving) {
            account = new SavingAccount(id);
            money.sub(Bank.CREATE_ACCOUNT_FEE);
            account.save(money);
            account.interestRate = Bank.INTEREST_RATE;
        } else if (type == AccountType.Checking) {
            account = new CheckingAccount(id);
            money.sub(Bank.CREATE_ACCOUNT_FEE);
            account.save(money);
        } else if (type == AccountType.Loan) {
            account = new LoanAccount(id, money);
            account.interestRate = Bank.LOAN_RATE;
        }
        return account;
    }

    public Account(int id) {
        this.id = id;
    }

    public void save(final Money money) {
        deposit.addMoney(money);
    }

    public boolean withdraw(final Money money, final Msg err) {
        return deposit.takeOutMoney(money, err, Bank.WITHDRAW_FEE);
    }

    public boolean transact(Money money, Account account, Msg err) { return false; }

    public boolean payInterest() {
        if (getAccountTotalMoney(Money.Currency.USD).moreThan(Bank.HIGH_BALANCE)) {
            deposit.payInterest(interestRate);
            return true;
        }
        return false;
    }

    public boolean destroy(Msg err) {
        return deposit.destroy(Bank.DESTROY_ACCOUNT_FEE, err);
    }

    public Money getAccountTotalMoney(Money.Currency currency) {
        Money total = new Money(currency, 0);
        for (Money.Currency c : Money.Currency.values()) {
            if (deposit.query(c) != null) {
                total.add(deposit.query(c).toCurrency(currency));
            }
        }
        return total;
    }

    public AccountInfo getAccountInfo() {
        return new AccountInfo(this.getClass().getSimpleName(),
                               Integer.toString(id),
                               Double.toString(interestRate),
                               deposit.getDepositInfo());
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
