package Panel;
import Form.connect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.TableRowSorter;
public class PCariBarang extends JDialog {

   
    public PCariBarang() {  
        initComponents();
        pack(); // menyesuaikan ukuran otomatis dengan isi layout
        TxPencarian.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
    public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
    public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
});

        setLocationRelativeTo(null); // tengah layar
    }

    public void loadBarangFromDatabase() {
    DefaultTableModel model = (DefaultTableModel) TbDataProduk.getModel();
    model.setRowCount(0);

    try {
        Connection conn = connect.getConnection();
        String sql = "SELECT IDBarang, NamaBarang, Barcode, HargaJual FROM barang";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Object[] row = {
                rs.getString("IDBarang"),
                rs.getString("NamaBarang"),
                rs.getString("Barcode"),
                rs.getDouble("HargaJual")
            };
            model.addRow(row);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage());
        e.printStackTrace();
    }
}
    private String selectedBarang = null;

public String getSelectedBarang() {
    return selectedBarang;
}


private void filter() {
    DefaultTableModel model = (DefaultTableModel) TbDataProduk.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    TbDataProduk.setRowSorter(sorter);
    String text = TxPencarian.getText();
    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TbDataProduk = new javax.swing.JTable();
        BtPilih = new javax.swing.JButton();
        TxPencarian = new javax.swing.JTextField();

        getContentPane().add(jSeparator1, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(0, 204, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA PRODUK");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(24, 24, 24))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        TbDataProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "IDBarang", "NamaBarang", "Barcode", "HargaJual"
            }
        ));
        jScrollPane1.setViewportView(TbDataProduk);

        BtPilih.setText("PILIH");
        BtPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtPilihActionPerformed(evt);
            }
        });

        TxPencarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxPencarianActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtPilih)
                .addGap(18, 18, 18)
                .addComponent(TxPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 91, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtPilih, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxPencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void BtPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtPilihActionPerformed
        BtPilih.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int row = TbDataProduk.getSelectedRow();
        if (row != -1) {
            selectedBarang = TbDataProduk.getValueAt(row, 0).toString();
            dispose(); // tutup dialog otomatis
        } else {
            JOptionPane.showMessageDialog(null, "Pilih barang terlebih dahulu!");
        }
    }
});

    }//GEN-LAST:event_BtPilihActionPerformed

    private void TxPencarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxPencarianActionPerformed
        filter();
    }//GEN-LAST:event_TxPencarianActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtPilih;
    private javax.swing.JTable TbDataProduk;
    private javax.swing.JTextField TxPencarian;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables






}
