package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InfoReader extends BaseDBReader {
    public InfoReader() {
        initConnection();
    }

    // Insert logs
    public boolean insert(String name, Double value) {
        try {
            String sql = "INSERT " +
                    "INTO info " +
                    "(name, Value) "+
                    "VALUES (?, ?)" +
                    "ON DUPLICATE KEY UPDATE " +
                    "name = VALUES(name), " +
                    "Value = VALUES(Value)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, value);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // get all logs and return as string
    public Double getInfo(String name) {
        String result = "" ;
        try {
            String sql = "SELECT * FROM info WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            // concatenate all the strings
            while (rs.next()){
                result += rs.getString("Value");
            }

            return Double.parseDouble(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1.0;
    }
}
