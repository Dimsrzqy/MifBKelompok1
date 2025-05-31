/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Panel.PopupEditManBarang;
import Form.FormMenuUtama;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author cindy
 */
public class PanelManajemenBarang extends javax.swing.JPanel {

    /**
     * Creates new form PanelManajemenBarang
     */
    public PanelManajemenBarang() {
        initComponents();
        tampilkanDataBarang();
        JTableHeader header = tabel_barang.getTableHeader();
        header.setBackground(new Color(0, 102, 204)); // biru tua
        header.setForeground(Color.WHITE);           // teks putih
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void loadStokData() {
        try (Connection conn = Koneksi.getConnection()) {
            String query
                    = "SELECT "
                    + "s.IDBarang, "
                    + "s.JumlahMasuk, "
                    + "s.JumlahKeluar, "
                    + "(s.JumlahMasuk - s.JumlahKeluar) AS Stok, "
                    + "s.TanggalKadaluarsa "
                    + "FROM Stok s "
                    + "LEFT JOIN barang b ON s.IDBarang = b.IDBarang";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Kosongkan tabel sebelum isi data
            DefaultTableModel model = (DefaultTableModel) tabel_barang.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String kdBarang = rs.getString("kd_barang");
                int masuk = rs.getInt("jumlah_masuk");
                int keluar = rs.getInt("jumlah_keluar");
                int stok = rs.getInt("Stok");
                Date tanggalKadaluarsa = rs.getDate("TanggalKadaluarsa");

                // Tambahkan baris ke tabel GUI
                model.addRow(new Object[]{
                    kdBarang, masuk, keluar, stok, tanggalKadaluarsa
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memuat data stok: " + ex.getMessage());
        }
    }

    public class Koneksi {

        public static Connection getConnection() {
            Connection conn = null;
            try {
                String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
                String user = "root"; // atau user lain
                String pass = "";     // password MySQL kamu

                conn = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                System.out.println("Koneksi gagal: " + e.getMessage());
            }
            return conn;
        }
    }

    public void tampilkanDataBarang() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Kategori");
        model.addColumn("Stok"); // Kolom stok akan menampilkan "-" jika tidak ada data
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Tanggal Kadaluarsa");
        model.addColumn("Tanggal Masuk");
        model.addColumn("Barcode");
        // Sembunyikan kolom No (index 0) dan ID Barang (index 1)

        try {
            int no = 1;
            String sql = "SELECT b.*, k.NamaKategori AS nama_kategori, "
                    + "s.JumlahMasuk, s.JumlahKeluar, s.TanggalKadaluarsa "
                    + "FROM barang b "
                    + "LEFT JOIN kategori k ON b.IDKategori = k.IDKategori "
                    + "LEFT JOIN stok s ON b.IDBarang = s.IDBarang";

            Connection conn = Koneksi.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                String idBarang = res.getString("IDBarang");
                if (!idBarang.toUpperCase().startsWith("BRG")) {
                    try {
                        int angka = Integer.parseInt(idBarang);
                        idBarang = String.format("BRG%03d", angka);
                    } catch (NumberFormatException e) {
                        // jika tidak bisa di-parse, biarkan nilai aslinya
                    }
                }

                String Stok = "-";
                try {
                    int masuk = res.getInt("JumlahMasuk");
                    int keluar = res.getInt("JumlahKeluar");
                    Stok = String.valueOf(masuk - keluar);
                } catch (Exception e) {
                    Stok = "-";
                }

                String kadaluarsa = res.getDate("TanggalKadaluarsa") != null
                        ? res.getDate("TanggalKadaluarsa").toString()
                        : "-";

                String tanggalMasuk = (res.getDate("TanggalMasuk") != null)
                        ? res.getDate("TanggalMasuk").toString()
                        : "-";

                model.addRow(new Object[]{
                    no++,
                    idBarang,
                    res.getString("NamaBarang"),
                    res.getString("nama_kategori"),
                    Stok,
                    res.getDouble("HargaBeli"),
                    res.getDouble("HargaJual"),
                    kadaluarsa, // ✅ tampilkan tanggal kadaluarsa
                    tanggalMasuk,
                    res.getString("Barcode")
                });
            }

            tabel_barang.setModel(model);
            // Sembunyikan kolom "No" (index 0) dan "ID Barang" (index 1)
            tabel_barang.getColumnModel().getColumn(0).setMinWidth(0);
            tabel_barang.getColumnModel().getColumn(0).setMaxWidth(0);
            tabel_barang.getColumnModel().getColumn(0).setWidth(0);

            tabel_barang.getColumnModel().getColumn(1).setMinWidth(0);
            tabel_barang.getColumnModel().getColumn(1).setMaxWidth(0);
            tabel_barang.getColumnModel().getColumn(1).setWidth(0);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Map<String, String> getBarangDataFromDatabase(String idBarang) {
        Map<String, String> barangData = new HashMap<>();
        String query = "SELECT b.IDBarang, b.NamaBarang, b.IDKategori, k.NamaKategori AS nama_kategori, "
                + "b.HargaBeli, b.HargaJual, b.TanggalMasuk, b.Barcode, "
                + "s.TanggalKadaluarsa "
                + "FROM barang b "
                + "LEFT JOIN kategori k ON b.IDKategori = k.IDKategori "
                + "LEFT JOIN stok s ON b.IDBarang = s.IDBarang "
                + "WHERE b.IDBarang = ? "
                + "LIMIT 1";  // Optional: hanya ambil 1 baris jika stok ada lebih dari 1

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, idBarang);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    barangData.put("IDBarang", rs.getString("IDBarang"));
                    barangData.put("NamaBarang", rs.getString("NamaBarang"));
                    barangData.put("IDKategori", rs.getString("IDKategori"));
                    barangData.put("nama_kategori", rs.getString("nama_kategori"));
                    barangData.put("HargaBeli", String.valueOf(rs.getDouble("HargaBeli")));
                    barangData.put("HargaJual", String.valueOf(rs.getDouble("HargaJual")));
                    barangData.put("TanggalKadaluarsa", rs.getString("TanggalKadaluarsa"));
                    barangData.put("TanggalMasuk", rs.getString("TanggalMasuk"));
                    barangData.put("Barcode", rs.getString("Barcode"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data: " + e.getMessage());
            e.printStackTrace();
        }
        return barangData;
    }

    private void showEditBarangDialog(Map<String, String> barangData) {
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Edit Data Barang - " + barangData.get("NamaBarang"));
        editDialog.setModal(true);

        PopupEditManBarang editPanel = new PopupEditManBarang(barangData);
        editPanel.setParentDialog(editDialog); // ← penting agar bisa ditutup dari dalam editPanel

        editDialog.add(editPanel);
        editDialog.pack();
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    private void saveBarangData(Map<String, String> updatedData) {
        String query = "UPDATE barang SET "
                + "NamaBarang = ?, "
                + "IDKategori = ?, "
                + "HargaBeli = ?, "
                + "HargaJual = ?, "
                + "TanggalKadaluarsa = ?, "
                + "TanggalMasuk = ?, "
                + "Barcode = ? "
                + "WHERE IDBarang = ?";

        try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, updatedData.get("NamaBarang"));
            pst.setString(2, updatedData.get("IDKategori"));
            pst.setString(3, updatedData.get("HargaBeli"));
            pst.setString(4, updatedData.get("HargaJual"));
            pst.setString(5, updatedData.get("TanggalKadaluarsa"));
            pst.setString(6, updatedData.get("TanggalMasuk"));
            pst.setString(7, updatedData.get("Barcode"));
            pst.setString(8, updatedData.get("IDBarang"));  // WHERE ID

            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui.");
                tampilkanDataBarang();
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang diperbarui.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel) tabel_barang.getModel();
        model.setRowCount(0);
        tampilkanDataBarang(); // Pastikan kamu punya method loadTable() yang ambil data dari DB
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        BtnHapus = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        BtCari = new javax.swing.JButton();
        txBarcode = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_barang = new javax.swing.JTable();

        jCheckBox1.setText("jCheckBox1");

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        BtnHapus.setBackground(new java.awt.Color(255, 0, 0));
        BtnHapus.setText("Hapus");
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });

        edit.setBackground(new java.awt.Color(51, 255, 51));
        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        BtCari.setBackground(new java.awt.Color(255, 230, 0));
        BtCari.setText("Cari");
        BtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtCariActionPerformed(evt);
            }
        });

        txBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBarcodeActionPerformed(evt);
            }
        });

        jLabel17.setText("Admin > Manajemen Barang");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("BARANG");

        tabel_barang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabel_barang);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtCari))
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addContainerGap(736, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1088, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 1010, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(edit)
                                    .addComponent(BtnHapus))))
                        .addGap(26, 26, 26))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnHapus)
                    .addComponent(BtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE))
        );

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1120, 660));
    }// </editor-fold>//GEN-END:initComponents

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed

        // Pastikan ada baris yang dipilih di tabel
        int selectedRow = tabel_barang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih barang yang akan dihapus terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Konfirmasi penghapusan
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus barang ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Ambil ID Barang dari kolom yang benar (kolom 1)
        DefaultTableModel model = (DefaultTableModel) tabel_barang.getModel();
        String idBarang = model.getValueAt(selectedRow, 0).toString().trim();

        try {
            // Query hapus data
            String sql = "DELETE FROM barang WHERE IDBarang = ?";

            try (Connection conn = Koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, idBarang);
                int affectedRows = pst.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Data barang berhasil dihapus!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Refresh tabel setelah penghapusan
                    tampilkanDataBarang();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Data barang tidak ditemukan!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat menghapus karena data digunakan di tabel lain",
                    "Constraint Violation",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        int selectedRow = tabel_barang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil IDBarang langsung dari model
        String idBarang = String.valueOf(tabel_barang.getValueAt(selectedRow, 1));

        Map<String, String> barangData = getBarangDataFromDatabase(idBarang);
        if (barangData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data barang tidak ditemukan.");
            return;
        }

        showEditBarangDialog(barangData);
        tampilkanDataBarang(); // Refresh data
    }//GEN-LAST:event_editActionPerformed

    private void BtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtCariActionPerformed
        JDialogCariBRG popup = new JDialogCariBRG();
        popup.setModal(true);
        popup.setLocationRelativeTo(this);
        popup.setVisible(true); // tunggu dialog ditutup baru lanjut

        // Setelah dialog ditutup:
        String idBarang = popup.getSelectedBarang();
        if (idBarang != null) {
            getBarangDataFromDatabase(idBarang);
        }
    }//GEN-LAST:event_BtCariActionPerformed

    private void txBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txBarcodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtCari;
    private javax.swing.JButton BtnHapus;
    private javax.swing.JButton edit;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabel_barang;
    private javax.swing.JTextField txBarcode;
    // End of variables declaration//GEN-END:variables
}
