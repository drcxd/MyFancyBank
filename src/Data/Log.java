package Data;

import java.util.HashMap;
import java.util.Date;

public class Log {
    private static int lastPostion = 0;

    private static Msg globalLog = new Msg();

    private static HashMap<User, Msg> userLog = new HashMap<User, Msg>();

    public static String getGlobalLog() {
        lastPostion = globalLog.msg.length();
        return new String(globalLog.msg);
    }

    public static String getGlobalLogUpdate() {
        String log = new String(globalLog.msg.substring(lastPostion));
        lastPostion = globalLog.msg.length();
        return log;
    }

    public static String getUserLog(User user) {
        if (userLog.containsKey(user)) {
            return new String(userLog.get(user).msg);
        }
        return new String();
    }

    private static String getCurrentTime() { return new Date().toString(); }

    private static void log(Msg dest, String content) {
        dest.msg = dest.msg.concat(content);
    }

    private static String makeCreateUserLog(String name) {
        return String.format("[%s] User %s was created\n", getCurrentTime(), name);
    }

    public static void globalLogCreateUser(User user, String name) {
        log(globalLog, makeCreateUserLog(name));
        if (!userLog.containsKey(user)) {
            userLog.put(user, new Msg());
        }
        log(userLog.get(user), makeCreateUserLog(name));
     }

    public static void userLogCreateUser(User user, String name) {
        if (!userLog.containsKey(user)) {
            userLog.put(user, new Msg());
        }
        log(userLog.get(user), makeCreateUserLog(name));
    }

    private static String makeDestroyAccountLog(int id, String name) {
        return String.format("[%s] account %d destroied by %s\n",
                             getCurrentTime(), id, name);
    }

    public static void globalLogDestroyAccount(User user, int id, String name) {
        log(globalLog, makeDestroyAccountLog(id, name));
        log(userLog.get(user), makeDestroyAccountLog(id, name));
    }

    public static void userLogDestroyAccount(User user, int id, String name) {
        log(userLog.get(user), makeDestroyAccountLog(id, name));
    }

    private static String makeCreateAccountLog(int id, String name, Account.AccountType type, Money money) {
        return String.format("[%s] %s account %d created by %s with initial deposit %s %f\n",
                             getCurrentTime(), type, id, name, money.currency, money.amount);
    }

    public static void globalLogCreateAccount(User user, int id, String name, Account.AccountType type, Money money) {
        log(globalLog, makeCreateAccountLog(id, name, type, money));
        log(userLog.get(user), makeCreateAccountLog(id, name, type, money));
    }

    public static void userLogCreateAccount(User user, int id, String name, Account.AccountType type, Money money) {
        log(userLog.get(user), makeCreateAccountLog(id, name, type, money));
    }

    private static String makeSaveLog(int id, String name, Money money) {
        return String.format("[%s] %s saved %s %f to account %d\n",
                             getCurrentTime(), name, money.currency, money.amount, id);
    }

    public static void globalLogSave(User user, int id, String name, Money money) {
        log(globalLog, makeSaveLog(id, name, money));
        log(userLog.get(user), makeSaveLog(id, name, money));
    }

    public static void userLogSave(User user, int id, String name, Money money) {
        log(userLog.get(user), makeSaveLog(id, name, money));
    }

    private static String makeWithdrawLog(int id, String name, Money money) {
        return String.format("[%s] %s withdrawed %s %f to account %d\n",
                             getCurrentTime(), name, money.currency, money.amount, id);
    }

    public static void globalLogWithdraw(User user, int id, String name, Money money) {
        log(globalLog, makeWithdrawLog(id, name, money));
        log(userLog.get(user), makeWithdrawLog(id, name, money));
    }

    public static void userLogWithdraw(User user, int id, String name, Money money) {
        log(userLog.get(user), makeWithdrawLog(id, name, money));
    }

    private static String makeTransactionLog(int fromID, int toID, String name, Money money) {
        return String.format("[%s] %s transacted %s %f from account %d to account %d\n",
                             getCurrentTime(), name, money.currency, money.amount, fromID, toID);
    }

    public static void globalLogTransact(User user, int fromID, int toID, String name, Money money) {
        log(globalLog, makeTransactionLog(fromID, toID, name, money));
        log(userLog.get(user), makeTransactionLog(fromID, toID, name, money));
    }

    public static void userLogTransact(User user, int fromID, int toID, String name, Money money) {
        log(userLog.get(user), makeTransactionLog(fromID, toID, name, money));
    }

    private static String makeCollectFeeLog(Money money) {
        return String.format("[%s] Collect %s %f fee\n",
                             getCurrentTime(), money.currency, money.amount);
    }

    public static void globalLogCollectFee(Money money) {
        log(globalLog, makeCollectFeeLog(money));
    }
}