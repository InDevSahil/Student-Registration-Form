package StudentRegistration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistration extends JFrame implements ActionListener {
    // GUI components
    private JTextField nameField, emailField, phoneField;
    private JButton submitButton;

    // Constructor
    public StudentRegistration() {
        setTitle("Student Registration Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10)); // 5 rows, 2 columns, with gaps

        // Add components
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("")); // Empty label for spacing
        submitButton = new JButton("Register Student");
        submitButton.addActionListener(this);
        add(submitButton);

        setVisible(true);
    }

    // Action listener for the submit button
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Input validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number (digits only).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Store in database
        if (storeInDatabase(name, email, phone)) {
            JOptionPane.showMessageDialog(this, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Clear fields after successful registration
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register student. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    // Phone validation method (simple: digits only)
    private boolean isValidPhone(String phone) {
        return phone.matches("\\d+");
    }

    // Database storage method
    private boolean storeInDatabase(String name, String email, String phone) {
        // Database connection details (adjust as needed)
        String url = "jdbc:mysql://localhost:3306/student_registration_db";
        String user = "root"; // Change to your MySQL username
        String password = ""; // Change to your MySQL password

        String sql = "INSERT INTO students (name, email, phone) VALUES (?, ?, ?)";

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection conn = DriverManager.getConnection(url, user, password);

            // Prepare statement
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);

            // Execute update
            int rowsAffected = pstmt.executeUpdate();

            // Close resources
            pstmt.close();
            conn.close();

            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistration());
    }
}
