package com.foodorder.gui;
 
import com.foodorder.dao.UserDAO;
import com.foodorder.model.User;
 
import java.awt.*;
import java.awt.event.*;
 
/**
 * AWT-based user registration screen.
 */
public class RegisterFrame extends Frame implements ActionListener {
 
    private final TextField txtFullName = new TextField(25);
    private final TextField txtUsername = new TextField(25);
    private final TextField txtEmail    = new TextField(25);
    private final TextField txtPassword = new TextField(25);
    private final TextField txtPhone    = new TextField(25);
    private final TextArea  taAddress   = new TextArea(3, 25);
    private final Button    btnRegister = new Button("Create Account");
    private final Button    btnBack     = new Button("Back to Login");
    private final Label     lblMsg      = new Label("", Label.CENTER);
 
    public RegisterFrame() {
        setTitle("Register – Food Ordering System");
        setSize(480, 500);
        setLayout(new BorderLayout(8, 8));
        setBackground(new Color(255, 248, 225));
        setResizable(false);
 
        // Title
        Panel titlePanel = new Panel();
        titlePanel.setBackground(new Color(230, 74, 25));
        Label title = new Label("Create New Account", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
 
        // Form
        Panel form = new Panel(new GridBagLayout());
        form.setBackground(new Color(255, 248, 225));
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(5, 10, 5, 10);
        g.anchor  = GridBagConstraints.WEST;
        g.fill    = GridBagConstraints.HORIZONTAL;
 
        String[] labels = {"Full Name:", "Username:", "Email:", "Password:", "Phone:", "Address:"};
        Component[] fields = {txtFullName, txtUsername, txtEmail, txtPassword, txtPhone, taAddress};
 
        txtPassword.setEchoChar('*');
 
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            form.add(new Label(labels[i]), g);
            g.gridx = 1; g.weightx = 1;
            form.add(fields[i], g);
        }
 
        // Buttons
        Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        btnPanel.setBackground(new Color(255, 248, 225));
        styleBtn(btnRegister, new Color(46, 125, 50), Color.WHITE);
        styleBtn(btnBack,     new Color(96, 125, 139), Color.WHITE);
        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);
 
        lblMsg.setFont(new Font("SansSerif", Font.PLAIN, 12));
 
        Panel center = new Panel(new BorderLayout());
        center.setBackground(new Color(255, 248, 225));
        center.add(form,   BorderLayout.CENTER);
        center.add(lblMsg, BorderLayout.SOUTH);
 
        add(titlePanel, BorderLayout.NORTH);
        add(center,     BorderLayout.CENTER);
        add(btnPanel,   BorderLayout.SOUTH);
 
        btnRegister.addActionListener(this);
        btnBack.addActionListener(this);
 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
 
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            dispose();
            new LoginFrame();
        } else if (e.getSource() == btnRegister) {
            handleRegister();
        }
    }
 
    private void handleRegister() {
        String fullName  = txtFullName.getText().trim();
        String username  = txtUsername.getText().trim();
        String email     = txtEmail.getText().trim();
        String password  = txtPassword.getText().trim();
        String phone     = txtPhone.getText().trim();
        String address   = taAddress.getText().trim();
 
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showMsg("Full Name, Username, Email and Password are required.", Color.RED);
            return;
        }
        if (password.length() < 6) {
            showMsg("Password must be at least 6 characters.", Color.RED);
            return;
        }
 
        UserDAO dao = new UserDAO();
        if (dao.usernameExists(username)) {
            showMsg("Username already taken. Choose another.", Color.RED);
            return;
        }
 
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setAddress(address);
 
        if (dao.register(user)) {
            showMsg("Account created! Redirecting to login...", new Color(27, 94, 32));
            // Brief pause, then go to login
            new Thread(() -> {
                try { Thread.sleep(1200); } catch (InterruptedException ex) { /* ignore */ }
                dispose();
                new LoginFrame();
            }).start();
        } else {
            showMsg("Registration failed. Please try again.", Color.RED);
        }
    }
 
    private void showMsg(String msg, Color color) {
        lblMsg.setForeground(color);
        lblMsg.setText(msg);
    }
 
    private void styleBtn(Button btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(140, 34));
    }
}
 
