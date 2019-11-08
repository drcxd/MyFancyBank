package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogReader extends BaseDBReader{

    public LogReader() {
        initConnection();
    }

    // Insert logs
    public boolean insert(String name, String log) {
        try {
            String sql = "INSERT  INTO logs VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, log);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // get all logs and return as string
    public String getLog_by_Name(String name) {
        String result = "" ;
        try {
            String sql = "SELECT * FROM logs AS l WHERE l.name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            // concatenate all the strings
            while (rs.next()){
                result += rs.getString("log");
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail to retrieve log";
    }




}
