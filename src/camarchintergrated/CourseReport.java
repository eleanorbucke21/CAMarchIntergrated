package camarchintergrated;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseReport implements ReportGenerator {
    private static final Logger LOGGER = Logger.getLogger(CourseReport.class.getName());

    @Override
    public void generateReportTxt(String fileName) {
        String query = "SELECT CourseID, CourseName, Lecturer FROM Courses";
        generateReport("reports/" + fileName, query, false); 
    }

    @Override
    public void generateReportCsv(String fileName) {
        String query = "SELECT CourseID, CourseName, Lecturer FROM Courses";
        generateReport("reports/" + fileName, query, true);
    }

    @Override
    public void generateReportConsole() {
        System.out.println(getReportDescription());
        String query = "SELECT CourseID, CourseName, Lecturer FROM Courses";
        try (Connection connection = DBConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("CourseID | CourseName | Lecturer");
            if (!resultSet.isBeforeFirst()) {
                LOGGER.info("No data found in Courses table.");
                System.out.println("No data found.");
                return;
            }

            while (resultSet.next()) {
                System.out.println(resultSet.getString("CourseID") + " | " +
                        resultSet.getString("CourseName") + " | " +
                        resultSet.getString("Lecturer"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred outputting the course report to console", e);
        }
    }


    @Override
    public String getReportDescription() {
        return "Report containing details of all courses.";
    }

    private void generateReport(String filePath, String query, boolean csvFormat) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        BufferedWriter writer = null;

        try {
            connection = DBConnect.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            File reportFile = new File(filePath);
            File directory = reportFile.getParentFile(); 
            if (!directory.exists() && !directory.mkdirs()) {
                LOGGER.severe("Failed to create directory: " + directory.getAbsolutePath());
                return; 
            }

            writer = new BufferedWriter(new FileWriter(reportFile));

            if (csvFormat) {
                writer.write("CourseID,CourseName,Lecturer");
            } else {
                writer.write("CourseID | CourseName | Lecturer");
            }
            writer.newLine();

            while (resultSet.next()) {
                String line = csvFormat ? 
                    resultSet.getString("CourseID") + "," + resultSet.getString("CourseName") + "," + resultSet.getString("Lecturer") : 
                    resultSet.getString("CourseID") + " | " + resultSet.getString("CourseName") + " | " + resultSet.getString("Lecturer");
                writer.write(line);
                writer.newLine();
            }

            LOGGER.info(csvFormat ? "CSV report generated successfully." : "TXT report generated successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred generating the report", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                if (writer != null) writer.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }
}