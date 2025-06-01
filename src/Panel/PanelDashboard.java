package Panel;

import Form.connect;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.util.Locale.filter;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import java.awt.Color;
import java.awt.BasicStroke;
import org.jfree.chart.renderer.category.BarRenderer;


public class PanelDashboard extends javax.swing.JPanel {

    
    public PanelDashboard() {
        initComponents();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        LbTanggal.setText(LocalDate.now().format(dtf));
        CbFilterPenjualan.setSelectedIndex(0);
        tampilkanChartPenjualan("4 Hari Terakhir");
        tampilkanChartKeuntungan("4 Hari Terakhir");
        
    }
    private void tampilkanChartPenjualan(String filter) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String sql = "";

    switch (filter) {
        case "4 Hari Terakhir":
            sql = "SELECT DATE(TanggalJual) AS Label, SUM(TotalHarga) AS Total " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 4 DAY " +
                  "GROUP BY DATE(TanggalJual) ORDER BY TanggalJual";
            break;

        case "4 Minggu Terakhir":
            sql = "SELECT YEARWEEK(TanggalJual, 1) AS Label, SUM(TotalHarga) AS Total " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 4 WEEK " +
                  "GROUP BY YEARWEEK(TanggalJual, 1)";
            break;
            
        case "12 Bulan Terakhir":
        sql = "SELECT DATE_FORMAT(TanggalJual, '%Y-%m') AS Label, SUM(TotalHarga) AS Total " +
              "FROM transaksi_jual " +
              "WHERE TanggalJual >= CURDATE() - INTERVAL 12 MONTH " +
              "GROUP BY DATE_FORMAT(TanggalJual, '%Y-%m') " +
              "ORDER BY TanggalJual";
        break;
        
        
    }

    try (Connection conn = connect.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            String label = rs.getString("Label");
            double total = rs.getDouble("Total");
            dataset.addValue(total, "Total Penjualan", label);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Grafik Penjualan - " + filter,
                filter.contains("Bulan") ? "Bulan" : (filter.contains("Minggu") ? "Minggu" : "Tanggal"),
                "Total Penjualan",
                dataset
        );
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.BLUE); // Ubah warna garis jadi biru
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        chart.setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 300));

        PanelChartPenjualan.removeAll();
        PanelChartPenjualan.setLayout(new BorderLayout());
        PanelChartPenjualan.add(chartPanel, BorderLayout.CENTER);
        PanelChartPenjualan.revalidate();
        PanelChartPenjualan.repaint();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan chart: " + e.getMessage());
    }
}
    
    
    private void tampilkanChartKeuntungan(String filter) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String sql = "";

    switch (filter) {
        case "4 Hari Terakhir":
            sql = "SELECT DATE(TanggalJual) AS Label, SUM(Bayar - TotalHarga) AS TotalKeuntungan " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 4 DAY " +
                  "GROUP BY DATE(TanggalJual) ORDER BY TanggalJual";
            break;

        case "4 Minggu Terakhir":
            sql = "SELECT YEARWEEK(TanggalJual, 1) AS Label, SUM(Bayar - TotalHarga) AS TotalKeuntungan " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 4 WEEK " +
                  "GROUP BY YEARWEEK(TanggalJual, 1)";
            break;

        case "12 Bulan Terakhir":
            sql = "SELECT DATE_FORMAT(TanggalJual, '%Y-%m') AS Label, SUM(Bayar - TotalHarga) AS TotalKeuntungan " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 12 MONTH " +
                  "GROUP BY DATE_FORMAT(TanggalJual, '%Y-%m') " +
                  "ORDER BY TanggalJual";
            break;
    }

    try (Connection conn = connect.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            String label = rs.getString("Label");
            double total = rs.getDouble("TotalKeuntungan");
            dataset.addValue(total, "Total Keuntungan", label);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Grafik Keuntungan - " + filter,
                filter.contains("Bulan") ? "Bulan" : (filter.contains("Minggu") ? "Minggu" : "Tanggal"),
                "Total Keuntungan",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer();

        renderer.setSeriesPaint(0, new Color(76, 175, 80)); // Hijau untuk keuntungan
        plot.setRenderer(renderer);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        chart.setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 300));

        PanelChartKeuntungan.removeAll();
        PanelChartKeuntungan.setLayout(new BorderLayout());
        PanelChartKeuntungan.add(chartPanel, BorderLayout.CENTER);
        PanelChartKeuntungan.revalidate();
        PanelChartKeuntungan.repaint();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan chart keuntungan: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        PanelChartPenjualan = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        LbTanggal = new javax.swing.JLabel();
        CbFilterPenjualan = new javax.swing.JComboBox<>();
        PanelChartKeuntungan = new javax.swing.JPanel();
        CbFilterKeuntungan = new javax.swing.JComboBox<>();

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        PanelChartPenjualan.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout PanelChartPenjualanLayout = new javax.swing.GroupLayout(PanelChartPenjualan);
        PanelChartPenjualan.setLayout(PanelChartPenjualanLayout);
        PanelChartPenjualanLayout.setHorizontalGroup(
            PanelChartPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );
        PanelChartPenjualanLayout.setVerticalGroup(
            PanelChartPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        jLabel17.setText("Admin > Dashboard");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("Dashboard");

        LbTanggal.setText("Hari Tanggal");

        CbFilterPenjualan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "4 Hari Terakhir", "4 Minggu Terakhir", "12 Bulan Terakhir" }));
        CbFilterPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbFilterPenjualanActionPerformed(evt);
            }
        });

        PanelChartKeuntungan.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout PanelChartKeuntunganLayout = new javax.swing.GroupLayout(PanelChartKeuntungan);
        PanelChartKeuntungan.setLayout(PanelChartKeuntunganLayout);
        PanelChartKeuntunganLayout.setHorizontalGroup(
            PanelChartKeuntunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PanelChartKeuntunganLayout.setVerticalGroup(
            PanelChartKeuntunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        CbFilterKeuntungan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "4 Hari Terakhir", "4 Minggu Terakhir", "12 Bulan Terakhir" }));
        CbFilterKeuntungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbFilterKeuntunganActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LbTanggal))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addComponent(jSeparator6)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelChartKeuntungan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(0, 284, Short.MAX_VALUE)
                                .addComponent(CbFilterKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CbFilterPenjualan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PanelChartPenjualan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LbTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(215, 215, 215)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CbFilterPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CbFilterKeuntungan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(PanelChartPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(PanelChartKeuntungan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CbFilterPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbFilterPenjualanActionPerformed
       CbFilterPenjualan.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        String filter = CbFilterPenjualan.getSelectedItem().toString();
        tampilkanChartPenjualan(filter);
    }
        });
       
       
    }//GEN-LAST:event_CbFilterPenjualanActionPerformed

    private void CbFilterKeuntunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbFilterKeuntunganActionPerformed
        CbFilterKeuntungan.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        String filter = CbFilterKeuntungan.getSelectedItem().toString();
        tampilkanChartKeuntungan(filter);
    }
        });
    }//GEN-LAST:event_CbFilterKeuntunganActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbFilterKeuntungan;
    private javax.swing.JComboBox<String> CbFilterPenjualan;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JPanel PanelChartKeuntungan;
    private javax.swing.JPanel PanelChartPenjualan;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
