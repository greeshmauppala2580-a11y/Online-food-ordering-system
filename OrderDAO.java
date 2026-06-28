package com.foodorder.dao;
 
import com.foodorder.db.DBConnection;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Data-Access Object for orders and order_items tables.
 */
public class OrderDAO {
 
    /**
     * Persist an order (header + all line-items) inside a transaction.
     * @return the generated order_id, or -1 on failure.
     */
    public int placeOrder(Order order) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
 
            // 1. Insert order header
            String insertOrder = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, 'Placed')";
            PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.executeUpdate();
 
            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) { conn.rollback(); return -1; }
            int orderId = keys.getInt(1);
            order.setOrderId(orderId);
 
            // 2. Insert each line item
            String insertItem = "INSERT INTO order_items (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement psi = conn.prepareStatement(insertItem);
            for (OrderItem item : order.getItems()) {
                psi.setInt(1, orderId);
                psi.setInt(2, item.getItemId());
                psi.setInt(3, item.getQuantity());
                psi.setDouble(4, item.getPrice());
                psi.addBatch();
            }
            psi.executeBatch();
 
            conn.commit();
            return orderId;
 
        } catch (SQLException e) {
            System.err.println("Order placement error: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return -1;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
    }
 
    /** Fetch all orders (with their items) placed by a given user. */
    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getDouble("total_amount"),
                    rs.getString("status"),
                    rs.getTimestamp("order_date")
                );
                o.setItems(getOrderItems(o.getOrderId()));
                orders.add(o);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }
        return orders;
    }
 
    /** Fetch the line-items for a given order. */
    private List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, m.name AS item_name "
                   + "FROM order_items oi "
                   + "JOIN menu_items m ON oi.item_id = m.item_id "
                   + "WHERE oi.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem oi = new OrderItem(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
                oi.setOrderItemId(rs.getInt("order_item_id"));
                oi.setOrderId(orderId);
                items.add(oi);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order items: " + e.getMessage());
        }
        return items;
    }
}
 
