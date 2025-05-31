
package Panel;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Window;
import javax.swing.JPasswordField;

/**
 *
 * @author dimsrzqy
 */
public class PopupEditManAkun extends javax.swing.JPanel {

    /**
     * Creates new form PopipEditManAkun
     */
    public PopupEditManAkun(Map<String, String> userData) {
    initComponents();
    initializeComponents();
    loadUserData(userData);
}

private void initializeComponents() {
    // Set password field to show dots instead of plain text
    if (txPassword instanceof JPasswordField) {
        ((JPasswordField) txPassword).setEchoChar('•');
    }

    // Buat txLevel tidak bisa diedit
    txLevel.setEditable(false); 
    txLevel.setEnabled(false); // Pastikan tidak bisa diubah
    txLevel.setBackground(new java.awt.Color(240, 240, 240)); // Warna abu-abu untuk menunjukkan readonly

    // Buat txIDuser tidak bisa diedit
    txIDUser.setEditable(false); 
    txIDUser.setEnabled(false); // Pastikan txIDuser tidak bisa diubah
    txIDUser.setBackground(new java.awt.Color(240, 240, 240)); // Warna abu-abu untuk menunjukkan readonly
}

private void loadUserData(Map<String, String> userData) {
    if (userData == null || userData.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Data user tidak tersedia", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    txIDUser.setText(userData.getOrDefault("IDUser", ""));
    txIDUser.setEditable(false);  // IDUser tidak boleh diubah

    txNama.setText(userData.getOrDefault("NamaUser", ""));
    txUsername.setText(userData.getOrDefault("Username", ""));

    String password = userData.getOrDefault("Password", "");
    if (txPassword instanceof JPasswordField) {
        ((JPasswordField) txPassword).setText(password);
    } else {
        txPassword.setText(password);
    }

    txEmail.setText(userData.getOrDefault("Email", ""));
    txNoHp.setText(userData.getOrDefault("NoHp", ""));
    txLevel.setText(userData.getOrDefault("Level", "user"));  // Default ke "user"
}


public boolean validateData() {
    if (txNama.getText().trim().isEmpty()) {
        showError("Nama User tidak boleh kosong!");
        txNama.requestFocus();
        return false;
    }

    if (txUsername.getText().trim().isEmpty()) {
        showError("Username tidak boleh kosong!");
        txUsername.requestFocus();
        return false;
    }

    String password = getPassword().trim();
    if (password.isEmpty()) {
        showError("Password tidak boleh kosong!");
        txPassword.requestFocus();
        return false;
    }

    String email = txEmail.getText().trim();
    if (!isValidEmail(email)) {
        showError("Format email tidak valid!");
        txEmail.requestFocus();
        return false;
    }

    return true;
}


private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
}

private boolean isValidEmail(String email) {
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  // Regex untuk validasi email
    return email.matches(regex);
}

// Inside the same class as saveUserData
Map<String, String> getUpdatedData() {
    Map<String, String> updatedData = new HashMap<>();
    updatedData.put("IDUser", txIDUser.getText().trim());
    updatedData.put("NamaUser", txNama.getText().trim());
    updatedData.put("Username", txUsername.getText().trim());
    updatedData.put("Password", getPassword().trim());
    updatedData.put("Email", txEmail.getText().trim());
    updatedData.put("NoHp", txNoHp.getText().trim());
    return updatedData;
}



private String getPassword() {
    if (txPassword instanceof JPasswordField) {
        return new String(((JPasswordField) txPassword).getPassword());
    }
    return txPassword.getText();
}

public void closePopup() {
    Window window = SwingUtilities.getWindowAncestor(this);
    if (window != null) {
        window.dispose();
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txUsername = new javax.swing.JTextField();
        txPassword = new javax.swing.JTextField();
        txNama = new javax.swing.JTextField();
        txEmail = new javax.swing.JTextField();
        txNoHp = new javax.swing.JTextField();
        txLevel = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txIDUser = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        txNoHp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNoHpActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(28, 69, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jLabel1.setText("Nama User");

        jLabel2.setText("Username");

        jLabel3.setText("Password");

        jLabel4.setText("Email");

        jLabel5.setText("Telephone");

        jLabel6.setText("Role");

        jButton1.setBackground(new java.awt.Color(28, 69, 50));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("SIMPAN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText("ID User");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txUsername)
                                    .addComponent(txPassword)
                                    .addComponent(txEmail)
                                    .addComponent(txNoHp)
                                    .addComponent(txLevel)
                                    .addComponent(txNama)
                                    .addComponent(txIDUser, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(55, 55, 55))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txIDUser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNama, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txNoHpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNoHpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNoHpActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         if (validateData()) {
        // Dapatkan parent dialog
        Window window = SwingUtilities.getWindowAncestor(this);
        
        // Konfirmasi penyimpanan
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Simpan perubahan data pengguna?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Tutup dialog
            if (window != null) {
                window.dispose();
            }
        }
    }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txEmail;
    private javax.swing.JTextField txIDUser;
    private javax.swing.JTextField txLevel;
    private javax.swing.JTextField txNama;
    private javax.swing.JTextField txNoHp;
    private javax.swing.JTextField txPassword;
    private javax.swing.JTextField txUsername;
    // End of variables declaration//GEN-END:variables

    public boolean isCancelled() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
