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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rubyb
 */
public class LecturerReport {
    private static final Logger LOGGER = Logger.getLogger(LecturerReport.class.getName());

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
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
            writer = new BufferedWriter(new FileWriter("LecturerReport.txt"));
            writer.write("Lecturer Report\n");
            writer.write("=================\n\n");

            // Query to retrieve lecturer details
            String query = "SELECT Lecturer, ModuleName, COUNT(e.StudentID) AS NumStudents " +
                           "FROM Modules m " +
                           "LEFT JOIN Enrollments e ON m.ModuleID = e.CourseID " +
                           "GROUP BY Lecturer, ModuleName";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            String currentLecturer = "";
            while (resultSet.next()) {
                String lecturerName = resultSet.getString("Lecturer");
                String moduleName = resultSet.getString("ModuleName");
                int numStudents = resultSet.getInt("NumStudents");

                // Group by lecturer
                if (!lecturerName.equals(currentLecturer)) {
                    writer.write("\nLecturer Name: " + lecturerName + "\n");
                    currentLecturer = lecturerName;
                }
                
                // Write module details and enrollment count
                writer.write("Module: " + moduleName + ", Students Enrolled: " + numStudents + "\n");
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
                if (writer != null) {
                    writer.close();
                }
            } catch (SQLException | IOException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }
}