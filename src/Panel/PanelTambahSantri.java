/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Form.connect;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author dimsrzqy
 */
public class PanelTambahSantri extends javax.swing.JPanel {
    private PanelDataSantri panelInduk;

    /**
     * Creates new form PanelTambahSantri
     */
public PanelTambahSantri(PanelDataSantri panelInduk) {
    initComponents();
    this.panelInduk = panelInduk;

    txIDPengguna.setText(generateIdPengguna());
    txIDPengguna.setEditable(false);

    pasangFormatRupiahDenganPrefix(txSaldo);
    txSaldo.setText("Rp");  // Default hanya tampil "Rp", tanpa angka 0
}

    public String generateIdPengguna() {
        String prefix = "SNTR-";
        int nextNumber = 1;

        String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
        String user = "root";
        String pass = "";

        String query = "SELECT MAX(CAST(SUBSTRING(IDPengguna, 6) AS UNSIGNED)) AS max_id FROM pengguna WHERE IDPengguna LIKE 'SNTR-%'";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                if (maxId > 0) {
                    nextNumber = maxId + 1;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return String.format("%s%04d", prefix, nextNumber);
    }

private void pasangFormatRupiahDenganPrefix(JTextField field) {
    field.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            String text = field.getText();
            if (!text.startsWith("Rp")) {
                field.setText("Rp");
            }
            SwingUtilities.invokeLater(() -> field.setCaretPosition(field.getText().length()));
        }

        @Override
        public void focusLost(FocusEvent e) {
            String text = field.getText().replace("Rp", "").trim();
            if (text.isEmpty()) {
                field.setText("Rp");
            } else {
                try {
                    String digitsOnly = text.replaceAll("[^\\d]", "");
                    long value = Long.parseLong(digitsOnly);
                    String formattedNumber = formatRupiah(value).replace("Rp", "").trim();
                    field.setText("Rp " + formattedNumber);
                } catch (NumberFormatException ex) {
                    field.setText("Rp");
                }
            }
        }
    });
}

private String formatRupiah(long value) {
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    return nf.format(value);
}

private BigDecimal parseRupiahToBigDecimal(String text) {
    if (text == null || text.isEmpty()) return BigDecimal.ZERO;

    // Hapus "Rp", spasi, dan karakter non-digit kecuali koma dan titik
    String clean = text.replaceAll("[Rp\\s]", "");
    // Hapus tanda titik ribuan
    clean = clean.replaceAll("\\.", "");
    // Ganti koma desimal menjadi titik
    clean = clean.replace(',', '.');

    try {
        return new BigDecimal(clean);
    } catch (NumberFormatException e) {
        return BigDecimal.ZERO; // Bisa juga lempar exception jika ingin lebih ketat
    }
}

private void simpanData() {
    String idBaru = txIDPengguna.getText().trim();
    String nama = txNama.getText().trim();
    String rfid = txRfid.getText().trim();
    String jenisKelamin = cbxJenisKelamin.getSelectedItem().toString();
    String asrama = cbxAsrama.getSelectedItem().toString();
    String lembaga = cbxLembaga.getSelectedItem().toString();
    String saldoText = txSaldo.getText().trim();

    if (nama.isEmpty() || rfid.isEmpty() || saldoText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!");
        return;
    }

    if (!jenisKelamin.equalsIgnoreCase("Laki Laki") && !jenisKelamin.equalsIgnoreCase("Perempuan")) {
        JOptionPane.showMessageDialog(this, "Jenis kelamin tidak valid!");
        return;
    }

    String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
    String user = "root";
    String pass = "";

    try (Connection conn = DriverManager.getConnection(url, user, pass)) {
        // Cek duplikat RFID
        String cekRFID = "SELECT COUNT(*) FROM pengguna WHERE NoRFID = ?";
        try (PreparedStatement cekStmt = conn.prepareStatement(cekRFID)) {
            cekStmt.setString(1, rfid);
            ResultSet rs = cekStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "RFID sudah terdaftar! Gunakan RFID lain.");
                return;
            }
        }

        // Lanjut insert jika RFID unik
        BigDecimal saldo = parseRupiahToBigDecimal(saldoText);
        java.sql.Date tanggal = new java.sql.Date(System.currentTimeMillis());

        String sql = "INSERT INTO pengguna (IDPengguna, NamaPengguna, NoRFID, JenisKelamin, Asrama, Lembaga, Saldo, TanggalUpdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, idBaru);
            pst.setString(2, nama);
            pst.setString(3, rfid);
            pst.setString(4, jenisKelamin);
            pst.setString(5, asrama);
            pst.setString(6, lembaga);
            pst.setBigDecimal(7, saldo);
            pst.setDate(8, tanggal);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");

                // Refresh data di panel induk
                if (panelInduk != null) {
                    panelInduk.refreshTableData();
                }

                // Tutup dialog popup jika ada
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof JDialog) {
                    ((JDialog) window).dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal simpan data: " + e.getMessage());
        e.printStackTrace();
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txIDPengguna = new javax.swing.JTextField();
        txNama = new javax.swing.JTextField();
        txRfid = new javax.swing.JTextField();
        txSaldo = new javax.swing.JTextField();
        cbxJenisKelamin = new javax.swing.JComboBox<>();
        cbxAsrama = new javax.swing.JComboBox<>();
        cbxLembaga = new javax.swing.JComboBox<>();
        btSimpan = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        cbxJenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "laki laki", "Perempuan" }));

        cbxAsrama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Putra Pusat", "Putra SMK", "Nuris 3", "Nuris 4", "Dalbar", "Daltim", "Tahfidz" }));

        cbxLembaga.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SMP", "MTS", "SMA", "SMK", "MA" }));

        btSimpan.setText("SIMPAN");
        btSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSimpanActionPerformed(evt);
            }
        });

        jLabel8.setText("ID Pengguna");

        jLabel9.setText("Nama Santri");

        jLabel10.setText("NO RFID");

        jLabel11.setText("Jenis Kelamin");

        jLabel12.setText("Asrama");

        jLabel13.setText("Lembaga");

        jLabel14.setText("Saldo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(105, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel10))
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13))))
                        .addGap(129, 129, 129)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txIDPengguna)
                                .addComponent(txNama)
                                .addComponent(txRfid)
                                .addComponent(txSaldo)
                                .addComponent(cbxJenisKelamin, 0, 284, Short.MAX_VALUE)
                                .addComponent(cbxAsrama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(cbxLembaga, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txIDPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNama, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txRfid, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxJenisKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxAsrama, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxLembaga, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addComponent(btSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSimpanActionPerformed
        simpanData();
    }//GEN-LAST:event_btSimpanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSimpan;
    private javax.swing.JComboBox<String> cbxAsrama;
    private javax.swing.JComboBox<String> cbxJenisKelamin;
    private javax.swing.JComboBox<String> cbxLembaga;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txIDPengguna;
    private javax.swing.JTextField txNama;
    private javax.swing.JTextField txRfid;
    private javax.swing.JTextField txSaldo;
    // End of variables declaration//GEN-END:variables
}
