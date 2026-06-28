package com.foodorder.gui;
 
import com.foodorder.dao.OrderDAO;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.util.SessionManager;
 
import java.awt.*;
import java.awt.event.*;
import java.util.List;
 
/**
 * Shows the logged-in user's past orders with item details.
 */
public class OrderHistoryFrame extends Frame implements ActionListener {
 
    private final TextArea  taOrders  = new TextArea("", 20, 60, TextArea.SCROLLBARS_VERTICAL_ONLY);
    private final Button    btnBack   = new Button("← Back to Dashboard");
    private final Button    btnRefresh = new Button("Refresh");
 
    private final OrderDAO  orderDAO  = new OrderDAO();
 
    public OrderHistoryFrame() {
        setTitle("My Orders – Food Ordering System");
        setSize(600, 500);
        setLayout(new BorderLayout(8, 8));
        setBackground(new Color(255, 248, 225));
 
        // Header
        Panel header = new Panel(new BorderLayout());
        header.setBackground(new Color(230, 74, 25));
        Label title = new Label("  My Orders", Label.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
 
        // Orders text area
        taOrders.setEditable(false);
        taOrders.setFont(new Font("Monospaced", Font.PLAIN, 12));
        taOrders.setBackground(Color.WHITE);
 
        // Bottom buttons
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        btnPanel.setBackground(new Color(255, 248, 225));
        styleBtn(btnBack,    new Color(96, 125, 139), Color.WHITE);
        styleBtn(btnRefresh, new Color(230, 74, 25),  Color.WHITE);
        btnBack.addActionListener(this);
        btnRefresh.addActionListener(this);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnBack);
 
        add(header,   BorderLayout.NORTH);
        add(taOrders, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
 
        loadOrders();
 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
 
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private void loadOrders() {
        int userId = SessionManager.getCurrentUser().getUserId();
        List<Order> orders = orderDAO.getOrdersByUser(userId);
 
        if (orders.isEmpty()) {
            taOrders.setText("You have not placed any orders yet.");
            return;
        }
 
        StringBuilder sb = new StringBuilder();
        for (Order o : orders) {
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append(String.format("Order #%-6d  Status: %-10s  Date: %s%n",
                o.getOrderId(), o.getStatus(), o.getOrderDate()));
            sb.append("─────────────────────────────────────────────\n");
            for (OrderItem item : o.getItems()) {
                sb.append(String.format("  %-25s x%d  ₹%.2f%n",
                    item.getItemName(), item.getQuantity(), item.getSubtotal()));
            }
            sb.append(String.format("  %-25s     ₹%.2f%n", "TOTAL", o.getTotalAmount()));
            sb.append("\n");
        }
        taOrders.setText(sb.toString());
        taOrders.setCaretPosition(0);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new DashboardFrame();
        } else if (e.getSource() == btnRefresh) {
            loadOrders();
        }
    }
 
    private void styleBtn(Button btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(180, 34));
    }
}
 
