package Panel;
import Form.FormMenuUtama;
import Panel.PanelTransaksiJual;
import javax.swing.JFrame;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import kasir.main.Main;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class PanelLogin extends javax.swing.JPanel {

    private String activeUserId;
    public PanelLogin() {
        initComponents();
        
        
        TxUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "username anda");
        TxPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "password anda");
        
        TxPassword.putClientProperty(FlatClientProperties.STYLE, ""
            + "showRevealButton:true;"
            + "showCapsLock:true");
        
      
    }
    public enum LevelUser {
    admin,
    user
}

   private String hashPasswordMD5(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pn_login1 = new javax.swing.JPanel();
        TxUsername = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        TxPassword = new javax.swing.JPasswordField();
        tx_login = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btLogin = new javax.swing.JButton();
        Pn_login2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btRegister = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        Pn_login1.setBackground(new java.awt.Color(255, 255, 255));
        Pn_login1.setMinimumSize(new java.awt.Dimension(475, 600));
        Pn_login1.setPreferredSize(new java.awt.Dimension(475, 600));
        Pn_login1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TxUsername.setPreferredSize(new java.awt.Dimension(70, 23));
        TxUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxUsernameActionPerformed(evt);
            }
        });
        Pn_login1.add(TxUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 350, 60));

        jLabel1.setText("Username");
        Pn_login1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 220, 20));
        Pn_login1.add(TxPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 350, 60));

        tx_login.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        tx_login.setText("Login");
        Pn_login1.add(tx_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 230, 70));

        jLabel3.setText("Password");
        Pn_login1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, 220, 20));

        btLogin.setBackground(new java.awt.Color(51, 51, 51));
        btLogin.setForeground(new java.awt.Color(255, 255, 255));
        btLogin.setText("LOGIN");
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });
        Pn_login1.add(btLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 450, 300, 50));

        Pn_login2.setBackground(new java.awt.Color(28, 69, 50));
        Pn_login2.setForeground(new java.awt.Color(28, 69, 50));
        Pn_login2.setMinimumSize(new java.awt.Dimension(475, 600));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Daftar akun baru jika belum mempunyai akun");

        btRegister.setBackground(new java.awt.Color(51, 51, 51));
        btRegister.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btRegister.setForeground(new java.awt.Color(255, 255, 255));
        btRegister.setText("Register");
        btRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegisterActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Ketika seorang muslim menjaga kejujuran dalam jual beli ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Maka Allah akan menjaga hartanya dari kebinasaan");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/nue.png"))); // NOI18N

        javax.swing.GroupLayout Pn_login2Layout = new javax.swing.GroupLayout(Pn_login2);
        Pn_login2.setLayout(Pn_login2Layout);
        Pn_login2Layout.setHorizontalGroup(
            Pn_login2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pn_login2Layout.createSequentialGroup()
                .addGroup(Pn_login2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pn_login2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(Pn_login2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(Pn_login2Layout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Pn_login2Layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(btRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(117, 117, 117))
        );
        Pn_login2Layout.setVerticalGroup(
            Pn_login2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pn_login2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(161, 161, 161))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pn_login1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pn_login2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pn_login1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Pn_login2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void TxUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxUsernameActionPerformed
public class UserSession {
    private static String username;
    private static String namaKasir; // tambahkan ini kalau belum ada

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void setNamaKasir(String nama) {
        namaKasir = nama;
    }

    public static String getNamaKasir() {
        return namaKasir;
    }
}

    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        String User, Pw, query, passDB;
String SUrl, SUser, SPass;
PanelLogin.LevelUser level = null;

SUrl = "jdbc:mysql://localhost:3306/koperasi_nuris";
SUser = "root";
SPass = "";

User = TxUsername.getText();
Pw = hashPasswordMD5(TxPassword.getText());

if ("".equals(User)) {
    JOptionPane.showMessageDialog(new JFrame(), "Username harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
    return;
} else if ("".equals(Pw)) {
    JOptionPane.showMessageDialog(new JFrame(), "Password harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    java.sql.Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
    query = "SELECT * FROM user WHERE Username = ?";

    PreparedStatement s = con.prepareStatement(query);
    s.setString(1, User);
    ResultSet rs = s.executeQuery();

    if (rs.next()) {
        passDB = rs.getString("Password");
        String levelStr = rs.getString("Level");
        String nama = rs.getString("NamaUser");
        UserSession.setNamaKasir(nama); // ini yang dibutuhkan panel transaksi


        if (levelStr == null) {
            JOptionPane.showMessageDialog(new JFrame(), "Level kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Penyesuaian dengan enum Java
        if (levelStr.equalsIgnoreCase("admin")) {
            level = PanelLogin.LevelUser.admin;
        } else if (levelStr.equalsIgnoreCase("user")) {
            level = PanelLogin.LevelUser.user;
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Level tidak dikenali: " + levelStr, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Pw.equals(passDB)) {
            // Menyimpan ID pengguna yang aktif setelah login
            activeUserId = rs.getString("IDUser");
            

            System.out.println("ID Pengguna yang aktif: " + activeUserId);

            if (level == PanelLogin.LevelUser.admin) {
                System.out.println("Login sebagai Admin");
                System.out.println("Nama kasir diset: " + nama);
                UserSession.setNamaKasir(rs.getString("NamaUser"));
                UserSession.setNamaKasir(nama); // ini yang dibutuhkan panel transaksi

                FormMenuUtama.login();
                
            } else if (level == PanelLogin.LevelUser.user) {
                System.out.println("Login sebagai User");
                FormMenuUtama.login();    
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Username atau Password salah", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(new JFrame(), "User tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
    }

    con.close();
    TxPassword.setText("");
    TxUsername.setText("");

} catch (Exception e) {
    JOptionPane.showMessageDialog(new JFrame(), "Terjadi kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_btLoginActionPerformed



    private void btRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegisterActionPerformed
        FormMenuUtama.showRegister();
    }//GEN-LAST:event_btRegisterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Pn_login1;
    private javax.swing.JPanel Pn_login2;
    private javax.swing.JPasswordField TxPassword;
    private javax.swing.JTextField TxUsername;
    private javax.swing.JButton btLogin;
    private javax.swing.JButton btRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel tx_login;
    // End of variables declaration//GEN-END:variables
        
    
}

