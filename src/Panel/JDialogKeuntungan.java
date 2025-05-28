package Panel;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import Form.connect;



public class JDialogKeuntungan extends JDialog {
    private JTextField txTotalKeuntungan;
    
    public JDialogKeuntungan(Frame parent) {
        super(parent, "Total Keuntungan", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setLayout(new FlowLayout());

        txTotalKeuntungan = new JTextField(20);
        txTotalKeuntungan.setEditable(false);
        add(new JLabel("Total Keuntungan:"));
        add(txTotalKeuntungan);

        hitungTotalKeuntungan();
    }

    private void hitungTotalKeuntungan() {
        try {
            Connection conn = connect.getConnection();
            String sql = """
                SELECT SUM((b.HargaJual - b.HargaBeli) * dp.jumlah) AS totalKeuntungan
                FROM transaksi_jual_dtl dp
                JOIN barang b ON dp.IDBarang = b.IDBarang
            """;
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("totalKeuntungan");
                txTotalKeuntungan.setText(String.format("Rp%,.2f", total));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error menghitung keuntungan: " + e.getMessage());
        }
    }
}
