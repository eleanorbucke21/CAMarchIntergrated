package camarchintergrated;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentReport {
    private static final Logger LOGGER = Logger.getLogger(StudentReport.class.getName());

    public static void main(String[] args) {
        
        try (Connection connection = DBConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT StudentID, StudentName FROM Students")) {

            while (resultSet.next()) {
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        }
    }
}
