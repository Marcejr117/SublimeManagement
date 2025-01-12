// 
// Decompiled by Procyon v0.5.36
// 

package Sublime_Management;

import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JFrame;

public class LoginView extends JFrame
{
    private JButton btnAcceder;
    private JButton btnCambiarContra;
    private JCheckBox chMostrarPass;
    private JPasswordField fldPass;
    private JTextField fldUsuario;
    private JLabel jLabel1;
    private JLabel jLabel2;
    public Inicio inicio;
    public PasswordRecoveryView passwordRecoveryView;
    
    public LoginView() {
        SqlManager.isLoggin = true;
        SqlManager.ajustarDatosBDD();
        SqlManager.cargarDeLocal2();
        SqlManager.db.close();
        this.initComponents();
        this.setLocationRelativeTo(null);
        final Image image = new ImageIcon(this.getClass().getResource("/res/windowIcon.png")).getImage();
        this.setIconImage(image);
        System.out.println("Numero: " + Usuarios.getUsuarios().size());
        final char passChar = this.fldPass.getEchoChar();
        this.chMostrarPass.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent ce) {
                if (LoginView.this.chMostrarPass.isSelected()) {
                    LoginView.this.fldPass.setEchoChar('\0');
                }
                else {
                    LoginView.this.fldPass.setEchoChar(passChar);
                }
            }
        });
    }
    
    private void initComponents() {
        this.btnAcceder = new JButton();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.fldUsuario = new JTextField();
        this.btnCambiarContra = new JButton();
        this.fldPass = new JPasswordField();
        this.chMostrarPass = new JCheckBox();
        this.setDefaultCloseOperation(3);
        this.setTitle("Login SublimeManagement");
        this.setResizable(false);
        this.btnAcceder.setText("Acceder");
        this.btnAcceder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                LoginView.this.btnAccederActionPerformed(evt);
            }
        });
        this.jLabel1.setText("Correo");
        this.jLabel2.setText("Contrase\u00f1a");
        this.btnCambiarContra.setText("He olvidad la contrase\u00f1a");
        this.btnCambiarContra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                LoginView.this.btnCambiarContraActionPerformed(evt);
            }
        });
        this.fldPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                LoginView.this.fldPassKeyPressed(evt);
            }
        });
        this.chMostrarPass.setText("Mostrar");
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel2, -1, 77, 32767).addComponent(this.jLabel1, -1, -1, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.fldPass).addComponent(this.fldUsuario))).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.btnCambiarContra).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 24, 32767).addComponent(this.btnAcceder)).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.chMostrarPass))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.fldUsuario, -2, -1, -2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.fldPass, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.chMostrarPass).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.btnAcceder).addComponent(this.btnCambiarContra)).addContainerGap(-1, 32767)));
        this.pack();
    }
    
    public void EliminarPasswordRecoveryView() {
        this.passwordRecoveryView = null;
    }
    
    private void btnCambiarContraActionPerformed(final ActionEvent evt) {
        if (this.passwordRecoveryView == null) {
            (this.passwordRecoveryView = new PasswordRecoveryView()).setVisible(true);
            this.setVisible(false);
            this.passwordRecoveryView.setLoginView(this);
        }
        else {
            JOptionPane.showMessageDialog(this, "Ya esta habierto est Ventana", "Error", 0);
        }
    }
    
    private void acceder() {
        boolean acceso = false;
        try {
            System.out.println("Vamos a loggear");
            for (final Usuario aux : Usuarios.getUsuarios()) {
                System.out.println("credenciales a comprobar: " + this.fldPass.getText());
                if (aux.getCorreo().equals(this.fldUsuario.getText()) && aux.getPassword().equals(this.fldPass.getText())) {
                    if (aux.changePassword) {
                        final String nPass = JOptionPane.showInputDialog(this, "Introduce una nueva contrase\u00f1a ", "Nueva Contrase\u00f1a", 1);
                        aux.setPassword(nPass);
                        aux.changePassword = false;
                        SqlManager.guardarEnLocal();
                        SqlManager.db.close();
                    }
                    acceso = true;
                }
            }
            if (!acceso) {
                throw new Exception("No hay usuarios con estos credenciales");
            }
            if (this.inicio == null) {
                (this.inicio = new Inicio()).cambiarlbNombreUsuario(this.fldUsuario.getText());
                this.inicio.setVisible(true);
                this.setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(this, "Ya esta abierta esta Ventana", "Error", 0);
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 2);
        }
    }
    
    private void btnAccederActionPerformed(final ActionEvent evt) {
        this.acceder();
    }
    
    private void fldPassKeyPressed(final KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            this.acceder();
        }
    }
    
    public static void main(final String[] args) {
        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Config_Management.AplicarTema(Config_Management.ObtenerDatosConfig().getTema());
                new LoginView().setVisible(true);
            }
        });
    }
}
