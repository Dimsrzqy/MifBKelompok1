package Panel;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JPasswordField;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author alsitia
 */
public class PopupTambahBarang extends javax.swing.JPanel {

    /**
     * Creates new form PopipEditManAkun
     */
    public PopupTambahBarang() {
        initComponents();
        loadKategori();
    }

    // Method untuk koneksi ke database
    public class Koneksi {

        public static Connection getConnection() throws SQLException {
            String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
            String user = "root";
            String pass = "";
            return DriverManager.getConnection(url, user, pass);
        }
    }

// Method untuk mengisi ComboBox dengan NamaKategori
    public void loadKategori() {
        try {
            Connection con = Koneksi.getConnection();
            String sql = "SELECT NamaKategori FROM kategori";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            dd_kategori.removeAllItems();
            while (rs.next()) {
                String nama = rs.getString("NamaKategori");
                dd_kategori.addItem(nama);
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Method untuk generate IDBarang otomatis (contoh: BR + timestamp)
    private String generateIDBarang() {
        String prefix = "BRG";
        String idBarang = prefix + "0001"; // default jika belum ada data

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", ""); PreparedStatement ps = conn.prepareStatement(
                "SELECT IDBarang FROM barang WHERE IDBarang LIKE ? ORDER BY IDBarang DESC LIMIT 1")) {

            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString("IDBarang"); // contoh: BRG0025
                int lastNumber = Integer.parseInt(lastID.substring(3)); // ambil 0025
                int nextNumber = lastNumber + 1;
                idBarang = prefix + String.format("%04d", nextNumber); // jadi BRG0026
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idBarang;
    }

// Method untuk menambah data barang
    private void tambah_barang() {
        String namaKategori = (String) dd_kategori.getSelectedItem();
        if (namaKategori == null || namaKategori.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kategori belum dipilih.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil IDKategori berdasarkan NamaKategori
        String idkategori = null;
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT IDKategori FROM kategori WHERE NamaKategori = ?")) {

            ps.setString(1, namaKategori);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idkategori = rs.getString("IDKategori");
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil ID kategori.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (idkategori == null) {
            JOptionPane.showMessageDialog(this, "Kategori tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idBarang = generateIDBarang();
        String namaBarang = txt_nama_barang.getText().trim();
        String hargaBeli = txt_harga_beli.getText().trim();
        String hargaJual = txt_harga_jual.getText().trim();
        String barcode = txt_barcode.getText().trim();

        if (namaBarang.isEmpty() || hargaBeli.isEmpty() || hargaJual.isEmpty() || barcode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int harga_beli = Integer.parseInt(hargaBeli);
            int harga_jual = Integer.parseInt(hargaJual);
            java.sql.Date tanggalMasuk = new java.sql.Date(System.currentTimeMillis());

            try (Connection conn = Koneksi.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO barang (IDBarang, IDKategori, NamaBarang, HargaBeli, HargaJual, Barcode, TanggalMasuk) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                stmt.setString(1, idBarang);
                stmt.setString(2, idkategori);
                stmt.setString(3, namaBarang);
                stmt.setInt(4, harga_beli);
                stmt.setInt(5, harga_jual);
                stmt.setString(6, barcode);
                stmt.setDate(7, tanggalMasuk);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data barang berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                SwingUtilities.getWindowAncestor(this).dispose();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga beli dan harga jual harus berupa angka.", "Format Salah", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String pesan = "Gagal menambahkan data.\nError SQL: " + e.getMessage();
            JOptionPane.showMessageDialog(this, pesan, "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

//
//    public void loadKategori() {
//        try {
//            Connection con = Koneksi.getConnection(); // pastikan ini method yg mengembalikan Connection
//            String sql = "SELECT NamaKategori FROM kategori";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//
//            dd_kategori.removeAllItems(); // kosongkan dulu combobox
//
//            while (rs.next()) {
//                String nama = rs.getString("NamaKategori");
//                dd_kategori.addItem(nama);  // masukkan nama kategori saja
//
//                dd_kategori.addItem(nama);  // masukkan nama kategori saja
//            }
//
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txt_nama_barang = new javax.swing.JTextField();
        txt_harga_beli = new javax.swing.JTextField();
        txt_harga_jual = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dd_kategori = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        txt_barcode = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(700, 380));

        txt_nama_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nama_barangActionPerformed(evt);
            }
        });

        jLabel2.setText("Kategori ");

        jLabel3.setText("Nama Barang ");

        jLabel5.setText("Harga Beli");

        jLabel6.setText("Harga Jual");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("TAMBAH BARANG");

        dd_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Barcode");

        btn_simpan.setBackground(new java.awt.Color(255, 204, 0));
        btn_simpan.setText("Simpan");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_simpan))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addGap(85, 85, 85)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_harga_jual)
                            .addComponent(dd_kategori, 0, 333, Short.MAX_VALUE)
                            .addComponent(txt_harga_beli)
                            .addComponent(txt_barcode)
                            .addComponent(txt_nama_barang))))
                .addGap(67, 67, 67))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_nama_barang)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_harga_jual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txt_harga_beli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(dd_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_simpan)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nama_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nama_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama_barangActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        tambah_barang();
    }//GEN-LAST:event_btn_simpanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_simpan;
    private javax.swing.JComboBox<String> dd_kategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt_barcode;
    private javax.swing.JTextField txt_harga_beli;
    private javax.swing.JTextField txt_harga_jual;
    private javax.swing.JTextField txt_nama_barang;
    // End of variables declaration//GEN-END:variables

}
