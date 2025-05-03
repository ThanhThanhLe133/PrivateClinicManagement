/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author WINDOWS 10
 */
public class Database {

    public static Connection connectDB() {

        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	String url = "jdbc:mysql://localhost:3306/clinic?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = ""; 
            Connection connect = DriverManager.getConnection(url, user, password);
            return connect;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
