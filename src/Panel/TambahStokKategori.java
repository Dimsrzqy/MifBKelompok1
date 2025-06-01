/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import java.awt.Dialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Infinix
 */
public class TambahStokKategori extends javax.swing.JPanel {

    public TambahStokKategori() {
        initComponents();
        IDKategori.setText(generateIdKategoriBaru());
        IDKategori.setEditable(false);
    }

    // Method untuk menutup window induk (JDialog/JFrame)
    private void tutupWindowInduk() {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        IDKategori = new javax.swing.JTextField();
        NamaKategori = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Id Kategori");

        jLabel2.setText("Nama Kategori");

        IDKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDKategoriActionPerformed(evt);
            }
        });

        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(IDKategori)
                            .addComponent(NamaKategori, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(IDKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(116, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
     private String generateIdKategoriBaru() {
    String idBaru = "KTG01";
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
        Statement stmt = conn.createStatement();
        String sql = "SELECT IDKategori FROM kategori ORDER BY IDKategori DESC LIMIT 1";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            String idTerakhir = rs.getString("IDKategori"); // Misalnya: KTG12
            int angka = Integer.parseInt(idTerakhir.substring(3)); // Ambil angka "12"
            angka++;
            idBaru = String.format("KTG%02d", angka); // Hasil: KTG13
        }

        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return idBaru;
}

    
    
    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
String idKategori = IDKategori.getText().trim();
String namaKategori = NamaKategori.getText().trim();

try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO kategori (IDKategori, NamaKategori) VALUES (?, ?)")) {

    pstmt.setString(1, idKategori);
    pstmt.setString(2, namaKategori);

    int rowsAffected = pstmt.executeUpdate();

    if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!", 
            "Sukses", JOptionPane.INFORMATION_MESSAGE);

        // Kosongkan field setelah berhasil ditambahkan
        IDKategori.setText("");
        NamaKategori.setText("");

        tutupWindowInduk(); // Tutup dialog setelah berhasil
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menambahkan kategori", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

} catch (SQLException e) {
    e.printStackTrace();
}

    }//GEN-LAST:event_jButton1ActionPerformed

    private void IDKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDKategoriActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IDKategori;
    private javax.swing.JTextField NamaKategori;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
