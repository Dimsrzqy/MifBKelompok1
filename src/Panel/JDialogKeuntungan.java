package Panel;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


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
            Connection conn = koneksi.koneksiDB();
            String sql = """
                SELECT SUM((b.harga_jual - b.harga_beli) * dp.jumlah) AS totalKeuntungan
                FROM datapenjualan dp
                JOIN barang b ON dp.kode_barang = b.kode_barang
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
