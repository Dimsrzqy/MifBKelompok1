package Form;
import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
    Statement stm;
    Connection conn;
    public Statement stmt;
           
    public static void main(String[] args) {
        
    }   public static Connection configDB() {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/koperasi_nuris";  // URL database Anda
        String user = "root";  // Username database
        String password = "";  // Password database Anda

        try {
            // Membuat koneksi ke database
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return conn;
    }
    Connection con;
}
