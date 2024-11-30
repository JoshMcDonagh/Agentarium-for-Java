package utilities;

import java.sql.*;

public class DatabaseHandler {
    private static final String dbPath = "jdbc:sqlite:data.db";

    static {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS properties (name TEXT PRIMARY KEY, values TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS events (name TEXT PRIMARY KEY, values TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOrUpdate(String table, String key, String value) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String query = "INSERT INTO " + table + " (name, values) VALUES (?, ?) "
                    + "ON CONFLICT(name) DO UPDATE SET values = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.setString(3, value); // The value to update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String get(String table, String key) {
        try (Connection conn = DriverManager.getConnection(dbPath)) {
            String query = "SELECT values FROM " + table + " WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("values");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
