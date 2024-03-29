/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camarchintergrated;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author rubyb
 */
public class Admin extends User {
    private Scanner scanner = new Scanner(System.in);

    public Admin(int id, String username, String password) {
        super(id, username, password, "admin");
    }

    // CRUD for Admin
    public void addUser() {
        System.out.println("Enter details for new user.");
            System.out.print("Username: ");
            String username = scanner.nextLine();
    
            System.out.print("Password: ");
            String password = scanner.nextLine();
    
            System.out.print("Choose role (admin/office/lecturer): ");
            String role = scanner.nextLine().toLowerCase();
    
            // Validate the role
            if (!role.equals("admin") && !role.equals("office") && !role.equals("lecturer")) {
            System.out.println("Invalid role. Please enter 'admin', 'office', or 'lecturer'.");
            return;
        }

        try {
            insertUser(username, password, role);
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }
    
    private void insertUser(String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.setString(3, role);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("A problem occurred, and the user was not added.");
            }
        }
    }

    // Add to MYSQL
    public void modifyUsername() {
        System.out.print("Enter the current username of the user: ");
        String currentUsername = scanner.nextLine();
    
        System.out.print("Enter the new username for the user: ");
        String newUsername = scanner.nextLine();
    
        String sql = "UPDATE users SET username = ? WHERE username = ?";
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, newUsername);
            pstmt.setString(2, currentUsername);
    
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                System.out.println("Username updated successfully.");
            } else {
                System.out.println("Could not find a user with username: " + currentUsername + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error updating username: " + e.getMessage());
        }
    }

    // Modify user password
    public void modifyUserPassword() {
        System.out.print("Enter the username of the user whose password you want to modify: ");
        String username = scanner.nextLine();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User password updated successfully.");
            } else {
                System.out.println("Could not find a user with that username.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user password: " + e.getMessage());
        }
    }  

    // Delete User
    public void deleteUser() {
        System.out.print("Enter the username of the user you want to delete: ");
        String username = scanner.nextLine();

        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Could not find a user with that username.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    // Change admin credentials
    public void changeAdminCredentials() {
        System.out.print("Enter new admin username: ");
        String newUsername = scanner.nextLine();
        System.out.print("Enter new admin password: ");
        String newPassword = scanner.nextLine();
        try {
            updateAdminCredentials(newUsername, newPassword);
        } catch (SQLException e) {
            System.out.println("Error updating admin credentials: " + e.getMessage());
        }
    }

    //Update admin credentials in MYSQL
    private void updateAdminCredentials(String newUsername, String newPassword) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, this.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Admin credentials have been updated.");

                this.setUsername(newUsername);
                this.setPassword(newPassword); 
            } else {
                System.out.println("Failed to update admin credentials.");
            }
        }
    }
    public void close() {
        scanner.close();
    }
}