package camarchintergrated;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import camarchintergrated.User;
import camarchintergrated.DBConnect;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author rubyb
 */
public class UserManagement {
    private static Scanner scanner = new Scanner(System.in);
    private static String adminUsername = "admin";
    private static String adminPassword = "java";
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the User Management System");
        authenticateAdmin();
        showMainMenu();
    }

    private static void authenticateAdmin() {
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Simplified authentication logic
            if ("admin".equals(username) && "java".equals(password)) {
                System.out.println("Authentication successful!");
                break;
            } else {
                System.out.println("Authentication failed, try again.");
            }
        }
    }

        private static void showMainMenu() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Change User Password");
            System.out.println("3. Change User Username"); // New option for changing the username
            System.out.println("4. Delete User");
            System.out.println("5. Change Admin Credentials");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    modifyUserPassword();
                    break;
                case 3:
                    modifyUsername();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    changeAdminCredentials();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

        private static void addUser() {
            System.out.println("Enter details for new user.");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine(); // Consider using password hashing here
            System.out.print("Role (admin/user): ");
            String role = scanner.nextLine();

            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, password); // In a real app, hash the password before storing it
                pstmt.setString(3, role);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("User added successfully.");
                } else {
                    System.out.println("A problem occurred and the user was not added.");
                }
            } catch (SQLException e) {
                System.out.println("Error adding user: " + e.getMessage());
                // If you're using a unique constraint for the username, handle duplicate username cases here.
            }
        }
        
        private static void modifyUsername() {
            System.out.print("Enter the current username of the user you want to modify: ");
            String currentUsername = scanner.nextLine();

            System.out.print("Enter new username: ");
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
                    System.out.println("Could not find a user with that username.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating username: " + e.getMessage());
        }
    }
    
        private static void modifyUserPassword() {
            System.out.print("Enter the username of the user whose password you want to modify: ");
            String username = scanner.nextLine();

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine(); // Remember, in a real application, hash this password

            String sql = "UPDATE users SET password = ? WHERE username = ?";

            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, newPassword); // In a real app, hash the password before storing it
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

        private static void deleteUser() {
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


        private static void changeAdminCredentials() {
                System.out.print("Enter new admin username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Enter new admin password: ");
                String newPassword = scanner.nextLine();
                adminUsername = newUsername;
                adminPassword = newPassword;

                System.out.println("Admin credentials have been updated.");
        }
    }

