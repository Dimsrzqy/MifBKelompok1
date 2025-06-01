
package Panel;

import Form.connect;
import com.fazecast.jSerialComm.SerialPort;
import com.mysql.cj.xdevapi.Statement;
import java.awt.Dialog;
import java.awt.Window;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.DriverManager;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import Panel.PanelTambahSantri;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author dimsrzqy
 */
public class PanelDataSantri extends javax.swing.JPanel {

    private Container PanelTambahSantri;
    private javax.swing.Timer searchTimer;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/koperasi_nuris";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final int MIN_RFID_LENGTH = 5;
    
public PanelDataSantri() {
    initComponents();
    loadTable();
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    LbTanggal.setText(LocalDate.now().format(dtf));

    setupRFIDListener(); // <--- WAJIB DIPANGGIL DI SINI !!!

    JTableHeader header = jTable.getTableHeader();
    header.setBackground(new Color(28, 69, 50));
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));

    jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
            } else {
                c.setBackground(new Color(41, 157, 145));
            }

            return c;
        }
    });
}



private void setupRFIDListener() {
    searchTimer = new javax.swing.Timer(150, e -> {
        String rfid = txCariRFid.getText().trim();
        if (rfid.length() >= MIN_RFID_LENGTH) {
            processRFIDScan(rfid);
        }
    });
    searchTimer.setRepeats(false);

    txCariRFid.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
        @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
        @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
    });
}


// Di panel utama atau class yang scan RFID
private void processRFIDScan(String rfid) {
    String query = "SELECT NamaPengguna, NoRFID, SaldoMasuk, SaldoKeluar, Saldo, JenisTransaksi, Tanggal " +
                   "FROM mutasi WHERE NoRFID = ? ORDER BY Tanggal DESC LIMIT 1";

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, rfid);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String namaPengguna = rs.getString("NamaPengguna");
                String noRFID = rs.getString("NoRFID");
                double saldoMasuk = rs.getDouble("SaldoMasuk");
                double saldoKeluar = rs.getDouble("SaldoKeluar");
                double saldo = rs.getDouble("Saldo");
                String jenisTransaksi = rs.getString("JenisTransaksi");
                java.sql.Timestamp tanggal = rs.getTimestamp("Tanggal");

                // Misal nominal 0 atau bisa kamu atur sendiri
                double nominal = 0;

                showDetailPopup(noRFID, namaPengguna, saldo, nominal);
                txCariRFid.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "RFID tidak terdaftar pada mutasi",
                    "Data Tidak Ditemukan",
                    JOptionPane.WARNING_MESSAGE);
                txCariRFid.setText("");
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "Error database: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

private void showDetailPopup(String noRFID, String nama, double saldo, double nominal) {
    PanelMutasi mutasiPanel = new PanelMutasi(noRFID);

    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "PanelMutasi", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.getContentPane().add(mutasiPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(null);  // posisi di tengah layar
    dialog.setVisible(true);

    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent e) {
            loadTable();  // jika perlu refresh
        }
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            loadTable();
        }
    });
}


 public void loadTable() {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0); // Reset isi tabel

        String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
        String user = "root";
        String pass = "";

        String query = "SELECT NamaPengguna, NoRFID, JenisKelamin, Asrama, Lembaga, TanggalUpdate FROM pengguna";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("NamaPengguna"),
                    rs.getString("NoRFID"),
                    rs.getString("JenisKelamin"),
                    rs.getString("Asrama"),
                    rs.getString("Lembaga"),
                    rs.getDate("TanggalUpdate")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 public void refreshTableData() {
        loadTable();
 }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        BtHapus = new javax.swing.JButton();
        btTambah = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        LbTanggal = new javax.swing.JLabel();
        txCariRFid = new javax.swing.JTextField();

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "RFID", "Jenis Kelamin", "Asrama", "Lembaga", "Tanggal Update"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        BtHapus.setBackground(new java.awt.Color(255, 0, 0));
        BtHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtHapus.setForeground(new java.awt.Color(255, 255, 255));
        BtHapus.setText("HAPUS");
        BtHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtHapusActionPerformed(evt);
            }
        });

        btTambah.setBackground(new java.awt.Color(28, 69, 50));
        btTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btTambah.setForeground(new java.awt.Color(255, 255, 255));
        btTambah.setText("TAMBAH");
        btTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTambahActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N

        jLabel17.setText("Admin > Manajemen");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("Data Santri");

        LbTanggal.setText("Hari Tanggal");

        txCariRFid.setBorder(javax.swing.BorderFactory.createTitledBorder("Cari RFID"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 590, Short.MAX_VALUE)
                                .addComponent(LbTanggal)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator8)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txCariRFid, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel18)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LbTanggal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txCariRFid, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(267, 267, 267))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 287, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 1, Short.MAX_VALUE)
                    .addComponent(jLabel18)
                    .addGap(0, 289, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BtHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtHapusActionPerformed
int selectedRow = jTable.getSelectedRow();

if (selectedRow == -1) {
    JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
    return;
}

// Asumsikan kolom ke-2 adalah NoRFID (indeks 2 berarti kolom ke-3)
String noRFID = jTable.getValueAt(selectedRow, 2).toString();

int confirm = JOptionPane.showConfirmDialog(
    this,
    "Apakah Anda yakin ingin menghapus data dengan RFID: " + noRFID + "?",
    "Konfirmasi Hapus",
    JOptionPane.YES_NO_OPTION
);

if (confirm == JOptionPane.YES_OPTION) {
    String sql = "DELETE FROM pengguna WHERE NoRFID = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, noRFID);
        int rowsDeleted = stmt.executeUpdate();

        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau sudah dihapus sebelumnya.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Kesalahan saat menghapus: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    }//GEN-LAST:event_BtHapusActionPerformed

    private void btTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTambahActionPerformed
try {
    PanelTambahSantri panelTambah = new PanelTambahSantri(this);
    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah Santri", true);
    dialog.setContentPane(panelTambah);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal membuka form tambah: " + e.getMessage());
    e.printStackTrace();
}

    }//GEN-LAST:event_btTambahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtHapus;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JButton btTambah;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField txCariRFid;
    // End of variables declaration//GEN-END:variables
}
