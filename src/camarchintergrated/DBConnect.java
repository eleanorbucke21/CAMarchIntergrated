/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camarchintergrated;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rubyb
 */
public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/CMS";
    private static final String USER = "root";
    private static final String PASSWORD = "QaisaR123!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
