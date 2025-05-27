package Model;

import java.time.LocalDate;

public class ModelPenjualanDetail {
    private String IDTransaksiJual;
    private String IDBarang;
    private String NamaBarang;
    private String Kategori;  // atau BigDecimal untuk presisi tinggi
    private double totalHarga;
    private double totalBayar;
    private double kembalian;
    private LocalDate tanggalJual;  // java.time.LocalDate (Java 8+)
}
