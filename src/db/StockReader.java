package db;
import stock.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StockReader extends BaseDBReader {
    public StockReader() {
        initConnection();
    }
    public Stock getById(int id) {
        try {
            String sql = "SELECT * FROM stocks WHERE stock_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            Stock.Builder builder = new Stock.Builder();
            rs.next();
            builder.setId(Integer.valueOf(rs.getString("stock_id")));
            builder.setName(rs.getString("name"));
            builder.setPrice(Double.valueOf(rs.getString("price")));
            return builder.build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Stock stock) {
        try {
            String sql = "INSERT IGNORE INTO stocks VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(stock.getId()));
            stmt.setString(2, stock.getName());
            stmt.setDouble(3, stock.getPrice());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getAllIds() {
        List<Integer> res = new ArrayList<>();
        try {
            String sql = "SELECT stock_id from stocks where stock_id != 0";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                res.add(Integer.valueOf(rs.getString("stock_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void delete(int id) {
        try {
            String sql = "Delete from stocks where stock_id = " + id;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
