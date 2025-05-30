package Panel;

import Form.connect;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class JDialogCariBRG extends JDialog {
    private JTable TbDataProduk;
    private JTextField TxPencarian;
    private JButton btPilih;
    private String selectedBarang = null;

    public String getSelectedBarang() {
        return selectedBarang;
    }

    public JDialogCariBRG() {
        setTitle("Cari Barang");
        setModal(true);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        TxPencarian = new JTextField();
        TxPencarian.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(new JLabel("Cari Nama Barang: "), BorderLayout.WEST);
        panelAtas.add(TxPencarian, BorderLayout.CENTER);

        TbDataProduk = new JTable();
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"IDBarang", "NamaBarang", "Kategori", "Harga",}
        );
        TbDataProduk.setModel(model);

        JScrollPane scrollPane = new JScrollPane(TbDataProduk);

        btPilih = new JButton("Pilih");
        btPilih.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = TbDataProduk.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Pilih satu baris barang terlebih dahulu.");
                    return;
                }

                Object val = TbDataProduk.getValueAt(row, 0);
                if (val != null) {
                    selectedBarang = val.toString();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Data barang tidak lengkap (IDBarang kosong)!");
                }
            }
        });

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBawah.add(btPilih);

        add(panelAtas, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);

        loadBarangFromDatabase();
    }

    private void loadBarangFromDatabase() {
        DefaultTableModel model = (DefaultTableModel) TbDataProduk.getModel();
        model.setRowCount(0);

        try {
            Connection conn = connect.getConnection();
            String sql = "SELECT IDBarang, NamaBarang, Barcode, HargaJual FROM barang";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                rs.getString("IDBarang"),
                rs.getString("NamaBarang"),
                rs.getString("Barcode"),
                rs.getDouble("HargaJual")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filter() {
        DefaultTableModel model = (DefaultTableModel) TbDataProduk.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        TbDataProduk.setRowSorter(sorter);
        String keyword = TxPencarian.getText();
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
    }
}
