/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Panel;

import Panel.PopupTambahStok;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.TableModel;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author ASUS Vivobook
 */
public class stok extends javax.swing.JPanel {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/koperasi_nuris";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Creates new form stok
     */
    public stok() {
    initComponents();
    tampilDatastok();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    LbTanggal.setText(LocalDate.now().format(dtf));
    
        JTableHeader header = tabel_stok.getTableHeader();
        header.setBackground(new Color(28, 69, 50)); // biru tua
        header.setForeground(Color.WHITE);           // teks putih
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        tabel_stok.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

    // Event saat mengetik di TxCari
    txCari.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            filterDataStok();
        }

        public void removeUpdate(DocumentEvent e) {
            filterDataStok();
        }

        public void changedUpdate(DocumentEvent e) {
            filterDataStok();
        }
    });
}


 public static Connection getConnection() {
    Connection conn = null;
    try {
        String url = "jdbc:mysql://localhost:3306/koperasi_nuris";
        String user = "root";
        String pass = "";

        conn = DriverManager.getConnection(url, user, pass);
    } catch (SQLException e) {
        System.out.println("Koneksi gagal: " + e.getMessage());
    }
    return conn;
}

private void filterDataStok() {
    String keyword = txCari.getText().trim().toLowerCase();

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Nama Barang");
    model.addColumn("Jumlah Masuk");
    model.addColumn("Jumlah Keluar");
    model.addColumn("Tanggal Update");
    model.addColumn("Kadaluarsa");
    model.addColumn("Kategori");

    String query = "SELECT b.NamaBarang, s.JumlahMasuk, s.JumlahKeluar, "
                 + "s.TanggalUpdate, s.TanggalKadaluarsa, k.NamaKategori "
                 + "FROM barang b "
                 + "JOIN stok s ON s.IDBarang = b.IDBarang "
                 + "JOIN kategori k ON k.IDKategori = b.IDKategori "
                 + "WHERE LOWER(b.NamaBarang) LIKE ?";

    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("NamaBarang"),
                rs.getInt("JumlahMasuk"),
                rs.getInt("JumlahKeluar"),
                rs.getDate("TanggalUpdate"),
                rs.getDate("TanggalKadaluarsa"),
                rs.getString("NamaKategori")
            });
        }

        tabel_stok.setModel(model);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void bukaPopupEditStok(Map<String, String> stokData) {
        PopupTambahStok panel = new PopupTambahStok(stokData);

        panel.setDataSavedListener(() -> {
            tampilDatastok();
        });

        JDialog dialog = new JDialog((JFrame) null, "Edit Stok", true);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public void tampilDatastok() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Nama Barang");
    model.addColumn("Jumlah Masuk");
    model.addColumn("Jumlah Keluar");
    model.addColumn("Tanggal Update");
    model.addColumn("Kadaluarsa");
    model.addColumn("Kategori");

    String query = "SELECT b.NamaBarang, s.JumlahMasuk, s.JumlahKeluar, "
                 + "s.TanggalUpdate, s.TanggalKadaluarsa, k.NamaKategori "
                 + "FROM barang b "
                 + "JOIN stok s ON s.IDBarang = b.IDBarang "
                 + "JOIN kategori k ON k.IDKategori = b.IDKategori";

    try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("NamaBarang"),
                rs.getInt("JumlahMasuk"),
                rs.getInt("JumlahKeluar"),
                rs.getDate("TanggalUpdate"),
                rs.getDate("TanggalKadaluarsa"),
                rs.getString("NamaKategori")
            });
        }

        tabel_stok.setModel(model);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_stok = new javax.swing.JTable();
        BtnHapus = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txCari = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        LbTanggal = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        tabel_stok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Barang", "Nama Barang", "Jumlah Masuk", "Jumlah Keluar", "Tanggal Update", "Kadaluarsa"
            }
        ));
        jScrollPane1.setViewportView(tabel_stok);

        BtnHapus.setBackground(new java.awt.Color(255, 0, 0));
        BtnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnHapus.setForeground(new java.awt.Color(255, 255, 255));
        BtnHapus.setText("Hapus");
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });

        jLabel17.setText("Admin > Manajemen Barang");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("STOK");

        txCari.setBorder(javax.swing.BorderFactory.createTitledBorder("Cari Nama Barang"));
        txCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txCariActionPerformed(evt);
            }
        });

        LbTanggal.setText("Hari Tanggal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addContainerGap(778, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LbTanggal)
                        .addGap(21, 21, 21))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txCari, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator8)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(LbTanggal))
                .addGap(18, 18, 18)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        // Pastikan ada baris yang dipilih di tabel
        int selectedRow = tabel_stok.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih barang yang akan dihapus terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Konfirmasi penghapusan
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus barang ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Ambil Nama Barang dari kolom pertama (kolom 0)
        DefaultTableModel model = (DefaultTableModel) tabel_stok.getModel();
        String namaBarang = model.getValueAt(selectedRow, 0).toString().trim();

        try (Connection conn = stok.getConnection()) {
            // Langkah 1: Ambil IDStok berdasarkan NamaBarang
            String selectSql = "SELECT s.IDStok "
                    + "FROM stok s "
                    + "JOIN barang b ON s.IDBarang = b.IDBarang "
                    + "WHERE b.NamaBarang = ?";

            String idStok = null;
            try (PreparedStatement selectPst = conn.prepareStatement(selectSql)) {
                selectPst.setString(1, namaBarang);
                ResultSet rs = selectPst.executeQuery();
                if (rs.next()) {
                    idStok = rs.getString("IDStok");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Data stok tidak ditemukan!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Langkah 2: Hapus data dari tabel stok
            String deleteSql = "DELETE FROM stok WHERE IDStok = ?";
            try (PreparedStatement deletePst = conn.prepareStatement(deleteSql)) {
                deletePst.setString(1, idStok);
                int affectedRows = deletePst.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Data stok berhasil dihapus!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Refresh tabel setelah penghapusan
                    tampilDatastok();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menghapus data stok!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void txCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txCariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnHapus;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable tabel_stok;
    private javax.swing.JTextField txCari;
    // End of variables declaration//GEN-END:variables
}
