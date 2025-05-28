package Panel;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;


public class PaginationTable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JPanel paginationPanel;
    private int rowsPerPage;
    private int currentPage = 1;
    private int totalPage = 1;
    private List<Object[]> allData = new ArrayList<>();

    public PaginationTable(String[] columnNames, int rowsPerPage) {
        setLayout(new BorderLayout());
        this.rowsPerPage = rowsPerPage;

        // Table setup
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Pagination panel
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(paginationPanel, BorderLayout.SOUTH);
    }

    // Load all data to table
    public void setData(List<Object[]> data) {
        this.allData = data;
        currentPage = 1;
        updateTable();
        updatePagination();
    }

    private void updateTable() {
        model.setRowCount(0);
        int start = (currentPage - 1) * rowsPerPage;
        int end = Math.min(start + rowsPerPage, allData.size());

        for (int i = start; i < end; i++) {
            model.addRow(allData.get(i));
        }
    }

    private void updatePagination() {
        paginationPanel.removeAll();
        totalPage = (int) Math.ceil(allData.size() / (double) rowsPerPage);

        JButton firstBtn = createNavButton("<<", () -> goToPage(1));
        JButton prevBtn = createNavButton("<", () -> goToPage(currentPage - 1));
        JButton nextBtn = createNavButton(">", () -> goToPage(currentPage + 1));
        JButton lastBtn = createNavButton(">>", () -> goToPage(totalPage));

        paginationPanel.add(firstBtn);
        paginationPanel.add(prevBtn);

        // Numbered buttons
        for (int i = 1; i <= totalPage; i++) {
            JButton pageBtn = new JButton(String.valueOf(i));
            if (i == currentPage) {
                pageBtn.setEnabled(false);
            }
            final int page = i;
            pageBtn.addActionListener(e -> goToPage(page));
            paginationPanel.add(pageBtn);
        }

        paginationPanel.add(nextBtn);
        paginationPanel.add(lastBtn);

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JButton createNavButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void goToPage(int page) {
        if (page >= 1 && page <= totalPage) {
            currentPage = page;
            updateTable();
            updatePagination();
        }
    }

    // Getter jika kamu perlu akses tabel-nya
    public JTable getTable() {
        return table;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
