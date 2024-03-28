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
public class CourseReport {

    private static final Logger LOGGER = Logger.getLogger(CourseReport.class.getName());

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
            
            // Prepare the statement and execute the query
            statement = connection.createStatement();
            String query = "SELECT CourseName, Lecturer, Room FROM Courses";
            resultSet = statement.executeQuery(query);
            
            // Prepare the file writer
            writer = new BufferedWriter(new FileWriter("CourseReport.txt"));
            
            // Iterate through the result set
            while (resultSet.next()) {
                String courseName = resultSet.getString("CourseName");
                String lecturer = resultSet.getString("Lecturer");
                String room = resultSet.getString("Room");
                
                // Print to terminal
                System.out.println("Course Name: " + courseName);
                System.out.println("Lecturer: " + lecturer);
                System.out.println("Room: " + room);
                System.out.println();

                // Write the same content to the file
                writer.write("Course Name: " + courseName);
                writer.newLine();
                writer.write("Lecturer: " + lecturer);
                writer.newLine();
                writer.write("Room: " + room);
                writer.newLine();
                writer.newLine(); // Extra newline for spacing between entries
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