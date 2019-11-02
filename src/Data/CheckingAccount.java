package Data;

public class CheckingAccount extends MoneyAccount {
    public CheckingAccount(int id) {
        super(id);
    }

    @Override
    public boolean transact(Money money, MoneyAccount account, Msg err) {
        if (!deposit.takeOutMoney(money, err, Bank.TRANSANCT_FEE)) {
            err.msg = "You have no much money to transact!";
            return false;
        }
        account.save(money);
        return true;
    }
}