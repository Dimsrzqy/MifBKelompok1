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
    }
    private void loadStokData() {
    try (Connection conn = Koneksi.getConnection()) {
       String query = 
       "SELECT " +
       "s.IDBarang, " +
       "s.JumlahMasuk, " +
       "s.JumlahKeluar, " +
       "(s.JumlahMasuk - s.JumlahKeluar) AS Stok, " +
       "s.TanggalKadaluarsa " +
       "FROM Stok s " +
       "LEFT JOIN barang b ON s.IDBarang = b.IDBarang";

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
    model.addColumn("Kategori");
    model.addColumn("Stok"); // Kolom stok akan menampilkan "-" jika tidak ada data
    model.addColumn("Nama Barang");
    model.addColumn("Harga Beli");
    model.addColumn("Harga Jual");
    model.addColumn("Kadaluarsa");
    model.addColumn("Tanggal Masuk");
    model.addColumn("Barcode");

    try {
        int no = 1;
        String sql = "SELECT b.*, k.Kategori AS nama_kategori, " +
                         "s.JumlahMasuk, s.JumlahKeluar, s.TanggalKadaluarsa " +
                         "FROM barang b " +
                         "LEFT JOIN kategori k ON b.IDKategori = k.IDKategori " +
                         "LEFT JOIN stok s ON b.IDBarang = s.IDBarang";
            
            Connection conn = Koneksi.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                String stok;
            try {
                int jumlahMasuk = res.getInt("JumlahMasuk");
                int jumlahKeluar = res.getInt("JumlahKeluar");
                stok = String.valueOf(jumlahMasuk - jumlahKeluar);
            } catch (SQLException e) {
                stok = "-"; // Jika data stok tidak ada
            }
                
                String kadaluarsa = res.getDate("TanggalKadaluarsa") != null 
                    ? res.getDate("TanggalKadaluarsa").toString() 
                    : "-";

            model.addRow(new Object[]{
                no++,
                res.getString("IDBarang"),
                res.getString("IDKategori"),
                stok,
                res.getString("NamaBarang"),
                res.getDouble("HargaBeli"),
                res.getDouble("HargaJual"),
                kadaluarsa, // ✅ tampilkan tanggal kadaluarsa
                res.getString("TanggalMasuk"),
                res.getString("Barcode")
            });
        }

        tabel_barang.setModel(model);
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
    String query = "SELECT b.*, k.Kategori AS nama_kategori " +
                   "FROM barang b " +
                   "LEFT JOIN kategori k ON b.IDKategori = k.IDKategori " +
                   "WHERE b.IDBarang = ?";

    try (Connection conn = Koneksi.getConnection();
         PreparedStatement pst = conn.prepareStatement(query)) {
        
        pst.setString(1, idBarang);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                barangData.put("IDBarang", rs.getString("IDBarang"));
                barangData.put("IDKategori", rs.getString("IDKategori"));
                barangData.put("NamaBarang", rs.getString("NamaBarang"));
                barangData.put("HargaBeli", String.valueOf(rs.getDouble("HargaBeli")));
                barangData.put("HargaJual", String.valueOf(rs.getDouble("HargaJual")));
                barangData.put("Kadaluarsa", rs.getString("Kadaluarsa"));
                barangData.put("TanggalMasuk", rs.getString("TanggalMasuk"));
                barangData.put("Barcode", rs.getString("Barcode"));
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, 
            "Error ambil data: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
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
    String query = "UPDATE barang SET " +
               "IDBarang = ?, " +
               "IDKategori = ?, " +        // perbaikan di sini
               "Stok = ?, " +
               "NamaBarang = ?, " +
               "HargaBeli = ?, " +
               "HargaJual = ?, " +
               "Kadaluarsa = ? " +
               "TanggalMasuk = ? " +
               "WHERE IDBarang = ?";


    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
         PreparedStatement pst = conn.prepareStatement(query)) {

        pst.setString(1, updatedData.get("IDBarang"));
        pst.setString(2, updatedData.get("Kategori"));
        pst.setString(3, updatedData.get("Stok"));
        pst.setString(4, updatedData.get("NamaBarang"));
        pst.setString(5, updatedData.get("HargaBeli"));
        pst.setString(6, updatedData.get("HargaJual"));
        pst.setString(7, updatedData.get("Kadaluarsa"));
        pst.setString(8, updatedData.get("TanggalMasuk"));
        pst.setString(9, updatedData.get("Barcode"));

        int result = pst.executeUpdate();

        if (result > 0) {
            JOptionPane.showMessageDialog(this,
                "Data barang berhasil diperbarui.",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal memperbarui data barang.",
                "Gagal",
                JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Error menyimpan data: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_barang = new javax.swing.JTable();
        BtnHapus = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        Search = new javax.swing.JTextField();

        jCheckBox1.setText("jCheckBox1");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tabel_barang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama Barang", "Kategori", "Stok", "Harga Beli", "Harga Jual", "Kadaluarsa", "Tanggal masuk", "Barcode"
            }
        ));
        jScrollPane1.setViewportView(tabel_barang);

        BtnHapus.setBackground(new java.awt.Color(255, 0, 0));
        BtnHapus.setText("Hapus");
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });

        edit.setBackground(new java.awt.Color(255, 255, 0));
        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        Search.setBackground(new java.awt.Color(204, 204, 255));
        Search.setText("  Search");
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(edit))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnHapus)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnHapus)
                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
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
    String idBarang = tabel_barang.getValueAt(selectedRow, 1).toString();

    try {
        // Query hapus data
        String sql = "DELETE FROM barang WHERE IDBarang = ?";
        
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
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
        JOptionPane.showMessageDialog(this, 
            "Pilih baris data yang akan diedit terlebih dahulu",
            "Peringatan",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Ambil ID Barang dari kolom ke-1 (indeks 1)
        String idBarang = tabel_barang.getValueAt(selectedRow, 1).toString().trim();
        System.out.println("DEBUG: ID Barang yang dipilih = " + idBarang);

        // Ambil data dari database
        Map<String, String> barangData = getBarangDataFromDatabase(idBarang);
        if (barangData.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Data barang dengan ID " + idBarang + " tidak ditemukan di database",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tampilkan dialog edit
        showEditBarangDialog(barangData);
        
        // Refresh tabel setelah edit
        tampilkanDataBarang();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Terjadi kesalahan: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_editActionPerformed

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnHapus;
    private javax.swing.JTextField Search;
    private javax.swing.JButton edit;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabel_barang;
    // End of variables declaration//GEN-END:variables
}
