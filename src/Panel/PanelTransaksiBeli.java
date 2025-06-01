package Panel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class PanelTransaksiBeli extends javax.swing.JPanel {
    
    
    public PanelTransaksiBeli() {
        initComponents();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        LbTanggal.setText(LocalDate.now().format(dtf));
        txNoTransaksi.setText(generateNoTransaksi());
        loadKategori();
    }
    
    private void tampilkanNamaKasir() {
    txNamaKasir.setText(PanelLogin.UserSession.getNamaKasir());
    
}
    
    public String generateNoTransaksi() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return "TRB" + LocalDateTime.now().format(dtf);
    
}
    
    private void insertData() {
    String namaBarang = txNamaProduk.getText().trim();
    String kategoriNama = CbKategori.getSelectedItem().toString();
    String hargaBeliStr = txHargaBeli.getText().trim();
    String hargaJualStr = txHargaJual.getText().trim();
    String barcode = txBarcode.getText().trim();
    String stokStr = txStok.getText().trim();

    java.util.Date kadaluarsaUtil = jDateKadaluarsa.getDate();
    java.sql.Date tanggalKadaluarsa = kadaluarsaUtil != null ? new java.sql.Date(kadaluarsaUtil.getTime()) : null;

    if (namaBarang.isEmpty() || kategoriNama.equals("Pilih Kategori") || hargaBeliStr.isEmpty()
            || hargaJualStr.isEmpty() || stokStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua Kolom Wajib Diisi (kecuali Barcode dan Tanggal Kadaluarsa)!", "Validasi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection conn = kasir.main.PanelTransaksiBeli.Koneksi.getConnection()) {
        conn.setAutoCommit(false); // Mulai transaksi

        double hargaBeli = Double.parseDouble(hargaBeliStr);
        double hargaJual = Double.parseDouble(hargaJualStr);
        int stok = Integer.parseInt(stokStr);

        // Ambil IDKategori
        String idKategori = null;
        String sqlCari = "SELECT IDKategori FROM kategori WHERE NamaKategori = ?";
        try (PreparedStatement pst = conn.prepareStatement(sqlCari)) {
            pst.setString(1, kategoriNama);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    idKategori = rs.getString("IDKategori");
                } else {
                    JOptionPane.showMessageDialog(this, "Kategori tidak ditemukan di database!");
                    return;
                }
            }
        }

        // Generate IDBarang: BRGxxx
        String idBarang = null;
        String sqlMaxBarang = "SELECT MAX(IDBarang) AS max_id FROM barang";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlMaxBarang)) {
            if (rs.next() && rs.getString("max_id") != null) {
                String lastId = rs.getString("max_id");
                int lastNum = Integer.parseInt(lastId.substring(3)); // Ambil angka setelah BRG
                idBarang = String.format("BRG%03d", lastNum + 1);
            } else {
                idBarang = "BRG001";
            }
        }

        // Generate IDStok: STKxxx
        String idStok = null;
        String sqlMaxStok = "SELECT MAX(IDStok) AS max_id FROM stok";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sqlMaxStok)) {
            if (rs.next() && rs.getString("max_id") != null) {
                String lastId = rs.getString("max_id");
                int lastNum = Integer.parseInt(lastId.substring(3)); // Ambil angka setelah STK
                idStok = String.format("STK%03d", lastNum + 1);
            } else {
                idStok = "STK001";
            }
        }

        // Insert ke tabel barang
        String sqlBarang = "INSERT INTO barang (IDBarang, IDKategori, NamaBarang, HargaBeli, HargaJual, Barcode, TanggalMasuk) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sqlBarang)) {
            st.setString(1, idBarang);
            st.setString(2, idKategori);
            st.setString(3, namaBarang);
            st.setDouble(4, hargaBeli);
            st.setDouble(5, hargaJual);
            st.setString(6, barcode);
            st.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            st.executeUpdate();
        }

        // Insert ke tabel stok
        String sqlStok = "INSERT INTO stok (IDStok, IDBarang, JumlahMasuk, JumlahKeluar, TanggalUpdate, TanggalKadaluarsa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sqlStok)) {
            st.setString(1, idStok);
            st.setString(2, idBarang);
            st.setInt(3, stok);
            st.setInt(4, 0); // Jumlah keluar otomatis 0
            st.setDate(5, new java.sql.Date(System.currentTimeMillis())); // Tanggal update = now
            if (tanggalKadaluarsa != null) {
                st.setDate(6, tanggalKadaluarsa);
            } else {
                st.setNull(6, java.sql.Types.DATE);
            }
            st.executeUpdate();
        }

        conn.commit(); // Jika semua berhasil, commit
        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
        resetForm();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Pastikan harga dan stok berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        try {
            Connection conn = kasir.main.PanelTransaksiBeli.Koneksi.getConnection();
            conn.rollback(); // Gagal salah satu, rollback semua
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    
    private void resetForm() {
        txNamaProduk.setText("");
        txHargaBeli.setText("");
        txHargaJual.setText("");
        txBarcode.setText("");
        jDateKadaluarsa.setDate(null); // untuk JDateChooser
        CbKategori.setSelectedIndex(0); // asumsi index 0 adalah pilihan default seperti "Pilih Kategori"
        txStok.setText(""); // jika kamu punya input stok
    }
    
    public void loadKategori() {
        try {
            Connection con = kasir.main.PanelTransaksiBeli.Koneksi.getConnection(); // pastikan ini method bukan variabel
            String sql = "SELECT NamaKategori FROM kategori";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            CbKategori.removeAllItems(); // kosongkan combobox dulu
            while (rs.next()) {
                String nama = rs.getString("NamaKategori");
                CbKategori.addItem(nama); // hanya nama kategori
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelPembelian = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        LbTanggal = new javax.swing.JLabel();
        txNoTransaksi = new javax.swing.JTextField();
        txNamaKasir = new javax.swing.JTextField();
        txNamaProduk = new javax.swing.JTextField();
        CbKategori = new javax.swing.JComboBox<>();
        btn_simpan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTransaksi = new javax.swing.JTable();
        jDateKadaluarsa = new com.toedter.calendar.JDateChooser();
        txJumlah = new javax.swing.JTextField();
        txStok = new javax.swing.JTextField();
        txHargaJual = new javax.swing.JTextField();
        txHargaBeli = new javax.swing.JTextField();
        BtCari = new javax.swing.JButton();
        txBarcode = new javax.swing.JTextField();
        btHapus = new javax.swing.JButton();
        btBeli = new javax.swing.JButton();

        PanelPembelian.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("Admin > Transaksi Pembelian");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("Transaksi Pembelian Stok");

        LbTanggal.setText("Hari Tanggal");

        txNoTransaksi.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "No Transaksi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txNoTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNoTransaksiActionPerformed(evt);
            }
        });

        txNamaKasir.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Nama Kasir", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txNamaProduk.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nama Produk", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txNamaProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNamaProdukActionPerformed(evt);
            }
        });

        CbKategori.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Kategori", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        CbKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbKategoriActionPerformed(evt);
            }
        });

        btn_simpan.setBackground(new java.awt.Color(255, 204, 0));
        btn_simpan.setText("Simpan");

        tbTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Barang", "Kategori", "Harga Beli", "Harga Jual", "Kadaluarsa", "Stok"
            }
        ));
        jScrollPane1.setViewportView(tbTransaksi);

        jDateKadaluarsa.setBackground(new java.awt.Color(255, 255, 255));
        jDateKadaluarsa.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tanggal Kadaluarsa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txJumlah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txJumlah.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Jumlah", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txStok.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txStok.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Stok", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txHargaJual.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Harga Jual", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txHargaBeli.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Harga Beli", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        BtCari.setBackground(new java.awt.Color(255, 230, 0));
        BtCari.setText("...");

        txBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        btHapus.setBackground(new java.awt.Color(255, 4, 4));
        btHapus.setForeground(new java.awt.Color(255, 255, 255));
        btHapus.setText("Hapus");

        btBeli.setBackground(new java.awt.Color(13, 87, 48));
        btBeli.setForeground(new java.awt.Color(255, 255, 255));
        btBeli.setText("Beli");

        javax.swing.GroupLayout PanelPembelianLayout = new javax.swing.GroupLayout(PanelPembelian);
        PanelPembelian.setLayout(PanelPembelianLayout);
        PanelPembelianLayout.setHorizontalGroup(
            PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addGroup(PanelPembelianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LbTanggal))
                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                        .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateKadaluarsa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelPembelianLayout.createSequentialGroup()
                                .addGap(149, 149, 149)
                                .addComponent(BtCari)
                                .addGap(426, 426, 426)
                                .addComponent(btn_simpan)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(CbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(315, 315, 315))
                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                        .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(PanelPembelianLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(btHapus)
                                .addGap(28, 28, 28)
                                .addComponent(btBeli))
                            .addGroup(PanelPembelianLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txNamaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txStok, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        PanelPembelianLayout.setVerticalGroup(
            PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPembelianLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(LbTanggal))
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNamaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txStok, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(btHapus))
                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                        .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelPembelianLayout.createSequentialGroup()
                                .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                                        .addGap(97, 97, 97)
                                        .addComponent(btn_simpan))
                                    .addGroup(PanelPembelianLayout.createSequentialGroup()
                                        .addGap(51, 51, 51)
                                        .addGroup(PanelPembelianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(CbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(BtCari))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPembelianLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                .addComponent(jDateKadaluarsa, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)))
                        .addComponent(btBeli)))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPembelian, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPembelian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txNamaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNamaProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNamaProdukActionPerformed

    private void txNoTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNoTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNoTransaksiActionPerformed

    private void CbKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbKategoriActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtCari;
    private javax.swing.JComboBox<String> CbKategori;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JPanel PanelPembelian;
    private javax.swing.JButton btBeli;
    private javax.swing.JButton btHapus;
    private javax.swing.JButton btn_simpan;
    private com.toedter.calendar.JDateChooser jDateKadaluarsa;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable tbTransaksi;
    private javax.swing.JTextField txBarcode;
    private javax.swing.JTextField txHargaBeli;
    private javax.swing.JTextField txHargaJual;
    private javax.swing.JTextField txJumlah;
    private javax.swing.JTextField txNamaKasir;
    private javax.swing.JTextField txNamaProduk;
    private javax.swing.JTextField txNoTransaksi;
    private javax.swing.JTextField txStok;
    // End of variables declaration//GEN-END:variables


}
