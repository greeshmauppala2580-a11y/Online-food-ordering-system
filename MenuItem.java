package com.foodorder.model;
 
/**
 * Represents a single item on the food menu.
 */
public class MenuItem {
 
    private int     itemId;
    private String  name;
    private String  description;
    private double  price;
    private int     categoryId;
    private String  categoryName;
    private boolean available;
 
    public MenuItem() {}
 
    public MenuItem(int itemId, String name, String description,
                    double price, int categoryId, String categoryName, boolean available) {
        this.itemId       = itemId;
        this.name         = name;
        this.description  = description;
        this.price        = price;
        this.categoryId   = categoryId;
        this.categoryName = categoryName;
        this.available    = available;
    }
 
    // Getters
    public int     getItemId()       { return itemId; }
    public String  getName()         { return name; }
    public String  getDescription()  { return description; }
    public double  getPrice()        { return price; }
    public int     getCategoryId()   { return categoryId; }
    public String  getCategoryName() { return categoryName; }
    public boolean isAvailable()     { return available; }
 
    // Setters
    public void setItemId(int id)            { this.itemId       = id; }
    public void setName(String n)            { this.name         = n; }
    public void setDescription(String d)     { this.description  = d; }
    public void setPrice(double p)           { this.price        = p; }
    public void setCategoryId(int cid)       { this.categoryId   = cid; }
    public void setCategoryName(String cn)   { this.categoryName = cn; }
    public void setAvailable(boolean avail)  { this.available    = avail; }
 
    @Override
    public String toString() {
        return name + " - ₹" + String.format("%.2f", price);
    }
}
 
