package com.foodorder;
 
import com.foodorder.gui.LoginFrame;
 
/**
 * Entry point for the Food Ordering System.
 * Run this class to start the application.
 */
public class Main {
    public static void main(String[] args) {
        // Launch the login window on the AWT event thread
        java.awt.EventQueue.invokeLater(LoginFrame::new);
    }
}
 
