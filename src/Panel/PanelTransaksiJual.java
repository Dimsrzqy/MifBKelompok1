
package Panel;

import Form.connect;
import com.mysql.cj.Session;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTextArea;

public class PanelTransaksiJual extends javax.swing.JPanel {

    private static java.sql.Connection conn;
    public PanelTransaksiJual() {
        initComponents();
        DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
        model.setRowCount(0); // Hapus semua baris default

        SwingUtilities.invokeLater(()->{
        txBarcode.requestFocusInWindow();
        });
        txBarcode.requestFocusInWindow();
        tampilkanNamaKasir();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        LbTanggal.setText(LocalDate.now().format(dtf));
        txNoTransaksi.setText(generateNoTransaksi());
        txBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            isiDataProdukDariBarcode();
        }
    }
});
        txBayar.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { hitungKembalian(); }
        public void removeUpdate(DocumentEvent e) { hitungKembalian(); }
        public void changedUpdate(DocumentEvent e) { hitungKembalian(); }
    });
    }
    
    
    private void tampilkanNamaKasir() {
    txNamaKasir.setText(PanelLogin.UserSession.getNamaKasir());
    txNamaKasir.setEditable(false);
    txNoTransaksi.setEditable(false);
    txTotalUtama.setEditable(false);
    txNamaProduk.setVisible(false);
    txHargaSatuan.setVisible(false);
    btTambah.setVisible(false);
    txJumlah.setVisible(false);
}
    
public String generateNoTransaksi() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return "TRX" + LocalDateTime.now().format(dtf);
    
}
    
public String formatRupiah(double value) {
    Locale indo = new Locale("id", "ID");
    NumberFormat formatter = NumberFormat.getCurrencyInstance(indo);
    return formatter.format(value); // contoh hasil: Rp21.000
}

private void isiDataProdukDariBarcode() {
    String barcode = txBarcode.getText().trim();
    if (barcode.isEmpty()) return;

    try {
        Connection conn = connect.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM barang WHERE Barcode = ?");
        ps.setString(1, barcode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String namaProduk = rs.getString("NamaBarang");
            double harga = rs.getDouble("HargaJual");
            
            // Cek apakah produk sudah ada di tabel
            DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
            boolean produkSudahAda = false;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).equals(namaProduk)) {
                    // Jika produk sudah ada, update jumlah dan subtotal
                    int jumlahSekarang = Integer.parseInt(model.getValueAt(i, 3).toString());
                    double hargaSekarang = Double.parseDouble(model.getValueAt(i, 2).toString());
                    
                    model.setValueAt(jumlahSekarang + 1, i, 3); // Update jumlah
                    model.setValueAt(hargaSekarang * (jumlahSekarang + 1), i, 4); // Update subtotal
                    
                    produkSudahAda = true;
                    break;
                }
            }
            
            if (!produkSudahAda) {
                // Jika produk belum ada, tambahkan baru
                model.addRow(new Object[]{
                    null,          // Kolom 0: No (diisi nanti)
                    namaProduk,    // Kolom 1: Nama Produk
                    harga,         // Kolom 2: Harga
                    1,             // Kolom 3: Jumlah (default 1)
                    harga         // Kolom 4: Subtotal
                });
            }
            
            updateNomorBaris();
            updateTotalHarga();
            
            // Reset input dan fokus kembali ke barcode
            txBarcode.setText("");
            txBarcode.requestFocusInWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Produk tidak ditemukan.");
            txBarcode.setText("");
            txBarcode.requestFocusInWindow();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Kesalahan: " + e.getMessage());
        e.printStackTrace();
    }
}
private void isiBarangKeTransaksi(String idBarang) {
    try {
        Connection conn = connect.getConnection();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM barang WHERE IDBarang = ?");
        pst.setString(1, idBarang);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String namaProduk = rs.getString("NamaBarang");
            double harga = rs.getDouble("HargaJual");

            DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
            boolean sudahAda = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 1).toString().equalsIgnoreCase(namaProduk)) {
                    int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString()) + 1;
                    model.setValueAt(jumlah, i, 3);
                    model.setValueAt(jumlah * harga, i, 4);
                    sudahAda = true;
                    break;
                }
            }

            if (!sudahAda) {
                model.addRow(new Object[]{
                    null,
                    namaProduk,
                    harga,
                    1,
                    harga
                });
            }

            updateNomorBaris();
            updateTotalHarga();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menambahkan barang dari popup: " + e.getMessage());
        e.printStackTrace();
    }
}

