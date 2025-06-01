package Form;

import Panel.PanelLogin;
import Panel.PanelRegister;
import Panel.PanelTransaksiJual;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import kasir.main.Main;

/**
 *
 * @author USER
 */
public class FormMenuUtama extends javax.swing.JFrame {
        private final PanelLogin PLogin;
        private static FormMenuUtama app;
        private final Main mainForm;
        private final PanelRegister PRegister;
        private final PanelTransaksiJual PJual;
    
    public FormMenuUtama() {
        initComponents();
        setTitle("Koperasi Nuris");
        ImageIcon icon = new ImageIcon(getClass().getResource("/Icon/resnures.png"));
        setIconImage(icon.getImage());

        PLogin = new PanelLogin();
        PRegister = new PanelRegister();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(PLogin);
        mainForm = new Main();
        PJual = new PanelTransaksiJual();
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(950, 600));
        setMinimumSize(new java.awt.Dimension(950, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void showRegister(){
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.PRegister);
        app.PRegister.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.PRegister);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }
    
    public static void showLogin(){
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.PLogin);
        app.PRegister.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.PLogin);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }
    public static void main(String args[]) {
         FlatLaf.registerCustomDefaultsSource("Theme");
         FlatLightLaf.setup();
         
         java.awt.EventQueue.invokeLater(() -> {
         app = new FormMenuUtama();
         app.setVisible(true);
         });
    }
    public static void showForm(Component component){
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }
    public static void login(String levelStr){
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.mainForm);
        app.mainForm.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.getLevel(levelStr);
        setSelectedMenu(0,0);
        app.mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
     }
    
    public static void logout() {
    // Buat tombol custom dengan ikon
    Object[] options = {"Ya, Keluar", "Batal"};
    
    int confirm = JOptionPane.showOptionDialog(
        app,
        "Anda akan keluar dari aplikasi. Lanjutkan?",
        "Logout",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.WARNING_MESSAGE,
        null,
        options,
        options[1] // default ke Batal
    );
    
    if (confirm == 0) { // Ya, Keluar
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.PLogin);
        app.PLogin.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.PLogin);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }
}
    
    public static void setSelectedMenu(int index, int subMenu){
        app.mainForm.setSelectedMenu(index, subMenu);
    }

 }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
