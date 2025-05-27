
package Panel;

import Form.connect;
import com.fazecast.jSerialComm.SerialPort;
import com.mysql.cj.xdevapi.Statement;
import java.awt.Dialog;
import java.awt.Window;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.DriverManager;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import Panel.PanelTambahSantri;
import java.awt.Container;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dimsrzqy
 */
public class PanelDataSantri extends javax.swing.JPanel {

    private Container PanelTambahSantri;
public PanelDataSantri() {
    initComponents();
    loadTable();// Memuat data ke dalam tabel
 // Optional: agar tidak bisa diketik manual
}

 public void loadTable() {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0); // Reset isi tabel

        String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
        String user = "root";
        String pass = "";

        String query = "SELECT NamaPengguna, NoRFID, JenisKelamin, Asrama, Lembaga, Saldo, TanggalUpdate FROM pengguna";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("NamaPengguna"),
                    rs.getString("NoRFID"),
                    rs.getString("JenisKelamin"),
                    rs.getString("Asrama"),
                    rs.getString("Lembaga"),
                    rs.getBigDecimal("Saldo"),
                    rs.getDate("TanggalUpdate")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 public void refreshTableData() {
        loadTable();
 }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        BtHapus = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nama", "RFID", "Jenis Kelamin", "Asrama", "Lembaga", "Saldo", "Tanggal Update"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        BtHapus.setText("Hapus");
        BtHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtHapusActionPerformed(evt);
            }
        });

        btTambah.setText("TAMBAH");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(btTambah)
                .addGap(64, 64, 64))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BtHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtHapusActionPerformed
         int selectedRow = jTable.getSelectedRow(); // Ambil baris yang dipilih

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Ambil nilai NoRFID dari baris yang dipilih
    String noRFID = jTable.getValueAt(selectedRow, 1).toString(); // Kolom ke-1 adalah NoRFID

    int confirm = JOptionPane.showConfirmDialog(this,
        "Apakah Anda yakin ingin menghapus data dengan RFID: " + noRFID + "?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM pengguna WHERE NoRFID = ?")) {

            stmt.setString(1, noRFID);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadTable(); // Refresh tabel
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau gagal dihapus.", "Gagal", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_BtHapusActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
    PanelTambahSantri panelTambah = new PanelTambahSantri(this);  // Kirim 'this' sebagai panelInduk
    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah Santri", true);
    dialog.setContentPane(panelTambah);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    }//GEN-LAST:event_btTambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtHapus;
    private javax.swing.JButton btTambah;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