private void updateNomorBaris() {
    DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        model.setValueAt(i + 1, i, 0); // Set nilai kolom 'No' (kolom ke-0)
    }
}
    
private String getIdBarangByNama(String namaBarang) {
    try {
        Connection conn = connect.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT IDBarang FROM barang WHERE NamaBarang = ?");
        ps.setString(1, namaBarang);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("IDBarang");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
private void resetForm() {
    txNoTransaksi.setText(generateNoTransaksi());
    txTotalUtama.setText("0");
    txBayar.setText("");
    txKembalian.setText("");

    DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
    model.setRowCount(0);
    updateNomorBaris();
}
private double parseRupiah(String formatted) {
    return Double.parseDouble(formatted.replace("Rp", "").replace(".", "").replace(",", ".").trim());
}


private void updateTotalHarga() {
    DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
    double total = 0;

    for (int i = 0; i < model.getRowCount(); i++) {
        double subtotal = Double.parseDouble(model.getValueAt(i, 4).toString());
        total += subtotal;
    }

    txTotalUtama.setText(formatRupiah(total));
}
   
private void hitungKembalian() {
   
    String totalText = txTotalUtama.getText();
    String bayarText = txBayar.getText();

    if (totalText.isEmpty() || bayarText.isEmpty()) {
        txKembalian.setText("");
        return;
    }

    try {
        double total = parseRupiah(txTotalUtama.getText());
        double bayar = Double.parseDouble(txBayar.getText());

        if (bayar < total) {
            txKembalian.setText("0");
        } else {
            double kembali = bayar - total;
            txKembalian.setText(formatRupiah(kembali));
        }
    } catch (NumberFormatException e) {
        txKembalian.setText("0");
    }
}
private void cetakStruk(String idTransaksi, String namaKasir, double total, double bayar, double kembalian, DefaultTableModel model) {
    StringBuilder struk = new StringBuilder();
    struk.append("     KOPERASI NURIS\n");
    struk.append("    JL. Pesantren No. 1 Jember\n");
    struk.append("-------------------------------\n");
    struk.append("No. Transaksi : ").append(idTransaksi).append("\n");
    struk.append("Kasir         : ").append(namaKasir).append("\n");
    struk.append("Tanggal       : ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n");
    struk.append("-------------------------------\n");

    // List item
    for (int i = 0; i < model.getRowCount(); i++) {
        String nama = model.getValueAt(i, 1).toString();
        String jumlah = model.getValueAt(i, 3).toString();
        String subtotal = formatRupiah(Double.parseDouble(model.getValueAt(i, 4).toString()));
        struk.append(nama).append(" x").append(jumlah).append(" = ").append(subtotal).append("\n");
    }

    struk.append("-------------------------------\n");
    struk.append("Total     : ").append(formatRupiah(total)).append("\n");
    struk.append("Bayar     : ").append(formatRupiah(bayar)).append("\n");
    struk.append("Kembalian : ").append(formatRupiah(kembalian)).append("\n");
    struk.append("-------------------------------\n");
    struk.append("Terima kasih atas kunjungannya!\n\n\n");

    try {
        JTextArea textArea = new JTextArea(struk.toString());
        textArea.print(); // Mengirim ke printer default
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mencetak struk: " + e.getMessage());
        e.printStackTrace();
    }
}


   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel16 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        LbTanggal = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        txNamaKasir = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txNoTransaksi = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        txTotalUtama = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        txNamaProduk = new javax.swing.JTextField();
        tbCari = new javax.swing.JButton();
        txBarcode = new javax.swing.JTextField();
        txJumlah = new javax.swing.JTextField();
        txHargaSatuan = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTransaksi = new javax.swing.JTable();
        btBayar = new javax.swing.JButton();
        btHapus = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();
        CbMetodePembayaran = new javax.swing.JComboBox<>();
        txBayar = new javax.swing.JTextField();
        txKembalian = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(757, 536));
        setMinimumSize(new java.awt.Dimension(757, 536));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("Admin > Transaksi Jual");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("PENJUALAN");

        LbTanggal.setText("Hari Tanggal");

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Nama Kasir"));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txNamaKasir, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txNamaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("No. Transaksi"));

        txNoTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNoTransaksiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txNoTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));

        txTotalUtama.setFont(new java.awt.Font("Segoe UI Black", 1, 34)); // NOI18N
        txTotalUtama.setText("0");
        txTotalUtama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txTotalUtamaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txTotalUtama)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txTotalUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaksi"));

        txNamaProduk.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nama Produk", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txNamaProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNamaProdukActionPerformed(evt);
            }
        });

        tbCari.setBackground(new java.awt.Color(255, 230, 0));
        tbCari.setText("Cari");
        tbCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbCariActionPerformed(evt);
            }
        });

        txBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBarcodeActionPerformed(evt);
            }
        });

        txJumlah.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Jumlah", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txJumlahActionPerformed(evt);
            }
        });

        txHargaSatuan.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Harga Satuan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txHargaSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txHargaSatuanActionPerformed(evt);
            }
        });

        tbTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Produk", "Harga", "Jumlah", "Sub total"
            }
        ));
        jScrollPane1.setViewportView(tbTransaksi);

        btBayar.setBackground(new java.awt.Color(13, 87, 48));
        btBayar.setForeground(new java.awt.Color(255, 255, 255));
        btBayar.setText("Bayar");
        btBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBayarActionPerformed(evt);
            }
        });

        btHapus.setBackground(new java.awt.Color(255, 4, 4));
        btHapus.setForeground(new java.awt.Color(255, 255, 255));
        btHapus.setText("Hapus");
        btHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHapusActionPerformed(evt);
            }
        });

        btTambah.setText("Tambah");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        CbMetodePembayaran.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tunai\t", "Cashless" }));
        CbMetodePembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbMetodePembayaranActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tbCari)
                        .addGap(57, 57, 57)
                        .addComponent(CbMetodePembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                        .addComponent(txNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbCari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CbMetodePembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(btHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        txBayar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBayarActionPerformed(evt);
            }
        });

        txKembalian.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Bayar");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Kembalian");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LbTanggal))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txBayar, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                    .addComponent(txKembalian))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LbTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txKembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txTotalUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txTotalUtamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txTotalUtamaActionPerformed

    private void btBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBayarActionPerformed
        String idTransaksi = txNoTransaksi.getText().trim();

    // Ambil dan parsing angka dari tampilan
    double totalHarga, bayar, kembalian;
    try {
        totalHarga = parseRupiah(txTotalUtama.getText()); // Gunakan method aman
        bayar = parseRupiah(txBayar.getText());
        kembalian = bayar - totalHarga;

        // Validasi pembayaran
        if (bayar < totalHarga) {
            JOptionPane.showMessageDialog(this, "Jumlah pembayaran tidak mencukupi total harga!", "Pembayaran Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txKembalian.setText(formatRupiah(kembalian)); // tampilkan kembaliannya
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    String metode = CbMetodePembayaran.getSelectedItem().toString();
String namaPembayar = "";

if (metode.equalsIgnoreCase("Cashless")) {
    // Munculkan dialog RFID
    double total = Double.parseDouble(txTotalUtama.getText().replaceAll("[^\\d.]", ""));
    JDialogRFIDPembayaran rfidDialog = new JDialogRFIDPembayaran((Frame) SwingUtilities.getWindowAncestor(this), total);
    rfidDialog.setVisible(true);

    if (!rfidDialog.isPembayaranBerhasil()) {
        JOptionPane.showMessageDialog(this, "Pembayaran RFID dibatalkan.");
        return;
    }

    namaPembayar = rfidDialog.getNamaPemilik(); // bisa simpan ke kolom NamaUser atau NamaPembayar
    bayar = total; // dianggap bayar pas
    kembalian = 0;
} else {
    // tunai biasa
    namaPembayar = PanelLogin.UserSession.getNamaKasir();
    // totalHarga, bayar, kembalian sudah dihitung sebelumnya
}

    try {
        Connection conn = connect.getConnection();

        // Simpan ke transaksi_jual
        String sql = "INSERT INTO transaksi_jual (IDTransaksiJual, NamaUser, TotalHarga, Bayar, Kembalian, TanggalJual) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idTransaksi);
        pst.setString(2, PanelLogin.UserSession.getNamaKasir());
        pst.setDouble(3, totalHarga);
        pst.setDouble(4, bayar);
        pst.setDouble(5, kembalian);
        pst.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
        pst.executeUpdate();

        // Simpan ke transaksi_jual_dtl
        DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
        String sqlDtl = "INSERT INTO transaksi_jual_dtl (IDTransaksiJual, IDBarang, NamaBarang, Kategori, Barcode, jumlah, subtotal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstDtl = conn.prepareStatement(sqlDtl);

        for (int i = 0; i < model.getRowCount(); i++) {
            String namaProduk = model.getValueAt(i, 1).toString();
            int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString());
            double subtotal = Double.parseDouble(model.getValueAt(i, 4).toString());

            String idBarang = getIdBarangByNama(namaProduk);
            if (idBarang == null) continue;

            // Ambil kategori dan barcode dari DB
            String kategori = "";
            String barcode = "";
            String sqlInfo = "SELECT k.Kategori, b.Barcode FROM barang b JOIN kategori k ON b.IDKategori = k.IDKategori WHERE b.IDBarang = ?";
            PreparedStatement pstInfo = conn.prepareStatement(sqlInfo);
            pstInfo.setString(1, idBarang);
            ResultSet rsInfo = pstInfo.executeQuery();
            if (rsInfo.next()) {
                kategori = rsInfo.getString("Kategori");
                barcode = rsInfo.getString("Barcode");
            }

            pstDtl.setString(1, idTransaksi);
            pstDtl.setString(2, idBarang);
            pstDtl.setString(3, namaProduk);
            pstDtl.setString(4, kategori);
            pstDtl.setString(5, barcode);
            pstDtl.setInt(6, jumlah);
            pstDtl.setDouble(7, subtotal);
            pstDtl.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
        resetForm();
        cetakStruk(idTransaksi, PanelLogin.UserSession.getNamaKasir(), totalHarga, bayar, kembalian, model);


    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btBayarActionPerformed

    private void txHargaSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txHargaSatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txHargaSatuanActionPerformed

    private void txJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txJumlahActionPerformed

    private void txBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txBarcodeActionPerformed

    private void tbCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbCariActionPerformed
        PCariBarang popup = new PCariBarang();
        popup.loadBarangFromDatabase();
        popup.setModal(true);
        popup.setLocationRelativeTo(this);
        popup.setVisible(true);

    String idBarangDipilih = popup.getSelectedBarang();
    if (idBarangDipilih != null) {
        isiBarangKeTransaksi(idBarangDipilih);
    }
    }//GEN-LAST:event_tbCariActionPerformed

    private void txNamaProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNamaProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNamaProdukActionPerformed

    private void txBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBayarActionPerformed
       txBayar.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) {
        hitungKembalian();
    }

    public void removeUpdate(DocumentEvent e) {
        hitungKembalian();
    }

    public void changedUpdate(DocumentEvent e) {
        hitungKembalian();
    }
});

    }//GEN-LAST:event_txBayarActionPerformed

    private void txNoTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNoTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNoTransaksiActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
       
        try {
            String NamaBarang = txNamaProduk.getText().trim(); // Mengambil tipe produk dari field txtipe
            String qty = txJumlah.getText().trim(); // Mengambil jumlah produk dari field txjumlah
            String harga = txHargaSatuan.getText().trim(); // Mengambil ukuran produk dari field txukuran
            
            DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
            boolean produkDitemukan = false;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Format input salah");
        }
        updateNomorBaris();
       isiDataProdukDariBarcode();
       updateTotalHarga();
    
        
    }//GEN-LAST:event_btTambahActionPerformed

    private void btHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHapusActionPerformed
        int selectedRow = tbTransaksi.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih salah satu barang yang ingin dihapus dari transaksi.", "Tidak ada yang dipilih", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Apakah kamu yakin ingin menghapus barang ini dari transaksi?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        DefaultTableModel model = (DefaultTableModel) tbTransaksi.getModel();
        model.removeRow(selectedRow);
        updateNomorBaris();     // perbarui nomor urut
        updateTotalHarga();     // hitung ulang total harga setelah hapus
    }
    }//GEN-LAST:event_btHapusActionPerformed

    private void CbMetodePembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbMetodePembayaranActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbMetodePembayaranActionPerformed
    
 

   

    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbMetodePembayaran;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JButton btBayar;
    private javax.swing.JButton btHapus;
    private javax.swing.JButton btTambah;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JButton tbCari;
    private javax.swing.JTable tbTransaksi;
    private javax.swing.JTextField txBarcode;
    private javax.swing.JTextField txBayar;
    private javax.swing.JTextField txHargaSatuan;
    private javax.swing.JTextField txJumlah;
    private javax.swing.JTextField txKembalian;
    private javax.swing.JTextField txNamaKasir;
    private javax.swing.JTextField txNamaProduk;
    private javax.swing.JTextField txNoTransaksi;
    private javax.swing.JTextField txTotalUtama;
    // End of variables declaration//GEN-END:variables
}
