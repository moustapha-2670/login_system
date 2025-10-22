import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementSystem extends JFrame {

    // Champs de saisie
    private JTextField nameField, ageField, courseField;
    private JTextArea displayArea;

    // Connexion MySQL
    private Connection conn;

    public StudentManagementSystem() {
        // --- Configuration de la fenêtre principale ---
        setTitle("Student Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Connexion à la base de données ---
        connectToDatabase();

        // --- Panneau supérieur (formulaire) ---
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        add(inputPanel, BorderLayout.NORTH);

        // --- Zone d'affichage ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // --- Panneau des boutons ---
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Students");
        JButton deleteButton = new JButton("Delete Student");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // --- Actions des boutons ---
        addButton.addActionListener(e -> addStudent());
        viewButton.addActionListener(e -> viewStudents());
        deleteButton.addActionListener(e -> deleteStudent());

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/studentdb", "root", "");
            System.out.println("✅ Connected to database.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            e.printStackTrace();
        }
    }

    private void addStudent() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String course = courseField.getText();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO students(name, age, course) VALUES(?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, course);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Student added successfully!");
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Error adding student!");
            e.printStackTrace();
        }
    }

    private void viewStudents() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            displayArea.setText("ID\tName\tAge\tCourse\n---------------------------------\n");

            while (rs.next()) {
                displayArea.append(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getInt("age") + "\t" +
                        rs.getString("course") + "\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Error loading students!");
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");
        if (id != null && !id.trim().isEmpty()) {
            try {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id=?");
                stmt.setInt(1, Integer.parseInt(id));
                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "🗑️ Student deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ No student found with that ID!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "⚠️ Error deleting student!");
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        courseField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystem());
    }
}
