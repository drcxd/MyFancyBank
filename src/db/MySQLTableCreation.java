package db;

import db.MySQLDBUtil;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class MySQLTableCreation {
    // Run this as Java application to reset db schema.
    public static void main(String[] args) {
        try {
            // This is java.sql.Connection. Not com.mysql.jdbc.Connection.
            Connection conn = null;

            // Step 1 Connect to MySQL.
            try {
                System.out.println("Connecting to " + MySQLDBUtil.URL);
                Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
                conn = DriverManager.getConnection(MySQLDBUtil.URL);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            if (conn == null) {
                return;
            }
            // Step 2 Drop tables in case they exist.
            Statement stmt = conn.createStatement();

            String sql = "DROP TABLE IF EXISTS users_stock";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS logs";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS info";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS users";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS stocks";
            stmt.executeUpdate(sql);


            // Step 3 Create new tables
            sql = "CREATE TABLE users ("
                    + "name VARCHAR(255),"
                    + "account_id INTEGER,"
                    + "account_type INTEGER ,"
                    + "currency_type INTEGER ,"
                    + "money DOUBLE ,"
                    + "stock_id INTEGER ,"
                    + "stock_amount INTEGER,"
                    + "interest_rate DOUBLE,"
                    + "purchased_price_of_stock DOUBLE,"
                    + "PRIMARY KEY (name,account_id,currency_type,stock_id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE stocks ("
                    + "stock_id VARCHAR(255) NOT NULL,"
                    + "name VARCHAR(255),"
                    + "price DOUBLE,"
                    + "PRIMARY KEY (stock_id))";
            stmt.executeUpdate(sql);


            sql = "CREATE TABLE users_stock ("
                    + "name VARCHAR(255),"
                    + "account_id INTEGER,"
                    + "account_type INTEGER ,"
                    + "stock_id VARCHAR(255) NOT NULL,"
                    + "amount INTEGER,"
                    + "PRIMARY KEY (name, stock_id),"
                    + "FOREIGN KEY (stock_id) REFERENCES stocks(stock_id),"
                    + "FOREIGN KEY (name) REFERENCES users(name))";
            stmt.executeUpdate(sql);


            sql =  "CREATE TABLE logs ("
                    + "name VARCHAR(255),"
                    + "log  VARCHAR(8000))";
            stmt.executeUpdate(sql);

            sql =  "CREATE TABLE info ("
                    + "name VARCHAR(255),"
                    + "Value DOUBLE, PRIMARY KEY (name))";
            stmt.executeUpdate(sql);


            // Step 4: insert data
//            sql = "INSERT INTO users VALUES ("
//                    + "'1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith')";
//            System.out.println("Executing query: " + sql);
//            stmt.executeUpdate(sql);
            System.out.println("Import is done successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}