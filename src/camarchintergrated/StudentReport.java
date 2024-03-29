package camarchintergrated;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentReport implements ReportGenerator {
    private static final Logger LOGGER = Logger.getLogger(StudentReport.class.getName());

    @Override
    public void generateReportTxt(String fileName) {
        String query = "SELECT StudentID, StudentName FROM Students";
        generateReport(fileName, query, false);
    }

    @Override
    public void generateReportCsv(String fileName) {
        String query = "SELECT StudentID, StudentName FROM Students";
        generateReport(fileName, query, true);
    }

    @Override
    public void generateReportConsole() {
        System.out.println(getReportDescription());
        String query = "SELECT StudentID, StudentName FROM Students";
        try (Connection connection = DBConnect.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("StudentID | StudentName");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("StudentID") + " | " +
                        resultSet.getString("StudentName"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred outputting the student report to console", e);
        }
    }

    @Override
    public String getReportDescription() {
        return "Report containing details of all students.";
    }

    private void generateReport(String fileName, String query, boolean csvFormat) {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    BufferedWriter writer = null;

    try {
        connection = DBConnect.getConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);

        // Relative Path
        String filePath = "reports/" + fileName;
        File reportFile = new File(filePath);
        File directory = reportFile.getParentFile();

        if (!directory.exists() && !directory.mkdirs()) {
            LOGGER.severe("Failed to create directories for report file: " + filePath);
            return;
        }

        writer = new BufferedWriter(new FileWriter(reportFile));

        if (csvFormat) {
            writer.write("Student ID,StudentName");
            writer.write("Student ID | StudentName"); 
        }
        writer.newLine();

        while (resultSet.next()) {
            String dataLine = csvFormat ? 
                resultSet.getString("StudentID") + "," + resultSet.getString("StudentName") : 
                resultSet.getString("StudentID") + " | " + resultSet.getString("StudentName");
            writer.write(dataLine);
            writer.newLine();
        }

        LOGGER.info("Report generated successfully: " + filePath);
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "An error occurred generating the report", e);
    } finally {
        try {
            if (writer != null) writer.close();
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error closing resources", e);
        }
    }
}
}