package camarchintergrated;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import camarchintergrated.User;
import camarchintergrated.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubyb
 */
public class UserManagement {
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the User Management System");
        String role = authenticateUser();

        if ("office".equals(role)) {
            showOfficeUserMenu();
        } else if ("admin".equals(role)) {
            showMainMenu()
        } else {
            System.out.println("Authentication or role detection failed.");
        }
    }


     private static String authenticateUser() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String role = null;

        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            conn = DBConnect.getConnection();
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
                System.out.println(role + " authentication successful!");
            } else {
                System.out.println("Authentication failed, try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error during authentication: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return role;
    }

        private static void showMainMenu() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Change User Password");
            System.out.println("3. Change User Username");
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
            String password = scanner.nextLine();

            System.out.print("Choose role (admin/office/lecturer): ");
            String role = scanner.nextLine().toLowerCase();

            // Validate the role
            while (!role.equals("admin") && !role.equals("office") && !role.equals("lecturer")) {
                System.out.println("Invalid role. Please enter 'admin', 'office', or 'lecturer'.");
                role = scanner.nextLine().toLowerCase();
            }

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
            } catch (SQLException e) {
                System.out.println("Error adding user: " + e.getMessage());
            }
        }
        
        private static void modifyUsername() {
            System.out.print("Enter the username of the user whose role you want to modify: ");
            String username = scanner.nextLine();

            System.out.print("Enter new role (admin/user/office): ");
            String newRole = scanner.nextLine().toLowerCase();

            // Validate the new role
            while (!newRole.equals("admin") && !newRole.equals("lecturer") && !newRole.equals("office")) {
                System.out.println("Invalid role. Please enter 'admin', 'lecturer', or 'office'.");
                newRole = scanner.nextLine().toLowerCase();
            }

            String sql = "UPDATE users SET role = ? WHERE username = ?";

            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, newRole);
                pstmt.setString(2, username);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("User role updated successfully.");
                } else {
                    System.out.println("Could not find a user with that username.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating user role: " + e.getMessage());
            }
        }

    
        private static void modifyUserPassword() {
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
                
                System.out.println("Admin credentials have been updated.");
        }
        
        private static void showOfficeUserMenu() {
            System.out.println("\nOffice User Menu:");
            System.out.println("1. Change My Username");
            System.out.println("2. Change My Password");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    changeMyUsername();
                    break;
                case "2":
                    changeMyPassword();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
                    showOfficeUserMenu();
            }
        }

        private static void changeMyUsername() {
            System.out.print("Enter your current username: ");
            String currentUsername = scanner.nextLine();
            System.out.print("Enter your new username: ");
            String newUsername = scanner.nextLine();
        }

        private static void changeMyPassword() {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
        }
    }
        
