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


public class PanelDashboard extends javax.swing.JPanel {

    
    public PanelDashboard() {
        initComponents();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        LbTanggal.setText(LocalDate.now().format(dtf));
        CbFilter.setSelectedIndex(0);
        tampilkanChartPenjualan("7 Hari Terakhir");
        
    }
    private void tampilkanChartPenjualan(String filter) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String sql = "";

    switch (filter) {
        case "7 Hari Terakhir":
            sql = "SELECT DATE(TanggalJual) AS Label, SUM(TotalHarga) AS Total " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 7 DAY " +
                  "GROUP BY DATE(TanggalJual) ORDER BY TanggalJual";
            break;

        case "4 Minggu Terakhir":
            sql = "SELECT YEARWEEK(TanggalJual, 1) AS Label, SUM(TotalHarga) AS Total " +
                  "FROM transaksi_jual " +
                  "WHERE TanggalJual >= CURDATE() - INTERVAL 4 WEEK " +
                  "GROUP BY YEARWEEK(TanggalJual, 1)";
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

        PanelChart.removeAll();
        PanelChart.setLayout(new BorderLayout());
        PanelChart.add(chartPanel, BorderLayout.CENTER);
        PanelChart.revalidate();
        PanelChart.repaint();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan chart: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        PanelChart = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        LbTanggal = new javax.swing.JLabel();
        CbFilter = new javax.swing.JComboBox<>();

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        PanelChart.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout PanelChartLayout = new javax.swing.GroupLayout(PanelChart);
        PanelChart.setLayout(PanelChartLayout);
        PanelChartLayout.setHorizontalGroup(
            PanelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PanelChartLayout.setVerticalGroup(
            PanelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jLabel17.setText("Admin > Dashboard");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setText("Dashboard");

        LbTanggal.setText("Hari Tanggal");

        CbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7 Hari Terakhir", "4 Minggu Terakhir" }));
        CbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addComponent(PanelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 583, Short.MAX_VALUE)
                        .addComponent(LbTanggal))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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
                .addGap(75, 75, 75)
                .addComponent(CbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(136, 136, 136))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbFilterActionPerformed
       CbFilter.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        String filter = CbFilter.getSelectedItem().toString();
        tampilkanChartPenjualan(filter);
    }
        });

    }//GEN-LAST:event_CbFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbFilter;
    private javax.swing.JLabel LbTanggal;
    private javax.swing.JPanel PanelChart;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
