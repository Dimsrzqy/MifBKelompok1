package Panel;
import Form.connect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class JDialogDetailTransaksi extends JDialog {

    private JTable tabelDetail;
    private DefaultTableModel model;

    public JDialogDetailTransaksi(Frame parent, String idTransaksi) {
        super(parent, "Detail Transaksi - " + idTransaksi, true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Inisialisasi tabel
        model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Kategori");
        model.addColumn("Barcode");
        model.addColumn("Jumlah");
        model.addColumn("Subtotal");

        tabelDetail = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabelDetail);

        add(scrollPane, BorderLayout.CENTER);
        loadDetailTransaksi(idTransaksi);
    }

    private void loadDetailTransaksi(String idTransaksi) {
        try {
            Connection conn = connect.getConnection(); // pastikan class connect sudah dibuat
            String sql = "SELECT IDBarang, NamaBarang, Kategori, Barcode, jumlah, subtotal FROM transaksi_jual_dtl WHERE IDTransaksiJual = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idTransaksi);
            ResultSet rs = pst.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                        no++,
                        rs.getString("IDBarang"),
                        rs.getString("NamaBarang"),
                        rs.getString("Kategori"),
                        rs.getString("Barcode"),
                        rs.getInt("jumlah"),
                        rs.getDouble("subtotal")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat detail transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
