package com.foodorder.model;
 
/**
 * Represents a registered user of the system.
 */
public class User {
 
    private int    userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String address;
 
    public User() {}
 
    public User(int userId, String username, String email,
                String fullName, String phone, String address) {
        this.userId   = userId;
        this.username = username;
        this.email    = email;
        this.fullName = fullName;
        this.phone    = phone;
        this.address  = address;
    }
 
    // Getters
    public int    getUserId()   { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }
    public String getFullName() { return fullName; }
    public String getPhone()    { return phone; }
    public String getAddress()  { return address; }
 
    // Setters
    public void setUserId(int userId)       { this.userId   = userId; }
    public void setUsername(String u)       { this.username = u; }
    public void setPassword(String p)       { this.password = p; }
    public void setEmail(String e)          { this.email    = e; }
    public void setFullName(String n)       { this.fullName = n; }
    public void setPhone(String ph)         { this.phone    = ph; }
    public void setAddress(String a)        { this.address  = a; }
 
    @Override
    public String toString() {
        return "User{id=" + userId + ", username='" + username + "'}";
    }
}
 
