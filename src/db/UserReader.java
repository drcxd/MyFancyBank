package db;

import DBUser.DBUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserReader extends BaseDBReader {
    public UserReader() {
        initConnection();
    }
    public boolean insert( DBUser u ) {
        try {
            String sql = "INSERT " +
                         "INTO users " +
                         "(name, account_id, account_type, currency_type, money, stock_id, stock_amount, interest_rate,purchased_price_of_stock) "+
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)" +
                         "ON DUPLICATE KEY UPDATE " +
                         "name = VALUES(name), " +
                         "account_id = VALUES(account_id)," +
                         "account_type = VALUES(account_type)," +
                         "currency_type = VALUES(currency_type)," +
                         "money = VALUES(money)," +
                         "stock_id = VALUES(stock_id)," +
                         "stock_amount = VALUES(stock_amount)," +
                         "interest_rate = VALUES(interest_rate)," +
                         "purchased_price_of_stock = VALUES(purchased_price_of_stock)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getName());
            stmt.setInt(2, u.getAccount_id());
            stmt.setInt(3, u.getAccount_type());
            stmt.setInt(4, u.getCurrency_type());
            stmt.setDouble(5, u.getMoney());
            stmt.setInt(6,u.getStock_id());
            stmt.setInt(7,u.getStock_amount());
            stmt.setDouble(8,u.getInterest());
            stmt.setDouble(9,u.getPurchased_price_of_stock());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //default : checking : 0 , saving : 1, stock : 2

    // get saving account , change to return type to Acccount if neccessary
    // Right now it returns the money stored in saving account


    public double getCheckingByName(String name){
        double checking_amount = 0.0;
        try {
            String sql = "SELECT money FROM users WHERE name = ? AND account_type = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, 0);
            ResultSet rs = stmt.executeQuery();
            ArrayList user_list = new ArrayList();

            while (rs.next()){
                checking_amount = rs.getDouble(1);
            }
            return checking_amount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checking_amount;

    }



    public ArrayList<String> getAllUser(){
        ArrayList<String> User_name = new ArrayList<String>();
        try {
            String sql = "SELECT DISTINCT name FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                User_name.add( rs.getString(1) );
            }
            return User_name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return User_name;

    }

    public ArrayList<Integer> getAccountId(String name){
        ArrayList<Integer> Accounts = new ArrayList<Integer>();
        try {
            String sql = "SELECT DISTINCT account_id FROM users WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Accounts.add( rs.getInt(1) );
            }
            return Accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Accounts;

    }




    public double getSavingingByName(String name){
        double saving_amount = 0.0;
        try {
            String sql = "SELECT money FROM users WHERE name = ? AND account_type = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, 1);
            ResultSet rs = stmt.executeQuery();
            ArrayList user_list = new ArrayList();

            while (rs.next()){
                saving_amount = rs.getDouble(1);
            }
            return saving_amount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saving_amount;

    }

    // get all the stock ids a user bought
    public ArrayList<Integer> getStockIdByName(String name) {
        try {
            String sql = "SELECT stock_id FROM users WHERE name = ? AND stock_id > 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            ArrayList stock_id_list = new ArrayList();

            while (rs.next()){

                stock_id_list.add(rs.getInt(1));
            }
            return stock_id_list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // get info for all accounts for a specic user
    public ArrayList<DBUser> getByName(String name) {
        try {
            String sql = "SELECT * FROM users WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            ArrayList user_list = new ArrayList();

            while (rs.next()){
                DBUser.Builder builder = new DBUser.Builder();
                String user_name = rs.getString(1);
                int acc_id = rs.getInt(2);
                int acc_type = rs.getInt(3);
                int curr_type = rs.getInt(4);
                double money_ = rs.getDouble(5);
                int stock_id = rs.getInt(6);
                int stock_amount = rs.getInt(7);
                double interest_rate = rs.getDouble(8);
                double purchased_price_of_stock = rs.getDouble(9);
                /*
                System.out.println(" name = " + user_name + " account_id: " + acc_id + " account_type" + acc_type
                +" currency_type:" + curr_type + " money in account : " + money_);
                 */
                builder.setName(user_name);
                builder.setAcc_id(acc_id);
                builder.setAcc_type(acc_type);
                builder.setCurr_type(curr_type);
                builder.setMoney(money_);
                builder.setStock_id(stock_id);
                builder.setStock_amount(stock_amount);
                builder.setInterest(interest_rate);
                builder.setPurchased_price_of_stock(purchased_price_of_stock);

                user_list.add(builder.build());
            }
            return user_list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // get info for all accounts for a specic user
    public ArrayList<DBUser> getByAccountId(int account_id) {
        try {
            String sql = "SELECT * FROM users WHERE account_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, account_id);
            ResultSet rs = stmt.executeQuery();
            ArrayList user_list = new ArrayList();

            while (rs.next()){
                DBUser.Builder builder = new DBUser.Builder();
                String user_name = rs.getString(1);
                int acc_id = rs.getInt(2);
                int acc_type = rs.getInt(3);
                int curr_type = rs.getInt(4);
                double money_ = rs.getDouble(5);
                int stock_id = rs.getInt(6);
                int stock_amount = rs.getInt(7);
                double interest_rate = rs.getDouble(8);

                /*
                System.out.println(" name = " + user_name + " account_id: " + acc_id + " account_type" + acc_type
                +" currency_type:" + curr_type + " money in account : " + money_);
                 */
                builder.setName(user_name);
                builder.setAcc_id(acc_id);
                builder.setAcc_type(acc_type);
                builder.setCurr_type(curr_type);
                builder.setMoney(money_);
                builder.setStock_id(stock_id);
                builder.setStock_amount(stock_amount);
                builder.setInterest(interest_rate);

                user_list.add(builder.build());
            }
            return user_list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update_account(int account_id, int currency_type, double amount){
        try {
            String sql = "UPDATE users " +
                         "SET  money  = ? " +
                         "WHERE account_id = ? AND currency_type = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setInt(2, account_id);
            stmt.setInt(3, currency_type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update_stock(int account_id, int stock_id, int stock_amount, double purchased_price_of_stock ){
        try {
            String sql = "UPDATE users " +
                         "SET  stock_amount  = ?, " +
                         "purchased_price_of_stock = ?" +
                         "WHERE account_id = ? AND stock_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, stock_amount);
            stmt.setDouble(2, purchased_price_of_stock);
            stmt.setInt(3, account_id);
            stmt.setInt(4, stock_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete_account(int account_id){
        try {
            String sql = "DELETE " +
                         "FROM users " +
                         "WHERE account_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, account_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update_interest_rate(int account_id, double interest_rate){
        try {
            String sql = "UPDATE users " +
                         "SET  interest_rate  = ? " +
                         "WHERE account_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, interest_rate);
            stmt.setInt(2, account_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
