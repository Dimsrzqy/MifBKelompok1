package Form;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
    public static Connection con;

    public static Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            try {
                String url = "jdbc:mysql://localhost/koperasi_nuris";
                String user = "root";
                String pass = "";

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(url, user, pass);
                System.out.println("Koneksi berhasil terhubung!");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new SQLException("Koneksi gagal: " + e.getMessage());
            }
        }
        return con;
    }

    public static boolean startConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
