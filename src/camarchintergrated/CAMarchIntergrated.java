/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package camarchintergrated;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author rubyb
 */
public class CAMarchIntergrated {

    /**
     * @param args the command line arguments
     */
    // Initialize logger
      private static final Logger logger = Logger.getLogger(CAMarchIntergrated.class.getName());

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 1. Establish the connection to the database
            String url = "jdbc:mysql://localhost:3306/CMS"; // Assuming MySQL is running on localhost with port 3306
            String user = "root"; // Change this to your MySQL username
            String password = "QaisaR123!"; // Change this to your MySQL password
            connection = DriverManager.getConnection(url, user, password);

            // 2. Create a statement
            statement = connection.createStatement();

            // 3. Execute a query to fetch data from the Courses table
            String query = "SELECT * FROM Courses";
            resultSet = statement.executeQuery(query);

            // 4. Print the results to the terminal
            while (resultSet.next()) {
                int courseId = resultSet.getInt("CourseID");
                String courseName = resultSet.getString("CourseName");
                String instructor = resultSet.getString("Instructor");
                int credits = resultSet.getInt("Credits");
                String description = resultSet.getString("Description");

                // Print course details
                System.out.println("Course ID: " + courseId);
                System.out.println("Course Name: " + courseName);
                System.out.println("Instructor: " + instructor);
                System.out.println("Credits: " + credits);
                System.out.println("Description: " + description);
                System.out.println();
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Log the exception
            logger.log(Level.SEVERE, "An error occurred:", e);
        } finally {
            // 5. Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                // Log the exception
                logger.log(Level.SEVERE, "An error occurred while closing resources:", e);
            }
        }
    }
}