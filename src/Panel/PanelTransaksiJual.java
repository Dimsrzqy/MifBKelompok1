
package Panel;

import Form.connect;
import com.mysql.cj.Session;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
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
import java.awt.print.PrinterJob;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.print.*;

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
        
        JTableHeader header = tbTransaksi.getTableHeader();
        header.setBackground(new Color(0, 102, 204)); // biru tua
        header.setForeground(Color.WHITE);           // teks putih
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        tbTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!isSelected) {
            if (row % 2 == 0) {
                c.setBackground(Color.WHITE); // baris genap
            } else {
                c.setBackground(new Color(240, 240, 240)); // baris ganjil abu muda
            }
        } else {
            c.setBackground(new Color(204, 229, 255)); // warna saat dipilih
        }

        return c;
    }
});
        
        tbTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = tbTransaksi.getSelectedRow();
        if (selectedRow >= 0) {
            // Ambil jumlah dari kolom 3 (index 3 = jumlah)
            String jumlah = tbTransaksi.getValueAt(selectedRow, 3).toString();
            txJumlah.setText(jumlah);
        }
    }
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
    txJumlah.setVisible(true);
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
    formatted = formatted.trim();
    formatted = formatted.replace("Rp", "")
                         .replace(".", "")  // hapus titik ribuan
                         .replace(",", ".") // ubah koma jadi titik desimal
                         .trim();
    try {
        return Double.parseDouble(formatted);
    } catch (NumberFormatException e) {
        return 0;
    }
}
    private void hitungTotalBayar() {
    double total = 0;
    for (int i = 0; i < tbTransaksi.getRowCount(); i++) {
        total += Double.parseDouble(tbTransaksi.getValueAt(i, 4).toString());
    }
    txTotalUtama.setText(formatRupiah(total));
}




    private void updateJumlahProduk() {
    int selectedRow = tbTransaksi.getSelectedRow();
    if (selectedRow >= 0) {
        try {
            int newJumlah = Integer.parseInt(txJumlah.getText());
            if (newJumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0");
                return;
            }

            // Update jumlah di tabel
            tbTransaksi.setValueAt(newJumlah, selectedRow, 3);

            
            double harga = Double.parseDouble(tbTransaksi.getValueAt(selectedRow, 2).toString());
            double newSubtotal = newJumlah * harga;
            tbTransaksi.setValueAt(newSubtotal, selectedRow, 4);

            // Update total bayar (jika ada metode hitung total)
            hitungTotalBayar();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Pilih produk di tabel terlebih dahulu");
    }
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
private void cetakStruk(String isiStruk) {
    PrinterJob printerJob = PrinterJob.getPrinterJob();
    printerJob.setJobName("Cetak Struk");

    printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
        if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Font font = new Font("Monospaced", Font.PLAIN, 10);
        g2d.setFont(font);

        int y = 15;
        for (String line : isiStruk.split("\n")) {
            g2d.drawString(line, 10, y);
            y += 15;
        }

        return Printable.PAGE_EXISTS;
    });

    boolean doPrint = printerJob.printDialog(); // tampilkan dialog pilih printer
    if (doPrint) {
        try {
            printerJob.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak: " + ex.getMessage());
        }
    }
}
private String padRight(String text, int width) {
    return String.format("%-" + width + "s", text);
}

private String padLeft(String text, int width) {
    return String.format("%" + width + "s", text);
}

private String line(int length) {
    return "-".repeat(length);
}

private String centerText(String text, int width) {
    int pad = (width - text.length()) / 2;
    return " ".repeat(Math.max(0, pad)) + text;
}


