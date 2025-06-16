/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author WINDOWS 10
 *         //
 */

public class Database {
    private static Connection connection = null; // Single reusable connection (added to support reuse)

    public static Connection connectDB() {
        try {
         //cuong url	
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3060/clinic?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh";
            // dùng cái này nếu không cài env
////            String user = "root";
////            String password = "";
//        
//            
         // dùng cái này để bảo mật
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASS");
            // Check if connection exists and is valid before creating a new one
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            // Close existing connection if it exists (to handle stale connections)
            if (connection != null) {
                connection.close();
            }
            // Create new connection
            connection = DriverManager.getConnection(url, user, password);
         // Disable ONLY_FULL_GROUP_BY for the session
            Statement stmt = connection.createStatement();
            stmt.execute("SET SESSION sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));");
            stmt.close();
            return connection;
//            
//            //thanhthanh url cấm xoá
//            String url = "jdbc:mysql://localhost:3306/clinic?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh";
//            Connection connect = DriverManager.getConnection(url, "root", "");
//            return connect;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
