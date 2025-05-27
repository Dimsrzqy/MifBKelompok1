package Panel;

/**
 *
 * @author dimsrzqy
 */
import com.sun.jdi.connect.spi.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;

public class PanelDetailTabungan extends javax.swing.JPanel {

    // Konstruktor tanpa parameter
    public PanelDetailTabungan() {
        initComponents(); // Panggil method initComponents yang dihasilkan oleh NetBeans
    }

    // Konstruktor dengan parameter untuk mengisi data pengguna

  public PanelDetailTabungan(String noRFID, String nama, double saldo) {
    initComponents();
    TxNama.setText(nama);
    TxRFID.setText(noRFID);
    TxSaldo.setText(formatRupiah(saldo));
    txNominal.setText("");
}




    // Format Saldo menjadi format Rupiah
    private String formatRupiah(double amount) {
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
        return format.format(amount);
    }
   public static double parseRupiah(String rupiah) {
    // Hilangkan "Rp", titik, dan spasi
    String clean = rupiah.replace("Rp", "")
                         .replace(".", "")
                         .replace(",", ".")
                         .replaceAll("\\s+", "");
    return Double.parseDouble(clean);
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btTambah = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        txNominal = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        TxNama = new javax.swing.JTextField();
        TxSaldo = new javax.swing.JTextField();
        TxRFID = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();

        btTambah.setText("TAMBAH");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        jButton1.setText("SIMPAN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("KURANG");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txNominal, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(TxRFID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                        .addComponent(TxNama, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(TxSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(403, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(TxNama, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TxRFID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txNominal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btTambah)
                        .addComponent(jButton3)))
                .addGap(94, 94, 94)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
  try {
        // Ambil nilai dari field input
        String nominalStr = txNominal.getText().trim();
        String saldoStr = TxSaldo.getText().trim();

        // Jika saldo masih kosong atau belum terisi, anggap 0
        double nominal = Double.parseDouble(nominalStr);
        double saldo = saldoStr.isEmpty() ? 0 : parseRupiah(saldoStr);

        // Hitung total saldo baru
        double total = saldo + nominal;

        // Tampilkan hasilnya dalam format rupiah
        TxSaldo.setText(formatRupiah(total));

        // Kosongkan input nominal
        txNominal.setText("");

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Input nominal harus berupa angka!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btTambahActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
try {
        // Ambil data dari form
        String noRFID = TxRFID.getText().trim();
        String saldoStr = TxSaldo.getText().trim();
        double saldoBaru = parseRupiah(saldoStr); // Pastikan parseRupiah sudah dibuat

        // Koneksi ke database
        java.sql.Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/koperasi_nuris", "root", ""
        );

        // Query update saldo
        String sql = "UPDATE pengguna SET Saldo = ?, TanggalUpdate = NOW() WHERE NoRFID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, saldoBaru);
        stmt.setString(2, noRFID);

        int barisTerpengaruh = stmt.executeUpdate();

        if (barisTerpengaruh > 0) {
            JOptionPane.showMessageDialog(this, "Saldo berhasil diperbarui!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui saldo. RFID tidak ditemukan!");
        }

        // Tutup koneksi
        stmt.close();
        conn.close();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Saldo tidak valid! Masukkan angka yang benar.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
try {
    String nominalStr = txNominal.getText().trim();
    String saldoStr = TxSaldo.getText().trim();

    if (nominalStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nominal harus diisi!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    double nominal = Double.parseDouble(nominalStr);
    if (nominal <= 0) {
        JOptionPane.showMessageDialog(this, "Nominal harus lebih besar dari nol!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    double saldo = saldoStr.isEmpty() ? 0 : parseRupiah(saldoStr);

    if (nominal > saldo) {
        JOptionPane.showMessageDialog(this, "Saldo tidak mencukupi!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    double total = saldo - nominal;
    TxSaldo.setText(formatRupiah(total));
    txNominal.setText("");

} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Input nominal harus berupa angka!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxNama;
    private javax.swing.JTextField TxRFID;
    private javax.swing.JTextField TxSaldo;
    private javax.swing.JButton btTambah;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txNominal;
    // End of variables declaration//GEN-END:variables

 }
