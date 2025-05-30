
package Panel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author ASUS Vivobook
 */
public class PanelTransaksiBeli extends javax.swing.JPanel {

    /**
     * Creates new form PanelTambahBarang
     */
    public PanelTransaksiBeli() {
        initComponents();

        loadKategori();
        // Auto-generate barcode saat form dibuka
//        try {
//            String newBarcode = generateBarcode();
//            txt_barcode.setText(newBarcode);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            txt_barcode.setText(""); // Default jika error
//            JOptionPane.showMessageDialog(this, "Gagal generate barcode: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        } catch (Exception e) {
//            e.printStackTrace();
//            txt_barcode.setText(""); // Default jika error lain
//        }
    }

//    private String generateBarcode() throws SQLException {
//        String baseBarcode = null;
//        String lastBarcode = null;
//
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            con = Koneksi.getConnection();
//            String sql = "SELECT MAX(Barcode) as lastBarcode FROM barang WHERE Barcode LIKE ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, baseBarcode + "%");
//            rs = ps.executeQuery();
//
//            if (rs.next()) {
//                lastBarcode = rs.getString("lastBarcode");
//            }
//
//            if (lastBarcode != null) {
//                int lastNum = Integer.parseInt(lastBarcode.substring(9));
//                return baseBarcode + String.format("%03d", lastNum + 1);
//            }
//        } finally {
//            // Pastikan resources ditutup
//            if (rs != null) try {
//                rs.close();
//            } catch (SQLException e) {
//                /* ignore */ }
//            if (ps != null) try {
//                ps.close();
//            } catch (SQLException e) {
//                /* ignore */ }
//            if (con != null) try {
//                con.close();
//            } catch (SQLException e) {
//                /* ignore */ }
//        }
//
//        return baseBarcode + "001"; // Jika belum ada data
//    }

    private String generateIDStok() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void showPanel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static class Dashboard {

        public Dashboard() {
        }
    }

    public class Koneksi {

        public static Connection getConnection() throws SQLException {
            String url = "jdbc:mysql://localhost:3306/koperasi_nuris"; // ganti dengan database kamu
            String user = "root";
            String pass = "";
            return java.sql.DriverManager.getConnection(url, user, pass);
        }
    }

    private String generateIDBarang() {
        String prefix = "BRG";
        String newID = "";
        try {
            String sql = "SELECT MAX(IDBarang) AS lastID FROM barang";
            Connection con = (Connection) Koneksi.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getString("lastID") != null) {
                String lastID = rs.getString("lastID");
                int num = Integer.parseInt(lastID.substring(3)); // ambil angka setelah BRG
                num++;
                newID = prefix + String.format("%03d", num); // format ke 3 digit, contoh BRG005
            } else {
                newID = prefix + "001"; // kalau belum ada data
            }
            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generate ID: " + e.getMessage());
            newID = prefix + "001"; // fallback kalau error
        }
        return newID;
    }

    public void loadKategori() {
        try {
            Connection con = Koneksi.getConnection(); // pastikan ini method bukan variabel
            String sql = "SELECT NamaKategori FROM kategori";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbx_kategori.removeAllItems(); // kosongkan combobox dulu
            while (rs.next()) {
                String nama = rs.getString("NamaKategori");
                cbx_kategori.addItem(nama); // hanya nama kategori
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetForm() {
        txt_nama_barang.setText("");
        txt_harga_beli.setText("");
        txt_harga_jual.setText("");
        txt_barcode.setText("");
        tgl_kadaluarsa.setDate(null); // untuk JDateChooser
        cbx_kategori.setSelectedIndex(0); // asumsi index 0 adalah pilihan default seperti "Pilih Kategori"
        txt_stok.setText(""); // jika kamu punya input stok
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbx_kategori = new javax.swing.JComboBox<>();
        txt_stok = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        tgl_kadaluarsa = new com.toedter.calendar.JDateChooser();
        btn_simpan = new javax.swing.JButton();
        txt_barcode = new javax.swing.JTextField();

        txt_nama_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nama_barangActionPerformed(evt);
            }
        });

        jLabel2.setText("Kategori ");

        jLabel3.setText("Nama Barang ");

        jLabel5.setText("Harga Beli");

        jLabel6.setText("Harga Jual");

        jLabel7.setText("stok");

        jLabel8.setText("Kadaluarsa");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("TRANSAKSI BELI");

        cbx_kategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Barcode");

        jLabel17.setText("Admin > Transaksi Beli");

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_simpan))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel17)
                            .addComponent(jLabel1))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_harga_jual)
                            .addComponent(txt_stok, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbx_kategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_harga_beli)
                            .addComponent(tgl_kadaluarsa, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(txt_nama_barang)
                            .addComponent(txt_barcode, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(138, 138, 138))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_harga_jual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(tgl_kadaluarsa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(txt_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(txt_harga_beli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cbx_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_simpan)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        insertData();

    }//GEN-LAST:event_btn_simpanActionPerformed

    private void txt_nama_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nama_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama_barangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_simpan;
    private javax.swing.JComboBox<String> cbx_kategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private com.toedter.calendar.JDateChooser tgl_kadaluarsa;
    private javax.swing.JTextField txt_barcode;
    private javax.swing.JTextField txt_harga_beli;
    private javax.swing.JTextField txt_harga_jual;
    private javax.swing.JTextField txt_nama_barang;
    private javax.swing.JTextField txt_stok;
    // End of variables declaration//GEN-END:variables

    private void insertData() {
        String idProduk = "BR" + System.currentTimeMillis() % 1000000; // contoh: BR123456
        String namaBarang = txt_nama_barang.getText().trim();
        String kategoriNama = cbx_kategori.getSelectedItem().toString();
        String hargaBeliStr = txt_harga_beli.getText().trim();
        String hargaJualStr = txt_harga_jual.getText().trim();
        String barcode = txt_barcode.getText().trim();
        String stokStr = txt_stok.getText().trim();

        java.util.Date kadaluarsaUtil = tgl_kadaluarsa.getDate();
        java.sql.Date tanggalKadaluarsa = kadaluarsaUtil != null ? new java.sql.Date(kadaluarsaUtil.getTime()) : null;

        if (namaBarang.isEmpty() || kategoriNama.equals("Pilih Kategori") || hargaBeliStr.isEmpty()
                || hargaJualStr.isEmpty() || stokStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua Kolom Wajib Diisi (kecuali Barcode dan Tanggal Kadaluarsa)!", "Validasi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Koneksi.getConnection()) {
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

            // Insert ke tabel barang
            String sqlBarang = "INSERT INTO barang (IDBarang, IDKategori, NamaBarang, HargaBeli, HargaJual, Barcode, TanggalMasuk) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = conn.prepareStatement(sqlBarang)) {
                st.setString(1, idProduk);
                st.setString(2, idKategori);
                st.setString(3, namaBarang);
                st.setDouble(4, hargaBeli);
                st.setDouble(5, hargaJual);
                st.setString(6, barcode);
                st.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                st.executeUpdate();
            }

            // Insert ke tabel stok
            String idStok = "STK" + System.currentTimeMillis() % 1000000; // pastikan <= 15 char
            String sqlStok = "INSERT INTO stok (IDStok, IDBarang, JumlahMasuk, JumlahKeluar, TanggalUpdate, TanggalKadaluarsa) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = conn.prepareStatement(sqlStok)) {
                st.setString(1, idStok);
                st.setString(2, idProduk);
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
            loadData();
            showPanel();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Pastikan harga dan stok berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            try {
                Connection conn = Koneksi.getConnection();
                conn.rollback(); // Gagal salah satu, rollback semua
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
