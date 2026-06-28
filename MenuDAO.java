package com.foodorder.dao;
 
import com.foodorder.db.DBConnection;
import com.foodorder.model.MenuItem;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Data-Access Object for menu_items and categories.
 */
public class MenuDAO {
 
    /** Fetch all available menu items joined with their category name. */
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT m.*, c.category_name "
                   + "FROM menu_items m "
                   + "JOIN categories c ON m.category_id = c.category_id "
                   + "WHERE m.available = 1 "
                   + "ORDER BY c.category_name, m.name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu: " + e.getMessage());
        }
        return items;
    }
 
    /** Fetch items belonging to a specific category. */
    public List<MenuItem> getItemsByCategory(int categoryId) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT m.*, c.category_name "
                   + "FROM menu_items m "
                   + "JOIN categories c ON m.category_id = c.category_id "
                   + "WHERE m.category_id = ? AND m.available = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) items.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Error fetching by category: " + e.getMessage());
        }
        return items;
    }
 
    /** Fetch all distinct categories. Returns [categoryId, categoryName] pairs. */
    public List<String[]> getAllCategories() {
        List<String[]> cats = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM categories ORDER BY category_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                cats.add(new String[]{rs.getString("category_id"), rs.getString("category_name")});
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        return cats;
    }
 
    private MenuItem mapRow(ResultSet rs) throws SQLException {
        return new MenuItem(
            rs.getInt("item_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getInt("category_id"),
            rs.getString("category_name"),
            rs.getBoolean("available")
        );
    }
}
 
