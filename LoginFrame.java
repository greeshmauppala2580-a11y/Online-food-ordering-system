package com.foodorder.gui;
 
import com.foodorder.dao.UserDAO;
import com.foodorder.model.User;
import com.foodorder.util.SessionManager;
 
import java.awt.*;
import java.awt.event.*;
 
/**
 * AWT-based Login window.
 * Allows users to log in or navigate to the registration screen.
 */
public class LoginFrame extends Frame implements ActionListener {
 
    private final TextField txtUsername = new TextField(20);
    private final TextField txtPassword = new TextField(20);
    private final Button    btnLogin    = new Button("Login");
    private final Button    btnRegister = new Button("Register");
    private final Label     lblMsg      = new Label("", Label.CENTER);
 
    public LoginFrame() {
        setTitle("Food Ordering System – Login");
        setSize(420, 320);
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 248, 225));
        setResizable(false);
 
        // ---- Title panel ----
        Panel titlePanel = new Panel();
        titlePanel.setBackground(new Color(230, 74, 25));
        Label title = new Label("🍽  Food Ordering System", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
 
        // ---- Form panel ----
        Panel formPanel = new Panel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 248, 225));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
 
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new Label("Username:"), gbc);
        gbc.gridx = 1; formPanel.add(txtUsername, gbc);
 
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new Label("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword.setEchoChar('*');
        formPanel.add(txtPassword, gbc);
 
        // ---- Button panel ----
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        btnPanel.setBackground(new Color(255, 248, 225));
        styleButton(btnLogin,  new Color(230, 74, 25), Color.WHITE);
        styleButton(btnRegister, new Color(66, 165, 245), Color.WHITE);
        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
 
        // ---- Message label ----
        lblMsg.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMsg.setForeground(Color.RED);
 
        Panel centerPanel = new Panel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 248, 225));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(lblMsg,    BorderLayout.SOUTH);
 
        add(titlePanel,  BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(btnPanel,    BorderLayout.SOUTH);
 
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
 
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
        } else if (e.getSource() == btnRegister) {
            dispose();
            new RegisterFrame();
        }
    }
 
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
 
        if (username.isEmpty() || password.isEmpty()) {
            lblMsg.setText("Please enter both username and password.");
            return;
        }
 
        UserDAO dao  = new UserDAO();
        User    user = dao.login(username, password);
 
        if (user != null) {
            SessionManager.setCurrentUser(user);
            lblMsg.setForeground(new Color(27, 94, 32));
            lblMsg.setText("Login successful! Welcome, " + user.getFullName());
            dispose();
            new DashboardFrame();
        } else {
            lblMsg.setForeground(Color.RED);
            lblMsg.setText("Invalid username or password.");
        }
    }
 
    private void styleButton(Button btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 34));
    }
}
 
