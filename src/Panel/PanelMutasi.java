/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import static com.mysql.cj.telemetry.TelemetryAttribute.DB_USER;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author dimsrzqy
 */
public class PanelMutasi extends javax.swing.JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/koperasi_nuris";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private String noRFID;

    public PanelMutasi(String noRFID) {
        this.noRFID = noRFID;
        initComponents(); // generated dari NetBeans
        loadMutasiByRFID(noRFID); // isi jTable
        
        
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

    public void loadMutasiByRFID(String noRFID) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);

        String query = "SELECT NamaPengguna, NoRFID, SaldoMasuk, SaldoKeluar, Saldo, JenisTransaksi, Tanggal " +
                       "FROM mutasi WHERE NoRFID = ? ORDER BY Tanggal DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, noRFID);
            ResultSet rs = stmt.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[] {
                    no++,
                    rs.getString("NamaPengguna"),
                    rs.getString("NoRFID"),
                    rs.getDouble("SaldoMasuk"),
                    rs.getDouble("SaldoKeluar"),
                    rs.getDouble("Saldo"),
                    rs.getString("JenisTransaksi"),
                    rs.getDate("Tanggal")
                });
            }
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data mutasi: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        resizeColumnWidth(jTable);

    }

public void resizeColumnWidth(JTable table) {
    final TableColumnModel columnModel = table.getColumnModel();
    for (int column = 0; column < table.getColumnCount(); column++) {
        int width = 50; // Minimum width
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component comp = table.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width + 10, width);
        }

        TableColumn tableColumn = columnModel.getColumn(column);
        tableColumn.setPreferredWidth(width);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "RFID", "SaldoMasuk", "SaldoKeluar", "Saldo", "Jenis", "Tanggal"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        jPanel1.setBackground(new java.awt.Color(28, 69, 52));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        jLabel17.setText("Admin > Data Santri");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("Mutasi Tabungan");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
