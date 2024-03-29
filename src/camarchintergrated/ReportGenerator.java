/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camarchintergrated;

/**
 *
 * @author rubyb
 */
    public interface ReportGenerator {
    void generateReportTxt(String fileName);
    void generateReportCsv(String fileName);
    void generateReportConsole();
    String getReportDescription();
}

