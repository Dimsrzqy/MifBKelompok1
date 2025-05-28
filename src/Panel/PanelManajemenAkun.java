
package Panel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Panel.PopupEditManAkun;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class PanelManajemenAkun extends javax.swing.JPanel {

    /**
     * Creates new form ManajemenAkun
     */
    public PanelManajemenAkun() {
        initComponents();
        loadTable();
        
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
    }
List<String> idUserList = new ArrayList<>();

private void loadTable() {
    // Buat model tabel dengan kolom "No" di awal, "IDUser" disembunyikan nanti
    DefaultTableModel model = new DefaultTableModel(
        new Object[]{"No", "IDUser", "NamaUser", "Username", "Password", "Email", "Telephone", "Level"}, 0
    );
    jTable.setModel(model);

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT IDUser, NamaUser, Username, Password, Email, NoHp, Level FROM user")) {

        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,                              // Kolom No
                rs.getString("IDUser"),            // Kolom IDUser (disembunyikan di bawah)
                rs.getString("NamaUser"),
                rs.getString("Username"),
                rs.getString("Password"),
                rs.getString("Email"),
                rs.getString("NoHp"),
                rs.getString("Level")
            });
        }
    } catch (Exception e) {
        System.out.println("Error saat mengambil data: " + e.getMessage());
        e.printStackTrace();
    }

    // 🔒 Sembunyikan kolom IDUser dari tampilan, tetap tersedia di model
    jTable.getColumnModel().getColumn(1).setMinWidth(0);
    jTable.getColumnModel().getColumn(1).setMaxWidth(0);
    jTable.getColumnModel().getColumn(1).setWidth(0);
}


private Map<String, String> getUserDataFromDatabase(String idUser) {
    Map<String, String> userData = new HashMap<>();
    System.out.println("Mencari user dengan ID: " + idUser);

    String query = "SELECT IDUser, NamaUser, Username, Password, Email, NoHp, Level FROM user WHERE IDUser = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
         PreparedStatement pst = conn.prepareStatement(query)) {

        pst.setString(1, idUser);  // <-- pakai setString bukan setInt

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                userData.put("IDUser", rs.getString("IDUser"));  // ambil langsung string
                userData.put("NamaUser", rs.getString("NamaUser"));
                userData.put("Username", rs.getString("Username"));
                userData.put("Password", rs.getString("Password"));
                userData.put("Email", rs.getString("Email"));
                userData.put("NoHp", rs.getString("NoHp"));
                userData.put("Level", rs.getString("Level"));
            } else {
                JOptionPane.showMessageDialog(null,
                    "User dengan ID " + idUser + " tidak ditemukan",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Gagal mengambil data user: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }

    return userData;
}




private void showEditDialog(Map<String, String> userData) {
    if (userData == null || userData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Data user tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JDialog editDialog = new JDialog();
    editDialog.setTitle("Edit Data Pengguna - " + userData.get("IDUser"));

    PopupEditManAkun editPanel = new PopupEditManAkun(userData);

    // Setelah dialog ditutup, ambil data yang sudah diperbarui dan simpan
    editDialog.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
            Map<String, String> updatedData = editPanel.getUpdatedData(); // Ambil data yang sudah diperbarui
            if (updatedData != null) {
                saveUserData(updatedData); // Simpan data yang diperbarui
                refreshTableData(); // Segarkan tabel
            }
        }
    });

    editDialog.add(editPanel);
    editDialog.pack();
    editDialog.setLocationRelativeTo(this);
    editDialog.setVisible(true);
}




    private void saveUserData(Map<String, String> updatedData) {
        String sql = "UPDATE user SET NamaUser = ?, Username = ?, Password = ?, Email = ?, NoHp = ? WHERE IDUser = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, updatedData.get("NamaUser"));
            pst.setString(2, updatedData.get("Username"));
            pst.setString(3, updatedData.get("Password"));
            pst.setString(4, updatedData.get("Email"));
            pst.setString(5, updatedData.get("NoHp"));
            pst.setString(6, updatedData.get("IDUser"));

            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error menyimpan data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

   private void updateUserData(Map<String, String> updatedData) {
    String updateQuery = "UPDATE user SET NamaUser=?, Username=?, Password=?, Email=?, NoHp=? WHERE IDUser=?";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
         PreparedStatement pst = conn.prepareStatement(updateQuery)) {

        pst.setString(1, updatedData.get("NamaUser"));
        pst.setString(2, updatedData.get("Username"));
        pst.setString(3, updatedData.get("Password"));
        pst.setString(4, updatedData.get("Email"));
        pst.setString(5, updatedData.get("NoHp"));
        pst.setString(6, updatedData.get("IDUser"));

        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this,
                "Data user berhasil diperbarui",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Tidak ada data yang diupdate",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Gagal update data user: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }
}


   private void refreshTableData() {
    DefaultTableModel model = (DefaultTableModel) jTable.getModel();
    model.setRowCount(0);  // Hapus semua data lama
    loadTable();           // Muat ulang dari database
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        BtHapus = new javax.swing.JButton();
        BtEdit = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        LbTanggal = new javax.swing.JLabel();

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "ID User", "Nama", "Username", "Password", "Email", "Telephone", "Role"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
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

        BtEdit.setBackground(new java.awt.Color(255, 199, 18));
        BtEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtEdit.setForeground(new java.awt.Color(255, 255, 255));
        BtEdit.setText("EDIT");
        BtEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtEditActionPerformed(evt);
            }
        });

        jLabel17.setText("Admin > Data Manajemen Akun");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("Akun Kasir");

        LbTanggal.setText("Hari Tanggal");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BtEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LbTanggal)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LbTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BtHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtHapusActionPerformed
int selectedRow = jTable.getSelectedRow();

if (selectedRow != -1) {
    DefaultTableModel model = (DefaultTableModel) jTable.getModel();
    String idUser = model.getValueAt(selectedRow, 0).toString(); // Ambil IDUser kolom ke-0

    int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data user dengan IDUser '" + idUser + "'?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

    if (konfirmasi == JOptionPane.YES_OPTION) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user WHERE IDUser = ?")) {

            pstmt.setString(1, idUser); // Hapus berdasarkan IDUser, bukan username
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                loadTable(); // Refresh tabel setelah hapus
            } else {
                JOptionPane.showMessageDialog(this, "Data gagal dihapus atau tidak ditemukan.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kesalahan database: " + e.getMessage());
        }
    }
} else {
    JOptionPane.showMessageDialog(this, "Silakan pilih baris yang ingin dihapus.");
}

    }//GEN-LAST:event_BtHapusActionPerformed

    private void BtEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtEditActionPerformed
int selectedRow = jTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Pilih baris data yang akan diedit terlebih dahulu",
            "Peringatan",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    Object idValue = jTable.getValueAt(selectedRow, 1);

    if (idValue == null) {
        JOptionPane.showMessageDialog(this,
            "ID User tidak valid (null)",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    String idUser = idValue.toString();  // Simpan sebagai String

    Map<String, String> userData = getUserDataFromDatabase(idUser);
    if (userData.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Data user dengan ID " + idUser + " tidak ditemukan di database",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Panggil dialog edit
    showEditDialog(userData);
    refreshTableData();
    }//GEN-LAST:event_BtEditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtEdit;
    private javax.swing.JButton BtHapus;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
