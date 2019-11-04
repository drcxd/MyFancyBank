package Data;

public class CheckingAccount extends MoneyAccount {
    protected CheckingAccount(int id) {
        super(id);
    }

    @Override
    public boolean transact(Money money, MoneyAccount account, Msg err) {
        if (!deposit.takeOutMoney(money, Bank.TRANSANCT_FEE, err)) {
            err.msg = "You have no much money to transact!";
            return false;
        }
        account.save(money);
        return true;
    }
}