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


/**
 *
 * @author rubyb
 */
public class UserManagement {
    private static Scanner scanner = new Scanner(System.in);
    private static Admin admin;

    public static void main(String[] args) {
        System.out.println("Welcome to the User Management System");
        User user = authenticateUser();

        if (user instanceof Admin) {
            showAdminUserMenu();
        } else if (user instanceof Office) {
            showOfficeUserMenu();
        } else if (user instanceof Lecturer) {
            showLecturerUserMenu();
        } else {
            System.out.println("Authentication failed. Exiting the system.");
        }
    }

    private static User authenticateUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String role = null;
        int userId = -1;

        String sql = "SELECT id, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println("Authentication failed due to an error: " + e.getMessage());
            return null;
        }

        if (role == null) {
            System.out.println("Authentication failed. Exiting the system.");
            return null;
        }

        switch (role) {
            case "admin":
                return new Admin(userId, username, password);
            case "office":
                return new Office(userId, username, password);
            case "lecturer":
                return new Lecturer(userId, username, password);
            default:
                System.out.println("Unknown role. Exiting the system.");
                return null;
        }
    }

    private static void showAdminUserMenu() {
        System.out.println("\nAdmin User Menu:");
        System.out.println("1. Add a new User");
        System.out.println("2. Delete a User");
        System.out.println("3. Modify a User's Username");
        System.out.println("4. Modify a User's Password");
        System.out.println("5. Change Admin Credentials");
        System.out.println("6. Exit");

        while (true) {
            System.out.print("Enter choice: ");
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            if (admin == null) {
                System.out.println("Authentication required. Please log in as an admin.");
                break; // Break out of the loop if admin is not authenticated
            }

            switch (choice) {
                case 1:
                    admin.addUser();
                    break;
                case 2:
                    admin.deleteUser();
                    break;
                case 3:
                    admin.modifyUsername();
                    break;
                case 4:
                    admin.modifyUserPassword();
                    break;
                case 5:
                    admin.changeAdminCredentials();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return; // Exiting the application or returning from the method
                default:
                    System.out.println("Invalid choice, try again.");
                    break;
            }
        }
    }

    private static void showOfficeUserMenu() {
        System.out.println("\nOffice User Menu:");
        System.out.println("1. Generate Student Report");
        System.out.println("2. Generate Course Report");
        System.out.println("3. Generate Lecturer Report");
        System.out.println("4. Change My Password");
        System.out.println("5. Exit");

        while (true) {
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
                    selectReportFormat("Student");
                    break;
                case 2:
                    selectReportFormat("Course");
                    break;
                case 3:
                    selectReportFormat("Lecturer");
                    break;
                case 4:
                    changeMyPassword(); // Make sure to implement this method securely
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void showLecturerUserMenu() {
        System.out.println("\nLecturer Menu:");
        System.out.println("1. Change My Password");
        System.out.println("2. Generate Lecturer Report");
        System.out.println("3. Exit");

        while (true) {
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
                    changeMyPassword(); // Implementation needed
                    break;
                case 2:
                    generateLecturerReport();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    // Report Options
    private static void selectReportFormat(String reportType) {
        System.out.println("\nSelect the report format for " + reportType + " Report:");
        System.out.println("1. TXT file");
        System.out.println("2. CSV file");
        System.out.println("3. Output to Console");

        System.out.print("Enter choice: ");
        String formatChoice = scanner.nextLine();
        switch (formatChoice) {
            case "1":
                generateReport(reportType, "txt");
                break;
            case "2":
                generateReport(reportType, "csv");
                break;
            case "3":
                generateReport(reportType, "console");
                break;
            default:
                System.out.println("Invalid choice, try again.");
                break;
        }
    }

    private static void generateReport(String reportType, String format) {
        ReportGenerator reportGenerator;
        switch (reportType) {
            case "Student":
                reportGenerator = new StudentReport();
                break;
            case "Course":
                reportGenerator = new CourseReport();
                break;
            case "Lecturer":
                reportGenerator = new LecturerReport();
                break;
            default:
                System.out.println("Invalid report type.");
                return;
        }

        generateReportByFormat(reportGenerator, format);
    }

    private static void generateReportByFormat(ReportGenerator reportGenerator, String format) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        switch (format) {
            case "txt":
                reportGenerator.generateReportTxt("Report_" + timeStamp + ".txt");
                break;
            case "csv":
                reportGenerator.generateReportCsv("Report_" + timeStamp + ".csv");
                break;
            case "console":
                reportGenerator.generateReportConsole();
                break;
            default:
                System.out.println("Invalid format. Please choose TXT, CSV, or console output.");
                break;
        }
    }

    // Generate reports(STUDENT, LECTURER,COURSE)
    private static void generateStudentReport() {
        // Assuming you want to generate a TXT report and console output
        System.out.println("Generating Student Report...");
        StudentReport report = new StudentReport();
        report.generateReportTxt("StudentReport_" + System.currentTimeMillis() + ".txt");
        report.generateReportConsole();
    }

    private static void generateCourseReport() {
        // Assuming you want to generate a TXT report and console output
        System.out.println("Generating Course Report...");
        CourseReport report = new CourseReport();
        report.generateReportTxt("CourseReport_" + System.currentTimeMillis() + ".txt");
        report.generateReportConsole();
    }

    private static void generateLecturerReport() {
        // Assuming you want to generate a TXT report and console output
        System.out.println("Generating Lecturer Report...");
        LecturerReport report = new LecturerReport();
        report.generateReportTxt("LecturerReport_" + System.currentTimeMillis() + ".txt");
        report.generateReportConsole();
    }

    // CRUD for USERS

    // Change my username
    private static void changeMyUsername() {
        System.out.print("Enter your current username: ");
        String currentUsername = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your new username: ");
        String newUsername = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();

            // Verification before changing username
            String verifySql = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(verifySql);
            pstmt.setString(1, currentUsername);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Check if the new username already exists
                String checkNewUsernameSql = "SELECT * FROM users WHERE username = ?";
                pstmt = conn.prepareStatement(checkNewUsernameSql);
                pstmt.setString(1, newUsername);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("The new username already exists. Please choose a different username.");
                } else {
                    // If the user is authenticated, change username
                    String updateSql = "UPDATE users SET username = ? WHERE username = ?";
                    pstmt = conn.prepareStatement(updateSql);
                    pstmt.setString(1, newUsername);
                    pstmt.setString(2, currentUsername);

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Username updated successfully.");
                    } else {
                        System.out.println("An error occurred. Username was not updated.");
                    }
                }
            } else {
                System.out.println("Authentication failed. Current username or password is incorrect.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Change password
    private static void changeMyPassword() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            // Verification before change
            String sqlVerify = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sqlVerify);
            pstmt.setString(1, username);
            pstmt.setString(2, currentPassword);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the user is authenticated, change password
                String sqlUpdate = "UPDATE users SET password = ? WHERE username = ?";
                pstmt = conn.prepareStatement(sqlUpdate);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, username);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Password updated successfully.");
                } else {
                    System.out.println("An error occurred while updating the password.");
                }
            } else {
                System.out.println("Current username or password is incorrect.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
