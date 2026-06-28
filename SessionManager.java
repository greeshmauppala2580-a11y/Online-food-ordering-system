package com.foodorder.util;
 
import com.foodorder.model.User;
 
/**
 * Holds the currently logged-in user for the lifetime of the application.
 * Acts as a simple in-memory session.
 */
public class SessionManager {
 
    private static User currentUser = null;
 
    private SessionManager() {}
 
    public static void setCurrentUser(User user) { currentUser = user; }
    public static User getCurrentUser()          { return currentUser; }
    public static boolean isLoggedIn()           { return currentUser != null; }
    public static void logout()                  { currentUser = null; }
}
 
