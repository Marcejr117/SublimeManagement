// 
// Decompiled by Procyon v0.5.36
// 

package Sublime_Management;

import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import java.awt.Component;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;

public class CrearUsuario extends JFrame
{
    private JButton btnCrearUsuario;
    private JTextField fldCorreoUsuario;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel lbCorreo;
    private JLabel lbpass;
    private JPasswordField pfldContra1;
    private JPasswordField pfldContra2;
    private ViewConfig viewConfig;
    
    public CrearUsuario() {
        this.initComponents();
        this.setLocationRelativeTo(null);
        final Image image = new ImageIcon(this.getClass().getResource("/res/windowIcon.png")).getImage();
        this.setIconImage(image);
        this.setDefaultCloseOperation(2);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(final WindowEvent we) {
            }
            
            @Override
            public void windowClosing(final WindowEvent we) {
            }
            
            @Override
            public void windowClosed(final WindowEvent we) {
                CrearUsuario.this.viewConfig.limpiarCrearUsuario();
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
        this.jLabel1 = new JLabel();
        this.fldCorreoUsuario = new JTextField();
        this.lbCorreo = new JLabel();
        this.jLabel3 = new JLabel();
        this.lbpass = new JLabel();
        this.btnCrearUsuario = new JButton();
        this.pfldContra1 = new JPasswordField();
        this.pfldContra2 = new JPasswordField();
        this.setDefaultCloseOperation(2);
        this.setTitle("Crear Nuevo Usuario");
        this.jLabel1.setText("Correo Electronico");
        this.jLabel3.setText("Contrase\u00f1a");
        this.btnCrearUsuario.setText("Crear Usuario");
        this.btnCrearUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                CrearUsuario.this.btnCrearUsuarioActionPerformed(evt);
            }
        });
        this.pfldContra2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                CrearUsuario.this.pfldContra2KeyPressed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.jLabel3).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.lbCorreo, -2, 24, -2)).addComponent(this.fldCorreoUsuario).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(0, 0, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.lbpass, GroupLayout.Alignment.TRAILING, -2, 24, -2).addComponent(this.btnCrearUsuario, GroupLayout.Alignment.TRAILING))).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addGap(0, 0, 32767)).addComponent(this.pfldContra1).addComponent(this.pfldContra2, -1, 415, 32767)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.fldCorreoUsuario, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.lbCorreo, -2, 24, -2).addComponent(this.jLabel3, GroupLayout.Alignment.TRAILING)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.pfldContra1, -2, -1, -2).addGap(3, 3, 3).addComponent(this.pfldContra2, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.lbpass, -2, 24, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.btnCrearUsuario).addContainerGap(-1, 32767)));
        this.pack();
    }
    
    public boolean ValidarCorreo(final String correo) {
        final String regx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        final Pattern pattern = Pattern.compile(regx);
        final Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }
    
    public void CrearUsuario() {
        boolean correcto = true;
        if (!this.ValidarCorreo(this.fldCorreoUsuario.getText())) {
            correcto = false;
            JOptionPane.showMessageDialog(this, "El Correo no es Valido", "Aviso", 2);
            final ImageIcon image = new ImageIcon(this.getClass().getResource("/res/remove.png"));
            this.lbCorreo.setIcon(image);
            this.lbCorreo.setToolTipText("El correo no cumple con los requisitos\nEjemplo: Examen@gmail.com");
        }
        else {
            final ImageIcon image = new ImageIcon(this.getClass().getResource("/res/checked.png"));
            this.lbCorreo.setIcon(image);
            this.lbCorreo.setToolTipText("El correo cumple con los requisitos");
        }
        if (!this.pfldContra1.getText().equals(this.pfldContra2.getText())) {
            correcto = false;
            JOptionPane.showMessageDialog(this, "Las Contrase\u00f1as no coinciden", "Aviso", 2);
            final ImageIcon image = new ImageIcon(this.getClass().getResource("/res/remove.png"));
            this.lbpass.setIcon(image);
            this.lbpass.setToolTipText("Las Contrase\u00f1as no coinciden");
        }
        else {
            final ImageIcon image = new ImageIcon(this.getClass().getResource("/res/checked.png"));
            this.lbpass.setIcon(image);
            this.lbpass.setToolTipText("La Contrase\u00f1a cumple con los requisitos");
        }
        if (correcto) {
            Usuarios.getUsuarios().add(new Usuario(this.fldCorreoUsuario.getText(), this.pfldContra1.getText()));
            SqlManager.isLoggin = true;
            SqlManager.guardarEnLocal();
            SqlManager.db.close();
            JOptionPane.showMessageDialog(this, "Usuario Creado con exito", "Informacion", 1);
        }
    }
    
    private void btnCrearUsuarioActionPerformed(final ActionEvent evt) {
        this.CrearUsuario();
        this.viewConfig.actualizarTablaUsuarios();
        this.dispose();
    }
    
    private void pfldContra2KeyPressed(final KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            this.CrearUsuario();
            this.viewConfig.actualizarTablaUsuarios();
        }
    }
    
    public void setViewConfig(final ViewConfig config) {
        this.viewConfig = config;
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
            Logger.getLogger(CrearUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(CrearUsuario.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(CrearUsuario.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(CrearUsuario.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CrearUsuario().setVisible(true);
            }
        });
    }
}
