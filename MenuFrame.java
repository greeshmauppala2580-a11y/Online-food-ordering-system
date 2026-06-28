package com.foodorder.gui;
 
import com.foodorder.dao.MenuDAO;
import com.foodorder.dao.OrderDAO;
import com.foodorder.model.MenuItem;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.util.SessionManager;
 
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
 
/**
 * Browse all menu items and add them to a cart, then place the order.
 */
public class MenuFrame extends Frame implements ActionListener {
 
    // Cart: itemId -> [OrderItem]
    private final Map<Integer, OrderItem> cart = new LinkedHashMap<>();
 
    private final List<MenuItem> menuItems;
    private final List<String[]> categories;
 
    private List<Button>    addButtons  = new ArrayList<>();
    private List<MenuItem>  displayed   = new ArrayList<>();
 
    private final java.awt.List categoryList = new java.awt.List(6);
    private final Panel    menuPanel    = new Panel(new GridLayout(0, 1, 0, 4));
    private final TextArea taCart       = new TextArea(8, 30);
    private final Label    lblTotal     = new Label("Total: ₹0.00", Label.RIGHT);
    private final Button   btnOrder     = new Button("Place Order");
    private final Button   btnClear     = new Button("Clear Cart");
    private final Button   btnBack      = new Button("← Back");
 
    private final MenuDAO  menuDAO  = new MenuDAO();
    private final OrderDAO orderDAO = new OrderDAO();
 
