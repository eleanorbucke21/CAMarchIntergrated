/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camarchintergrated;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rubyb
 */
public class generateSLCReports {

    public static void generateReports() {
        // Initialize a list of reports to generate
        List<ReportGenerator> reports = new ArrayList<>();
        reports.add(new StudentReport());
        reports.add(new LecturerReport());
        reports.add(new CourseReport());

        // Generate each report in both TXT and CSV formats, and output to console
        for (ReportGenerator report : reports) {
            System.out.println(report.getReportDescription()); // Print what the report is about
            
            // Generate TXT report
            String txtFilePath = generateFilePathForReport(report, "txt");
            report.generateReportTxt(txtFilePath);
            System.out.println("Generated TXT report: " + txtFilePath);
            
            // Generate CSV report
            String csvFilePath = generateFilePathForReport(report, "csv");
            report.generateReportCsv(csvFilePath);
            System.out.println("Generated CSV report: " + csvFilePath);
            
            // Output report to console
            report.generateReportConsole();
            System.out.println("Output report to console.\n");
        }
    }

    private static String generateFilePathForReport(ReportGenerator report, String extension) {
        // This helper method determines the file path based on the report type and desired file extension
        String reportType = report.getClass().getSimpleName(); // Gets the class name as the report type
        String fileName = reportType + "_Report_" + System.currentTimeMillis() + "." + extension;
        return "reports/" + fileName; // Assumes a 'reports' directory at the project root
    }

    public static void main(String[] args) {
        generateReports();
    }
}