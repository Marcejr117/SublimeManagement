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
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PasswordRecoveryView extends JFrame
{
    private JButton btnGuardarRecuperacion;
    private JTextField fldCorreoRecuperacion;
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel3;
    public LoginView loginView;
    private boolean cambiado;
    
    public PasswordRecoveryView() {
        this.cambiado = false;
        this.initComponents();
        this.setLocationRelativeTo(null);
        final Image image = new ImageIcon(this.getClass().getResource("/res/windowIcon.png")).getImage();
        this.setIconImage(image);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(final WindowEvent we) {
            }
            
            @Override
            public void windowClosing(final WindowEvent we) {
            }
            
            @Override
            public void windowClosed(final WindowEvent we) {
                PasswordRecoveryView.this.loginView.setVisible(true);
                if (!PasswordRecoveryView.this.cambiado) {
                    JOptionPane.showMessageDialog(null, "No se cambio la contrase\u00f1a", "Informacion", 1);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Se cambio la contrase\u00f1a", "Informacion", 1);
                }
            }
            
            @Override
            public void windowIconified(final WindowEvent we) {
            }
            
            @Override
            public void windowDeiconified(final WindowEvent we) {
            }
            
            @Override
            public void windowActivated(final WindowEvent we) {
            }
            
            @Override
            public void windowDeactivated(final WindowEvent we) {
            }
        });
    }
    
    private void initComponents() {
        this.fldCorreoRecuperacion = new JTextField();
        this.jLabel1 = new JLabel();
        this.jLabel3 = new JLabel();
        this.btnGuardarRecuperacion = new JButton();
        this.jButton1 = new JButton();
        this.setDefaultCloseOperation(2);
        this.setTitle("Recuperacion Contrase\u00f1a");
        this.setUndecorated(true);
        this.setResizable(false);
        this.jLabel1.setText("Correo Electronico de la cuenta a recuperar");
        this.jLabel3.setPreferredSize(new Dimension(24, 24));
        this.btnGuardarRecuperacion.setText("Enviar Contrase\u00f1a");
        this.btnGuardarRecuperacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                PasswordRecoveryView.this.btnGuardarRecuperacionActionPerformed(evt);
            }
        });
        this.jButton1.setText("Salir");
        this.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                PasswordRecoveryView.this.jButton1ActionPerformed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel1, -1, 367, 32767).addComponent(this.fldCorreoRecuperacion, -1, 367, 32767).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.jButton1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.jLabel3, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnGuardarRecuperacion)))));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.fldCorreoRecuperacion, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel3, -2, -1, -2).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButton1).addComponent(this.btnGuardarRecuperacion))).addContainerGap(-1, 32767)));
        this.pack();
    }
    
    private void btnGuardarRecuperacionActionPerformed(final ActionEvent evt) {
        boolean coincidencia = false;
        for (final Usuario aux : Usuarios.getUsuarios()) {
            if (aux.getCorreo().equals(this.fldCorreoRecuperacion.getText())) {
                final String nuevaPass = Manager.getStrongPassword();
                aux.changePassword = true;
                aux.setPassword(nuevaPass);
                System.out.println("Temporal pass: " + nuevaPass);
                this.cambiado = true;
                this.dispose();
                this.loginView.setVisible(true);
                this.loginView.EliminarPasswordRecoveryView();
                SqlManager.guardarEnLocal();
                SqlManager.db.close();
                System.out.println("Mandamos Correo a: " + this.fldCorreoRecuperacion.getText());
                EmailManager.SendEmailRecoverPass(this.fldCorreoRecuperacion.getText(), nuevaPass);
                coincidencia = true;
                break;
            }
        }
        if (!coincidencia) {
            JOptionPane.showMessageDialog(this, "No hay ningun usuarios con este correo", "Error", 0);
        }
    }
    
    private void jButton1ActionPerformed(final ActionEvent evt) {
        this.dispose();
        this.loginView.EliminarPasswordRecoveryView();
    }
    
    public LoginView getLoginView() {
        return this.loginView;
    }
    
    public void setLoginView(final LoginView loginView) {
        this.loginView = loginView;
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
            Logger.getLogger(PasswordRecoveryView.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(PasswordRecoveryView.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(PasswordRecoveryView.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(PasswordRecoveryView.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Config_Management.AplicarTema(Config_Management.ObtenerDatosConfig().getTema());
                new PasswordRecoveryView().setVisible(true);
            }
        });
    }
}
