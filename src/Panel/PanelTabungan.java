/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Panel.PanelDetailTabungan;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JDialog;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author dimsrzqy
 */
public class PanelTabungan extends javax.swing.JPanel {

    private javax.swing.Timer searchTimer;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/koperasi_nuris";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final int MIN_RFID_LENGTH = 5;

    public PanelTabungan() {
        initComponents(); // komponen seperti txCari dan jTable dibuat otomatis oleh NetBeans
        setupRFIDListener();
        loadTable();
        loadTotalSaldoKeTextField();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        LbTanggal.setText(LocalDate.now().format(dtf));
        
        JTableHeader header = jTable.getTableHeader();
        header.setBackground(new Color(28, 69, 50)); // biru tua
        header.setForeground(Color.WHITE);           // teks putih
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
            c.setBackground(new Color(41, 157, 145)); // warna saat dipilih
        }

        return c;
    }
});
           SwingUtilities.invokeLater(() -> TxCari.requestFocusInWindow());
    }

    private void setupRFIDListener() {
    searchTimer = new javax.swing.Timer(150, e -> {
        String rfid = TxCari.getText().trim();
        if (rfid.length() >= MIN_RFID_LENGTH) {
            processRFIDScan(rfid);
        }
    });
        searchTimer.setRepeats(false);

        TxCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
    }

private void processRFIDScan(String rfid) {
    String query = "SELECT NamaPengguna, NoRFID, Saldo FROM pengguna WHERE NoRFID = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, rfid);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double nominal = 0; // default nominal bisa diubah sesuai kebutuhan
                showDetailPopup(
                    rs.getString("NoRFID"),
                    rs.getString("NamaPengguna"),
                    rs.getDouble("Saldo"),
                    nominal
                );
                TxCari.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "RFID tidak terdaftar",
                    "Data Tidak Ditemukan",
                    JOptionPane.WARNING_MESSAGE);
                TxCari.setText("");
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
    PanelDetailTabungan detailPanel = new PanelDetailTabungan(noRFID, nama, saldo, nominal);
    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Detail Santri", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.getContentPane().add(detailPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent e) {
            loadTable(); // refresh data saat popup ditutup
        }

        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            loadTable();
        }
    });
}



    private void loadTable() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable.getModel();
        model.setRowCount(0);

        String query = "SELECT NamaPengguna, Saldo, TanggalUpdate FROM pengguna";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("NamaPengguna"),
                    rs.getDouble("Saldo"),
                    rs.getDate("TanggalUpdate")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTotalSaldoKeTextField() {
    String query = "SELECT SUM(Saldo) AS TotalSaldo FROM pengguna";

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        if (rs.next()) {
            double totalSaldo = rs.getDouble("TotalSaldo");

            // Format ke Rupiah
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String totalFormatted = formatRupiah.format(totalSaldo);

            // Tampilkan ke jTextField
            txTotalSaldo.setText(totalFormatted);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal mengambil total saldo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        TxCari = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        LbTanggal = new javax.swing.JLabel();
        txTotalSaldo = new javax.swing.JTextField();

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Saldo", "Tanggal"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        TxCari.setBorder(javax.swing.BorderFactory.createTitledBorder("Cari RFID"));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("Tabungan");

        jLabel17.setText("Admin > Tabungan");

        LbTanggal.setText("Hari Tanggal");

        txTotalSaldo.setBorder(javax.swing.BorderFactory.createTitledBorder("Total  Saldo Santri"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 556, Short.MAX_VALUE)
                        .addComponent(LbTanggal))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txTotalSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxCari, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LbTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txTotalSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JTextField TxCari;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField txTotalSaldo;
    // End of variables declaration//GEN-END:variables
}
