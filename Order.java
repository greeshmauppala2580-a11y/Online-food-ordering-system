package com.foodorder.model;
 
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Represents a customer order.
 */
public class Order {
 
    private int       orderId;
    private int       userId;
    private double    totalAmount;
    private String    status;
    private Timestamp orderDate;
    private List<OrderItem> items;
 
    public Order() {
        items = new ArrayList<>();
    }
 
    public Order(int orderId, int userId, double totalAmount,
                 String status, Timestamp orderDate) {
        this.orderId     = orderId;
        this.userId      = userId;
        this.totalAmount = totalAmount;
        this.status      = status;
        this.orderDate   = orderDate;
        this.items       = new ArrayList<>();
    }
 
    // Getters
    public int             getOrderId()     { return orderId; }
    public int             getUserId()      { return userId; }
    public double          getTotalAmount() { return totalAmount; }
    public String          getStatus()      { return status; }
    public Timestamp       getOrderDate()   { return orderDate; }
    public List<OrderItem> getItems()       { return items; }
 
    // Setters
    public void setOrderId(int id)            { this.orderId     = id; }
    public void setUserId(int uid)            { this.userId      = uid; }
    public void setTotalAmount(double amt)    { this.totalAmount = amt; }
    public void setStatus(String s)           { this.status      = s; }
    public void setOrderDate(Timestamp ts)    { this.orderDate   = ts; }
    public void setItems(List<OrderItem> its) { this.items       = its; }
 
    public void addItem(OrderItem item) { items.add(item); }
 
    @Override
    public String toString() {
        return "Order#" + orderId + " | ₹" + String.format("%.2f", totalAmount)
               + " | " + status + " | " + orderDate;
    }
}
 
