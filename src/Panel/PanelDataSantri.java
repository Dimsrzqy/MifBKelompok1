
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

    // Set ID otomatis saat panel dibuka
    txIDPengguna.setText(generateNoTransaksi());
    txIDPengguna.setEditable(false); // Optional: agar tidak bisa diketik manual
}
public String generateNoTransaksi() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return "TRX" + LocalDateTime.now().format(dtf);
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

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nama", "RFID", "Jenis Kelamin", "Saldo", "Tanggal"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        cbxjenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "laki laki", "perempuan", " " }));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jScrollPane1)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txnama, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txIDPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txrfid, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbxjenis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(230, 230, 230)
                        .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txnama, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(txIDPengguna))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txrfid, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txsaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxjenis, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addGap(36, 36, 36))
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
String idPengguna = generateNoTransaksi(); // Gunakan sebagai ID unik
String nama = txnama.getText().trim();
String rfid = txrfid.getText().trim();
String jenis = cbxjenis.getSelectedItem().toString();
String saldoText = txsaldo.getText().trim();

if (nama.isEmpty() || rfid.isEmpty() || saldoText.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!");
    return;
}

if (!jenis.equalsIgnoreCase("laki laki") && !jenis.equalsIgnoreCase("perempuan")) {
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
        txIDPengguna.setText(idPengguna); // Tampilkan di JTextField
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtHapus;
    private javax.swing.JComboBox<String> cbxjenis;
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
