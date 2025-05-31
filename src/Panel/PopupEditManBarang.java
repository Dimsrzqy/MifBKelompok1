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
import java.text.NumberFormat;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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

        txbarcode.setEditable(false);

        loadKategoriFromDatabase();

        this.idBarang = userData.get("IDBarang"); // Set idBarang agar tidak null saat update
        // Set nilai ke semua field dengan pengecekan null
        txnama.setText(userData.getOrDefault("NamaBarang", ""));
        txhargab.setText("Rp " + userData.getOrDefault("HargaBeli", ""));
        txhargaj.setText("Rp " + userData.getOrDefault("HargaJual", ""));
        txkadaluarsa.setText(formatTanggal(userData.getOrDefault("TanggalKadaluarsa", "")));
        txtanggalm.setText(formatTanggal(userData.getOrDefault("TanggalMasuk", "")));
        txbarcode.setText(userData.getOrDefault("Barcode", ""));
        txbarcode.setEditable(false);

        // Handle combo box (Kategori)
        String NamaKategori = userData.get("NamaKategori");
        if (NamaKategori != null) {
            for (Map.Entry<String, String> entry : kategoriMap.entrySet()) {
                if (entry.getValue().equals(NamaKategori)) {
                    cbkategori.setSelectedItem(entry.getKey());
                    break;
                }
            }
        }

    }

    private String parseTanggal(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat fromDisplay = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat toDB = new SimpleDateFormat("yyyy-MM-dd");
            return toDB.format(fromDisplay.parse(input));
        } catch (ParseException e) {
            return input;
        }
    }

    private String formatRupiah(String value) {
        try {
            int amount = Integer.parseInt(value);
            Locale localeID = new Locale("in", "ID");
            NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
            return format.format(amount).replace("Rp", "Rp ");
        } catch (NumberFormatException e) {
            return value; // fallback jika bukan angka
        }
    }

    private String formatTanggal(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat toDisplay = new SimpleDateFormat("dd-MM-yyyy");
            return toDisplay.format(fromDB.parse(input));
        } catch (ParseException e) {
            return input;
        }
    }

    public Map<String, String> getUpdatedData() {
        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("IDBarang", idBarang);
        updatedData.put("NamaBarang", txnama.getText().trim());

        String namaKategoriDipilih = cbkategori.getSelectedItem().toString();
        String NamaKategori = kategoriMap.get(namaKategoriDipilih); // ✅ ambil ID dari map

        updatedData.put("NamaKategori", NamaKategori);
        updatedData.put("HargaBeli", txhargab.getText().trim());
        updatedData.put("HargaJual", txhargaj.getText().trim());
        updatedData.put("Kadaluarsa", txkadaluarsa.getText().trim());
        updatedData.put("TanggalMasuk", txtanggalm.getText().trim());
        updatedData.put("Barcode", txbarcode.getText().trim());
        return updatedData;
    }

    private String idBarang; // untuk menyimpan IDBarang yang sedang diedit

    public void setIDBarang(String id) {
        this.idBarang = id;
    }

    Map<String, String> kategoriMap = new HashMap<>(); // key: IDKategori, value: NamaKategori

    private void loadKategoriFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
            String sql = "SELECT IDKategori, NamaKategori FROM kategori"; // ✅ Diperbaiki
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            cbkategori.removeAllItems();
            kategoriMap.clear();

            while (rs.next()) {
                String IDKategori = rs.getString("IDKategori");
                String NamaKategori = rs.getString("NamaKategori");
                kategoriMap.put(NamaKategori, IDKategori); // key: nama, value: ID
                cbkategori.addItem(NamaKategori);
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
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txhargab)
                        .addComponent(txkadaluarsa)
                        .addComponent(txnama, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                        .addComponent(txhargaj, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                        .addComponent(txtanggalm)
                        .addComponent(cbkategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
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
                    .addComponent(cbkategori, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtanggalm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txbarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txkadaluarsaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txkadaluarsaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txkadaluarsaActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        try {
            // Ambil data dari form
            String NamaKategoriTerpilih = cbkategori.getSelectedItem().toString();
            String NamaKategori = kategoriMap.get(NamaKategoriTerpilih);
            Map<String, String> data = getUpdatedData();

            if (NamaKategori == null) {
                JOptionPane.showMessageDialog(this, "Kategori tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse dan validasi data
            String namaBarang = data.get("NamaBarang");
            String hargaBeliStr = data.get("HargaBeli").replaceAll("[^\\d]", "");
            String hargaJualStr = data.get("HargaJual").replaceAll("[^\\d]", "");
            int hargaBeli = Integer.parseInt(hargaBeliStr);
            int hargaJual = Integer.parseInt(hargaJualStr);
            String tanggalMasuk = parseTanggal(data.get("TanggalMasuk"));
            String kadaluarsa = parseTanggal(data.get("Kadaluarsa")); // ⬅️ ambil tanggal kadaluarsa dari form
            String barcode = data.get("Barcode");

            if (hargaJual <= hargaBeli) {
                JOptionPane.showMessageDialog(this, "Harga jual harus lebih besar dari harga beli", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "")) {
                conn.setAutoCommit(false); // ⬅️ Mulai transaksi

                // Update barang
                String sqlBarang = "UPDATE barang SET NamaBarang=?, IDKategori=?, HargaBeli=?, HargaJual=?, TanggalMasuk=?, Barcode=? WHERE IDBarang=?";
                try (PreparedStatement pstBarang = conn.prepareStatement(sqlBarang)) {
                    pstBarang.setString(1, namaBarang);
                    pstBarang.setString(2, NamaKategori);
                    pstBarang.setInt(3, hargaBeli);
                    pstBarang.setInt(4, hargaJual);
                    pstBarang.setString(5, tanggalMasuk);
                    pstBarang.setString(6, barcode);
                    pstBarang.setString(7, idBarang);
                    pstBarang.executeUpdate();
                }

                // Update stok
                String sqlStok = "UPDATE stok SET TanggalKadaluarsa=? WHERE IDBarang=?";
                try (PreparedStatement pstStok = conn.prepareStatement(sqlStok)) {
                    pstStok.setString(1, kadaluarsa);
                    pstStok.setString(2, idBarang);
                    pstStok.executeUpdate();
                }

                conn.commit(); // ⬅️ Commit transaksi

                JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
                FormMenuUtama.showForm(new PanelManajemenBarang());

                // Tambahan: Menutup popup form
                if (parentDialog != null) {
                parentDialog.dispose();
                }  
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format harga tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

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
