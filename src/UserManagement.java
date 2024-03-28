/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubyb
 */
public class UserManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static String adminUsername = "admin";
    private static String adminPassword = "java";
    private static List<User> users = new ArrayList<>(); // User storage

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
            System.out.println("2. Modify User");
            System.out.println("3. Delete User");
            System.out.println("4. Change Admin Credentials");
            System.out.println("5. Exit");
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
                    modifyUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    changeAdminCredentials();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
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

        System.out.print("Role (admin/user): ");
        String role = scanner.nextLine();

        // Validation
        if(username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                System.out.println("A user with this username already exists.");
                return;
            }
        }

        // Create a new User object and add it to the users list
        User newUser = new User(users.size() + 1, username, password, role); // Assuming User has an appropriate constructor
        users.add(newUser);
        System.out.println("User added successfully.");
    }

    private static void modifyUser() {
        // Prompt for which user to modify and apply changes to the database
    }

    private static void deleteUser() {
        // Prompt for which user to delete and remove from the database
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
