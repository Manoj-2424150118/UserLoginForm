public class UserForm import java.awt.*;
        import java.awt.event.*;
        import java.sql.*;

public class UserForm extends Frame implements ActionListener {
    // Form components
    Label l1, l2, l3, l4;
    TextField tfName, tfUsername, tfPassword;
    Button btnRegister, btnLogin;

    Connection conn;
    PreparedStatement pst;

    public UserForm() {
        // Initialize Form
        setTitle("User Registration and Login");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setLocationRelativeTo(null); // Center window
        setVisible(true);

        l1 = new Label("Name:");
        l2 = new Label("Username:");
        l3 = new Label("Password:");
        l4 = new Label("");

        tfName = new TextField();
        tfUsername = new TextField();
        tfPassword = new TextField();
        tfPassword.setEchoChar('*');

        btnRegister = new Button("Register");
        btnLogin = new Button("Login");

        add(l1);
        add(tfName);
        add(l2);
        add(tfUsername);
        add(l3);
        add(tfPassword);
        add(btnRegister);
        add(btnLogin);
        add(l4);

        btnRegister.addActionListener(this);
        btnLogin.addActionListener(this);

        // Connect to Database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/userdb", "root", "yourpassword"
            );
        } catch (Exception e) {
            System.out.println(e);
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        String name = tfName.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        if (ae.getSource() == btnRegister) {
            try {
                pst = conn.prepareStatement("INSERT INTO users (name, username, password) VALUES (?, ?, ?)");
                pst.setString(1, name);
                pst.setString(2, username);
                pst.setString(3, password);
                int i = pst.executeUpdate();
                if (i > 0) {
                    l4.setText("Registration Successful!");
                } else {
                    l4.setText("Registration Failed!");
                }
            } catch (SQLException e) {
                l4.setText("Error: " + e.getMessage());
            }
        } else if (ae.getSource() == btnLogin) {
            try {
                pst = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                pst.setString(1, username);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    l4.setText("Login Successful!");
                } else {
                    l4.setText("Invalid Credentials!");
                }
            } catch (SQLException e) {
                l4.setText("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new UserForm();
    }
}
{
}
