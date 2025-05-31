package Panel;

/**
 *
 * @author dimsrzqy
 */
import com.sun.jdi.connect.spi.Connection;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Date;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PanelDetailTabungan extends javax.swing.JPanel {
    private static final String PREFIX = "Rp ";
   
    public PanelDetailTabungan() {
        initComponents();
    }

    // Konstruktor dengan parameter untuk mengisi data pengguna

public PanelDetailTabungan(String noRFID, String nama, double saldo, double nominal) {
    initComponents();
    TxNama.setText(nama);
    TxRFID.setText(noRFID);
    TxSaldo.setText(formatRupiah(saldo));

    if (nominal <= 0) {
        txNominal.setText("");  // kosongkan kalau nominal <= 0
    } else {
        txNominal.setText(formatRupiah(nominal));
    }

    setupRpPrefixFilter(txNominal);
}

private void setupRpPrefixFilter(JTextField field) {
    ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null) {
                string = string.replaceAll("[^0-9]", "");
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null) {
                text = text.replaceAll("[^0-9]", "");
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    });
}

private void resetTxNominal() {
    SwingUtilities.invokeLater(() -> {
        txNominal.setText("");
        txNominal.requestFocus();
    });
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
        btSimpan = new javax.swing.JButton();
        txNominal = new javax.swing.JTextField();
        btKurang = new javax.swing.JButton();
        TxNama = new javax.swing.JTextField();
        TxSaldo = new javax.swing.JTextField();
        TxRFID = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        btTambah.setBackground(new java.awt.Color(28, 69, 50));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("TAMBAH");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        btSimpan.setBackground(new java.awt.Color(28, 69, 50));
        btSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btSimpan.setText("SIMPAN");
        btSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSimpanActionPerformed(evt);
            }
        });

        btKurang.setBackground(new java.awt.Color(255, 0, 0));
        btKurang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btKurang.setForeground(new java.awt.Color(255, 255, 255));
        btKurang.setText("KURANG");
        btKurang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKurangActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(28, 69, 52));

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

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("Detail Tabungan");

        jLabel17.setText("Admin > Data Manajemen Akun");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txNominal, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btKurang))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(TxRFID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                                .addComponent(TxNama, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(TxSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(401, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(TxNama, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TxRFID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TxSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txNominal, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(btTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btKurang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(94, 94, 94)
                .addComponent(btSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
try {
    String nominalStr = txNominal.getText().trim();

    // Validasi kosong
    if (nominalStr.equals("Rp") || nominalStr.equals("Rp ")) {
        JOptionPane.showMessageDialog(this, "Nominal harus diisi!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Konversi nominal
    double nominal = parseRupiah(nominalStr);
    if (nominal <= 0) {
        JOptionPane.showMessageDialog(this, "Nominal harus lebih besar dari nol!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Ambil saldo saat ini
    double saldo = TxSaldo.getText().isEmpty() ? 0 : parseRupiah(TxSaldo.getText());

    // Hitung saldo baru
    double total = saldo + nominal;

    // Update field saldo
    TxSaldo.setText(formatRupiah(total));

    // Reset nominal input
    resetTxNominal();

    // Beri notifikasi sukses
    JOptionPane.showMessageDialog(this, "Saldo berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Input nominal harus berupa angka!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_btTambahActionPerformed

    private void btSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSimpanActionPerformed
try {
    // Ambil data dari form
    String noRFID = TxRFID.getText().trim();
    String namaPenggunaBaru = TxNama.getText().trim();  // Pastikan ada input nama pengguna di form
    String saldoStr = TxSaldo.getText().trim();
    double saldoBaru = parseRupiah(saldoStr); // Fungsi parseRupiah harus mengubah format Rupiah ke double

    // Koneksi ke database
    java.sql.Connection conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/koperasi_nuris", "root", ""
    );

    // Ambil data pengguna lama berdasarkan NoRFID
    String queryOld = "SELECT IDPengguna, NamaPengguna, Saldo FROM pengguna WHERE NoRFID = ?";
    PreparedStatement stmtOld = conn.prepareStatement(queryOld);
    stmtOld.setString(1, noRFID);
    ResultSet rs = stmtOld.executeQuery();

    if (rs.next()) {
        // Ambil IDPengguna sebagai String karena formatnya non-numeric (misal 'SNTR-0020')
        String idPengguna = rs.getString("IDPengguna");
        String namaPenggunaLama = rs.getString("NamaPengguna");
        double saldoLama = rs.getDouble("Saldo");

        // Hitung mutasi masuk/keluar dan jenis transaksi
        double saldoMasuk = 0;
        double saldoKeluar = 0;
        String jenisTransaksi = "";

        if (saldoBaru > saldoLama) {
            saldoMasuk = saldoBaru - saldoLama;
            jenisTransaksi = "Setor";
        } else if (saldoBaru < saldoLama) {
            saldoKeluar = saldoLama - saldoBaru;
            jenisTransaksi = "Tarik";
        } else {
            jenisTransaksi = "Tidak Berubah";
        }

        // Update data pengguna (nama dan saldo)
        String sqlUpdate = "UPDATE pengguna SET NamaPengguna = ?, Saldo = ?, TanggalUpdate = NOW() WHERE NoRFID = ?";
        PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        stmtUpdate.setString(1, namaPenggunaBaru);
        stmtUpdate.setDouble(2, saldoBaru);
        stmtUpdate.setString(3, noRFID);

        int barisTerpengaruh = stmtUpdate.executeUpdate();

        if (barisTerpengaruh > 0) {
            // Insert mutasi jika ada perubahan saldo
            if (!jenisTransaksi.equals("Tidak Berubah")) {
                String insertMutasi = "INSERT INTO mutasi (IDMutasi, IDPengguna, NamaPengguna, NoRFID, SaldoMasuk, SaldoKeluar, Saldo, JenisTransaksi, Tanggal) " +
                                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

                String tanggal = new SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
                String idMutasi = String.format("MT%s%04d", tanggal, (int)(Math.random() * 10000));

                PreparedStatement stmtMutasi = conn.prepareStatement(insertMutasi);
                stmtMutasi.setString(1, idMutasi);
                stmtMutasi.setString(2, idPengguna);           // <-- ganti setInt jadi setString
                stmtMutasi.setString(3, namaPenggunaBaru);
                stmtMutasi.setString(4, noRFID);
                stmtMutasi.setDouble(5, saldoMasuk);
                stmtMutasi.setDouble(6, saldoKeluar);
                stmtMutasi.setDouble(7, saldoBaru);
                stmtMutasi.setString(8, jenisTransaksi);
                stmtMutasi.executeUpdate();
                stmtMutasi.close();
            }

            JOptionPane.showMessageDialog(this, "Data pengguna dan saldo berhasil diperbarui!");

            // Tutup jendela popup
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data pengguna!");
        }

        stmtUpdate.close();
    } else {
        JOptionPane.showMessageDialog(this, "Pengguna dengan RFID tersebut tidak ditemukan!");
    }

    rs.close();
    stmtOld.close();
    conn.close();

} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Saldo tidak valid! Masukkan angka yang benar.",
        "Kesalahan", JOptionPane.ERROR_MESSAGE);
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage(),
        "Kesalahan", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_btSimpanActionPerformed

    private void btKurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKurangActionPerformed
try {
    String nominalStr = txNominal.getText().trim();

    if (nominalStr.isEmpty() || nominalStr.equals("Rp")) {
        JOptionPane.showMessageDialog(this, "Nominal harus diisi!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    double nominal = parseRupiah(nominalStr);
    if (nominal <= 0) {
        JOptionPane.showMessageDialog(this, "Nominal harus lebih besar dari nol!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String saldoStr = TxSaldo.getText().trim();
    double saldo = saldoStr.isEmpty() ? 0 : parseRupiah(saldoStr);

    if (nominal > saldo) {
        JOptionPane.showMessageDialog(this, "Saldo tidak mencukupi!", "Kesalahan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Transaksi berhasil: update saldo dan reset nominal
    double total = saldo - nominal;
    TxSaldo.setText(formatRupiah(total)); // update tampilan saldo
    resetTxNominal();                     // reset input nominal

    JOptionPane.showMessageDialog(this, "Transaksi berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Input nominal harus berupa angka!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_btKurangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxNama;
    private javax.swing.JTextField TxRFID;
    private javax.swing.JTextField TxSaldo;
    private javax.swing.JButton btKurang;
    private javax.swing.JButton btSimpan;
    private javax.swing.JButton btTambah;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txNominal;
    // End of variables declaration//GEN-END:variables

 }
