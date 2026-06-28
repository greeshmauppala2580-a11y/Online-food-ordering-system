package com.foodorder.dao;
 
import com.foodorder.db.DBConnection;
import com.foodorder.model.User;
 
import java.sql.*;
 
/**
 * Data-Access Object for the users table.
 */
public class UserDAO {
 
    /**
     * Validate login credentials.
     * @return User object on success, null on failure.
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }
 
    /**
     * Register a new user.
     * @return true if registration succeeded.
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, email, full_name, phone, address) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
 
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
 
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) user.setUserId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
        }
        return false;
    }
 
    /** Check whether a username is already taken. */
    public boolean usernameExists(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Username check error: " + e.getMessage());
        }
        return false;
    }
 
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setFullName(rs.getString("full_name"));
        u.setPhone(rs.getString("phone"));
        u.setAddress(rs.getString("address"));
        return u;
    }
}
