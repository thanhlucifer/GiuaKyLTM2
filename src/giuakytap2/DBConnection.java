/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuakytap2;

/**
 *
 * @author thanh
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Đường dẫn kết nối MySQL
                String url = "jdbc:mysql://localhost:3306/vanphongpham?useSSL=false";

                
                // Tên người dùng và mật khẩu của MySQL
                String username = "root";
                String password = "12345";

                // Kết nối
                conn = DriverManager.getConnection(url, username, password);

                System.out.println("Kết nối thành công!");
            } catch (SQLException e) {
                System.out.println("Lỗi kết nối: " + e.getMessage());
            }
        }
        return conn;
    }
}
