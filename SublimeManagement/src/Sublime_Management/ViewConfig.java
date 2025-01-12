/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sublime_Management;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Marce
 */
public class ViewConfig extends javax.swing.JFrame {

    public List<JToggleButton> listaToggleButtons = new ArrayList<JToggleButton>();
    public List<JPanel> listaJPanel = new ArrayList<JPanel>();
    public static DataType_Config configActual;
    public static Inicio ventanaPadreInicio;
    public static ViewPrograma ventanaPadreViewPrograma;

    public ViewConfig() {
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        Image image = new ImageIcon(getClass().getResource("/res/windowIcon.png")).getImage();
        this.setIconImage(image);
        cargarListaPaneles();
        cargarListaToggleButtons();
        unSelectAllToggleButtons();
        hideAllPanel();
        setLocationRelativeTo(null);
        CargarDatosConfig();
        agregarListeners();

    }

    public void agregarListeners() {
        char passChar = pfPassBDD.getEchoChar();

        cbTema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Item seleccionado: " + cbTema.getSelectedIndex());
                Config_Management.AplicarTema(cbTema.getSelectedIndex());
            }
        });

        if (chHabilitarBDDOnline.isSelected()) {
            pnDatosConexion.setVisible(true);
        } else {
            pnDatosConexion.setVisible(false);
        }
        chHabilitarBDDOnline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (chHabilitarBDDOnline.isSelected()) {
                    pnDatosConexion.setVisible(true);
                    //System.out.println("habilitado panel");
                } else {
                    pnDatosConexion.setVisible(false);
                    //System.out.println("Deshabilitado panel");
                }
            }
        });
        chMostrarPass.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {

                if (chMostrarPass.isSelected()) {
                    pfPassBDD.setEchoChar((char) 0);
                } else {

                    pfPassBDD.setEchoChar(passChar);
                }
            }
        });
        //show and hide the data shop panel
        if (cbHabilitarDatosEmp.isSelected()) {
            pnDatosEmpresaInterno.setVisible(true);
            lbIconoMuesta.setVisible(true);
        } else {
            pnDatosEmpresaInterno.setVisible(false);
            lbIconoMuesta.setVisible(false);
        }
        cbHabilitarDatosEmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (cbHabilitarDatosEmp.isSelected()) {
                    pnDatosEmpresaInterno.setVisible(true);
                    lbIconoMuesta.setVisible(true);
                } else {
                    pnDatosEmpresaInterno.setVisible(false);
                    lbIconoMuesta.setVisible(false);
                }
            }
        });

    }

    public void CargarDatosConfig() {
        configActual = Config_Management.ObtenerDatosConfig();
        AplicarConfiguracionActual();
    }

    public void Recolectar_GuardarDatos() {
        //Configuracion de base de datos
        configActual.setHabilitarBDDLocal(chHabilitarBDDLocal.isSelected());
        configActual.setHabilitarBDDOnline(chHabilitarBDDOnline.isSelected());
        configActual.setDireccionBDD(tfDireccionBDD.getText());
        configActual.setNombreBDD(tfNombreBDD.getText());
        configActual.setPuertoBDD(tfPuertoBDD.getText());
        configActual.setUsuarioBDD(tfUsuarioBDD.getText());
        configActual.setPassBDD(String.valueOf(pfPassBDD.getPassword()));

        //configuraciones de apariencia
        configActual.setTema(cbTema.getSelectedIndex());

        //configurar datos empresa
        configActual.setHabilitarDatosEmp(cbHabilitarDatosEmp.isSelected());
        configActual.setNombreEmp(tfNombreEmp.getText());
        configActual.setDireccionEmp(tfDireccionEmp.getText());
        configActual.setTefEmp(tfTelEmp.getText());
        configActual.setEmailEmp(tfEmailEmp.getText());
        configActual.setLogo(new File(lbDireccionLogo.getText()));
        System.out.println("actual logo: " + configActual.getLogo());
        Config_Management.GuardarDatosConfig(configActual);
    }

    public void AplicarConfiguracionActual() {
        if (configActual != new DataType_Config()) {
            //Configuracion de base de datos
            chHabilitarBDDLocal.setSelected(configActual.getHabilitarBDDLocal());
            chHabilitarBDDOnline.setSelected(configActual.getHabilitarBDDOnline());
            tfDireccionBDD.setText(configActual.getDireccionBDD());
            tfNombreBDD.setText(configActual.getNombreBDD());
            tfPuertoBDD.setText(configActual.getPuertoBDD());
            tfUsuarioBDD.setText(configActual.getUsuarioBDD());
            pfPassBDD.setText(configActual.getPassBDD());
            SqlManager.ajustarDatosBDD();
            //aplicando la apariencia
            cbTema.setSelectedIndex(configActual.getTema());
            //aplicando datos empresa
            cbHabilitarDatosEmp.setSelected(configActual.getHabilitarDatosEmp());
            tfNombreEmp.setText(configActual.getNombreEmp());
            tfDireccionEmp.setText(configActual.getDireccionEmp());
            tfTelEmp.setText(configActual.getTefEmp());
            tfEmailEmp.setText(configActual.getEmailEmp());
            try {
                if (cbHabilitarDatosEmp.isSelected()) {
                    lbDireccionLogo.setText(configActual.getLogo().toString());
                    ImageIcon icon = new ImageIcon(lbDireccionLogo.getText());
                    Image foto = icon.getImage().getScaledInstance(lbIconoMuesta.getHeight(), lbIconoMuesta.getWidth(), 2);
                    lbIconoMuesta.setIcon(new ImageIcon(foto));
                } else {
                    lbDireccionLogo.setText("");
                    //ImageIcon icon = new ImageIcon(null);
                    //Image foto = icon.getImage().getScaledInstance(lbIconoMuesta.getHeight(), lbIconoMuesta.getWidth(), 2);
                    lbIconoMuesta.setIcon(null);
                }

            } catch (java.lang.NullPointerException ex) {
                lbDireccionLogo.setText("");
            }

        }

    }

    public void cargarListaToggleButtons() {
        listaToggleButtons.add(btnApariencia);
        listaToggleButtons.add(btnBaseDatos);
        listaToggleButtons.add(btnCuentasCorreo);
        listaToggleButtons.add(btnDatosEmpresa);
        listaToggleButtons.add(btnUsuario);
        listaToggleButtons.add(btnLenguaje);
    }

    public void cargarListaPaneles() {
        listaJPanel.add(pnBaseDatos);
        listaJPanel.add(pnApariencia);
        listaJPanel.add(pnDatosEmpresa);
        listaJPanel.add(pnLenguaje);
        listaJPanel.add(pnCuentasCorreo);
        listaJPanel.add(pnUsuarios);
    }

    public void unSelectAllToggleButtons() {
        for (JToggleButton aux : listaToggleButtons) {
            aux.setSelected(false);
        }
    }

    public void hideAllPanel() {
        for (JPanel aux : listaJPanel) {
            aux.setVisible(false);
        }
    }

    ;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnBotones = new javax.swing.JPanel();
        btnBaseDatos = new javax.swing.JToggleButton();
        btnApariencia = new javax.swing.JToggleButton();
        btnLenguaje = new javax.swing.JToggleButton();
        btnCuentasCorreo = new javax.swing.JToggleButton();
        btnDatosEmpresa = new javax.swing.JToggleButton();
        btnUsuario = new javax.swing.JToggleButton();
        pnDatos = new javax.swing.JPanel();
        pnBaseDatos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        chHabilitarBDDLocal = new javax.swing.JCheckBox();
        chHabilitarBDDOnline = new javax.swing.JCheckBox();
        pnDatosConexion = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfDireccionBDD = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfNombreBDD = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfPuertoBDD = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfUsuarioBDD = new javax.swing.JTextField();
        JLabelContra = new javax.swing.JLabel();
        pfPassBDD = new javax.swing.JPasswordField();
        btnProbarConexion = new javax.swing.JButton();
        chMostrarPass = new javax.swing.JCheckBox();
        btnAplicarBDD = new javax.swing.JButton();
        pnApariencia = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbTema = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnAplicarTema = new javax.swing.JButton();
        pnLenguaje = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbIdioma = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        btnAplicarIdioma = new javax.swing.JButton();
        pnCuentasCorreo = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCorreos = new javax.swing.JTable();
        btnAgregarCorreo = new javax.swing.JButton();
        btnEliminarCorreo = new javax.swing.JButton();
        btnAplicarCorreo = new javax.swing.JButton();
        pnDatosEmpresa = new javax.swing.JPanel();
        pnDatosEmpresaInterno = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        tfNombreEmp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfDireccionEmp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tfTelEmp = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tfEmailEmp = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnElegirLogo = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        lbDireccionLogo = new javax.swing.JLabel();
        cbHabilitarDatosEmp = new javax.swing.JCheckBox();
        btnAplicarEmp = new javax.swing.JButton();
        lbIconoMuesta = new javax.swing.JLabel();
        pnUsuarios = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        tbAgregarUsuario = new javax.swing.JButton();
        tbEliminarUsuario = new javax.swing.JButton();
        btnCargarUsuarios = new javax.swing.JButton();
        btnEnviarContraUsuario = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuracion SublimeManagement");
        setResizable(false);

        pnBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnBotones.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pnBotones.setLayout(new java.awt.GridLayout(10, 0, 0, 5));

        btnBaseDatos.setText("Base de Datos");
        btnBaseDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaseDatosActionPerformed(evt);
            }
        });
        pnBotones.add(btnBaseDatos);

        btnApariencia.setText("Apariencia");
        btnApariencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAparienciaActionPerformed(evt);
            }
        });
        pnBotones.add(btnApariencia);

        btnLenguaje.setText("Lenguaje");
        btnLenguaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLenguajeActionPerformed(evt);
            }
        });
        pnBotones.add(btnLenguaje);

        btnCuentasCorreo.setText("Cuentas de Correo");
        btnCuentasCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCuentasCorreoActionPerformed(evt);
            }
        });
        pnBotones.add(btnCuentasCorreo);

        btnDatosEmpresa.setText("Datos Empresa");
        btnDatosEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosEmpresaActionPerformed(evt);
            }
        });
        pnBotones.add(btnDatosEmpresa);

        btnUsuario.setText("Usuarios");
        btnUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuarioActionPerformed(evt);
            }
        });
        pnBotones.add(btnUsuario);

        pnDatos.setLayout(new javax.swing.OverlayLayout(pnDatos));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Habilitar base de datos local:");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Habilitar base de datos Online:");

        chHabilitarBDDLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chHabilitarBDDLocalActionPerformed(evt);
            }
        });

        chHabilitarBDDOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chHabilitarBDDOnlineActionPerformed(evt);
            }
        });

        pnDatosConexion.setLayout(new java.awt.GridLayout(6, 2, -200, 2));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Dirección");
        pnDatosConexion.add(jLabel3);
        pnDatosConexion.add(tfDireccionBDD);

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("Nombre Base Datos");
        pnDatosConexion.add(jLabel6);
        pnDatosConexion.add(tfNombreBDD);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Puerto");
        pnDatosConexion.add(jLabel4);
        pnDatosConexion.add(tfPuertoBDD);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Usuario");
        pnDatosConexion.add(jLabel5);
        pnDatosConexion.add(tfUsuarioBDD);

        JLabelContra.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        JLabelContra.setText("Contraseña");
        pnDatosConexion.add(JLabelContra);
        pnDatosConexion.add(pfPassBDD);

        btnProbarConexion.setText("Probar Conexión");
        btnProbarConexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProbarConexionActionPerformed(evt);
            }
        });
        pnDatosConexion.add(btnProbarConexion);

        chMostrarPass.setText("Mostrar");
        chMostrarPass.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chMostrarPass.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        pnDatosConexion.add(chMostrarPass);

        btnAplicarBDD.setText("Aplicar");
        btnAplicarBDD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarBDDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBaseDatosLayout = new javax.swing.GroupLayout(pnBaseDatos);
        pnBaseDatos.setLayout(pnBaseDatosLayout);
        pnBaseDatosLayout.setHorizontalGroup(
            pnBaseDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBaseDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnBaseDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnDatosConexion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnBaseDatosLayout.createSequentialGroup()
                        .addGroup(pnBaseDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnBaseDatosLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chHabilitarBDDLocal))
                            .addGroup(pnBaseDatosLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chHabilitarBDDOnline)))
                        .addGap(0, 333, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBaseDatosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAplicarBDD)))
                .addContainerGap())
        );
        pnBaseDatosLayout.setVerticalGroup(
            pnBaseDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBaseDatosLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnBaseDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chHabilitarBDDLocal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnBaseDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chHabilitarBDDOnline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(pnDatosConexion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(btnAplicarBDD)
                .addContainerGap())
        );

        pnDatos.add(pnBaseDatos);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Tema");
        jPanel2.add(jLabel7);

        cbTema.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FlatArcDark", "FlatArcDarkOrange", "FlatDarkFlat", "FlatLightLaf", "FlatOneDark", "FlatMoonlightContrast", "FlatMonokaiPro", "FlatMaterialDeepOceanContrast" }));
        cbTema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTemaActionPerformed(evt);
            }
        });
        jPanel2.add(cbTema);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        btnAplicarTema.setText("Aplicar");
        btnAplicarTema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarTemaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnAparienciaLayout = new javax.swing.GroupLayout(pnApariencia);
        pnApariencia.setLayout(pnAparienciaLayout);
        pnAparienciaLayout.setHorizontalGroup(
            pnAparienciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAparienciaLayout.createSequentialGroup()
                .addGroup(pnAparienciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnAparienciaLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAplicarTema))
                    .addGroup(pnAparienciaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnAparienciaLayout.setVerticalGroup(
            pnAparienciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnAparienciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)
                .addGroup(pnAparienciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarTema, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pnDatos.add(pnApariencia);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Idioma");
        jPanel3.add(jLabel8);

        cbIdioma.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Español", "Alemán", "Italiano", "Inglés" }));
        cbIdioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbIdiomaActionPerformed(evt);
            }
        });
        jPanel3.add(cbIdioma);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        btnAplicarIdioma.setText("Aplicar");

        javax.swing.GroupLayout pnLenguajeLayout = new javax.swing.GroupLayout(pnLenguaje);
        pnLenguaje.setLayout(pnLenguajeLayout);
        pnLenguajeLayout.setHorizontalGroup(
            pnLenguajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLenguajeLayout.createSequentialGroup()
                .addGroup(pnLenguajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnLenguajeLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAplicarIdioma))
                    .addGroup(pnLenguajeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnLenguajeLayout.setVerticalGroup(
            pnLenguajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnLenguajeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)
                .addGroup(pnLenguajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarIdioma, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pnDatos.add(pnLenguaje);

        tbCorreos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Correos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCorreos.setColumnSelectionAllowed(true);
        tbCorreos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbCorreos);
        tbCorreos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tbCorreos.getColumnModel().getColumnCount() > 0) {
            tbCorreos.getColumnModel().getColumn(0).setResizable(false);
        }

        btnAgregarCorreo.setText("+");
        btnAgregarCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCorreoActionPerformed(evt);
            }
        });

        btnEliminarCorreo.setText("-");
        btnEliminarCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCorreoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnAgregarCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregarCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnAplicarCorreo.setText("Aplicar");
        btnAplicarCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarCorreoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnCuentasCorreoLayout = new javax.swing.GroupLayout(pnCuentasCorreo);
        pnCuentasCorreo.setLayout(pnCuentasCorreoLayout);
        pnCuentasCorreoLayout.setHorizontalGroup(
            pnCuentasCorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCuentasCorreoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnCuentasCorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnCuentasCorreoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAplicarCorreo)))
                .addContainerGap())
        );
        pnCuentasCorreoLayout.setVerticalGroup(
            pnCuentasCorreoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCuentasCorreoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(btnAplicarCorreo)
                .addContainerGap())
        );

        pnDatos.add(pnCuentasCorreo);

        pnDatosEmpresaInterno.setLayout(new java.awt.GridLayout(6, 2, -200, 1));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Nombre Empresa");
        pnDatosEmpresaInterno.add(jLabel9);
        pnDatosEmpresaInterno.add(tfNombreEmp);

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Direccion");
        pnDatosEmpresaInterno.add(jLabel10);
        pnDatosEmpresaInterno.add(tfDireccionEmp);

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("Telefono");
        pnDatosEmpresaInterno.add(jLabel11);
        pnDatosEmpresaInterno.add(tfTelEmp);

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setText("E-mail");
        pnDatosEmpresaInterno.add(jLabel12);
        pnDatosEmpresaInterno.add(tfEmailEmp);

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setText("Logo");
        pnDatosEmpresaInterno.add(jLabel13);

        btnElegirLogo.setText("Elegir Icono");
        btnElegirLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElegirLogoActionPerformed(evt);
            }
        });
        pnDatosEmpresaInterno.add(btnElegirLogo);
        pnDatosEmpresaInterno.add(jLabel14);
        pnDatosEmpresaInterno.add(lbDireccionLogo);

        cbHabilitarDatosEmp.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cbHabilitarDatosEmp.setText("Habilitar datos de Empresa");
        cbHabilitarDatosEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbHabilitarDatosEmpActionPerformed(evt);
            }
        });

        btnAplicarEmp.setText("Aplicar");
        btnAplicarEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarEmpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnDatosEmpresaLayout = new javax.swing.GroupLayout(pnDatosEmpresa);
        pnDatosEmpresa.setLayout(pnDatosEmpresaLayout);
        pnDatosEmpresaLayout.setHorizontalGroup(
            pnDatosEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDatosEmpresaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDatosEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnDatosEmpresaInterno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnDatosEmpresaLayout.createSequentialGroup()
                        .addComponent(cbHabilitarDatosEmp)
                        .addGap(0, 525, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDatosEmpresaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnDatosEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAplicarEmp, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbIconoMuesta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pnDatosEmpresaLayout.setVerticalGroup(
            pnDatosEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDatosEmpresaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbHabilitarDatosEmp)
                .addGap(84, 84, 84)
                .addComponent(pnDatosEmpresaInterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbIconoMuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAplicarEmp)
                .addContainerGap())
        );

        pnDatos.add(pnDatosEmpresa);

        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Contraseña"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbUsuarios.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbUsuarios);
        if (tbUsuarios.getColumnModel().getColumnCount() > 0) {
            tbUsuarios.getColumnModel().getColumn(0).setResizable(false);
            tbUsuarios.getColumnModel().getColumn(1).setResizable(false);
            tbUsuarios.getColumnModel().getColumn(2).setResizable(false);
        }

        tbAgregarUsuario.setText("+");
        tbAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbAgregarUsuarioActionPerformed(evt);
            }
        });

        tbEliminarUsuario.setText("-");
        tbEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbEliminarUsuarioActionPerformed(evt);
            }
        });

        btnCargarUsuarios.setText("Cargar Usuarios");
        btnCargarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarUsuariosActionPerformed(evt);
            }
        });

        btnEnviarContraUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/eye.png"))); // NOI18N
        btnEnviarContraUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarContraUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(tbAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tbEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEnviarContraUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCargarUsuarios)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tbAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tbEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnEnviarContraUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCargarUsuarios)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnUsuariosLayout = new javax.swing.GroupLayout(pnUsuarios);
        pnUsuarios.setLayout(pnUsuariosLayout);
        pnUsuariosLayout.setHorizontalGroup(
            pnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnUsuariosLayout.setVerticalGroup(
            pnUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(44, 44, 44))
        );

        pnDatos.add(pnUsuarios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(pnDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBaseDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaseDatosActionPerformed
        unSelectAllToggleButtons();
        hideAllPanel();
        btnBaseDatos.setSelected(true);
        pnBaseDatos.setVisible(true);
    }//GEN-LAST:event_btnBaseDatosActionPerformed

    private void btnAparienciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAparienciaActionPerformed
        unSelectAllToggleButtons();
        hideAllPanel();
        btnApariencia.setSelected(true);
        pnApariencia.setVisible(true);
    }//GEN-LAST:event_btnAparienciaActionPerformed

    private void btnLenguajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLenguajeActionPerformed
        unSelectAllToggleButtons();
        hideAllPanel();
        btnLenguaje.setSelected(true);
        pnLenguaje.setVisible(true);
    }//GEN-LAST:event_btnLenguajeActionPerformed

    private void btnCuentasCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCuentasCorreoActionPerformed
        unSelectAllToggleButtons();
        hideAllPanel();
        btnCuentasCorreo.setSelected(true);
        pnCuentasCorreo.setVisible(true);
    }//GEN-LAST:event_btnCuentasCorreoActionPerformed

    private void btnUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuarioActionPerformed
        unSelectAllToggleButtons();
        hideAllPanel();
        btnUsuario.setSelected(true);
        pnUsuarios.setVisible(true);
    }//GEN-LAST:event_btnUsuarioActionPerformed

    private void btnDatosEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosEmpresaActionPerformed
        unSelectAllToggleButtons();
        hideAllPanel();
        btnDatosEmpresa.setSelected(true);
        pnDatosEmpresa.setVisible(true);
    }//GEN-LAST:event_btnDatosEmpresaActionPerformed

    private void chHabilitarBDDLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chHabilitarBDDLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chHabilitarBDDLocalActionPerformed

    private void chHabilitarBDDOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chHabilitarBDDOnlineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chHabilitarBDDOnlineActionPerformed

    private void tbEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbEliminarUsuarioActionPerformed
        int userID = Integer.valueOf(String.valueOf(tbUsuarios.getModel().getValueAt(tbUsuarios.getSelectedRow(), 0)));
        System.out.println("A eliminar: " + userID);
        Usuarios.removeUsuariosByID(userID);
        actualizarTablaUsuarios();
    }//GEN-LAST:event_tbEliminarUsuarioActionPerformed

    private void tbAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbAgregarUsuarioActionPerformed
        if (crearUsuario == null) {
            crearUsuario = new CrearUsuario();
            crearUsuario.setVisible(true);

            crearUsuario.setViewConfig(this);
        } else {
            JOptionPane.showMessageDialog(this, "Ya esta habierto est Ventana", "Error", 0);
        }
    }//GEN-LAST:event_tbAgregarUsuarioActionPerformed

    private void btnAplicarEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarEmpActionPerformed
        Recolectar_GuardarDatos();
        AplicarConfiguracionActual();
        JOptionPane.showMessageDialog(this, "Debes reiniciar el programa "
                + "para aplicar estos cambios", "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnAplicarEmpActionPerformed

    private void cbHabilitarDatosEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbHabilitarDatosEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbHabilitarDatosEmpActionPerformed

    private void btnAplicarCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAplicarCorreoActionPerformed

    private void btnEliminarCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarCorreoActionPerformed

    private void btnAgregarCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarCorreoActionPerformed

    private void cbIdiomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbIdiomaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbIdiomaActionPerformed

    private void cbTemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTemaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTemaActionPerformed

    private void btnAplicarBDDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarBDDActionPerformed
        Recolectar_GuardarDatos();
        AplicarConfiguracionActual();
        JOptionPane.showMessageDialog(this, "Debes reiniciar el programa "
                + "para aplicar estos cambios", "Informacion", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_btnAplicarBDDActionPerformed

    private void btnProbarConexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProbarConexionActionPerformed
        String ruta = tfDireccionBDD.getText();
        String nombre = tfNombreBDD.getText();
        String puerto = tfPuertoBDD.getText();
        String usuario = tfUsuarioBDD.getText();
        String password = String.valueOf(pfPassBDD.getPassword());
        SqlManager.testConnectionBDD(ruta, nombre, puerto, usuario, password);
    }//GEN-LAST:event_btnProbarConexionActionPerformed

    private void btnAplicarTemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarTemaActionPerformed
        Recolectar_GuardarDatos();
        AplicarConfiguracionActual();
        JOptionPane.showMessageDialog(this, "Se recomienda reiniciar"
                + " para aplicar los cambios", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        if (ventanaPadreInicio != null && ventanaPadreInicio.isVisible()) {
            SwingUtilities.updateComponentTreeUI(ventanaPadreInicio);
        }
        if (ventanaPadreViewPrograma != null && ventanaPadreViewPrograma.isVisible()) {
            SwingUtilities.updateComponentTreeUI(ventanaPadreViewPrograma);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }//GEN-LAST:event_btnAplicarTemaActionPerformed

    private void btnElegirLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElegirLogoActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showDialog(this, "Selecciona el Icono");
        System.out.println("imagen: " + chooser.getSelectedFile().getAbsolutePath());
        configActual.setLogo(chooser.getSelectedFile());
        try {
            lbDireccionLogo.setText(configActual.getLogo().getAbsolutePath());
            ImageIcon icon = new ImageIcon(lbDireccionLogo.getText());
            Image foto = icon.getImage().getScaledInstance(lbIconoMuesta.getHeight(), lbIconoMuesta.getWidth(), 2);
            lbIconoMuesta.setIcon(new ImageIcon(foto));
        } catch (java.lang.NullPointerException ex) {
            lbDireccionLogo.setText("");
        }

    }//GEN-LAST:event_btnElegirLogoActionPerformed
    public void actualizarTablaUsuarios() {
        DefaultTableModel dtm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbUsuarios.getModel());
        for (Usuario aux : Usuarios.getUsuarios()) {
            dtm.addRow(new String[]{String.valueOf(aux.getID()), aux.getCorreo(), Manager.getMD5(aux.getPassword())});
        }
        tbUsuarios.setModel(dtm);
    }
    private void btnCargarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarUsuariosActionPerformed
        actualizarTablaUsuarios();
    }//GEN-LAST:event_btnCargarUsuariosActionPerformed

    private void btnEnviarContraUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarContraUsuarioActionPerformed
        try {
            String correo = String.valueOf(tbUsuarios.getModel().getValueAt(tbUsuarios.getSelectedRow(), 1));
            String pass = String.valueOf(tbUsuarios.getModel().getValueAt(tbUsuarios.getSelectedRow(), 2));
            Usuario aux = Usuarios.getUsuarioByID(Integer.valueOf(String.valueOf(tbUsuarios.getModel().getValueAt(tbUsuarios.getSelectedRow(), 0))));
            EmailManager.SendEmailViewPassword(correo, aux.getPassword());
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Selecciona un correo y pulsa el boton", "Advertencia",JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(ViewConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnEnviarContraUsuarioActionPerformed
    public void limpiarCrearUsuario() {
        crearUsuario = null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewConfig().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabelContra;
    private javax.swing.JButton btnAgregarCorreo;
    private javax.swing.JToggleButton btnApariencia;
    private javax.swing.JButton btnAplicarBDD;
    private javax.swing.JButton btnAplicarCorreo;
    private javax.swing.JButton btnAplicarEmp;
    private javax.swing.JButton btnAplicarIdioma;
    private javax.swing.JButton btnAplicarTema;
    private javax.swing.JToggleButton btnBaseDatos;
    private javax.swing.JButton btnCargarUsuarios;
    private javax.swing.JToggleButton btnCuentasCorreo;
    private javax.swing.JToggleButton btnDatosEmpresa;
    private javax.swing.JButton btnElegirLogo;
    private javax.swing.JButton btnEliminarCorreo;
    private javax.swing.JButton btnEnviarContraUsuario;
    private javax.swing.JToggleButton btnLenguaje;
    private javax.swing.JButton btnProbarConexion;
    private javax.swing.JToggleButton btnUsuario;
    private javax.swing.JCheckBox cbHabilitarDatosEmp;
    private javax.swing.JComboBox<String> cbIdioma;
    private javax.swing.JComboBox<String> cbTema;
    private javax.swing.JCheckBox chHabilitarBDDLocal;
    private javax.swing.JCheckBox chHabilitarBDDOnline;
    private javax.swing.JCheckBox chMostrarPass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbDireccionLogo;
    private javax.swing.JLabel lbIconoMuesta;
    private javax.swing.JPasswordField pfPassBDD;
    private javax.swing.JPanel pnApariencia;
    private javax.swing.JPanel pnBaseDatos;
    private javax.swing.JPanel pnBotones;
    private javax.swing.JPanel pnCuentasCorreo;
    private javax.swing.JPanel pnDatos;
    private javax.swing.JPanel pnDatosConexion;
    private javax.swing.JPanel pnDatosEmpresa;
    private javax.swing.JPanel pnDatosEmpresaInterno;
    private javax.swing.JPanel pnLenguaje;
    private javax.swing.JPanel pnUsuarios;
    private javax.swing.JButton tbAgregarUsuario;
    private javax.swing.JTable tbCorreos;
    private javax.swing.JButton tbEliminarUsuario;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField tfDireccionBDD;
    private javax.swing.JTextField tfDireccionEmp;
    private javax.swing.JTextField tfEmailEmp;
    private javax.swing.JTextField tfNombreBDD;
    private javax.swing.JTextField tfNombreEmp;
    private javax.swing.JTextField tfPuertoBDD;
    private javax.swing.JTextField tfTelEmp;
    private javax.swing.JTextField tfUsuarioBDD;
    // End of variables declaration//GEN-END:variables
    private CrearUsuario crearUsuario;
}
