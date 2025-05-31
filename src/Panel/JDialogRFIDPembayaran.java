package Panel;

import Form.connect;
import javax.swing.*; 
import java.awt.*;
import java.sql.*;

public class JDialogRFIDPembayaran extends JDialog {
    private JTextField tfRFID;
    private JLabel lblNama, lblSaldo;
    private JButton btnBayar;
    private double totalHarga;
    private String namaPemilik = "";
    private String noRFID = "";
    private boolean sukses = false;

    public boolean isPembayaranBerhasil() {
        return sukses;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public String getNoRFID() {
        return noRFID;
    }

    public JDialogRFIDPembayaran(Frame parent, double totalHarga) {
        super(parent, "Pembayaran via RFID", true);
        this.totalHarga = totalHarga;
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 1, 5, 5));

        add(new JLabel("Tempelkan Kartu RFID:"));
        tfRFID = new JTextField();
        add(tfRFID);

        add(new JLabel("Nama Pemilik:"));
        lblNama = new JLabel("-");
        add(lblNama);

        add(new JLabel("Saldo:"));
        lblSaldo = new JLabel("-");
        add(lblSaldo);

        btnBayar = new JButton("Bayar");
        btnBayar.setEnabled(false);
        add(btnBayar);

        tfRFID.addActionListener(e -> {
            noRFID = tfRFID.getText().trim();
            if (!noRFID.isEmpty()) {
                cekRFID(noRFID);
            }
        });

        btnBayar.addActionListener(e -> {
            if (kurangiSaldo(noRFID, totalHarga)) {
                sukses = true;
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengurangi saldo.");
            }
        });
    }

    private void cekRFID(String noRFID) {
        try {
            Connection conn = connect.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT NamaPengguna, Saldo FROM pengguna WHERE NoRFID = ?");
            pst.setString(1, noRFID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                namaPemilik = rs.getString("NamaPengguna");
                double saldo = rs.getDouble("Saldo");

                lblNama.setText(namaPemilik);
                lblSaldo.setText("Rp " + saldo);

                if (saldo >= totalHarga) {
                    btnBayar.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Saldo tidak mencukupi!");
                    btnBayar.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Kartu tidak ditemukan!");
                lblNama.setText("-");
                lblSaldo.setText("-");
                btnBayar.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean kurangiSaldo(String noRFID, double jumlah) {
        try {
            Connection conn = connect.getConnection();
            PreparedStatement pst = conn.prepareStatement("UPDATE pengguna SET Saldo = Saldo - ? WHERE NoRFID = ?");
            pst.setDouble(1, jumlah);
            pst.setString(2, noRFID);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

