
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

    /**
     * Creates new form PanelRegister
     */
    public PanelRegister() {
        initComponents();
        
        TxNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "nama lengkap");
        TxUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "username");
        JPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "password");
        TxEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "example@gmail.com");
        TxNoHp.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "nomor handphone");
    }
    public enum LevelUser {
    admin, user
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

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btLogin1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        TxNama = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        JPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BtRegister = new javax.swing.JButton();
        TxUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TxEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TxNoHp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

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

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/logo_koperasi.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(btLogin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(73, 73, 73)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(39, 39, 39)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxNama, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82))
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
                .addComponent(JPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(BtRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(123, Short.MAX_VALUE))
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
        BtRegister.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String nama, username, password, email, nohp, query;
        String SUrl, SUser, SPass;

        LevelUser level = LevelUser.user;

        SUrl = "jdbc:mysql://localhost:3306/koperasi_nuris";
        SUser = "root";
        SPass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(SUrl, SUser, SPass);
            Statement s = con.createStatement();

            if ("".equals(TxNama.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Nama harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(TxUsername.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Username harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(JPassword.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Password harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(TxEmail.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Email harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
            } else if ("".equals(TxNoHp.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Nomor handphone harus di isi", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                nama = TxNama.getText();
                username = TxUsername.getText();
                password = JPassword.getText();
                email = TxEmail.getText();
                nohp = TxNoHp.getText();

                String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

                if (email.matches(emailPattern)) {

                    // Cek apakah username sudah ada
                    String cekQuery = "SELECT * FROM user WHERE Username = '" + username + "'";
                    Statement sCek = con.createStatement();
                    ResultSet rs = sCek.executeQuery(cekQuery);


                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Username sudah digunakan, silakan pilih yang lain.");
                        rs.close();
                        sCek.close();
                        return;
                    }
                    rs.close();
                    sCek.close();

                    // Ambil IDUser terakhir
                    String lastId = "";
                    String getLastIdQuery = "SELECT IDUser FROM user ORDER BY IDUser DESC LIMIT 1";
                    Statement sLast = con.createStatement();
                    ResultSet rsLast = sLast.executeQuery(getLastIdQuery);

                    if (rsLast.next()) {
                        lastId = rsLast.getString("IDUser");
                    }
                    rsLast.close();
                    sLast.close();

                    // Generate IDUser baru
                    String newIdUser = "";
                    if (lastId.equals("")) {
                        newIdUser = "USR-001";
                    } else {
                        String numberPart = lastId.substring(4); // ambil angka setelah 'USR-'
                        int number = Integer.parseInt(numberPart) + 1;
                        newIdUser = String.format("USR-%03d", number);
                    }

                    // Hash password
                    String hashPassword = hashMD5(password);

                    // Insert jika username belum ada
                    query = "INSERT INTO user (IDUser, NamaUser, Username, Password, Email, NoHp, Level) " +
                            "VALUES ('" + newIdUser + "', '" + nama + "', '" + username + "', '" + hashPassword + "', '" + email + "', '" + nohp + "', '" + level.name() + "')";
                    s.execute(query);

                    // Kosongkan input
                    TxNama.setText("");
                    TxUsername.setText("");
                    JPassword.setText("");
                    TxEmail.setText("");
                    TxNoHp.setText("");

                    JOptionPane.showMessageDialog(null, "Akun Berhasil Dibuat");

                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Email tidak valid!");
                }
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
});


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
    private javax.swing.JPasswordField JPassword;
    private javax.swing.JTextField TxEmail;
    private javax.swing.JTextField TxNama;
    private javax.swing.JTextField TxNoHp;
    private javax.swing.JTextField TxUsername;
    private javax.swing.JButton btLogin1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
