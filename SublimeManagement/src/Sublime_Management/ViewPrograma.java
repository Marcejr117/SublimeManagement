package Sublime_Management;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Marce_Lite
 */
public class ViewPrograma extends javax.swing.JFrame {

    private float sumatorio = 0;
    private ViewSelectorRef vista;
    private ActionListener ac;

    public ViewPrograma() {

        initComponents();
        Image image = new ImageIcon(getClass().getResource("/res/windowIcon.png")).getImage();
        this.setIconImage(image);
        if (ViewConfig.configActual.getHabilitarBDDLocal()) {//si esta este valor a true
            SqlManager.isLoggin=false;
            SqlManager.cargarDeLocal2();//se cargar la base de datos local
        } else if (ViewConfig.configActual.getHabilitarBDDOnline()) {//si la anterior no esta ponemos pero si online
            try {
                SqlManager.cargarDeOnline2();//cargamos de la base de datos online
            } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex) {
                int respuesta = JOptionPane.showConfirmDialog(this, "El programa iniciara sin base de datos Online\n"
                        + "Ya que no se consiguio establecer la conexion\n"
                        + "Desea Continuar?", "Advertencia", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                switch (respuesta) {
                    case 0:
                        break;
                    default:
                        System.exit(0);
                        break;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "No se puedo establecer la conexion con la base de datos\n comprueba tu conexión","Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No tiene ninguna "
                    + "base de datos configurada,\n No se cargaran/guardaran sus datos!", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowClosing(WindowEvent we) {
                if (ViewConfig.configActual.getHabilitarBDDLocal()) {
                    System.out.println("Guardando Local...");
                    SqlManager.guardarEnLocal();//cuando cerremos el programa nos va a guardar los datos en local
                } else {
                    System.out.println("No se a guardado en local...");
                }
            }

            @Override
            public void windowClosed(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowIconified(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowActivated(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        if (ViewConfig.configActual.getHabilitarBDDOnline()) {
            servicio = new SqlManager.serviceComprobarCambios();
            servicio.start();
        } else {

        }

        this.setLocationRelativeTo(null);
        //para mostrar la ayuda, asigna la ayuda a un boton
        ponLaAyuda();

        tbAlbaranes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
//                DefaultTableModel tm = new DefaultTableModel();
//                tm.setColumnIdentifiers(new String[]{"Ref.Producto","Unidades","Precio.U"});
                DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbDatosAlbaran.getModel());
                Albaran a = null;
                Iterator<Albaran> it = Albaranes.getAlbaranes().iterator();
                while (it.hasNext()) {
                    a = it.next();
                    try {
                        if (a.getId() == Integer.parseInt(String.valueOf(tbAlbaranes.getValueAt(tbAlbaranes.getSelectedRow(), 0)))) {
                            for (int i = 0; i < a.getProducto().size(); i++) {
                                String refP = a.getProducto().get(i).getRef();
                                String cantidadP = String.valueOf(a.getCantidad().get(i));
                                String precioP = String.valueOf(a.getPrecio().get(i));
                                String datos[] = {refP, cantidadP, precioP};
                                tm.addRow(datos);
                            }
                        }
                    } catch (java.lang.IndexOutOfBoundsException ex) {

                    }

                }

                tbDatosAlbaran.setModel(tm);
            }
        });
        pnDatosClienteEnVenta.setVisible(false);
        cbClienteVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (cbClienteVenta.isSelected()) {
                    pnDatosClienteEnVenta.setVisible(true);
                } else {
                    pnDatosClienteEnVenta.setVisible(false);
                }
            }
        });
        fldPVPVenta.setText("0,00 €");
        fldDescuentoVenta.setText("0");
        fldPrecioVentaProductoFinal.setText("0,00");
        fldPVPVenta.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                String aux = fldPVPVenta.getText().split(" ")[0];
                if (aux.equals("")) {
                    aux = "0";
                }
                float precio = Float.parseFloat(aux.split(" ")[0].replaceAll(",", "."));
                float descuento = Float.parseFloat(fldDescuentoVenta.getText().split(" ")[0]);
                float precioFinal = precio - ((descuento * precio) / 100);
                fldPrecioVentaProductoFinal.setText(precioFinal + "");
            }
        });

        fldDescuentoVenta.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                float precio = Float.parseFloat(fldPVPVenta.getText().split(" ")[0].replaceAll(",", "."));
                String aux = fldDescuentoVenta.getText();
                if (aux.equals("")) {
                    aux = "0";
                }
                float descuento = Float.parseFloat(aux.split(" ")[0]);
                float precioFinal = precio - ((descuento * precio) / 100);
                fldPrecioVentaProductoFinal.setText(precioFinal + "");
            }
        });

        tbVentas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
