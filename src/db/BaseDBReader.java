package db;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
public abstract class BaseDBReader {
    protected Connection conn = null;
    protected void initConnection() {
        try {
            // Connect to MySQL.
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
