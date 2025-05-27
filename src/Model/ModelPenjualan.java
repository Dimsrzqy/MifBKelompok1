package Model;

import java.time.LocalDate;

public class ModelPenjualan {
    private String IDTransaksiJual;
    private String NamaUser;
    private int jumlah;
    private double hargaSatuan;  // atau BigDecimal untuk presisi tinggi
    private double totalHarga;
    private double totalBayar;
    private double kembalian;
    private LocalDate tanggalJual;  // java.time.LocalDate (Java 8+)

    public String getIDTransaksiJual() {
        return IDTransaksiJual;
    }

    public void setIDTransaksiJual(String IDTransaksiJual) {
        this.IDTransaksiJual = IDTransaksiJual;
    }

    public String getNamaUser() {
        return NamaUser;
    }

    public void setNamaUser(String NamaUser) {
        this.NamaUser = NamaUser;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(double hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }

    public LocalDate getTanggalJual() {
        return tanggalJual;
    }

    public void setTanggalJual(LocalDate tanggalJual) {
        this.tanggalJual = tanggalJual;
    }

}