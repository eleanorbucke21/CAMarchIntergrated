/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camarchintergrated;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubyb
 */
public class StudentReport {
    private static final Logger LOGGER = Logger.getLogger(StudentReport.class.getName());

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        BufferedWriter writer = null;

        try {
            // Establish the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/CMS";
            String user = "root";
            String password = "QaisaR123!";
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the file writer
            writer = new BufferedWriter(new FileWriter("StudentReport.txt"));
            
            // Example: Write headers or intro to the file
            writer.write("Student Report\n");
            writer.write("=================\n\n");
            
            // Prepare and execute your SQL queries here (this is just an example)
            String basicInfoQuery = "SELECT StudentID, StudentName FROM Students";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(basicInfoQuery);
            
            while (resultSet.next()) {
                // Extract each student's information
                int studentId = resultSet.getInt("StudentID");
                String studentName = resultSet.getString("StudentName");
                
                // Example: Write basic student info to the file
                writer.write("Student ID: " + studentId + ", Student Name: " + studentName + "\n");
                
                // Additional processing for programme, enrolled modules, completed modules and grades, and modules to repeat
                // This might involve more queries and writing the results to the file
                
                writer.write("\n"); // Add a newline for spacing between students' reports
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                if (writer != null) {
                    writer.close(); // Ensure the writer is closed properly
                }
            } catch (SQLException | IOException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }
}