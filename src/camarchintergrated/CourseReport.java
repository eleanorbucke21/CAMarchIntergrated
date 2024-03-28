package camarchintergrated;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseReport {
    private static final Logger LOGGER = Logger.getLogger(CourseReport.class.getName());

    public static void main(String[] args) {
        String query = "SELECT CourseName, Lecturer, Room FROM Courses";

        try (Connection connection = DBConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Process the result set
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        }
    }
}