private void cetakStrukLangsung(String idTransaksi, String namaKasir, double total, double bayar, double kembalian, DefaultTableModel model) {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName("Struk Transaksi");

    PageFormat pf = job.defaultPage();
    Paper paper = new Paper();
    double width = 2.83 * 72;  // 80mm lebar (2.83 inch * 72 dpi)
    double height = 11 * 72;   // Tinggi 11 inch
    double margin = 10;

    paper.setSize(width, height);
    paper.setImageableArea(margin, margin, width - 2 * margin, height - 2 * margin);
    pf.setPaper(paper);

    job.setPrintable(new Printable() {
        @Override
        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            if (pageIndex > 0) return NO_SUCH_PAGE;

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));

            int y = 10;
            int lineHeight = g2d.getFontMetrics().getHeight();

            // HEADER
            g2d.drawString(centerText("KOPERASI NURIS", 32), 0, y); y += lineHeight;
            g2d.drawString(centerText("Jl. Pangandaran No. 48", 32), 0, y); y += lineHeight;
            g2d.drawString(centerText("Kec Sumbersari Kab Jember", 32), 0, y); y += lineHeight;
            g2d.drawString(centerText("No Telp. (0331)339544", 32), 0, y); y += lineHeight;
            g2d.drawString("--------------------------------", 0, y); y += lineHeight;
            

            g2d.drawString("No. Transaksi : " + idTransaksi, 0, y); y += lineHeight;
            g2d.drawString("Kasir         : " + namaKasir, 0, y); y += lineHeight;
            g2d.drawString("Tanggal       : " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), 0, y); y += lineHeight;
            g2d.drawString("--------------------------------", 0, y); y += lineHeight;

            // LIST PRODUK
            int totalItem = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String namaProduk = model.getValueAt(i, 1).toString();
                String jumlah = model.getValueAt(i, 3).toString();
                String subtotal = formatRupiah(Double.parseDouble(model.getValueAt(i, 4).toString()));
                totalItem += Integer.parseInt(jumlah);

                g2d.drawString(namaProduk, 0, y); y += lineHeight;
                g2d.drawString(" x" + jumlah + padLeft(subtotal, 30 - jumlah.length()), 0, y); y += lineHeight;
            }

            g2d.drawString("--------------------------------", 0, y); y += lineHeight;

            // RINGKASAN
            g2d.drawString("Total Item    : " + totalItem, 0, y); y += lineHeight;
            g2d.drawString("Total Belanja : " + formatRupiah(total), 0, y); y += lineHeight;
            g2d.drawString("--------------------------------", 0, y); y += lineHeight;
            g2d.drawString("Bayar         : " + formatRupiah(bayar), 0, y); y += lineHeight;
            g2d.drawString("Kembalian     : " + formatRupiah(kembalian), 0, y); y += lineHeight;
            g2d.drawString("--------------------------------", 0, y); y += lineHeight;

            g2d.drawString(centerText("Terima Kasih ", 32), 0, y); y += lineHeight;
            g2d.drawString(centerText("Atas Kunjungannya", 32), 0, y); y += lineHeight;
            g2d.drawString(centerText("Email: yayasannurisjember@gmail.com", 32), 0, y); y += lineHeight;

            
            
            
            return PAGE_EXISTS;
        }
    }, pf);

    if (job.printDialog()) {
        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mencetak: " + e.getMessage());
        }
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
        BtCari = new javax.swing.JButton();
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

        BtCari.setBackground(new java.awt.Color(255, 230, 0));
        BtCari.setText("Cari");
        BtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtCariActionPerformed(evt);
            }
        });

        txBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBarcodeActionPerformed(evt);
            }
        });

        txJumlah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
                        .addComponent(BtCari)
                        .addGap(57, 57, 57)
                        .addComponent(CbMetodePembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                        .addComponent(txHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CbMetodePembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

String metode = CbMetodePembayaran.getSelectedItem().toString();
String namaPembayar = "";
double totalHarga, bayar = 0, kembalian = 0;

try {
    totalHarga = parseRupiah(txTotalUtama.getText());

} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Total tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

if (metode.equalsIgnoreCase("Cashless")) {
    // Metode RFID Cashless
    JDialogRFIDPembayaran rfidDialog = new JDialogRFIDPembayaran((Frame) SwingUtilities.getWindowAncestor(this), totalHarga);
    rfidDialog.setVisible(true);

    if (!rfidDialog.isPembayaranBerhasil()) {
        JOptionPane.showMessageDialog(this, "Pembayaran RFID dibatalkan.");
        return;
    }

    namaPembayar = rfidDialog.getNamaPemilik();
    bayar = totalHarga;
    kembalian = 0;

    txBayar.setText(String.valueOf((int) bayar));      // opsional: tampilkan untuk info
    txKembalian.setText("0");

} else {
    // Metode Tunai
    String bayarText = txBayar.getText();
    String kembaliText = txKembalian.getText();


    if (bayarText.isEmpty() || kembaliText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Harap lengkapi semua kolom pembayaran!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
            bayar = parseRupiah(bayarText);
            kembalian = parseRupiah(kembaliText);

        if (bayar < totalHarga) {
            JOptionPane.showMessageDialog(this, "Jumlah pembayaran tidak mencukupi total harga!", "Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }
        namaPembayar = PanelLogin.UserSession.getNamaKasir();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk bayar/kembalian!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
}


    try {
        Connection conn = connect.getConnection();

        // Simpan ke transaksi_jual
        String sql = "INSERT INTO transaksi_jual (IDTransaksiJual, NamaUser, TotalHarga, Bayar, Kembalian, MetodePembayaran, TanggalJual) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idTransaksi);
        pst.setString(2, PanelLogin.UserSession.getNamaKasir());
        pst.setDouble(3, totalHarga);
        pst.setDouble(4, bayar);
        pst.setDouble(5, kembalian);
        pst.setString(6, metode);
        pst.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
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
            String sqlInfo = "SELECT k.NamaKategori, b.Barcode FROM barang b JOIN kategori k ON b.IDKategori = k.IDKategori WHERE b.IDBarang = ?";
            PreparedStatement pstInfo = conn.prepareStatement(sqlInfo);
            pstInfo.setString(1, idBarang);
            ResultSet rsInfo = pstInfo.executeQuery();
            if (rsInfo.next()) {
                kategori = rsInfo.getString("NamaKategori");
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
        cetakStrukLangsung(idTransaksi, PanelLogin.UserSession.getNamaKasir(), totalHarga, bayar, kembalian, model);
        resetForm();
        

        


    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
        e.printStackTrace();
    }
    
    }//GEN-LAST:event_btBayarActionPerformed

    private void txHargaSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txHargaSatuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txHargaSatuanActionPerformed

    private void txJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txJumlahActionPerformed
        updateJumlahProduk();
    }//GEN-LAST:event_txJumlahActionPerformed

    private void txBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txBarcodeActionPerformed

    private void BtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtCariActionPerformed
        JDialogCariBRG popup = new JDialogCariBRG();
        popup.setModal(true);
        popup.setLocationRelativeTo(this);
        popup.setVisible(true); // tunggu dialog ditutup baru lanjut

    // Setelah dialog ditutup:
    String idBarang = popup.getSelectedBarang();
    if (idBarang != null) {
        isiBarangKeTransaksi(idBarang);
    }
    }//GEN-LAST:event_BtCariActionPerformed

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
       if (CbMetodePembayaran.getSelectedItem().toString().equalsIgnoreCase("Cashless")) {
    txBayar.setEditable(false);
    txKembalian.setEditable(false);
    txBayar.setText("0");
    txKembalian.setText("0");
} else {
    txBayar.setEditable(true);
    txKembalian.setEditable(true);
    txBayar.setText("");
    txKembalian.setText("");
}

    }//GEN-LAST:event_CbMetodePembayaranActionPerformed
    
 

   

    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtCari;
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
