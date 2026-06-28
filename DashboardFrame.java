package com.foodorder.gui;
 
import com.foodorder.util.SessionManager;
 
import java.awt.*;
import java.awt.event.*;
 
/**
 * Main dashboard shown after successful login.
 * Provides navigation to Browse Menu, My Orders, and Logout.
 */
public class DashboardFrame extends Frame implements ActionListener {
 
    private final Button btnMenu    = new Button("Browse Menu & Order");
    private final Button btnOrders  = new Button("My Orders");
    private final Button btnLogout  = new Button("Logout");
 
    public DashboardFrame() {
        String name = SessionManager.getCurrentUser().getFullName();
 
        setTitle("Dashboard – Food Ordering System");
        setSize(420, 340);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 248, 225));
        setResizable(false);
 
        // Header
        Panel header = new Panel(new BorderLayout());
        header.setBackground(new Color(230, 74, 25));
        header.setPreferredSize(new Dimension(420, 70));
        Label lbl1 = new Label("🍽  Food Ordering System", Label.CENTER);
        lbl1.setFont(new Font("SansSerif", Font.BOLD, 17));
        lbl1.setForeground(Color.WHITE);
        Label lbl2 = new Label("Welcome, " + name + "!", Label.CENTER);
        lbl2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl2.setForeground(new Color(255, 204, 128));
        header.add(lbl1, BorderLayout.CENTER);
        header.add(lbl2, BorderLayout.SOUTH);
 
        // Buttons panel
        Panel center = new Panel(new GridLayout(3, 1, 0, 14));
        center.setBackground(new Color(255, 248, 225));
        Insets pad = new Insets(20, 60, 20, 60);
 
        styleBtn(btnMenu,   new Color(230, 74, 25),  Color.WHITE);
        styleBtn(btnOrders, new Color(66, 165, 245),  Color.WHITE);
        styleBtn(btnLogout, new Color(96, 125, 139),  Color.WHITE);
 
        center.add(btnMenu);
        center.add(btnOrders);
        center.add(btnLogout);
 
        Panel wrapper = new Panel(new BorderLayout());
        wrapper.setBackground(new Color(255, 248, 225));
        wrapper.add(center, BorderLayout.CENTER);
 
        Panel padded = new Panel(new FlowLayout(FlowLayout.CENTER));
        padded.setBackground(new Color(255, 248, 225));
        padded.add(wrapper);
 
        add(header,  BorderLayout.NORTH);
        add(padded,  BorderLayout.CENTER);
 
        btnMenu.addActionListener(this);
        btnOrders.addActionListener(this);
        btnLogout.addActionListener(this);
 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
 
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMenu) {
            dispose();
            new MenuFrame();
        } else if (e.getSource() == btnOrders) {
            dispose();
            new OrderHistoryFrame();
        } else if (e.getSource() == btnLogout) {
            SessionManager.logout();
            dispose();
            new LoginFrame();
        }
    }
 
    private void styleBtn(Button btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(260, 42));
    }
}
 