    public MenuFrame() {
        setTitle("Menu – Food Ordering System");
        setSize(820, 580);
        setBackground(new Color(255, 248, 225));
        setLayout(new BorderLayout(8, 8));
        setResizable(true);
 
        menuItems  = menuDAO.getAllMenuItems();
        categories = menuDAO.getAllCategories();
 
        buildUI();
        loadCategory(-1); // load all initially
 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
 
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private void buildUI() {
        // ---- Top bar ----
        Panel topBar = new Panel(new BorderLayout());
        topBar.setBackground(new Color(230, 74, 25));
        Label title = new Label("  Browse Menu", Label.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        styleBtn(btnBack, new Color(255, 255, 255), new Color(230, 74, 25));
        btnBack.addActionListener(this);
        topBar.add(title,   BorderLayout.CENTER);
        topBar.add(btnBack, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
 
        // ---- Left: categories ----
        Panel leftPanel = new Panel(new BorderLayout(0, 4));
        leftPanel.setBackground(new Color(255, 248, 225));
        leftPanel.setPreferredSize(new Dimension(170, 0));
        Label catLabel = new Label("Categories", Label.CENTER);
        catLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
 
        categoryList.add("All Items");
        for (String[] cat : categories) categoryList.add(cat[1]);
        categoryList.select(0);
        categoryList.addActionListener(this);
 
        leftPanel.add(catLabel,    BorderLayout.NORTH);
        leftPanel.add(categoryList, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
 
        // ---- Center: menu items (scrollable) ----
        menuPanel.setBackground(new Color(255, 248, 225));
        ScrollPane scrollMenu = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollMenu.add(menuPanel);
        add(scrollMenu, BorderLayout.CENTER);
 
        // ---- Right: cart ----
        Panel cartPanel = new Panel(new BorderLayout(4, 6));
        cartPanel.setBackground(new Color(255, 248, 225));
        cartPanel.setPreferredSize(new Dimension(240, 0));
 
        Label cartTitle = new Label("Your Cart", Label.CENTER);
        cartTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
 
        taCart.setEditable(false);
        taCart.setFont(new Font("Monospaced", Font.PLAIN, 11));
 
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTotal.setForeground(new Color(230, 74, 25));
 
        Panel cartBtns = new Panel(new GridLayout(2, 1, 0, 4));
        styleBtn(btnOrder, new Color(46, 125, 50),  Color.WHITE);
        styleBtn(btnClear, new Color(198, 40, 40),  Color.WHITE);
        btnOrder.addActionListener(this);
        btnClear.addActionListener(this);
        cartBtns.add(btnOrder);
        cartBtns.add(btnClear);
 
        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartPanel.add(taCart,   BorderLayout.CENTER);
        cartPanel.add(lblTotal, BorderLayout.AFTER_LAST_LINE);
        cartPanel.add(cartBtns, BorderLayout.SOUTH);
 
        add(cartPanel, BorderLayout.EAST);
    }
 
    private void loadCategory(int categoryId) {
        menuPanel.removeAll();
        addButtons.clear();
        displayed.clear();
 
        List<MenuItem> filtered = (categoryId == -1)
            ? menuItems
            : new ArrayList<>();
 
        if (categoryId != -1) {
            for (MenuItem m : menuItems)
                if (m.getCategoryId() == categoryId) filtered.add(m);
        }
 
        for (MenuItem item : filtered) {
            displayed.add(item);
 
            Panel row = new Panel(new BorderLayout(6, 2));
            row.setBackground(Color.WHITE);
 
            Label name = new Label(item.getName());
            name.setFont(new Font("SansSerif", Font.BOLD, 12));
 
            Label desc = new Label(item.getDescription(), Label.LEFT);
            desc.setFont(new Font("SansSerif", Font.PLAIN, 11));
            desc.setForeground(Color.DARK_GRAY);
 
            Label price = new Label("₹" + String.format("%.2f", item.getPrice()), Label.RIGHT);
            price.setFont(new Font("SansSerif", Font.BOLD, 12));
            price.setForeground(new Color(230, 74, 25));
 
            Panel info = new Panel(new GridLayout(2, 1));
            info.setBackground(Color.WHITE);
            info.add(name);
            info.add(desc);
 
            Button btnAdd = new Button("+ Add");
            btnAdd.setBackground(new Color(230, 74, 25));
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setFont(new Font("SansSerif", Font.BOLD, 11));
            btnAdd.addActionListener(this);
            addButtons.add(btnAdd);
 
            row.add(info,   BorderLayout.CENTER);
            row.add(price,  BorderLayout.WEST);
            row.add(btnAdd, BorderLayout.EAST);
 
            menuPanel.add(row);
        }
 
        menuPanel.revalidate();
        menuPanel.repaint();
    }
 
    private void refreshCart() {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (OrderItem oi : cart.values()) {
            sb.append(oi.getItemName())
              .append(" x").append(oi.getQuantity())
              .append(" = ₹").append(String.format("%.2f", oi.getSubtotal()))
              .append("\n");
            total += oi.getSubtotal();
        }
        taCart.setText(sb.toString());
        lblTotal.setText("Total: ₹" + String.format("%.2f", total));
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new DashboardFrame();
 
        } else if (e.getSource() == btnClear) {
            cart.clear();
            refreshCart();
 
        } else if (e.getSource() == btnOrder) {
            handlePlaceOrder();
 
        } else if (e.getSource() == categoryList) {
            int idx = categoryList.getSelectedIndex();
            if (idx == 0) {
                loadCategory(-1);
            } else {
                int catId = Integer.parseInt(categories.get(idx - 1)[0]);
                loadCategory(catId);
            }
 
        } else {
            // Check if an "Add" button was clicked
            for (int i = 0; i < addButtons.size(); i++) {
                if (e.getSource() == addButtons.get(i)) {
                    MenuItem item = displayed.get(i);
                    if (cart.containsKey(item.getItemId())) {
                        OrderItem oi = cart.get(item.getItemId());
                        oi.setQuantity(oi.getQuantity() + 1);
                    } else {
                        cart.put(item.getItemId(),
                            new OrderItem(item.getItemId(), item.getName(), 1, item.getPrice()));
                    }
                    refreshCart();
                    break;
                }
            }
        }
    }
 
    private void handlePlaceOrder() {
        if (cart.isEmpty()) {
            showDialog("Cart is empty. Please add items first.");
            return;
        }
 
        int userId = SessionManager.getCurrentUser().getUserId();
        double total = cart.values().stream().mapToDouble(OrderItem::getSubtotal).sum();
 
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setItems(new ArrayList<>(cart.values()));
 
        int orderId = orderDAO.placeOrder(order);
        if (orderId > 0) {
            cart.clear();
            refreshCart();
            showDialog("Order placed successfully!\nOrder ID: " + orderId
                     + "\nTotal: ₹" + String.format("%.2f", total));
        } else {
            showDialog("Failed to place order. Please try again.");
        }
    }
 
    private void showDialog(String msg) {
        Dialog d = new Dialog(this, "Food Ordering System", true);
        d.setSize(320, 150);
        d.setLayout(new BorderLayout(8, 8));
        d.setBackground(new Color(255, 248, 225));
        Label lbl = new Label(msg, Label.CENTER);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        Button ok = new Button("OK");
        ok.addActionListener(ev -> d.dispose());
        d.add(lbl, BorderLayout.CENTER);
        d.add(ok,  BorderLayout.SOUTH);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }
 
    private void styleBtn(Button btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
    }
}
