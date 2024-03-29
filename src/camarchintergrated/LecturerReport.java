package camarchintergrated;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LecturerReport implements ReportGenerator {
    private static final Logger LOGGER = Logger.getLogger(LecturerReport.class.getName());

    @Override
    public void generateReportTxt(String fileName) {
        // Note: This query attempts to fetch lecturer details from both Courses and Modules tables.
        String query = "SELECT 'Course' AS Source, CourseName AS Name, Lecturer, Room FROM Courses "
                     + "UNION "
                     + "SELECT 'Module' AS Source, ModuleName AS Name, Lecturer, Room FROM Modules "
                     + "ORDER BY Lecturer, Name";
        generateReport("reports/" + fileName, query, false); // false for TXT format
    }

    @Override
    public void generateReportCsv(String fileName) {
        String query = "SELECT 'Course' AS Source, CourseName AS Name, Lecturer, Room FROM Courses "
                     + "UNION "
                     + "SELECT 'Module' AS Source, ModuleName AS Name, Lecturer, Room FROM Modules "
                     + "ORDER BY Lecturer, Name";
        generateReport("reports/" + fileName, query, true); // true for CSV format
    }

    @Override
    public void generateReportConsole() {
        System.out.println(getReportDescription());
        String query = "SELECT 'Course' AS Source, CourseName AS Name, Lecturer, Room FROM Courses "
                     + "UNION "
                     + "SELECT 'Module' AS Source, ModuleName AS Name, Lecturer, Room FROM Modules "
                     + "ORDER BY Lecturer, Name";
        try (Connection connection = DBConnect.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Source | Name | Lecturer | Room");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("Source") + " | " +
                                   resultSet.getString("Name") + " | " +
                                   resultSet.getString("Lecturer") + " | " +
                                   resultSet.getString("Room"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred outputting the lecturer report to console", e);
        }
    }

    @Override
    public String getReportDescription() {
        return "Report containing details of all lecturers.";
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
                return; // Exit the method if the directory cannot be created
            }

            writer = new BufferedWriter(new FileWriter(reportFile));

            if (csvFormat) {
                writer.write("Source,Name,Lecturer,Room");
            } else {
                writer.write("Source | Name | Lecturer | Room");
            }
            writer.newLine();

            while (resultSet.next()) {
                String line = csvFormat ? 
                    resultSet.getString("Source") + "," + resultSet.getString("Name") + "," +
                    resultSet.getString("Lecturer") + "," + resultSet.getString("Room") : 
                    resultSet.getString("Source") + " | " + resultSet.getString("Name") + " | " +
                    resultSet.getString("Lecturer") + " | " + resultSet.getString("Room");
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