
package Panel;

import Form.connect;
import com.fazecast.jSerialComm.SerialPort;
import com.mysql.cj.xdevapi.Statement;
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
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dimsrzqy
 */
public class PanelDataSantri extends javax.swing.JPanel {
public PanelDataSantri() {
    initComponents();
    loadTable(); // Memuat data ke dalam tabel
    txIDPengguna.setText(generateIdPengguna());
    // Set ID otomatis saat panel dibuka
    
    txIDPengguna.setEditable(false); // Optional: agar tidak bisa diketik manual
}
public String generateIdPengguna() {
    String prefix = "STR";
    int nextNumber = 1;

    String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
    String user = "root";
    String pass = "";

    String query = "SELECT MAX(SUBSTRING(IDPengguna, 4)) AS max_id FROM pengguna";

    try (Connection conn = DriverManager.getConnection(url, user, pass);
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        if (rs.next() && rs.getString("max_id") != null) {
            nextNumber = Integer.parseInt(rs.getString("max_id")) + 1;
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return String.format("%s%03d", prefix, nextNumber); // STR001, STR002, ...
}


private void loadTable() {
    DefaultTableModel model = (DefaultTableModel) jTable.getModel();
    model.setRowCount(0); // Reset isi tabel

    String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
    String user = "root";
    String pass = "";

    String query = "SELECT NamaPengguna, NoRFID, JenisKelamin, Saldo, TanggalUpdate FROM pengguna";

    try (Connection conn = DriverManager.getConnection(url, user, pass);
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("NamaPengguna"),
                rs.getString("NoRFID"),
                rs.getString("JenisKelamin"),
                rs.getBigDecimal("Saldo"),
                rs.getDate("TanggalUpdate")
            });
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}





    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        txnama = new javax.swing.JTextField();
        txrfid = new javax.swing.JTextField();
        txsaldo = new javax.swing.JTextField();
        cbxjenis = new javax.swing.JComboBox<>();
        simpan = new javax.swing.JButton();
        BtHapus = new javax.swing.JButton();
        txIDPengguna = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nama", "RFID", "Jenis Kelamin", "Saldo", "Tanggal Update"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        cbxjenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki Laki", "Perempuan" }));
        cbxjenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxjenisActionPerformed(evt);
            }
        });

        simpan.setText("simpan");
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        BtHapus.setText("Hapus");
        BtHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtHapusActionPerformed(evt);
            }
        });

        jLabel1.setText("ID Pengguna");

        jLabel2.setText("Nama");

        jLabel3.setText("No RFID");

        jLabel4.setText("Saldo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txnama)
                            .addComponent(txIDPengguna)
                            .addComponent(txrfid, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(cbxjenis, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txIDPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxjenis, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txnama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txrfid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addGap(35, 35, 35))
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

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
String idBaru = generateIdPengguna(); // Gunakan sebagai ID unik
String nama = txnama.getText().trim();
String rfid = txrfid.getText().trim();
String jenis = cbxjenis.getSelectedItem().toString();
String saldoText = txsaldo.getText().trim();

if (nama.isEmpty() || rfid.isEmpty() || saldoText.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!");
    return;
}

if (!jenis.equalsIgnoreCase("Laki Laki") && !jenis.equalsIgnoreCase("Perempuan")) {
    JOptionPane.showMessageDialog(this, "Jenis kelamin tidak valid!");
    return;
}

try {
    BigDecimal saldo = new BigDecimal(saldoText);
    java.sql.Date tanggal = new java.sql.Date(System.currentTimeMillis());

    String sql = "INSERT INTO pengguna (IDPengguna, NamaPengguna, NoRFID, JenisKelamin, Saldo, TanggalUpdate) VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement pst = connect.getConnection().prepareStatement(sql);
    pst.setString(1, txIDPengguna.getText().trim()); // ID dari JTextField
    pst.setString(2, nama);
    pst.setString(3, rfid);
    pst.setString(4, jenis);
    pst.setBigDecimal(5, saldo);
    pst.setDate(6, tanggal);


    int rows = pst.executeUpdate();
    if (rows > 0) {
        txIDPengguna.setText(idBaru); // Tampilkan di JTextField
        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        loadTable();
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
    }

    // Kosongkan input
    txnama.setText("");
    txrfid.setText("");
    txsaldo.setText("");
    cbxjenis.setSelectedIndex(0);

} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Format saldo salah!");
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Gagal simpan data: " + e.getMessage());
}

    }//GEN-LAST:event_simpanActionPerformed

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

    private void cbxjenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxjenisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxjenisActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtHapus;
    private javax.swing.JComboBox<String> cbxjenis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JButton simpan;
    private javax.swing.JTextField txIDPengguna;
    private javax.swing.JTextField txnama;
    private javax.swing.JTextField txrfid;
    private javax.swing.JTextField txsaldo;
    // End of variables declaration//GEN-END:variables
}
