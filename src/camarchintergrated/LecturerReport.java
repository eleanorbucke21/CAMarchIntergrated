package camarchintergrated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LecturerReport {
    private static final Logger LOGGER = Logger.getLogger(LecturerReport.class.getName());

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/CMS";
            String user = "root";
            String password = "QaisaR123!";
            connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT Lecturer, ModuleName, COUNT(e.StudentID) AS NumStudents " +
                           "FROM Modules m " +
                           "LEFT JOIN Enrollments e ON m.ModuleID = e.CourseID " +
                           "GROUP BY Lecturer, ModuleName";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
            }
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }
}
