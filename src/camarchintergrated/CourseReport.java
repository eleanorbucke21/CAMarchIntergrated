package camarchintergrated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseReport {

    private static final Logger LOGGER = Logger.getLogger(CourseReport.class.getName());

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/CMS";
            String user = "root";
            String password = "QaisaR123!"; 
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            String query = "SELECT CourseName, Lecturer, Room FROM Courses";
            resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
            }
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }
}
