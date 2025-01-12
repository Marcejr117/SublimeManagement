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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.LinkedList;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.util.List;
import javax.swing.JFrame;

public class SelectorProveedoresView extends JFrame
{
    public List<JTextField> tfList;
    public static ViewPrograma ventanaPadreViewPrograma;
    private JButton btnLimpiarCamposSelectorCliente;
    private JButton btnSeleccionarCliente;
    private JTextField fldApellidoSelectorCliente;
    private JTextField fldDNISelectorCliente;
    private JTextField fldNomberSelectorCliente;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JScrollPane jScrollPane1;
    private JTable tbSelectorCliente;
    
    public SelectorProveedoresView() {
        this.tfList = new LinkedList<JTextField>();
        this.initComponents();
        this.setListeners();
        this.actualizarTablaClienteSelector();
        final Image image = new ImageIcon(this.getClass().getResource("/res/windowIcon.png")).getImage();
        this.setIconImage(image);
    }
    
    private void initComponents() {
        this.jScrollPane1 = new JScrollPane();
        this.tbSelectorCliente = new JTable();
        this.btnSeleccionarCliente = new JButton();
        this.fldNomberSelectorCliente = new JTextField();
        this.fldApellidoSelectorCliente = new JTextField();
        this.fldDNISelectorCliente = new JTextField();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jLabel3 = new JLabel();
        this.btnLimpiarCamposSelectorCliente = new JButton();
        this.setDefaultCloseOperation(2);
        this.tbSelectorCliente.setAutoCreateRowSorter(true);
        this.tbSelectorCliente.setModel(new DefaultTableModel(new Object[0][], new String[] { "DNI/NIF", "Nombre", "Apellido" }) {
            boolean[] canEdit = { false, false, false };
            
            @Override
            public boolean isCellEditable(final int rowIndex, final int columnIndex) {
                return this.canEdit[columnIndex];
            }
        });
        this.tbSelectorCliente.getTableHeader().setReorderingAllowed(false);
        this.jScrollPane1.setViewportView(this.tbSelectorCliente);
        this.btnSeleccionarCliente.setText("Seleccionar");
        this.btnSeleccionarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                SelectorProveedoresView.this.btnSeleccionarClienteActionPerformed(evt);
            }
        });
        this.jLabel1.setText("DNI");
        this.jLabel2.setText("Nombre");
        this.jLabel3.setText("Apellido");
        this.btnLimpiarCamposSelectorCliente.setText("Limpiar");
        this.btnLimpiarCamposSelectorCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                SelectorProveedoresView.this.btnLimpiarCamposSelectorClienteActionPerformed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -1, 319, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.btnSeleccionarCliente, -1, -1, 32767).addComponent(this.fldApellidoSelectorCliente).addComponent(this.fldNomberSelectorCliente).addComponent(this.fldDNISelectorCliente).addComponent(this.jLabel1, -1, -1, 32767).addComponent(this.jLabel2, -1, -1, 32767).addComponent(this.jLabel3, GroupLayout.Alignment.TRAILING, -1, -1, 32767).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(119, 119, 119).addComponent(this.btnLimpiarCamposSelectorCliente, -1, 201, 32767))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1, -2, 0, 32767).addGroup(layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.fldDNISelectorCliente, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.fldNomberSelectorCliente, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel3).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.fldApellidoSelectorCliente, -2, -1, -2).addGap(18, 18, 18).addComponent(this.btnLimpiarCamposSelectorCliente).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 56, 32767).addComponent(this.btnSeleccionarCliente))).addContainerGap()));
        this.pack();
    }
    
    private void setListeners() {
        this.tfList.add(this.fldDNISelectorCliente);
        this.tfList.add(this.fldApellidoSelectorCliente);
        this.tfList.add(this.fldNomberSelectorCliente);
        for (final JTextField aux : this.tfList) {
            aux.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(final KeyEvent ke) {
                    SelectorProveedoresView.this.actualizarTablaClienteSelector();
                }
                
                @Override
                public void keyPressed(final KeyEvent ke) {
                }
                
                @Override
                public void keyReleased(final KeyEvent ke) {
                }
            });
        }
    }
    
    private void btnSeleccionarClienteActionPerformed(final ActionEvent evt) {
        try {
            final String ref = (String)this.tbSelectorCliente.getModel().getValueAt(this.tbSelectorCliente.getSelectedRow(), 0);
            SelectorProveedoresView.ventanaPadreViewPrograma.setRefAlbaran(ref);
            this.dispose();
        }
        catch (ArrayIndexOutOfBoundsException ex) {}
    }
    
    private void btnLimpiarCamposSelectorClienteActionPerformed(final ActionEvent evt) {
        for (final JTextField aux : this.tfList) {
            aux.setText("");
        }
        this.actualizarTablaClienteSelector();
    }
    
    private void actualizarTablaClienteSelector() {
        final DefaultTableModel dtm = Manager.limpiarDefaultTableModel((DefaultTableModel)this.tbSelectorCliente.getModel());
        final LinkedList<Cliente> clientes = Clientes.getClientes();
        final String dni = this.fldDNISelectorCliente.getText().toLowerCase();
        final String nombre = this.fldNomberSelectorCliente.getText().toLowerCase();
        final String apellido = this.fldApellidoSelectorCliente.getText().toLowerCase();
        final List<String[]> listaDatos = new LinkedList<String[]>();
        for (final Cliente aux : clientes) {
            if (aux.isProveedor() && aux.getDni().toLowerCase().matches(".*" + dni + ".*") && aux.getNombre().toLowerCase().matches(".*" + nombre + ".*") && aux.getApellido().toLowerCase().matches(".*" + apellido + ".*")) {
                final String[] datos = { aux.getDni(), aux.getNombre(), aux.getApellido() };
                listaDatos.add(datos);
            }
        }
        for (final String[] aux2 : listaDatos) {
            dtm.addRow(aux2);
        }
        this.tbSelectorCliente.setModel(dtm);
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
            Logger.getLogger(SelectorProveedoresView.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(SelectorProveedoresView.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(SelectorProveedoresView.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(SelectorProveedoresView.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SelectorProveedoresView().setVisible(true);
            }
        });
    }
}