//                DefaultTableModel tm = new DefaultTableModel();
//                tm.setColumnIdentifiers(new String[]{"Producto","Cantidad","Precio.U"});
                DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbDatosVenta.getModel());
                Venta a = null;
                Iterator<Venta> it = Ventas.getVentas().iterator();
                while (it.hasNext()) {
                    a = it.next();
                    try {
                        if (a.getId() == Integer.parseInt(String.valueOf(tbVentas.getValueAt(tbVentas.getSelectedRow(), 0)))) {
                            for (int i = 0; i < a.getProducto().size(); i++) {
                                String refP = a.getProducto().get(i).getRef();
                                String cantidadP = String.valueOf(a.getCantidad().get(i));
                                String precioP = String.valueOf(a.getPrecioProProducto().get(i));
                                String datos[] = {refP, cantidadP, precioP};
                                tm.addRow(datos);
                            }
                        }
                    } catch (java.lang.IndexOutOfBoundsException ex) {

                    }

                }

                tbDatosVenta.setModel(tm);
            }
        });

        //cambiamos le modelo del spinner para que sea en horas minutos y segundos
        spnHora.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnHora, "HH:mm");
        spnHora.setEditor(timeEditor);
        spnHora.setValue(new Date());

        //listener para limitar los caracteres en el textArea
        taDescripcionEvento.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                controladorCaracteresDescripcion();
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                controladorCaracteresDescripcion();
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                controladorCaracteresDescripcion();
            }
        });

        ComprobarEventosCalendario comprobarEventos = new ComprobarEventosCalendario();
        comprobarEventos.start();

    }

    public static class ComprobarEventosCalendario extends Thread {

        public void run() {
            while (true) {
                Date horaActual = new Date();
                System.out.println(horaActual);
                //tiempo actual
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-YYYY");
                String fechaFormateada = formatoFecha.format(horaActual);
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
                String horaFormateada = formatoHora.format(horaActual);
                //
                //comprobar con los evento
                for (Evento evento : Eventos.getEventos()) {
                    SimpleDateFormat formatoFechaEvento = new SimpleDateFormat("dd-MM-YYYY");
                    String fechaFormateadaEvento = formatoFechaEvento.format(evento.getFecha());
                    System.out.println("Primera Comprobacion: " + fechaFormateadaEvento + "=?" + fechaFormateada);
                    System.out.println("Segunda Comprobacion: " + horaFormateada + "=?" + evento.getHora());
                    if (fechaFormateadaEvento.equals(fechaFormateada) && horaFormateada.equals(evento.getHora())) {
                        JOptionPane.showMessageDialog(null, "Tienes un evento"
                                + "\n\tNombre:" + evento.getNombre()
                                + "\n\tPrioritario: " + evento.isPrioritario()
                                + "\n\t Hora: " + evento.getHora(), "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                try {
                    sleep(60000);
                } catch (InterruptedException ex) {

                }
            }
        }

    }

    public void controladorCaracteresDescripcion() {
        String text = taDescripcionEvento.getText();
        String words[] = text.split("\\s");
        lbNumeroPalabrasEvento.setText("" + words.length);
        if (text.equals("")) {
            lbNumeroPalabrasEvento.setText("0");
        }
        lbNumeroLetrasEvento.setText("" + text.length());

    }

    public void eliminarCliente() {

        try {
            String id = String.valueOf(tbClientes.getValueAt(tbClientes.getSelectedRow(), 0));
            boolean isUsing = false;
            for (Venta aux : Ventas.getVentas()) {
                isUsing = aux.getCliente().getId() == id;
            }
            if (!isUsing) {
                Clientes.RemoveClienteFromCLientesByID(Integer.parseInt(id));
            } else {
                JOptionPane.showMessageDialog(this, "No se puede "
                        + "eliminar este cliente ya que esta en uso en la tabla ventas", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "No hay ningun Cliente seleccinado");
        } catch (Exception ex) {

        }
    }

    public void buscarClienteYactualizarTabla() {
        DefaultTableModel tm = (DefaultTableModel) tbClientes.getModel();
        try {
            while (true) {
                tm.removeRow(tm.getRowCount() - 1);//hago esto por no poner los nombre de las cabezeras de la tabla
            }
        } catch (Exception ex) {

        }
        Iterator<Cliente> it = Clientes.getClientes().iterator();
        while (it.hasNext()) {
            Cliente aux = it.next();
            if (aux.getNombre().toLowerCase().matches(".*" + fldNombreCliente.getText().toLowerCase() + ".*")) {
                if (aux.getApellido().toLowerCase().matches(".*" + fldApellidoCliente.getText().toLowerCase() + ".*")) {
                    if (aux.getDni().toLowerCase().matches(".*" + fldDniCliente.getText().toLowerCase() + ".*")) {
                        if (aux.getTelefono().toLowerCase().matches(".*" + fldTelefonoCliente.getText().toLowerCase() + ".*")) {
                            if (aux.getMail().toLowerCase().matches(".*" + fldMailCliente.getText().toLowerCase() + ".*")) {
                                if (aux.getLocalidad().toLowerCase().matches(".*" + fldLocalidadCliente.getText().toLowerCase() + ".*")) {
                                    if (aux.getDireccion().toLowerCase().matches(".*" + fldDireccionCliente.getText().toLowerCase() + ".*")) {
                                        tm.addRow(aux.getDatosWithId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        tbClientes.setModel(tm);
    }

    public void crearCliente() {
        String nombre, apellido, dni, telefono, mail, localidad, direccion;
        boolean proveedor, error = true;
        nombre = fldNombreCliente.getText();
        apellido = fldApellidoCliente.getText();
        dni = fldDniCliente.getText();

        for (Cliente aux : Clientes.getClientes()) {
            if (aux.getDni().equals(dni)) {
                error = true;
            }
        }
        while (dni.equals("") || error) {
            try {
                if (dni.equals("")) {
                    throw new Exception("El cliente debe tener un DNI\nIntroduce Uno: ");
                }

                for (Cliente aux : Clientes.getClientes()) {
                    if (aux.getDni().equals(dni)) {
                        error = true;
                        throw new Exception("Un cliente ya tiene este DNI: " + dni + "\nIntroduce Uno: ");
                    }
                }
                error = false;
            } catch (Exception ex) {
                dni = JOptionPane.showInputDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        telefono = fldTelefonoCliente.getText();
        mail = fldMailCliente.getText();
        localidad = fldLocalidadCliente.getText();
        direccion = fldDireccionCliente.getText();
        proveedor = cbProveedorCliente.isSelected();
        if (error) {
            JOptionPane.showMessageDialog(this, "Ya hay un cliente con este DNI: " + dni, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Cliente aux = new Cliente(nombre, apellido, dni, telefono, mail, localidad, direccion, proveedor);
            Clientes.addClienteToClientes(aux);
            //EmailManager.addClienteToSendInBlue(mail);                        no funciona en java 8
            lbInfoCliente.setForeground(Color.GREEN);
            lbInfoCliente.setText("Cliente Creado con exito, id: " + aux.getId());

            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    lbInfoCliente.setText("");
                    lbInfoCliente.setForeground(Color.RED);
                }
            };

            Timer t = new Timer(3000, al);//3 segundo de tiempo
            t.start();
        }

    }

    public void limpiarCamposCliente() {
        fldNombreCliente.setText("");
        fldApellidoCliente.setText("");
        fldDniCliente.setText("");
        fldTelefonoCliente.setText("");
        fldDireccionCliente.setText("");
        fldMailCliente.setText("");
        cbProveedorCliente.setSelected(false);
        fldLocalidadCliente.setText("");
    }

    private void actualizarTablaClientes() {
        DefaultTableModel tm = (DefaultTableModel) tbClientes.getModel();
        try {
            while (true) {
                tm.removeRow(tm.getRowCount() - 1);//hago esto por no poner los nombre de las cabezeras de la tabla
            }
        } catch (Exception ex) {

        }
        Iterator<Cliente> it = Clientes.getClientes().iterator();
        while (it.hasNext()) {
            Cliente aux = it.next();
            tm.addRow(aux.getDatosWithId());
        }

        tbClientes.setModel(tm);
    }

    public void eliminarAlbaran() throws Exception {

        try {
            int id = Integer.valueOf(String.valueOf(tbAlbaranes.getValueAt(tbAlbaranes.getSelectedRow(), 0)));
            Iterator<Albaran> it = Albaranes.getAlbaranes().iterator();
            while (it.hasNext()) {
                Albaran aux = it.next();
                if (aux.getId() == id) {
                    for (int i = 0; i < aux.getProducto().size(); i++) {
                        Producto pro = aux.getProducto().get(i);
                        int nuevaCantidad = Integer.parseInt(pro.getCantidad()) - (aux.getCantidad().get(i));
                        if (nuevaCantidad < 0) {
                            throw new Exception("No puedes eliminar este albaran ya que no hay suficentes productos");

                        } else {
                            pro.setCantidad(String.valueOf(nuevaCantidad));
                            actualizarTablaStock();
                        }
                    }
                }
            }

            Albaranes.removeAlbaranFromAlbaranesByID(id);
            actualizarTablaAlbaranes();

        } catch (java.lang.IndexOutOfBoundsException ex) {

        }

    }

    public void eliminarVenta() throws Exception {

        try {
            int id = Integer.valueOf(String.valueOf(tbVentas.getValueAt(tbVentas.getSelectedRow(), 0)));
            System.out.println("Id producto a aliminar: "+id);
            Iterator<Venta> it = Ventas.getVentas().iterator();
            while (it.hasNext()) {
                Venta aux = it.next();
                if (aux.getId() == id) {
                    for (int i = 0; i < aux.getProducto().size(); i++) {
                        Producto pro = aux.getProducto().get(i);
                        int nuevaCantidad = Integer.parseInt(pro.getCantidad()) + (aux.getCantidad().get(i));
                        if (nuevaCantidad < 0) {
                            throw new Exception("No puedes eliminar esta venta ya que no hay suficentes productos");

                        } else {
                            pro.setCantidad(String.valueOf(nuevaCantidad));
                            actualizarTablaStock();
                        }
                    }
                }
            }

            Ventas.removeVentaFromVentasByID(id);
            actualizarTablaVentas();

        } catch (java.lang.IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, ex,"Advertencia",JOptionPane.WARNING_MESSAGE);
        }

    }

    public void actualizarTablaDatosALbaran() {
        System.out.println("marcado el elemento: " + tbAlbaranes.getValueAt(tbAlbaranes.getSelectedRow(), 1));
    }

    public void eliminarProductoAlbaran() {
        DefaultTableModel tm = (DefaultTableModel) tbCompras.getModel();
        try {
            tm.removeRow(tbCompras.getSelectedRow());
            sumatorio = 0;
            for (int k = 0; k < tm.getRowCount(); k++) {
                float precio = Float.parseFloat(String.valueOf(tm.getValueAt(k, 2)));
                float cantidad = Float.parseFloat(String.valueOf(tm.getValueAt(k, 1)));
                sumatorio += precio * cantidad;

            }

            fldPrecioTotal.setText(String.valueOf(sumatorio));
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Para eliminar selecciona un producto");
        }
    }

    public void eliminarProductoVenta() {
        DefaultTableModel tm = (DefaultTableModel) tbVenta.getModel();
        try {
            tm.removeRow(tbVenta.getSelectedRow());
            Float sum = 0f;
            for (int k = 0; k < tm.getRowCount(); k++) {
                float precio = Float.parseFloat(String.valueOf(tm.getValueAt(k, 2)));
                float cantidad = Float.parseFloat(String.valueOf(tm.getValueAt(k, 1)));
                sum += precio * cantidad;

            }

            fldPrecioTotalVenta.setText(String.valueOf(sum));
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Para eliminar selecciona un producto");
        }
    }

    public void crearAlbaran() {
        DefaultTableModel tm = (DefaultTableModel) tbCompras.getModel();
        Albaran al;
        List<Producto> p = new LinkedList<Producto>();
        List<Integer> c = new LinkedList<Integer>();
        List<Float> pre = new LinkedList<Float>();

        for (int i = 0; i < tm.getRowCount(); i++) {
            Producto producto = Productos.getProductoByReference2(String.valueOf(tm.getValueAt(i, 0)));
            System.out.println("Referencia del producto: " + producto.getRef());
            int cantidad = Integer.valueOf(String.valueOf((tm.getValueAt(i, 1))));
            float precio = Float.valueOf(String.valueOf((tm.getValueAt(i, 2))));

            p.add(producto);
            c.add(cantidad);
            pre.add(precio);

        }

        if (!fldRefAlbaran.getText().isEmpty() || !fldRefAlbaran.getText().equals("") || !fldRefAlbaran.getText().equals(null)) {
            if (p.size() > 0) {
                for (int i = 0; i < tm.getRowCount(); i++) {
                    Producto producto = Productos.getProductoByReference(String.valueOf(tm.getValueAt(i, 0)));
                    int cantidad = Integer.valueOf(String.valueOf((tm.getValueAt(i, 1))));
                    producto.setCantidad(String.valueOf(Integer.parseInt(producto.getCantidad()) + cantidad));

                }
                al = new Albaran(p, c, pre, fldRefAlbaran.getText());
                al.setImporteTotal(sumatorio);
                Albaranes.addAlbaranToAlbaranes(al);

                lbInfoAlbaran.setForeground(Color.GREEN);
                lbInfoAlbaran.setText("Albaran Creado Con el id: " + al.getId());

                ActionListener acli = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        lbInfoAlbaran.setText("");
                        lbInfoAlbaran.setForeground(Color.RED);
                    }
                };
                Timer t = new Timer(5000, acli);
                t.start();

            } else {

                lbInfoAlbaran.setText("No se pueden crear albaranes vacios");
                ActionListener action = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        lbInfoAlbaran.setText("");
                        lbInfoAlbaran.setForeground(Color.RED);
                    }
                };
                Timer t = new Timer(3000, action);
                t.start();

            }
        } else {
            lbInfoAlbaran.setText("No se pueden crear albaranes sin referencia");
            ActionListener action = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    lbInfoAlbaran.setText("");
                }
            };
            Timer t = new Timer(5000, action);
            t.start();
        }
        actualizarTablaStock();
        System.out.println("Albaranes totales" + Albaranes.getAlbaranes().size());
    }

    public void crearVenta() {
        DefaultTableModel tm = (DefaultTableModel) tbVenta.getModel();
        Venta al;
        List<Producto> p = new LinkedList<Producto>();
        List<Integer> c = new LinkedList<Integer>();
        List<Float> pre = new LinkedList<Float>();
        boolean error = false;
        String dni = "";
        for (int i = 0; i < tm.getRowCount(); i++) {
            Producto producto = Productos.getProductoByReference2(String.valueOf(tm.getValueAt(i, 0)));

            int cantidad = Integer.valueOf(String.valueOf((tm.getValueAt(i, 1))));
            float precio = Float.valueOf(String.valueOf((tm.getValueAt(i, 2))));

            p.add(producto);
            c.add(cantidad);
            pre.add(precio);

        }
        if (cbClienteVenta.isSelected()) {
            dni = tfClienteVenta.getText();
        }

        if (p.size() > 0) {
            for (int i = 0; i < tm.getRowCount(); i++) {
                Producto producto = Productos.getProductoByReference(String.valueOf(tm.getValueAt(i, 0)));
                int cantidad = Integer.valueOf(String.valueOf((tm.getValueAt(i, 1))));
                if (Integer.parseInt(producto.getCantidad()) < cantidad) {
                    try {
                        throw new Exception("No hay suficientes Productos");
                    } catch (Exception ex) {
                        String msg = "El producto: " + producto.getRef() + " Tiene: " + producto.getCantidad() + "\nNo hay suficientes unidades\n" + producto.getCantidad() + "<" + cantidad;
                        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
                        error = true;
                    }
                }
                if (error) {
                } else {
                    producto.setCantidad(String.valueOf(Integer.parseInt(producto.getCantidad()) - cantidad));
                }

            }

            if (error) {
            } else {
                al = new Venta(p, Clientes.getClientesByDNI(dni), c, pre);
                Ventas.addVentaToVentas(al);
                lbInfoVenta.setForeground(Color.GREEN);
                lbInfoVenta.setText("Venta Creada Con el id: " + al.getId());

                ActionListener acli = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        lbInfoVenta.setText("");
                        lbInfoVenta.setForeground(Color.RED);
                    }
                };
                Timer t = new Timer(5000, acli);
                t.start();
            }
        } else {

            lbInfoVenta.setText("No se pueden crear Ventas vacias");
            ActionListener action = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    lbInfoVenta.setText("");
                    lbInfoVenta.setForeground(Color.RED);
                }
            };
            Timer t = new Timer(3000, action);
            t.start();

        }

        actualizarTablaStock();
        System.out.println("Ventas totales" + Ventas.getVentas().size());

    }

    public void limpiarCamposAlbaran() {
        fldRefCompra.setText("");
        fldPrecioCompra.setText("0");
        spnCantidadCompras.setValue(0);
    }

    public void limpiarCamposVenta() {
        fldRefVenta.setText("");
        fldPVPVenta.setText("0");
        fldDescuentoVenta.setText("0");
        tfClienteVenta.setText("");
        spnCantidadVenta.setValue(0);
        fldPrecioTotalVenta.setText(0 + " €");
        tbVenta.setModel(Manager.limpiarDefaultTableModel((DefaultTableModel) tbVenta.getModel()));
    }

    public void setRefCompra(String ref) {
        fldRefCompra.setText(ref);
    }

    public void setRefVenta(String ref) {
        fldRefVenta.setText(ref);
    }

    public void setPrecioVenta(String precio) {
        fldPVPVenta.setText(precio);
    }

    public void setPrecioFinal() {
        String aux = fldPVPVenta.getText().split(" ")[0];
        if (aux.equals("")) {
            aux = "0";
        }
        String aux2 = fldDescuentoVenta.getText().split(" ")[0];
        if (aux2.equals("")) {
            aux2 = "0";
        }
        float precio2 = Float.parseFloat(aux.split(" ")[0].replaceAll(",", "."));
        float descuento = Float.parseFloat(aux2.split(" ")[0]);
        float precioFinal = precio2 - ((descuento * precio2) / 100);
        fldPrecioVentaProductoFinal.setText(precioFinal + "");
    }

    public void agregarProductoAlbaran() {

        boolean existe = false;
        DefaultTableModel tm = (DefaultTableModel) tbCompras.getModel();

        String datos[] = new String[3];
        datos[0] = fldRefCompra.getText();
        datos[1] = spnCantidadCompras.getValue().toString();
        datos[2] = fldPrecioCompra.getText();

        if (!datos[0].equals("")) {
            for (int i = 0; i < tm.getRowCount(); i++) {
                if (tm.getValueAt(i, 0).equals(datos[0]) && tm.getValueAt(i, 2).equals(datos[2])) {
                    System.out.println("Encontrada la coincidencia");
                    int antiguo = Integer.valueOf((String) tm.getValueAt(i, 1));
                    int nuevo = Integer.valueOf(datos[1]);
                    int suma = antiguo + nuevo;
                    tm.setValueAt(String.valueOf(suma), i, 1);
                    existe = true;
                }
            }
            if (!existe) {
                tm.addRow(datos);
            }
            sumatorio = 0;
            for (int k = 0; k < tm.getRowCount(); k++) {
                float precio = Float.parseFloat(String.valueOf(tm.getValueAt(k, 2)));
                float cantidad = Float.parseFloat(String.valueOf(tm.getValueAt(k, 1)));
                sumatorio += precio * cantidad;

            }

            fldPrecioTotal.setText(String.valueOf(sumatorio));

            tbCompras.setModel(tm);
        } else {
            JOptionPane.showMessageDialog(this, "No hay referencia");
        }

    }

    public void actualizarTablaAlbaranes() {
//        String nombres[] = {"ID","Ref","n.Productos","Importe Total"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbAlbaranes.getModel());

        Iterator<Albaran> it = Albaranes.getAlbaranes().iterator();
        while (it.hasNext()) {
            Albaran a = it.next();
            tm.addRow(new String[]{String.valueOf(a.getId()), a.getRefAlbaran(), String.valueOf(a.getProducto().size()), String.valueOf(a.getImporteTotal())});
        }
        tbAlbaranes.setModel(tm);
        tbAlbaranes.setCellSelectionEnabled(false);
        tbAlbaranes.setRowSelectionAllowed(true);
        tbAlbaranes.setSelectionMode(2);
    }

    public void actualizarTablaVentas() {
//        String nombres[] = {"ID", "Fecha", "Cliente", "Importe Total"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = (DefaultTableModel) tbVentas.getModel();
        tm = Manager.limpiarDefaultTableModel(tm);
        Iterator<Venta> it = Ventas.getVentas().iterator();
        while (it.hasNext()) {
            Venta a = it.next();
            float precioTotal = 0f;
            int contadorVenta = 0;
            for (float aux : a.getPrecioProProducto()) {
                int canti = a.getCantidad().get(contadorVenta);
                precioTotal += (canti * aux);
            }
            String id = String.valueOf(a.getId());
            String fechaV = String.valueOf(a.getFechaVenta());
            String dni = "";
            try {
                dni = a.getCliente().getDni();
            } catch (java.lang.NullPointerException ex) {
                dni = "";
            }

            String precioT = String.valueOf(precioTotal);
            tm.addRow(new String[]{id, fechaV, dni, precioT});
        }
        tbVentas.setModel(tm);
    }

    public void actualizarTablaCrearProducto() {

//        String nombres[] = {"Nombre","Referencia","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbCrearProducto.getModel());
        Iterator<Producto> it = Productos.getProductos().iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            String datos[] = {aux.getNombre(), aux.getRef(), aux.getPvp()};
            tm.addRow(datos);
        }

        tbCrearProducto.setModel(tm);
    }

    public void actualizarTablaCrearProductoPorNombre() {

//        String nombres[] = {"Nombre","Referencia","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbCrearProducto.getModel());
        Iterator<Producto> it = Manager.buscarProductoPorNombre(fldBuscarProductoPorNombre.getText()).iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            tm.addRow(aux.getDatos());
        }
        System.out.println(tm.getDataVector());
        tbCrearProducto.setModel(tm);
    }

    public void actualizarTablaCrearProductoPorReferencia() {

//        String nombres[] = {"Nombre","Referencia","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbCrearProducto.getModel());
        Iterator<Producto> it = Manager.buscarProductoPorReferencia(fldBuscarProductoPorReferencia.getText()).iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            tm.addRow(aux.getDatos());
        }
        System.out.println(tm.getDataVector());
        tbCrearProducto.setModel(tm);
    }

    public void actualizarTablaStockConStock() {
//        String nombres[] = {"Nombre","Referencia","Cantidad","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbStock.getModel());
        Iterator<Producto> it = Productos.getProductosConStock().iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            tm.addRow(aux.getDatos());
        }

        tbStock.setModel(tm);
    }

    public void actualizarTablaStockSinStock() {
//        String nombres[] = {"Nombre","Referencia","Cantidad","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbStock.getModel());
        Iterator<Producto> it = Productos.getProductosSinStock().iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            tm.addRow(aux.getDatos());
        }

        tbStock.setModel(tm);
    }

    public void actualizarTablaStockPorNombre() {

//        String nombres[] = {"Nombre","Referencia","Unidades","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbStock.getModel());
        Iterator<Producto> it = Manager.buscarProductoPorNombre(fldBuscarProductoEnStockPorNombre.getText()).iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            tm.addRow(aux.getDatos());
        }

        tbStock.setModel(tm);
    }

    public void actualizarTablaStockPorReferencia() {

//        String nombres[] = {"Nombre","Referencia","Unidades","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbStock.getModel());
        Producto aux = Productos.getProductoByReference(fldBuscarProductoEnStockPorRef.getText());
        if (aux == null) {

        } else {
            tm.addRow(aux.getDatos());
        }

        tbStock.setModel(tm);
    }

    public void actualizarTablaStock() {
//        String nombres[] = {"Nombre","Referencia","Unidades","Pvp"};
//        DefaultTableModel tm = new DefaultTableModel(nombres, 0);
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbStock.getModel());
        Iterator<Producto> it = Productos.getProductos().iterator();
        while (it.hasNext()) {
            Producto aux = it.next();
            tm.addRow(aux.getDatos());
        }

        tbStock.setModel(tm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelCuentas = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        Compras = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tbDatosAlbaran = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        tbAlbaranes = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        spnCantidadCompras = new javax.swing.JSpinner();
        fldAgregarCompra = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbCompras = new javax.swing.JTable();
        btnComprar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        fldPrecioTotal = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lbInfoAlbaran = new javax.swing.JLabel();
        fldRefAlbaran = new javax.swing.JTextField();
        btnProveedorSelector = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        fldRefCompra = new javax.swing.JTextField();
        btnAbriSeleccionRef = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        fldPrecioCompra = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        pnVentas = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbDatosVenta = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbVentas = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        pnDatosClienteEnVenta = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        tfClienteVenta = new javax.swing.JTextField();
        btnAbriSeleccionCliente = new javax.swing.JButton();
        cbClienteVenta = new javax.swing.JCheckBox();
        fldRefVenta = new javax.swing.JTextField();
        btnAbriSeleccionRef2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        spnCantidadVenta = new javax.swing.JSpinner();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbVenta = new javax.swing.JTable();
        btnAgregarProductoVenta = new javax.swing.JButton();
        btnLimpiarCamposVenta = new javax.swing.JButton();
        btnVenderTodo = new javax.swing.JButton();
        btnEliminarSeleccionadoVenta = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        fldDescuentoVenta = new javax.swing.JFormattedTextField();
        fldPVPVenta = new javax.swing.JFormattedTextField();
        fldPrecioVentaProductoFinal = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        fldPrecioTotalVenta = new javax.swing.JFormattedTextField();
        lbInfoVenta = new javax.swing.JLabel();
        cbDescuentoVenta = new javax.swing.JComboBox<>();
        Facturas = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jpClientes = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fldApellidoCliente = new javax.swing.JTextField();
        fldNombreCliente = new javax.swing.JTextField();
        fldDniCliente = new javax.swing.JTextField();
        cbProveedorCliente = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbClientes = new javax.swing.JTable();
        btnEliminarCliente = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        fldTelefonoCliente = new javax.swing.JTextField();
        btnBucarCliente = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        fldMailCliente = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        fldLocalidadCliente = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        fldDireccionCliente = new javax.swing.JTextField();
        lbInfoCliente = new javax.swing.JLabel();
        btnAgregarCliente = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        fldNombreProducto = new javax.swing.JTextField();
        fldRefProducto = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        fldPrecioProducto = new javax.swing.JTextField();
        btnCrearProducto = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbCrearProducto = new javax.swing.JTable();
        btnEliminarProducto = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        fldBuscarProductoPorReferencia = new javax.swing.JTextField();
        fldBuscarProductoPorNombre = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        fldCantidadInicial = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        lbProductosTotales = new javax.swing.JLabel();
        lbProductoEliminado = new javax.swing.JLabel();
        btnCargarTodosProductos = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbStock = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        fldBuscarProductoEnStockPorNombre = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        fldBuscarProductoEnStockPorRef = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        btnCargarTodosEnStock = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        calendarioEvento = new com.toedter.calendar.JCalendar();
        jLabel16 = new javax.swing.JLabel();
        fldNombreEvento = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        chbPrioritarioEvento = new javax.swing.JCheckBox();
        btnCrearEvento = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        spnHora = new javax.swing.JSpinner();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        taDescripcionEvento = new javax.swing.JTextArea();
        lbInfoEvento = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lbNumeroLetrasEvento = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        lbNumeroPalabrasEvento = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbEventos = new javax.swing.JTable();
        jButton31 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miInforme1 = new javax.swing.JMenuItem();
        miInforme2 = new javax.swing.JMenuItem();
        miInforme3 = new javax.swing.JMenuItem();
        miInforme4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        miAyuda = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1100, 770));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Albaranes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jScrollPane10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tbDatosAlbaran.setAutoCreateRowSorter(true);
        tbDatosAlbaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ref.Producto", "Unidades", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDatosAlbaran.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(tbDatosAlbaran);
        if (tbDatosAlbaran.getColumnModel().getColumnCount() > 0) {
            tbDatosAlbaran.getColumnModel().getColumn(0).setResizable(false);
            tbDatosAlbaran.getColumnModel().getColumn(1).setResizable(false);
            tbDatosAlbaran.getColumnModel().getColumn(2).setResizable(false);
        }

        jScrollPane11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tbAlbaranes.setAutoCreateRowSorter(true);
        tbAlbaranes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Ref", "n.Productos", "Importe Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbAlbaranes.getTableHeader().setReorderingAllowed(false);
        jScrollPane11.setViewportView(tbAlbaranes);
        if (tbAlbaranes.getColumnModel().getColumnCount() > 0) {
            tbAlbaranes.getColumnModel().getColumn(0).setResizable(false);
            tbAlbaranes.getColumnModel().getColumn(1).setResizable(false);
            tbAlbaranes.getColumnModel().getColumn(2).setResizable(false);
            tbAlbaranes.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setText("Producto REF:");

        jLabel18.setText("Cantidad:");

        spnCantidadCompras.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnCantidadComprasStateChanged(evt);
            }
        });

        fldAgregarCompra.setText("Agregar");
        fldAgregarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldAgregarCompraActionPerformed(evt);
            }
        });

        jButton16.setText("Limpiar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        tbCompras.setAutoCreateRowSorter(true);
        tbCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio U."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCompras.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tbCompras);
        if (tbCompras.getColumnModel().getColumnCount() > 0) {
            tbCompras.getColumnModel().getColumn(0).setResizable(false);
            tbCompras.getColumnModel().getColumn(1).setResizable(false);
            tbCompras.getColumnModel().getColumn(2).setResizable(false);
        }

        btnComprar.setText("Crear Albaran");
        btnComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprarActionPerformed(evt);
            }
        });

        jLabel12.setText("Precio total:");

        fldPrecioTotal.setEditable(false);
        fldPrecioTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fldPrecioTotal.setText("0");

        jButton14.setText("Eliminar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel27.setText("€");

        jLabel33.setText("Referencia proveedor:");

        lbInfoAlbaran.setForeground(new java.awt.Color(255, 0, 0));

        fldRefAlbaran.setEditable(false);
        fldRefAlbaran.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnProveedorSelector.setText("...");
        btnProveedorSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorSelectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fldPrecioTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addGap(235, 235, 235)
                        .addComponent(jButton14))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbInfoAlbaran, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fldRefAlbaran, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProveedorSelector)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fldPrecioTotal)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton14))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(fldRefAlbaran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProveedorSelector)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnComprar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbInfoAlbaran, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        fldRefCompra.setEditable(false);
        fldRefCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldRefCompraActionPerformed(evt);
            }
        });

        btnAbriSeleccionRef.setText("...");
        btnAbriSeleccionRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbriSeleccionRefActionPerformed(evt);
            }
        });

        jLabel25.setText("Precio:");

        fldPrecioCompra.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fldPrecioCompra.setText("0");
        fldPrecioCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fldPrecioCompraKeyPressed(evt);
            }
        });

        jLabel32.setText("€");

        jButton20.setText("Eliminar Albaran");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText("Cargar Albaranes");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ComprasLayout = new javax.swing.GroupLayout(Compras);
        Compras.setLayout(ComprasLayout);
        ComprasLayout.setHorizontalGroup(
            ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ComprasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ComprasLayout.createSequentialGroup()
                        .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(ComprasLayout.createSequentialGroup()
                                .addComponent(fldAgregarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ComprasLayout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(148, 148, 148))
                    .addGroup(ComprasLayout.createSequentialGroup()
                        .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ComprasLayout.createSequentialGroup()
                                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(spnCantidadCompras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                                    .addComponent(fldPrecioCompra, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel32))
                            .addComponent(fldRefCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAbriSeleccionRef)
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addGap(0, 0, 0)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ComprasLayout.createSequentialGroup()
                        .addComponent(jButton21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton20)))
                .addContainerGap())
        );
        ComprasLayout.setVerticalGroup(
            ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ComprasLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(fldRefCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbriSeleccionRef))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fldPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spnCantidadCompras))
                .addGap(25, 25, 25)
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldAgregarCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
            .addGroup(ComprasLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton21)
                    .addComponent(jButton20))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Albaran", Compras);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Registro Ventas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tbDatosVenta.setAutoCreateRowSorter(true);
        tbDatosVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio.U"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDatosVenta.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(tbDatosVenta);
        if (tbDatosVenta.getColumnModel().getColumnCount() > 0) {
            tbDatosVenta.getColumnModel().getColumn(0).setResizable(false);
            tbDatosVenta.getColumnModel().getColumn(1).setResizable(false);
            tbDatosVenta.getColumnModel().getColumn(2).setResizable(false);
        }

        tbVentas.setAutoCreateRowSorter(true);
        tbVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Cliente", "Importe Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbVentas.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(tbVentas);
        if (tbVentas.getColumnModel().getColumnCount() > 0) {
            tbVentas.getColumnModel().getColumn(0).setResizable(false);
            tbVentas.getColumnModel().getColumn(1).setResizable(false);
            tbVentas.getColumnModel().getColumn(2).setResizable(false);
            tbVentas.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Eliminar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Cargar Ventas");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText("Producto:");

        jLabel10.setText("DNI Cliente: ");

        tfClienteVenta.setEditable(false);

        btnAbriSeleccionCliente.setText("...");
        btnAbriSeleccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbriSeleccionClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnDatosClienteEnVentaLayout = new javax.swing.GroupLayout(pnDatosClienteEnVenta);
        pnDatosClienteEnVenta.setLayout(pnDatosClienteEnVentaLayout);
        pnDatosClienteEnVentaLayout.setHorizontalGroup(
            pnDatosClienteEnVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDatosClienteEnVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbriSeleccionCliente)
                .addContainerGap())
        );
        pnDatosClienteEnVentaLayout.setVerticalGroup(
            pnDatosClienteEnVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDatosClienteEnVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnDatosClienteEnVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tfClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbriSeleccionCliente))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        cbClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbClienteVentaActionPerformed(evt);
            }
        });

        btnAbriSeleccionRef2.setText("...");
        btnAbriSeleccionRef2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbriSeleccionRef2ActionPerformed(evt);
            }
        });

        jLabel9.setText("Cantidad:");

        spnCantidadVenta.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnCantidadVentaStateChanged(evt);
            }
        });

        tbVenta.setAutoCreateRowSorter(true);
        tbVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ref.", "Cantidad", "Precio.U"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbVenta.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(tbVenta);
        if (tbVenta.getColumnModel().getColumnCount() > 0) {
            tbVenta.getColumnModel().getColumn(0).setResizable(false);
            tbVenta.getColumnModel().getColumn(1).setResizable(false);
            tbVenta.getColumnModel().getColumn(2).setResizable(false);
        }

        btnAgregarProductoVenta.setText("Agregar");
        btnAgregarProductoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoVentaActionPerformed(evt);
            }
        });

        btnLimpiarCamposVenta.setText("Limpiar");
        btnLimpiarCamposVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCamposVentaActionPerformed(evt);
            }
        });

        btnVenderTodo.setText("Vender Todo");
        btnVenderTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenderTodoActionPerformed(evt);
            }
        });

        btnEliminarSeleccionadoVenta.setText("Eliminar");
        btnEliminarSeleccionadoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarSeleccionadoVentaActionPerformed(evt);
            }
        });

        jLabel37.setText("Precio total:");

        jLabel8.setText("Cliente:");

        jLabel39.setText("PVP.");

        jLabel40.setText("Descuento");

        jLabel41.setText("%");

        fldDescuentoVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        fldDescuentoVenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        fldPVPVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        fldPVPVenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        fldPrecioVentaProductoFinal.setEditable(false);
        fldPrecioVentaProductoFinal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel43.setText("€");

        fldPrecioTotalVenta.setEditable(false);
        fldPrecioTotalVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        fldPrecioTotalVenta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        cbDescuentoVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "14", "21", "30" }));
        cbDescuentoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDescuentoVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnVentasLayout = new javax.swing.GroupLayout(pnVentas);
        pnVentas.setLayout(pnVentasLayout);
        pnVentasLayout.setHorizontalGroup(
            pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(pnVentasLayout.createSequentialGroup()
                        .addComponent(btnLimpiarCamposVenta)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnVentasLayout.createSequentialGroup()
                        .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAgregarProductoVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnVentasLayout.createSequentialGroup()
                                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fldRefVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAbriSeleccionRef2))
                            .addGroup(pnVentasLayout.createSequentialGroup()
                                .addComponent(fldPVPVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnVentasLayout.createSequentialGroup()
                                        .addComponent(fldDescuentoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel41))
                                    .addComponent(cbDescuentoVenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                                .addComponent(fldPrecioVentaProductoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnVentasLayout.createSequentialGroup()
                        .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnVentasLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbClienteVenta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fldPrecioTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38))
                            .addComponent(pnDatosClienteEnVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnVenderTodo)
                            .addComponent(btnEliminarSeleccionadoVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnVentasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbInfoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );
        pnVentasLayout.setVerticalGroup(
            pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnVentasLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fldRefVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbriSeleccionRef2))
                .addGap(6, 6, 6)
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spnCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldDescuentoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldPVPVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldPrecioVentaProductoFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnVentasLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregarProductoVenta))
                    .addGroup(pnVentasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cbDescuentoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(btnLimpiarCamposVenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbClienteVenta))
                    .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fldPrecioTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarSeleccionadoVenta)))
                .addGroup(pnVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnVentasLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(pnDatosClienteEnVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnVentasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVenderTodo)
                        .addGap(12, 12, 12)))
                .addComponent(lbInfoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Ventas", pnVentas);

        jLabel7.setText("Cliente: ");

        jLabel11.setText("Venta: ");

        jButton9.setText("...");

        jButton10.setText("...");

        jCheckBox3.setText("Pagada");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Albacete", "Toledo", "Cuenca", "Guadalajara", "Ciudad Real" }));

        jButton11.setText("Crear");

        jButton12.setText("Limpiar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Facturas Realizadas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cliente", "Venta", "Direccion", "Pagada"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton3.setText("Cargar Todas");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel29.setText("Buscar por Cliente:");

        jLabel30.setText("Buscar por Venta:");

        jButton7.setText("Buscar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton13.setText("Buscar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton19.setText("Sin Pagar");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton19))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jTextField2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton13)))
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13))
                .addGap(111, 111, 111)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton19))
                .addGap(22, 22, 22))
        );

        jLabel13.setText("Provincia");

        jLabel14.setText("Direccion");

        javax.swing.GroupLayout FacturasLayout = new javax.swing.GroupLayout(Facturas);
        Facturas.setLayout(FacturasLayout);
        FacturasLayout.setHorizontalGroup(
            FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FacturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FacturasLayout.createSequentialGroup()
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addGap(354, 354, 354))
                    .addGroup(FacturasLayout.createSequentialGroup()
                        .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FacturasLayout.createSequentialGroup()
                                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField9)
                                    .addGroup(FacturasLayout.createSequentialGroup()
                                        .addGap(62, 62, 62)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(FacturasLayout.createSequentialGroup()
                                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField7)
                                    .addComponent(jTextField8))))
                        .addGap(6, 6, 6)
                        .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(FacturasLayout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        FacturasLayout.setVerticalGroup(
            FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FacturasLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1))
                .addGap(20, 20, 20)
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField9))
                .addGap(18, 18, 18)
                .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addGap(185, 185, 185)
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addGap(202, 202, 202))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FacturasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Facturas", Facturas);

        jLabel1.setText("Nombre");

        jLabel2.setText("Apellidos:");

        jLabel3.setText("DNI/NIF:");

        jLabel4.setText("Proveedor:");

        cbProveedorCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbProveedorClienteActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Clientes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tbClientes.setAutoCreateRowSorter(true);
        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Apellido", "DNI", "Telefono", "E-Mail", "Localidad", "Direccion", "Proveedor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbClientes);

        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        jButton2.setText("Cargar Todos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarCliente)
                            .addComponent(jButton2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminarCliente)
                .addGap(97, 97, 97))
        );

        jLabel5.setText("Telefono:");

        btnBucarCliente.setText("Buscar");
        btnBucarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBucarClienteActionPerformed(evt);
            }
        });

        jLabel34.setText("E-Mail");

        jLabel35.setText("Localidad");

        jLabel36.setText("Direccion");

        lbInfoCliente.setForeground(new java.awt.Color(255, 0, 0));

        btnAgregarCliente.setText("Crear Cliente");
        btnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpClientesLayout = new javax.swing.GroupLayout(jpClientes);
        jpClientes.setLayout(jpClientesLayout);
        jpClientesLayout.setHorizontalGroup(
            jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbInfoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpClientesLayout.createSequentialGroup()
                        .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 19, Short.MAX_VALUE)
                        .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpClientesLayout.createSequentialGroup()
                                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fldDireccionCliente)
                                    .addComponent(fldNombreCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(fldApellidoCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(fldDniCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(fldTelefonoCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(fldMailCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(fldLocalidadCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                .addGap(52, 52, 52))
                            .addGroup(jpClientesLayout.createSequentialGroup()
                                .addComponent(cbProveedorCliente)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpClientesLayout.createSequentialGroup()
                        .addComponent(btnAgregarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBucarCliente)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpClientesLayout.setVerticalGroup(
            jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpClientesLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldNombreCliente)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldApellidoCliente)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldDniCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldTelefonoCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldMailCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldLocalidadCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldDireccionCliente))
                .addGap(18, 18, 18)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cbProveedorCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(lbInfoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBucarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregarCliente))
                .addGap(302, 302, 302))
            .addGroup(jpClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jpClientesLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(51, 51, 51)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Clientes", jpClientes);

        javax.swing.GroupLayout panelCuentasLayout = new javax.swing.GroupLayout(panelCuentas);
        panelCuentas.setLayout(panelCuentasLayout);
        panelCuentasLayout.setHorizontalGroup(
            panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCuentasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        panelCuentasLayout.setVerticalGroup(
            panelCuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCuentasLayout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cuentas", panelCuentas);

        jLabel20.setText("Nombre Producto:");

        jLabel21.setText("Referencia:");

        jLabel22.setText("Precio venta:");

        fldPrecioProducto.setText("0");
        fldPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fldPrecioProductoKeyPressed(evt);
            }
        });

        btnCrearProducto.setText("Craer producto");
        btnCrearProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearProductoActionPerformed(evt);
            }
        });

        tbCrearProducto.setAutoCreateRowSorter(true);
        tbCrearProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Referencia", "Pvp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCrearProducto.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tbCrearProducto);
        if (tbCrearProducto.getColumnModel().getColumnCount() > 0) {
            tbCrearProducto.getColumnModel().getColumn(0).setResizable(false);
            tbCrearProducto.getColumnModel().getColumn(1).setResizable(false);
            tbCrearProducto.getColumnModel().getColumn(2).setResizable(false);
        }

        btnEliminarProducto.setText("Eliminar producto seleccionado");
        btnEliminarProducto.setToolTipText("Debes seleccinar la referencia del producto a eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Buscar por nombre:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("Buscar por referencia:");

        jButton25.setText("Buscar!");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setText("Buscar!");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jLabel28.setText("Cantidad Inicial: ");

        fldCantidadInicial.setText("0");
        fldCantidadInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fldCantidadInicialKeyPressed(evt);
            }
        });

        jLabel31.setText("Productos Totales:");

        lbProductosTotales.setText("0");

        lbProductoEliminado.setForeground(new java.awt.Color(255, 0, 0));

        btnCargarTodosProductos.setText("Cargar Todos los Productos");
        btnCargarTodosProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarTodosProductosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(fldPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(fldCantidadInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fldNombreProducto)
                            .addComponent(fldRefProducto)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnCrearProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 770, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(btnEliminarProducto)
                                .addGap(18, 18, 18)
                                .addComponent(btnCargarTodosProductos))
                            .addComponent(lbProductoEliminado, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbProductosTotales, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24))
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fldBuscarProductoPorReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton26))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(fldBuscarProductoPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton25)))
                                .addGap(44, 44, 44)))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fldNombreProducto)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldRefProducto))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldPrecioProducto)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldCantidadInicial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCrearProducto)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(lbProductosTotales)
                    .addComponent(lbProductoEliminado, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEliminarProducto)
                        .addComponent(btnCargarTodosProductos))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(fldBuscarProductoPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(fldBuscarProductoPorReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton26))))
                .addGap(74, 74, 74))
        );

        jTabbedPane3.addTab("Crear Producto", jPanel9);

        tbStock.setAutoCreateRowSorter(true);
        tbStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Referencia", "Cantidad", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbStock.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tbStock);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Buscar por nombre:");

        fldBuscarProductoEnStockPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldBuscarProductoEnStockPorNombreActionPerformed(evt);
            }
        });

        jButton8.setText("Buscar!");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("Buscar por referencia:");

        jButton17.setText("Buscar!");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("Cargar productos con Stock");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton15.setText("Cargar Productos Sin Stock");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        btnCargarTodosEnStock.setText("Cargar Productos");
        btnCargarTodosEnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarTodosEnStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fldBuscarProductoEnStockPorNombre)
                            .addComponent(fldBuscarProductoEnStockPorRef))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1072, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton15)
                        .addGap(18, 18, 18)
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCargarTodosEnStock)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fldBuscarProductoEnStockPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldBuscarProductoEnStockPorRef)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton18)
                    .addComponent(jButton15)
                    .addComponent(btnCargarTodosEnStock))
                .addGap(2407, 2407, 2407))
        );

        jTabbedPane3.addTab("Stock", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 726, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Almacen", jPanel3);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Selecciona una fecha"));

        calendarioEvento.setDate(new java.util.Date(1633727220000L));
        calendarioEvento.setMaxSelectableDate(new java.util.Date(253370764860000L));
        calendarioEvento.setMinSelectableDate(new java.util.Date(-62135769540000L));
        calendarioEvento.setNullDateButtonText("Sin Fecha");
        calendarioEvento.setTodayButtonText("Hoy");
        calendarioEvento.setTodayButtonVisible(true);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(calendarioEvento, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(calendarioEvento, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel16.setText("Nombre del Evento:");

        jLabel26.setText("Descripcion:");

        chbPrioritarioEvento.setText("Prioritario");

        btnCrearEvento.setText("Crear Evento");
        btnCrearEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearEventoActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        spnHora.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR_OF_DAY));

        jLabel38.setText("Hora");

        taDescripcionEvento.setColumns(20);
        taDescripcionEvento.setRows(5);
        jScrollPane13.setViewportView(taDescripcionEvento);

        jLabel42.setText("Letras:");

        lbNumeroLetrasEvento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbNumeroLetrasEvento.setText("0");
        lbNumeroLetrasEvento.setToolTipText("");

        jLabel44.setText("Palabras");

        lbNumeroPalabrasEvento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbNumeroPalabrasEvento.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chbPrioritarioEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fldNombreEvento)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(spnHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)
                                .addComponent(jLabel44)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbNumeroPalabrasEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbNumeroLetrasEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11))
                            .addComponent(jScrollPane13)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnCrearEvento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiar))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lbInfoEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fldNombreEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spnHora)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbNumeroLetrasEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbNumeroPalabrasEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(chbPrioritarioEvento)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnCrearEvento))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(btnLimpiar)))
                        .addGap(18, 18, 18)
                        .addComponent(lbInfoEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(248, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Crear Evento", jPanel6);

        tbEventos.setAutoCreateRowSorter(true);
        tbEventos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Descripcion", "Fecha", "Hora", "Prioritario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbEventos.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbEventos);
        if (tbEventos.getColumnModel().getColumnCount() > 0) {
            tbEventos.getColumnModel().getColumn(0).setResizable(false);
            tbEventos.getColumnModel().getColumn(1).setResizable(false);
            tbEventos.getColumnModel().getColumn(3).setResizable(false);
            tbEventos.getColumnModel().getColumn(4).setResizable(false);
            tbEventos.getColumnModel().getColumn(5).setResizable(false);
        }

        jButton31.setText("Eliminar");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton6.setText("Cargar Todos");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1072, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton31)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton31)
                    .addComponent(jButton6))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Eventos", jPanel15);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Calendario", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 757, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Archivo");

        miInforme1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        miInforme1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf (1).png"))); // NOI18N
        miInforme1.setText("Imprimir Albaranes");
        miInforme1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInforme1ActionPerformed(evt);
            }
        });
        jMenu1.add(miInforme1);

        miInforme2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        miInforme2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf (1).png"))); // NOI18N
        miInforme2.setText("Imprimir Clientes");
        miInforme2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInforme2ActionPerformed(evt);
            }
        });
        jMenu1.add(miInforme2);

        miInforme3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        miInforme3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf (1).png"))); // NOI18N
        miInforme3.setText("Imprimir Proporcion Productos");
        miInforme3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInforme3ActionPerformed(evt);
            }
        });
        jMenu1.add(miInforme3);

        miInforme4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        miInforme4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf (1).png"))); // NOI18N
        miInforme4.setText("Imprimir Informacion de un albaran");
        miInforme4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miInforme4ActionPerformed(evt);
            }
        });
        jMenu1.add(miInforme4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Configuracion");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Configuracion");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Ayuda");

        miAyuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        miAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/help (2).png"))); // NOI18N
        miAyuda.setText("Ayuda");
        miAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAyudaActionPerformed(evt);
            }
        });
        jMenu3.add(miAyuda);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/information.png"))); // NOI18N
        jMenuItem2.setText("Abaut");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1119, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        try {
            boolean isUsing = false;
            String ref = (String) tbCrearProducto.getModel().getValueAt(tbCrearProducto.getSelectedRow(), 1);
            for (Albaran aux : Albaranes.getAlbaranes()) {
                for (Producto aux2 : aux.getProducto()) {
                    if (aux2.getRef().equals(ref)) {
                        isUsing = true;
                    }
                }
            }
            for (Venta aux : Ventas.getVentas()) {
                for (Producto aux2 : aux.getProducto()) {
                    if (aux2.getRef().equals(ref)) {
                        isUsing = true;
                    }
                }
            }
            if (isUsing) {
                JOptionPane.showMessageDialog(this, "El producto esta en uso", "Avertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                if (Productos.RemoveProductoByReference(ref)) {
                    actualizarTablaCrearProducto();
                    lbProductoEliminado.setForeground(Color.GREEN);
                    lbProductoEliminado.setText("Producto eliminado: " + ref);
                } else {
                    lbProductoEliminado.setText("Hubo algun problema al eliminar el produto!");
                }
            }

            ac = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    lbProductoEliminado.setText("");
                    lbProductoEliminado.setForeground(Color.RED);
                }
            };
            Timer t = new Timer(3000, ac);//3 segundo de tiempo
            t.start();
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminarlo\n" + ex);
        }


    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnCrearProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearProductoActionPerformed
        String n = fldNombreProducto.getText();
        String r = fldRefProducto.getText();
        String pvp = fldPrecioProducto.getText();
        String cantidadIni = fldCantidadInicial.getText();
        if (pvp.length() == 0) {
            pvp = "0";
        }
        if (cantidadIni.length() == 0) {
            cantidadIni = "0";
        }
        Manager.crearProducto(n, r, pvp, cantidadIni);
        actualizarTablaCrearProducto();
        lbProductosTotales.setText(Productos.getProductos().size() + "");

        fldNombreProducto.setText("");
        fldRefProducto.setText("");
        fldPrecioProducto.setText("0");
        fldCantidadInicial.setText("0");

    }//GEN-LAST:event_btnCrearProductoActionPerformed

    private void btnBucarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBucarClienteActionPerformed
        buscarClienteYactualizarTabla();
    }//GEN-LAST:event_btnBucarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        eliminarCliente();
        actualizarTablaClientes();
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnAbriSeleccionRef2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbriSeleccionRef2ActionPerformed
        if (vista == null || !vista.isVisible()) {
            vista = new ViewSelectorRef();
            vista.setVisible(true);
            vista.setObjeto(this);
            vista.setPadre(2);
        } else {
            JOptionPane.showMessageDialog(this, "ya esta Abierto");
        }
    }//GEN-LAST:event_btnAbriSeleccionRef2ActionPerformed

    private void cbClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbClienteVentaActionPerformed

    private void btnAbriSeleccionRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbriSeleccionRefActionPerformed

        if (vista == null || !vista.isVisible()) {
            vista = new ViewSelectorRef();
            vista.setVisible(true);
            vista.setObjeto(this);
            vista.setPadre(1);
        } else {
            JOptionPane.showMessageDialog(this, "ya esta Abierto");
        }
    }//GEN-LAST:event_btnAbriSeleccionRefActionPerformed

    private void btnComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprarActionPerformed
        crearAlbaran();
        actualizarTablaAlbaranes();
    }//GEN-LAST:event_btnComprarActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        limpiarCamposAlbaran();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void fldAgregarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldAgregarCompraActionPerformed
        agregarProductoAlbaran();
    }//GEN-LAST:event_fldAgregarCompraActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void fldPrecioProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fldPrecioProductoKeyPressed
        int num = Character.getNumericValue(evt.getKeyChar());
        //System.out.println("valor: "+evt.getKeyCode());
        if (num >= 0 && num <= 9 || evt.getKeyCode() == 8 || evt.getKeyCode() == 10 || evt.getKeyCode() == 46 || evt.getKeyCode() == 44) {//el 8 y el 10 son el delete y el backspace

        } else {

            JOptionPane.showMessageDialog(this, "Debe ser un numero", "Error en la entrada", 0);
            JTextField aux = (JTextField) evt.getSource();
            try {
                aux.setText(aux.getText().substring(0, aux.getText().length() - 1));
            } catch (java.lang.StringIndexOutOfBoundsException ex) {

            }

        }

    }//GEN-LAST:event_fldPrecioProductoKeyPressed

    private void fldCantidadInicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fldCantidadInicialKeyPressed
        int num = Character.getNumericValue(evt.getKeyChar());

        if (num >= 0 && num <= 9 || evt.getKeyCode() == 8 || evt.getKeyCode() == 10) {

        } else {

            JOptionPane.showMessageDialog(this, "Debe ser un numero", "Error en la entrada", 0);
            JTextField aux = (JTextField) evt.getSource();
            try {
                aux.setText(aux.getText().substring(0, aux.getText().length() - 1));
            } catch (java.lang.StringIndexOutOfBoundsException ex) {

            }
        }
    }//GEN-LAST:event_fldCantidadInicialKeyPressed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        actualizarTablaCrearProductoPorNombre();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        actualizarTablaCrearProductoPorReferencia();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void btnCargarTodosProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarTodosProductosActionPerformed
        actualizarTablaCrearProducto();
    }//GEN-LAST:event_btnCargarTodosProductosActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        actualizarTablaStockSinStock();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        actualizarTablaStockConStock();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void btnCargarTodosEnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarTodosEnStockActionPerformed
        actualizarTablaStock();
    }//GEN-LAST:event_btnCargarTodosEnStockActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        actualizarTablaStockPorNombre();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        actualizarTablaStockPorReferencia();

    }//GEN-LAST:event_jButton17ActionPerformed

    private void fldRefCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldRefCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldRefCompraActionPerformed

    private void fldPrecioCompraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fldPrecioCompraKeyPressed
        int num = Character.getNumericValue(evt.getKeyChar());

        if (num >= 0 && num <= 9 || evt.getKeyCode() == 8 || evt.getKeyCode() == 10 || evt.getKeyCode() == 46) {//el 8 y el 10 son el delete y el backspace

        } else {
            System.out.println(evt.getKeyCode());
            JOptionPane.showMessageDialog(this, "Debe ser un numero", "Error en la entrada", 0);
            JTextField aux = (JTextField) evt.getSource();
            try {
                aux.setText(aux.getText().substring(0, aux.getText().length() - 1));
            } catch (java.lang.StringIndexOutOfBoundsException ex) {

            }

        }
    }//GEN-LAST:event_fldPrecioCompraKeyPressed

    private void spnCantidadComprasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnCantidadComprasStateChanged
        JSpinner spn = (JSpinner) evt.getSource();
        if ((int) spn.getValue() < 0) {
            spnCantidadCompras.setValue(0);
        }
    }//GEN-LAST:event_spnCantidadComprasStateChanged

    private void fldBuscarProductoEnStockPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldBuscarProductoEnStockPorNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldBuscarProductoEnStockPorNombreActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        eliminarProductoAlbaran();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        actualizarTablaAlbaranes();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        try {
            eliminarAlbaran();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }//GEN-LAST:event_jButton20ActionPerformed

    private void cbProveedorClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbProveedorClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbProveedorClienteActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        actualizarTablaClientes();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClienteActionPerformed
        crearCliente();
        limpiarCamposCliente();
        actualizarTablaClientes();
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void miInforme1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInforme1ActionPerformed
        GestionReportes.reporte1();//genera el informe 1
    }//GEN-LAST:event_miInforme1ActionPerformed

    private void miInforme2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInforme2ActionPerformed
        GestionReportes.reporte2();//genera el informe 2
    }//GEN-LAST:event_miInforme2ActionPerformed

    private void miInforme3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInforme3ActionPerformed
        GestionReportes.reporte3();//genera el informe 3
    }//GEN-LAST:event_miInforme3ActionPerformed

    private void miInforme4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miInforme4ActionPerformed
        GestionReportes.reporte4();//genera el informe 4
    }//GEN-LAST:event_miInforme4ActionPerformed

    private void miAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAyudaActionPerformed

    }//GEN-LAST:event_miAyudaActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (viewConfig == null || !viewConfig.isVisible()) {
            viewConfig = new ViewConfig();
            viewConfig.setVisible(true);
            viewConfig.ventanaPadreViewPrograma = this;
            //this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Ya esta abierto el programa", "Error", 0);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    private void crearEvento() {
        String nombre = fldNombreEvento.getText();
        String descripcion = taDescripcionEvento.getText();
        Date fecha = calendarioEvento.getDate();
        //vamos a foramtear el objeto date del spiner para que solo nos de la hora
        String patron = "HH:mm";
        SimpleDateFormat formato = new SimpleDateFormat(patron);
        Date aux = (Date) spnHora.getModel().getValue();
        System.out.println("hora: " + formato.format(aux));
        //
        String hora = formato.format(aux);
        boolean prioritario = chbPrioritarioEvento.isSelected();
        Evento eve = new Evento(nombre, descripcion, fecha, hora, prioritario);
        Eventos.getEventos().add(eve);
        JOptionPane.showMessageDialog(this, "Evento Creado con exito\n"
                + "Fecha: " + fecha.toLocaleString(), "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarListaEventos() {
        DefaultTableModel tm = Manager.limpiarDefaultTableModel((DefaultTableModel) tbEventos.getModel());
        for (Evento ev : Eventos.getEventos()) {
            tm.addRow(ev.getDatos());
        }

    }
    private void btnCrearEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearEventoActionPerformed
        crearEvento();
        actualizarListaEventos();

    }//GEN-LAST:event_btnCrearEventoActionPerformed

    private void btnAgregarProductoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoVentaActionPerformed
        boolean existe = false;
        DefaultTableModel tm = (DefaultTableModel) tbVenta.getModel();

        String datos[] = new String[4];

        datos[0] = fldRefVenta.getText();
        datos[1] = spnCantidadVenta.getValue().toString();
        datos[2] = fldPrecioVentaProductoFinal.getText();

        if (!datos[0].equals("")) {
            for (int i = 0; i < tm.getRowCount(); i++) {
                if (tm.getValueAt(i, 0).equals(datos[0]) && tm.getValueAt(i, 2).equals(datos[2])) {
                    System.out.println("Encontrada la coincidencia");
                    int antiguo = Integer.valueOf((String) tm.getValueAt(i, 1));
                    int nuevo = Integer.valueOf(datos[1]);
                    int suma = antiguo + nuevo;
                    tm.setValueAt(String.valueOf(suma), i, 1);
                    existe = true;
                }
            }
            if (!existe) {
                tm.addRow(datos);
            }
            sumatorio = 0;
            for (int k = 0; k < tm.getRowCount(); k++) {
                float precio = Float.parseFloat(String.valueOf(tm.getValueAt(k, 2)));
                float cantidad = Float.parseFloat(String.valueOf(tm.getValueAt(k, 1)));
                sumatorio += precio * cantidad;

            }

            fldPrecioTotalVenta.setText(String.valueOf(sumatorio) + " €");

            tbVenta.setModel(tm);
        } else {
            JOptionPane.showMessageDialog(this, "No hay referencia");
        }
    }//GEN-LAST:event_btnAgregarProductoVentaActionPerformed

    private void btnVenderTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenderTodoActionPerformed
        crearVenta();
        actualizarTablaVentas();
        limpiarCamposVenta();
    }//GEN-LAST:event_btnVenderTodoActionPerformed
    public void setDNIClienteEnVenta(String DNI) {
        tfClienteVenta.setText(DNI);
    }
    private void btnAbriSeleccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbriSeleccionClienteActionPerformed
        if (viewSelectorCliente == null || !viewSelectorCliente.isVisible()) {
            viewSelectorCliente = new ViewSelectorCliente();
            viewSelectorCliente.setVisible(true);
            viewSelectorCliente.ventanaPadreViewPrograma = this;
            //this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "La ventana ya esta abierta", "Error", 0);
        }
    }//GEN-LAST:event_btnAbriSeleccionClienteActionPerformed
    public void ImprimirVentaSelected(){
        try {
            int id = Integer.valueOf(String.valueOf(tbVentas.getModel().getValueAt(tbVentas.getSelectedRow(), 0)));
            GestionReportes.ImprimirVenta(id);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Selecciona una venta", "Error", 0);
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ImprimirVentaSelected();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        actualizarTablaVentas();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            eliminarVenta();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnEliminarSeleccionadoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarSeleccionadoVentaActionPerformed
        eliminarProductoVenta();
    }//GEN-LAST:event_btnEliminarSeleccionadoVentaActionPerformed

    private void btnLimpiarCamposVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCamposVentaActionPerformed
        limpiarCamposVenta();
    }//GEN-LAST:event_btnLimpiarCamposVentaActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        actualizarListaEventos();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void limpiarCamposEvento() {
        fldNombreEvento.setText("");
        fldDescuentoVenta.setText("");
        chbPrioritarioEvento.setSelected(false);
    }
    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCamposEvento();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        int id = Integer.valueOf((String) tbEventos.getModel().getValueAt(tbEventos.getSelectedRow(), 0));
        if (id >= 0) {
            Eventos.removeEventoByID(id);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una elemento de la lista para eliminarlo",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
        actualizarListaEventos();
    }//GEN-LAST:event_jButton31ActionPerformed

    private void spnCantidadVentaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnCantidadVentaStateChanged
        JSpinner spn = (JSpinner) evt.getSource();
        if ((int) spn.getValue() < 0) {
            spnCantidadVenta.setValue(0);
        }
    }//GEN-LAST:event_spnCantidadVentaStateChanged

    private void cbDescuentoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDescuentoVentaActionPerformed
        fldDescuentoVenta.setText(cbDescuentoVenta.getSelectedItem().toString());
        float precio = Float.parseFloat(fldPVPVenta.getText().split(" ")[0].replaceAll(",", "."));
        String aux = fldDescuentoVenta.getText();
        if (aux.equals("")) {
            aux = "0";
        }
        float descuento = Float.parseFloat(aux.split(" ")[0]);
        float precioFinal = precio - ((descuento * precio) / 100);
        fldPrecioVentaProductoFinal.setText(precioFinal + "");
    }//GEN-LAST:event_cbDescuentoVentaActionPerformed
    
    public void setRefAlbaran(String ref){
        fldRefAlbaran.setText(ref);
    }
    private void btnProveedorSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorSelectorActionPerformed
        if (proveedoresView == null || !proveedoresView.isVisible()) {
            proveedoresView = new SelectorProveedoresView();
            proveedoresView.setVisible(true);
            proveedoresView.ventanaPadreViewPrograma = this;
            //this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "La ventana ya esta abierta", "Error", 0);
        }
    }//GEN-LAST:event_btnProveedorSelectorActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (aboutView == null || !aboutView.isVisible()) {
            aboutView = new AboutView();
            aboutView.setVisible(true);
            aboutView.ventanaPadreViewPrograma = this;
        } else {
            JOptionPane.showMessageDialog(this, "La ventana ya esta abierta", "Error", 0);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    public void deleteAbout(){
        this.aboutView =null;
    }
    private void ponLaAyuda() {
        try {
            // Carga el fichero de ayuda
            File fichero = new File("./help/help_set.hs");
            URL hsURL = fichero.toURI().toURL();

            // Crea el HelpSet y el HelpBroker
            HelpSet helpset = new HelpSet(getClass().getClassLoader(), hsURL);
            helpset.setTitle("Ayuda SublimeManagement");
            HelpBroker hb = helpset.createHelpBroker();

            // Pone ayuda al pulsar el botón y a F1 en la ventana.
            hb.enableHelpOnButton(miAyuda, "aplicacion", helpset);
            hb.enableHelpKey(this.getContentPane(), "aplicacion", helpset);
            hb.setSize(new Dimension(1650, 900));

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (HelpSetException e) {
            e.printStackTrace();
        }
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
            System.load("./lib/*");
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewPrograma().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Compras;
    private javax.swing.JPanel Facturas;
    private javax.swing.JButton btnAbriSeleccionCliente;
    private javax.swing.JButton btnAbriSeleccionRef;
    private javax.swing.JButton btnAbriSeleccionRef2;
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnAgregarProductoVenta;
    private javax.swing.JButton btnBucarCliente;
    private javax.swing.JButton btnCargarTodosEnStock;
    private javax.swing.JButton btnCargarTodosProductos;
    private javax.swing.JButton btnComprar;
    private javax.swing.JButton btnCrearEvento;
    private javax.swing.JButton btnCrearProducto;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarSeleccionadoVenta;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiarCamposVenta;
    private javax.swing.JButton btnProveedorSelector;
    private javax.swing.JButton btnVenderTodo;
    private com.toedter.calendar.JCalendar calendarioEvento;
    private javax.swing.JCheckBox cbClienteVenta;
    private javax.swing.JComboBox<String> cbDescuentoVenta;
    private javax.swing.JCheckBox cbProveedorCliente;
    private javax.swing.JCheckBox chbPrioritarioEvento;
    private javax.swing.JButton fldAgregarCompra;
    private javax.swing.JTextField fldApellidoCliente;
    private javax.swing.JTextField fldBuscarProductoEnStockPorNombre;
    private javax.swing.JTextField fldBuscarProductoEnStockPorRef;
    private javax.swing.JTextField fldBuscarProductoPorNombre;
    private javax.swing.JTextField fldBuscarProductoPorReferencia;
    private javax.swing.JTextField fldCantidadInicial;
    private javax.swing.JFormattedTextField fldDescuentoVenta;
    private javax.swing.JTextField fldDireccionCliente;
    private javax.swing.JTextField fldDniCliente;
    private javax.swing.JTextField fldLocalidadCliente;
    private javax.swing.JTextField fldMailCliente;
    private javax.swing.JTextField fldNombreCliente;
    private javax.swing.JTextField fldNombreEvento;
    private javax.swing.JTextField fldNombreProducto;
    private javax.swing.JFormattedTextField fldPVPVenta;
    private javax.swing.JTextField fldPrecioCompra;
    private javax.swing.JTextField fldPrecioProducto;
    private javax.swing.JTextField fldPrecioTotal;
    private javax.swing.JFormattedTextField fldPrecioTotalVenta;
    private javax.swing.JTextField fldPrecioVentaProductoFinal;
    private javax.swing.JTextField fldRefAlbaran;
    private javax.swing.JTextField fldRefCompra;
    private javax.swing.JTextField fldRefProducto;
    private javax.swing.JTextField fldRefVenta;
    private javax.swing.JTextField fldTelefonoCliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel jpClientes;
    private javax.swing.JLabel lbInfoAlbaran;
    private javax.swing.JLabel lbInfoCliente;
    private javax.swing.JLabel lbInfoEvento;
    private javax.swing.JLabel lbInfoVenta;
    private javax.swing.JLabel lbNumeroLetrasEvento;
    private javax.swing.JLabel lbNumeroPalabrasEvento;
    private javax.swing.JLabel lbProductoEliminado;
    private javax.swing.JLabel lbProductosTotales;
    private javax.swing.JMenuItem miAyuda;
    private javax.swing.JMenuItem miInforme1;
    private javax.swing.JMenuItem miInforme2;
    private javax.swing.JMenuItem miInforme3;
    private javax.swing.JMenuItem miInforme4;
    private javax.swing.JPanel panelCuentas;
    private javax.swing.JPanel pnDatosClienteEnVenta;
    private javax.swing.JPanel pnVentas;
    private javax.swing.JSpinner spnCantidadCompras;
    private javax.swing.JSpinner spnCantidadVenta;
    private javax.swing.JSpinner spnHora;
    private javax.swing.JTextArea taDescripcionEvento;
    private javax.swing.JTable tbAlbaranes;
    private javax.swing.JTable tbClientes;
    private javax.swing.JTable tbCompras;
    private javax.swing.JTable tbCrearProducto;
    private javax.swing.JTable tbDatosAlbaran;
    private javax.swing.JTable tbDatosVenta;
    private javax.swing.JTable tbEventos;
    private javax.swing.JTable tbStock;
    private javax.swing.JTable tbVenta;
    private javax.swing.JTable tbVentas;
    private javax.swing.JTextField tfClienteVenta;
    // End of variables declaration//GEN-END:variables

    private SqlManager.serviceComprobarCambios servicio;
    private static ViewConfig viewConfig;
    private static ViewSelectorCliente viewSelectorCliente;
    private static SelectorProveedoresView proveedoresView;
    private static AboutView aboutView;
}
