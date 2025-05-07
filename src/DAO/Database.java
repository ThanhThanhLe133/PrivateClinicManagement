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
 *         //
 */
// public class Database {
//
// public static Connection connectDB() {
//
// try {
//
// Class.forName("com.mysql.jdbc.Driver");
//
// Connection connect
// = DriverManager.getConnection("jdbc:mysql://localhost/clinic", "root", "");
// // root IS OUR DEFAULT USERNAME AND EMPTY OR NULL OR BLANK TO OUR PASSWORD
// return connect;
// } catch (Exception e) {
// e.printStackTrace();
// }
// return null;
// }
//
// }

// public class Database {
//
// public static Connection connectDB() {
// try {
// // ✅ Dùng driver mới
// Class.forName("com.mysql.cj.jdbc.Driver");
//
// // ✅ Đảm bảo có database `clinic` đang tồn tại
// Connection connect = DriverManager.getConnection(
// "jdbc:mysql://localhost:3306/clinic?useSSL=false&serverTimezone=UTC",
// "root",
// "" // điền mật khẩu nếu có
// );
//
// return connect;
// } catch (Exception e) {
// e.printStackTrace();
// }
// return null;
// }
// }

public class Database {

    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3060/clinic?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh";
            
            // dùng cái này nếu không cài env
//            String user = "root";
//            String password = "";
            
            
            // dùng cái này để bảo mật
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASS");
            Connection connect = DriverManager.getConnection(url, user, password);
            return connect;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
