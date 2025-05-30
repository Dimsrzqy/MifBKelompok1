/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Panel.PanelDetailTabungan;
import java.awt.Dialog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JDialog;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dimsrzqy
 */
public class PanelTabungan extends javax.swing.JPanel {

    private javax.swing.Timer searchTimer;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/koperasi_nuris";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final int MIN_RFID_LENGTH = 10;

    public PanelTabungan() {
        initComponents(); // komponen seperti txCari dan jTable dibuat otomatis oleh NetBeans
        setupRFIDListener();
        loadTable();
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
                    showDetailPopup(
                        rs.getString("NoRFID"),
                        rs.getString("NamaPengguna"),
                        rs.getDouble("Saldo")
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

    private void showDetailPopup(String noRFID, String nama, double saldo) {
        PanelDetailTabungan detailPanel = new PanelDetailTabungan(noRFID, nama, saldo);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Detail Santri", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.getContentPane().add(detailPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadTable() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable.getModel();
        model.setRowCount(0);

        String query = "SELECT NamaPengguna, Saldo, TanggalUpdate FROM pengguna";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("NamaPengguna"),
                    rs.getDouble("Saldo"),
                    rs.getDate("TanggalUpdate")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        TxCari = new javax.swing.JTextField();

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama", "Saldo", "Tanggal"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TxCari, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE))
                .addGap(55, 55, 55))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(TxCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addGap(38, 38, 38))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxCari;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
