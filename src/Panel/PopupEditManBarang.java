package Panel;

import Form.FormMenuUtama;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author cindy
 */
public class PopupEditManBarang extends javax.swing.JPanel {

    /**
     * Creates new form PopipEditManAkun
     */
    public PopupEditManBarang(Map<String, String> userData) {
        initComponents();
        loadKategoriFromDatabase();

        this.idBarang = userData.get("IDBarang"); // Set idBarang agar tidak null saat update
        // Set nilai ke semua field dengan pengecekan null
        txnama.setText(userData.getOrDefault("NamaBarang", ""));
        txhargab.setText(userData.getOrDefault("HargaBeli", ""));
        txhargaj.setText(userData.getOrDefault("HargaJual", ""));
        txkadaluarsa.setText(userData.getOrDefault("Kadaluarsa", ""));
        txtanggalm.setText(userData.getOrDefault("TanggalMasuk", ""));
        txbarcode.setText(userData.getOrDefault("Barcode", ""));
        txbarcode.setEditable(false);

        // Handle combo box (Kategori)
        String idKategori = userData.get("IDKategori");
        if (idKategori != null) {
            // Cari nama kategori yang sesuai dengan ID
            for (Map.Entry<String, String> entry : kategoriMap.entrySet()) {
                if (entry.getValue().equals(idKategori)) {
                    cbkategori.setSelectedItem(entry.getKey());
                    break;
                }
            }
        }

    }

    public Map<String, String> getUpdatedData() {
        Map<String, String> data = new HashMap<>();

        data.put("NamaBarang", txnama.getText().trim());
        data.put("IDKategori", cbkategori.getSelectedItem().toString());
        data.put("HargaBeli", txhargab.getText().trim());
        data.put("HargaJual", txhargaj.getText().trim());
        data.put("Kadaluarsa", txkadaluarsa.getText().trim());
        data.put("TanggalMasuk", txtanggalm.getText().trim());
        data.put("Barcode", txbarcode.getText().trim());

        return data;
    }
    private String idBarang; // untuk menyimpan IDBarang yang sedang diedit

    public void setIDBarang(String id) {
        this.idBarang = id;
    }

    private Map<String, String> kategoriMap = new HashMap<>(); // key=NamaKategori, value=IDKategori

    private void loadKategoriFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
            String sql = "SELECT IDKategori, Kategori FROM kategori";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            cbkategori.removeAllItems();
            kategoriMap.clear();

            while (rs.next()) {
                String idKategori = rs.getString("IDKategori");
                String namaKategori = rs.getString("Kategori");
                kategoriMap.put(namaKategori, idKategori);
                cbkategori.addItem(namaKategori);
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat kategori: " + e.getMessage());
        }
    }

    private JDialog parentDialog;

    public void setParentDialog(JDialog dialog) {
        this.parentDialog = dialog;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txhargab = new javax.swing.JTextField();
        txnama = new javax.swing.JTextField();
        txhargaj = new javax.swing.JTextField();
        txkadaluarsa = new javax.swing.JTextField();
        txbarcode = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        simpan = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtanggalm = new javax.swing.JTextField();
        cbkategori = new javax.swing.JComboBox<>();

        txhargab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txhargabActionPerformed(evt);
            }
        });

        txnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txnamaActionPerformed(evt);
            }
        });

        txhargaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txhargajActionPerformed(evt);
            }
        });

        txkadaluarsa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txkadaluarsaActionPerformed(evt);
            }
        });

        txbarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbarcodeActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(28, 69, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jLabel1.setText("Nama Barang");

        jLabel2.setText("Kategori");

        jLabel3.setText("Harga Beli");

        jLabel4.setText("Harga Jual");

        jLabel5.setText("Kadaluarsa");

        jLabel6.setText("Tanggal Masuk");

        simpan.setBackground(new java.awt.Color(51, 153, 255));
        simpan.setText("SIMPAN");
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });

        jLabel7.setText("Barcode");

        txtanggalm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtanggalmActionPerformed(evt);
            }
        });

        cbkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbkategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbkategoriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel7)
                        .addGap(34, 34, 34)
                        .addComponent(txbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txhargab)
                            .addComponent(txkadaluarsa)
                            .addComponent(txnama, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txhargaj, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txtanggalm)
                            .addComponent(cbkategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txnama, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txhargab, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txhargaj, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txkadaluarsa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtanggalm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txkadaluarsaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txkadaluarsaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txkadaluarsaActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        try {
            // Ambil nama kategori dari combo box
            String namaKategoriTerpilih = cbkategori.getSelectedItem().toString();
            // Ambil IDKategori sesuai nama kategori yang dipilih
            String idKategori = kategoriMap.get(namaKategoriTerpilih);

            Map<String, String> data = getUpdatedData();

            String namaBarang = data.get("NamaBarang");
            // Ganti pakai idKategori yang diambil dari map
            // String idKategori = data.get("IDKategori");  // jangan pakai ini lagi
            int hargaBeli = Integer.parseInt(data.get("HargaBeli"));
            int hargaJual = Integer.parseInt(data.get("HargaJual"));
            String kadaluarsa = data.get("Kadaluarsa");
            String tanggalMasuk = data.get("TanggalMasuk");
            String barcode = data.get("Barcode");

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");

            String sql = "UPDATE barang SET NamaBarang=?, IDKategori=?, HargaBeli=?, HargaJual=?, Kadaluarsa=?, TanggalMasuk=?, Barcode=? WHERE IDBarang=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, namaBarang);
            pst.setString(2, idKategori);    // <- ini tambahan untuk kategori
            pst.setInt(3, hargaBeli);
            pst.setInt(4, hargaJual);
            pst.setString(5, kadaluarsa);
            pst.setString(6, tanggalMasuk);
            pst.setString(7, barcode);
            pst.setString(8, idBarang);

            int row = pst.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");

                // Tampilkan kembali panel utama Manajemen Barang
                FormMenuUtama.showForm(new PanelManajemenBarang());
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal diperbarui.");
            }

            pst.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("IDBarang yang diupdate: " + idBarang);

    }//GEN-LAST:event_simpanActionPerformed

    private void txhargajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txhargajActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txhargajActionPerformed

    private void txnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txnamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txnamaActionPerformed

    private void txtanggalmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtanggalmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtanggalmActionPerformed

    private void txbarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbarcodeActionPerformed

    private void cbkategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbkategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbkategoriActionPerformed

    private void txhargabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txhargabActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txhargabActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbkategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton simpan;
    private javax.swing.JTextField txbarcode;
    private javax.swing.JTextField txhargab;
    private javax.swing.JTextField txhargaj;
    private javax.swing.JTextField txkadaluarsa;
    private javax.swing.JTextField txnama;
    private javax.swing.JTextField txtanggalm;
    // End of variables declaration//GEN-END:variables

}
