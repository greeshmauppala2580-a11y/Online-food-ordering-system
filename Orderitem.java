package com.foodorder.model;
 
/**
 * Represents one line-item within an Order.
 */
public class OrderItem {
 
    private int    orderItemId;
    private int    orderId;
    private int    itemId;
    private String itemName;
    private int    quantity;
    private double price;
 
    public OrderItem() {}
 
    public OrderItem(int itemId, String itemName, int quantity, double price) {
        this.itemId   = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price    = price;
    }
 
    // Getters
    public int    getOrderItemId() { return orderItemId; }
    public int    getOrderId()     { return orderId; }
    public int    getItemId()      { return itemId; }
    public String getItemName()    { return itemName; }
    public int    getQuantity()    { return quantity; }
    public double getPrice()       { return price; }
    public double getSubtotal()    { return price * quantity; }
 
    // Setters
    public void setOrderItemId(int id)   { this.orderItemId = id; }
    public void setOrderId(int oid)      { this.orderId     = oid; }
    public void setItemId(int iid)       { this.itemId      = iid; }
    public void setItemName(String n)    { this.itemName    = n; }
    public void setQuantity(int q)       { this.quantity    = q; }
    public void setPrice(double p)       { this.price       = p; }
 
    @Override
    public String toString() {
        return itemName + " x" + quantity + " = ₹" + String.format("%.2f", getSubtotal());
    }
}
 
