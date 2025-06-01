
package Panel;
import Form.FormMenuUtama;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JFrame;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.*;                     
import java.security.MessageDigest;    
import java.security.NoSuchAlgorithmException;
import javax.swing.*;                  

public class PanelRegister extends javax.swing.JPanel {

    public PanelRegister() {
        initComponents();

        // Placeholder
        TxNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "nama lengkap");
        TxUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "username");
        TxPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "kombinasi huruf dan angka");
        TxPassword2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "konfirmasi password");
        TxEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "contoh@gmail.com");
        TxNoHp.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "nomor handphone");

        TxPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true;showCapsLock:true");
        TxPassword2.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true;showCapsLock:true");

        // Tambahkan listener hanya sekali
        setupRegisterButtonListener();
    }

    public enum LevelUser {
        admin, user
    }

    private void setupRegisterButtonListener() {
        // Hapus semua listener lama (hindari dobel klik)
        for (ActionListener al : BtRegister.getActionListeners()) {
            BtRegister.removeActionListener(al);
        }

        BtRegister.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String nama = TxNama.getText().trim();
        String username = TxUsername.getText().trim();
        String password = TxPassword2.getText();
        String confirmPassword = TxPassword.getText();
        String email = TxEmail.getText().trim();
        String nohp = TxNoHp.getText().trim();

        // Validasi input kosong
        if (nama.isEmpty() || username.isEmpty() || confirmPassword.isEmpty() || password.isEmpty() || email.isEmpty() || nohp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek password sama
        if (!confirmPassword.equals(password)) {
            JOptionPane.showMessageDialog(this, "Password dan Konfirmasi tidak sama!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi kekuatan password
        if (!isPasswordValid(password)) {
            JOptionPane.showMessageDialog(this, "Password harus terdiri dari huruf besar, kecil, dan angka, minimal 6 karakter.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi email
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Format email tidak valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/koperasi_nuris", "root", "");
             Statement s = con.createStatement()) {

            // Cek username sudah ada
            ResultSet rs = s.executeQuery("SELECT * FROM user WHERE Username = '" + username + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Ambil ID terakhir
            String lastId = "";
            ResultSet rsLast = s.executeQuery("SELECT IDUser FROM user ORDER BY IDUser DESC LIMIT 1");
            if (rsLast.next()) {
                lastId = rsLast.getString("IDUser");
            }

            String newIdUser = lastId.isEmpty() ? "USR001" :
                String.format("USR%03d", Integer.parseInt(lastId.substring(3)) + 1);

            // Enkripsi password
            String hashedPassword = hashMD5(password);

            // Simpan data
            String insert = "INSERT INTO user (IDUser, NamaUser, Username, Password, Email, NoHp, Level) " +
                            "VALUES ('" + newIdUser + "', '" + nama + "', '" + username + "', '" + hashedPassword + "', '" + email + "', '" + nohp + "', '" + LevelUser.user.name() + "')";
            s.execute(insert);

            JOptionPane.showMessageDialog(this, "Akun berhasil dibuat!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        TxNama.setText("");
        TxUsername.setText("");
        TxPassword.setText("");
        TxPassword2.setText("");
        TxEmail.setText("");
        TxNoHp.setText("");
    }

    public static String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPasswordValid(String password) {
        if (password.length() < 6) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }

    // Komponen GUI kamu seperti TxNama, TxUsername, BtRegister dll harus sudah dideklarasikan lewat GUI Builder


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btLogin1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        TxNama = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        TxPassword2 = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BtRegister = new javax.swing.JButton();
        TxUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TxEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TxNoHp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TxPassword = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(28, 69, 50));
        jPanel2.setForeground(new java.awt.Color(28, 69, 50));
        jPanel2.setMaximumSize(new java.awt.Dimension(475, 600));
        jPanel2.setMinimumSize(new java.awt.Dimension(475, 600));

        btLogin1.setBackground(new java.awt.Color(51, 51, 51));
        btLogin1.setForeground(new java.awt.Color(255, 255, 255));
        btLogin1.setText("LOGIN");
        btLogin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLogin1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Bersama Koperasi Nuris");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Kembali ke halaman Login");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("ciptakan masa depan yang lebih berkah!");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/nue.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(btLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(130, 130, 130))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(475, 600));
        jPanel1.setMinimumSize(new java.awt.Dimension(475, 600));

        TxNama.setPreferredSize(new java.awt.Dimension(70, 23));
        TxNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxNamaActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setText("Register");

        jLabel3.setText("Password");

        BtRegister.setBackground(new java.awt.Color(51, 51, 51));
        BtRegister.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        BtRegister.setForeground(new java.awt.Color(255, 255, 255));
        BtRegister.setText("Register");
        BtRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtRegisterActionPerformed(evt);
            }
        });

        TxUsername.setPreferredSize(new java.awt.Dimension(70, 23));
        TxUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxUsernameActionPerformed(evt);
            }
        });

        jLabel5.setText("Username");

        TxEmail.setPreferredSize(new java.awt.Dimension(70, 23));
        TxEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxEmailActionPerformed(evt);
            }
        });

        jLabel6.setText("E-mail");

        TxNoHp.setPreferredSize(new java.awt.Dimension(70, 23));
        TxNoHp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxNoHpActionPerformed(evt);
            }
        });

        jLabel7.setText("No. Handphone");

        jLabel10.setText("Konfirmasi Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxNama, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxNama, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btLogin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLogin1ActionPerformed
    FormMenuUtama.showLogin();
    }//GEN-LAST:event_btLogin1ActionPerformed

    private void TxNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxNamaActionPerformed

    private void BtRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtRegisterActionPerformed
    String nama, username, password, email, nohp, query;
    String SUrl = "jdbc:mysql://localhost:3306/koperasi_nuris";
    String SUser = "root";
    String SPass = "";

    LevelUser level = LevelUser.user;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
        Statement s = con.createStatement();

        // Validasi input kosong
        if (TxNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (TxUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (TxPassword.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (TxPassword2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Konfirmasi password harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (TxEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (TxNoHp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor handphone harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ambil data
        nama = TxNama.getText().trim();
        username = TxUsername.getText().trim();
        password = TxPassword2.getText();
        email = TxEmail.getText().trim();
        nohp = TxNoHp.getText().trim();

        // Validasi email
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailPattern)) {
            JOptionPane.showMessageDialog(this, "Email tidak valid!");
            return;
        }

        // Validasi password
        if (!TxPassword.getText().equals(password)) {
            JOptionPane.showMessageDialog(this, "Password dan Konfirmasi Password tidak sama!");
            return;
        }
        if (!isPasswordValid(password)) {
            JOptionPane.showMessageDialog(this, "Password harus minimal 6 karakter dan mengandung huruf besar, huruf kecil, dan angka!");
            return;
        }

        // Cek username unik
        String cekQuery = "SELECT * FROM user WHERE Username = '" + username + "'";
        ResultSet rs = s.executeQuery(cekQuery);
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Username sudah digunakan, silakan pilih yang lain.");
            rs.close();
            return;
        }
        rs.close();

        // Ambil ID terakhir
        String newIdUser = "USR001";
        String getLastIdQuery = "SELECT IDUser FROM user ORDER BY IDUser DESC LIMIT 1";
        ResultSet rsLast = s.executeQuery(getLastIdQuery);
        if (rsLast.next()) {
            String lastId = rsLast.getString("IDUser");
            int number = Integer.parseInt(lastId.substring(3)) + 1;
            newIdUser = String.format("USR%03d", number);
        }
        rsLast.close();

        // Hash password
        String hashPassword = hashMD5(password);

        // Insert
        query = "INSERT INTO user (IDUser, NamaUser, Username, Password, Email, NoHp, Level) " +
                "VALUES ('" + newIdUser + "', '" + nama + "', '" + username + "', '" + hashPassword + "', '" + email + "', '" + nohp + "', '" + level.name() + "')";
        s.executeUpdate(query);

        // Bersihkan form
        TxNama.setText("");
        TxUsername.setText("");
        TxPassword.setText("");
        TxPassword2.setText("");
        TxEmail.setText("");
        TxNoHp.setText("");

        JOptionPane.showMessageDialog(this, "Akun berhasil dibuat.");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_BtRegisterActionPerformed

    private void TxUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxUsernameActionPerformed

    private void TxEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxEmailActionPerformed

    private void TxNoHpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxNoHpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxNoHpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtRegister;
    private javax.swing.JTextField TxEmail;
    private javax.swing.JTextField TxNama;
    private javax.swing.JTextField TxNoHp;
    private javax.swing.JPasswordField TxPassword;
    private javax.swing.JPasswordField TxPassword2;
    private javax.swing.JTextField TxUsername;
    private javax.swing.JButton btLogin1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
