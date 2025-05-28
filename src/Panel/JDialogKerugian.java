package Panel;
import Form.connect;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class JDialogKerugian extends JDialog {
    private JTextField txTotalKerugian;
    
    public JDialogKerugian(Frame parent) {
        super(parent, "Total Kerugian", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout());

        txTotalKerugian = new JTextField(20);
        txTotalKerugian.setEditable(false);
        add(new JLabel("Total Kerugian (Modal Barang):"));
        add(txTotalKerugian);

        hitungTotalKerugian();
    }

    private void hitungTotalKerugian() {
        try {
            Connection conn = connect.getConnection();
            String sql = """
                SELECT SUM(b.harga_beli * dp.jumlah) AS totalKerugian
                FROM datapenjualan dp
                JOIN barang b ON dp.kode_barang = b.kode_barang
            """;
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("totalKerugian");
                txTotalKerugian.setText(String.format("Rp%,.2f", total));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error menghitung kerugian: " + e.getMessage());
        }
    }
}

