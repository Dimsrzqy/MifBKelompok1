package Panel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class JDialogKerugian extends JDialog {
    public JDialogKerugian(JFrame parent) {
        super(parent, "Tabel Kerugian", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        String[] column = {"ID Transaksi", "Produk", "Jumlah", "Kerugian"};
        DefaultTableModel model = new DefaultTableModel(column, 0);
        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp);

        loadData(model);
    }

    private void loadData(DefaultTableModel model) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
            String query = "SELECT t.IDTransaksi, b.NamaBarang, td.Jumlah, (b.HargaBeli - td.HargaJual) * td.Jumlah AS Kerugian FROM transaksi_detail td JOIN barang b ON td.IDBarang = b.IDBarang JOIN transaksi t ON td.IDTransaksi = t.IDTransaksi WHERE td.HargaJual < b.HargaBeli";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("IDTransaksi"),
                        rs.getString("NamaBarang"),
                        rs.getInt("Jumlah"),
                        "Rp" + String.format("%,.2f", rs.getDouble("Kerugian"))
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

