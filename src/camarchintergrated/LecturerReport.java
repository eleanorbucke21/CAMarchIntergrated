package camarchintergrated;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LecturerReport {
    private static final Logger LOGGER = Logger.getLogger(LecturerReport.class.getName());

    public static void main(String[] args) {
        String query = "SELECT Lecturer, ModuleName, COUNT(e.StudentID) AS NumStudents " +
                       "FROM Modules m " +
                       "LEFT JOIN Enrollments e ON m.ModuleID = e.CourseID " +
                       "GROUP BY Lecturer, ModuleName";
        
        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Process the result set
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        }
    }
}
