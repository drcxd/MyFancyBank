package Data;

import java.util.HashMap;
import java.util.Date;

public class Log {
    private static final String globalLogName = new String("BANKER");

    private static int lastPostion = 0;

    private static Msg globalLog = new Msg();

    private static HashMap<User, Msg> userLog = new HashMap<User, Msg>();

    public static String getGlobalLogName() { return globalLogName; }

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

    public static void createUserLog(User user) {
        if (!userLog.containsKey(user)) {
            userLog.put(user, new Msg());
        }
    }

    public static void loadGlobalLog(String log) {
        globalLog.msg = log;
    }

    public static void loadUserLog(User user, String log) {
        userLog.put(user, new Msg(log));
    }

    private static String makeCreateUserLog(String name) {
        return String.format("[%s] User %s was created\n", getCurrentTime(), name);
    }

    public static void globalLogCreateUser(User user, String name) {
        String newEntry = makeCreateUserLog(name);
        log(globalLog, newEntry);
        log(userLog.get(user), newEntry);
        DBManager.saveLog(user.getName(), getUserLog(user));
        DBManager.saveLog(globalLogName, globalLog.msg);
     }

    private static String makeDestroyAccountLog(int id, String name) {
        return String.format("[%s] account %d destroied by %s\n",
                             getCurrentTime(), id, name);
    }

    public static void globalLogDestroyAccount(User user, int id, String name) {
        log(globalLog, makeDestroyAccountLog(id, name));
        log(userLog.get(user), makeDestroyAccountLog(id, name));
        DBManager.saveLog(user.getName(), getUserLog(user));
        DBManager.saveLog(globalLogName, globalLog.msg);
    }

    private static String makeCreateAccountLog(int id, String name, Account.AccountType type, Money money) {
        return String.format("[%s] %s account %d created by %s with initial deposit %s %f\n",
                             getCurrentTime(), type, id, name, money.currency, money.amount);
    }

    public static void globalLogCreateAccount(User user, int id, String name, Account.AccountType type, Money money) {
        log(globalLog, makeCreateAccountLog(id, name, type, money));
        log(userLog.get(user), makeCreateAccountLog(id, name, type, money));
        DBManager.saveLog(user.getName(), getUserLog(user));
        DBManager.saveLog(globalLogName, globalLog.msg);
    }

    private static String makeSaveLog(int id, String name, Money money) {
        return String.format("[%s] %s saved %s %f to account %d\n",
                             getCurrentTime(), name, money.currency, money.amount, id);
    }

    public static void globalLogSave(User user, int id, String name, Money money) {
        log(globalLog, makeSaveLog(id, name, money));
        log(userLog.get(user), makeSaveLog(id, name, money));
        DBManager.saveLog(user.getName(), getUserLog(user));
        DBManager.saveLog(globalLogName, globalLog.msg);
    }

    private static String makeWithdrawLog(int id, String name, Money money) {
        return String.format("[%s] %s withdrawed %s %f to account %d\n",
                             getCurrentTime(), name, money.currency, money.amount, id);
    }

    public static void globalLogWithdraw(User user, int id, String name, Money money) {
        log(globalLog, makeWithdrawLog(id, name, money));
        log(userLog.get(user), makeWithdrawLog(id, name, money));
        DBManager.saveLog(user.getName(), getUserLog(user));
        DBManager.saveLog(globalLogName, globalLog.msg);
    }

    private static String makeTransactionLog(int fromID, int toID, String name, Money money) {
        return String.format("[%s] %s transacted %s %f from account %d to account %d\n",
                             getCurrentTime(), name, money.currency, money.amount, fromID, toID);
    }

    public static void globalLogTransact(User user, int fromID, int toID, String name, Money money) {
        log(globalLog, makeTransactionLog(fromID, toID, name, money));
        log(userLog.get(user), makeTransactionLog(fromID, toID, name, money));
        DBManager.saveLog(user.getName(), getUserLog(user));
        DBManager.saveLog(globalLogName, globalLog.msg);
    }

    private static String makeCollectFeeLog(Money money) {
        return String.format("[%s] Collect %s %f fee\n",
                             getCurrentTime(), money.currency, money.amount);
    }

    public static void globalLogCollectFee(Money money) {
        log(globalLog, makeCollectFeeLog(money));
    }
}