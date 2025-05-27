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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Panel.stok;

/**
 *
 * @author cindy
 */
public class PopupTambahStok extends javax.swing.JPanel {

    /**
     * Creates new form PopipEditManAkun
     */
    public PopupTambahStok(Map<String, String> stokData) {
        initComponents();
        if (stokData != null && !stokData.isEmpty()) {
        isiDataEdit(stokData);
    } else {
        // Jika tidak ada data, berarti mode tambah
        isiDataAwal();
    }
        
    }

    public interface DataSavedListener {

        void onDataSaved();
    }

    private DataSavedListener listener;

    public void setDataSavedListener(DataSavedListener listener) {
        this.listener = listener;
    }
    
    

    public void isiDataAwal() {
        // Isi combo IDBarang
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
            String sql = "SELECT IDBarang, NamaBarang FROM barang";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            id_barang.removeAllItems();
            while (rs.next()) {
                id_barang.addItem(rs.getString("IDBarang") + " - " + rs.getString("NamaBarang"));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set tanggal hari ini
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = LocalDate.now().format(formatter);
        txtanggal.setText(today);
        id_barang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) id_barang.getSelectedItem();
                if (selected != null && selected.contains(" - ")) {
                    String[] parts = selected.split(" - ", 2);
                    String namaBarang = parts[1];
                    txnama.setText(namaBarang);
                } else {
                    txnama.setText("");
                }
            }
        });
    }
    
    public void isiDataEdit(Map<String, String> stokData) {
    try {
        // Isi combo IDBarang
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
        String sql = "SELECT IDBarang, NamaBarang FROM barang";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        id_barang.removeAllItems();
        while (rs.next()) {
            id_barang.addItem(rs.getString("IDBarang") + " - " + rs.getString("NamaBarang"));
        }

        // Set nilai dari data yang akan diedit
        String idBarang = stokData.get("IDBarang");
        String namaBarang = stokData.get("NamaBarang");
        
        // Set combo box ke IDBarang yang sesuai
        for (int i = 0; i < id_barang.getItemCount(); i++) {
            if (id_barang.getItemAt(i).startsWith(idBarang + " - ")) {
                id_barang.setSelectedIndex(i);
                break;
            }
        }
        
        txnama.setText(namaBarang);
        txjumlam.setText(stokData.get("JumlahMasuk"));
        txjumlak.setText(stokData.get("JumlahKeluar"));
        txtanggal.setText(stokData.get("TanggalUpdate"));
        txkadal.setText(stokData.get("TanggalKadaluarsa"));
        
        // Nonaktifkan field yang tidak boleh diubah
        id_barang.setEnabled(false);
        txnama.setEnabled(false);
        txjumlak.setEnabled(false);
        txtanggal.setEnabled(false);
        txkadal.setEnabled(false);
        
        // Hanya jumlah masuk yang bisa diubah
        txjumlam.setEnabled(true);
        
        // Ubah teks tombol
        simpan.setText("Update Jumlah Masuk");

        rs.close();
        pst.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txnama = new javax.swing.JTextField();
        txjumlam = new javax.swing.JTextField();
        txjumlak = new javax.swing.JTextField();
        txtanggal = new javax.swing.JTextField();
        txkadal = new javax.swing.JTextField();
        id_barang = new javax.swing.JComboBox<>();
        simpan = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(28, 69, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 517, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jLabel8.setText("ID_Barang");

        jLabel9.setText("Nama Barang");

        jLabel10.setText("Jumah Masuk");

        jLabel11.setText("Jumlah Keluar");

        jLabel12.setText("Tanggal Update");

        jLabel13.setText("Kadaluarsa");

        txjumlam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txjumlamActionPerformed(evt);
            }
        });

        txjumlak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txjumlakActionPerformed(evt);
            }
        });

        txtanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtanggalActionPerformed(evt);
            }
        });

        txkadal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txkadalActionPerformed(evt);
            }
        });

        id_barang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        simpan.setText("Simpan");
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
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
                    .addComponent(simpan)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txnama)
                            .addComponent(txjumlam)
                            .addComponent(txjumlak)
                            .addComponent(txtanggal)
                            .addComponent(txkadal)
                            .addComponent(id_barang, 0, 250, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(id_barang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txjumlam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txjumlak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txkadal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(simpan)
                .addContainerGap(115, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
      try {
        String selected = (String) id_barang.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) {
            JOptionPane.showMessageDialog(this, "ID Barang tidak valid!");
            return;
        }

        String[] parts = selected.split(" - ", 2);
        String idBarang = parts[0];
        
        String jumlahMasukStr = txjumlam.getText();

        if (jumlahMasukStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah masuk harus diisi!");
            return;
        }

        int jumlahMasuk = Integer.parseInt(jumlahMasukStr);

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
        
        // Query UPDATE yang hanya memperbarui jumlah masuk
        String sql = "UPDATE stok SET JumlahMasuk = ? WHERE IDBarang = ?";
        
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, jumlahMasuk);
        pst.setString(2, idBarang);

        int row = pst.executeUpdate();
        if (row > 0) {
            JOptionPane.showMessageDialog(this, "Jumlah masuk berhasil diperbarui!");

            // kasih tahu listener kalau ada yang daftar (stok panel) buat refresh
            if (listener != null) {
                listener.onDataSaved();
            }

            // tutup popup
            SwingUtilities.getWindowAncestor(this).dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui jumlah masuk.");
        }

        pst.close();
        conn.close();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Jumlah masuk harus angka!");
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_simpanActionPerformed

    private void txjumlamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txjumlamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txjumlamActionPerformed

    private void txjumlakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txjumlakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txjumlakActionPerformed

    private void txtanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtanggalActionPerformed

    private void txkadalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txkadalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txkadalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> id_barang;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton simpan;
    private javax.swing.JTextField txjumlak;
    private javax.swing.JTextField txjumlam;
    private javax.swing.JTextField txkadal;
    private javax.swing.JTextField txnama;
    private javax.swing.JTextField txtanggal;
    // End of variables declaration//GEN-END:variables

}
