package Panel;

import Form.connect;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.awt.Frame;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;


public class PanelDataPenjualan extends javax.swing.JPanel {

    public PanelDataPenjualan() {
        initComponents();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        LbTanggal.setText(LocalDate.now().format(dtf));
        tampilkanTransaksiUtama();
        
        JTableHeader header = TbDataPenjualan.getTableHeader();
        header.setBackground(new Color(0, 102, 204)); // biru tua
        header.setForeground(Color.WHITE);           // teks putih
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        TbDataPenjualan.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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


    }
    private void tampilkanDetailTransaksi(String idTransaksi) {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("ID Barang");
    model.addColumn("Nama Barang");
    model.addColumn("Kategori");
    model.addColumn("Jumlah");
    model.addColumn("Subtotal");

    try {
        Connection conn = connect.getConnection();

        String sql = "SELECT IDBarang, NamaBarang, Kategori, jumlah, subtotal " +
                     "FROM transaksi_jual_dtl WHERE IDTransaksiJual = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idTransaksi);
        ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getString("IDBarang"),
                rs.getString("NamaBarang"),
                rs.getString("Kategori"),
                rs.getInt("jumlah"),
                rs.getDouble("subtotal")
            });
        }

        TbDataPenjualan.setModel(model); // asumsi kamu sudah punya JTable tabelDetail

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan detail: " + e.getMessage());
        e.printStackTrace();
    }
}

private void tampilkanTransaksiUtama() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No");
    model.addColumn("ID Transaksi");
    model.addColumn("Total Harga");
    model.addColumn("Bayar");
    model.addColumn("Kembalian");
    model.addColumn("Metode Pembayaran");
    model.addColumn("Tanggal");
    model.addColumn("Kasir");

    try {
        Connection conn = connect.getConnection();
        String sql = "SELECT IDTransaksiJual, TotalHarga, Bayar, Kembalian, MetodePembayaran, TanggalJual, NamaUser FROM transaksi_jual";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        

        int no = 1;
        while (rs.next()) {
            Timestamp ts = rs.getTimestamp("TanggalJual");
            String tanggalFormatted = new SimpleDateFormat("dd-MM-yyyy").format(ts);
            model.addRow(new Object[]{
                no++,
                rs.getString("IDTransaksiJual"),
                rs.getDouble("TotalHarga"),
                rs.getDouble("Bayar"),
                rs.getDouble("Kembalian"),
                rs.getString("MetodePembayaran"),
                tanggalFormatted,
                rs.getString("NamaUser")
            });
        }

        TbDataPenjualan.setModel(model);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data penjualan: " + e.getMessage());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        TbDataPenjualan = new javax.swing.JTable();
        TxCari = new javax.swing.JTextField();
        BtDetail = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1062, 700));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setText("Admin > Data Penjualan");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("Data Penjualan");

        LbTanggal.setText("Hari Tanggal");

        TbDataPenjualan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID Transaksi", "Total Harga", "Bayar", "Kembalian", "Metode Bayar", "Tanggal", "Kasir"
            }
        ));
        jScrollPane1.setViewportView(TbDataPenjualan);

        BtDetail.setBackground(new java.awt.Color(255, 255, 0));
        BtDetail.setText("Detail");
        BtDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtDetailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LbTanggal))))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(BtDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxCari, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxCari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void BtDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtDetailActionPerformed
       int selectedRow = TbDataPenjualan.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!");
        return;
    }

    String idTransaksi = TbDataPenjualan.getValueAt(selectedRow, 1).toString(); // kolom 1 = IDTransaksi
    new JDialogDetailTransaksi((Frame) SwingUtilities.getWindowAncestor(this), idTransaksi).setVisible(true);
    }//GEN-LAST:event_BtDetailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtDetail;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JTable TbDataPenjualan;
    private javax.swing.JTextField TxCari;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
