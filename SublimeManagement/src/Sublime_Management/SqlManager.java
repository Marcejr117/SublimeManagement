package Sublime_Management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.db4o.ObjectSet;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Marce
 */
public class SqlManager {

    public static String tablasCreadas = "";
    public static ObjectContainer db;
    public static String direccionBDD = "LocalHost", nombreBDD = "sublime_management", puertoBDD = "3306", usuarioBDD = "root", passBDD = "";
    public static String rutaSQL = "jdbc:mysql://" + direccionBDD + ":" + puertoBDD;
    public static String rutaBDD = "jdbc:mysql://" + direccionBDD + ":" + puertoBDD + "/sublime_management";
    public static boolean servicio = false;
    public static boolean isLoggin = false;

    public static void testConnectionBDD(String testRuta, String testNombre, String testPuerto, String testUser, String testPass) {
        try {
            String testDireccion = "jdbc:mysql://" + testRuta + ":" + testPuerto + "/" + testNombre;
            Connection conexion;
            conexion = DriverManager.getConnection(testDireccion, testUser, testPass);
            JOptionPane.showMessageDialog(null, "Conexion Correcta!", "Informacion", JOptionPane.PLAIN_MESSAGE);
            conexion.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al establecer la conexion", "Advertencia", JOptionPane.ERROR_MESSAGE);

        }
    }

    public static void ajustarDatosBDD() {
        DataType_Config dt = Config_Management.ObtenerDatosConfig();
        String direccion = dt.getDireccionBDD();
        String puerto = dt.getPuertoBDD();
        String nombre = dt.getNombreBDD();
        nombreBDD = nombre;
        SqlManager.rutaSQL = "jdbc:mysql://" + direccion + ":" + puerto;
        SqlManager.rutaBDD = "jdbc:mysql://" + direccion + ":" + puerto + "/" + nombre;
        SqlManager.usuarioBDD = dt.getUsuarioBDD();
        SqlManager.passBDD = dt.getPassBDD();

    }

    //crea la base de datos que va a usar la apliacacion
    //solo se usa para hacer los resportes ya que los datos al cargar la app de nuevo los cogue de una clase de datos enbebida
    public static void crearBDD() throws SQLException {
        String borrarBDD = "drop DATABASE IF EXISTS`" + nombreBDD + "`";
        String crearBDD = "CREATE DATABASE IF NOT EXISTS `" + nombreBDD + "`";
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(rutaSQL, usuarioBDD, passBDD);
            Statement sentencia = conexion.createStatement();
            if (servicio) {
                sentencia.executeUpdate(borrarBDD);
            }

            sentencia.executeUpdate(crearBDD);

            conexion.close();
        } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex) {
            JOptionPane.showMessageDialog(null, "No se puedo establer conexion con la base de datos\nComprueba tu conexion a internet", "Error", JOptionPane.ERROR_MESSAGE);
        }

        //System.out.println("Base de datos creada");
    }

    public static void eliminarBDD() throws SQLException {
        String borrarBDD = "drop DATABASE IF EXISTS`" + nombreBDD + "`";
        Connection conexion;

        conexion = DriverManager.getConnection(rutaSQL, usuarioBDD, passBDD);
        Statement sentencia = conexion.createStatement();
        sentencia.executeUpdate(borrarBDD);

    }

    //crea la tabla cliente en nuestra base de datos
    public static void crearTablaCliente() throws SQLException {
        Connection conexion;

        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);

        Statement sentencia = conexion.createStatement();

        //System.out.println(" creando tabla Cliente....");
        sentencia.executeUpdate("drop table if exists Cliente");
        sentencia.executeUpdate("create table Cliente(\n"
                + "    id varchar(10),\n"
                + "    nombre varchar(30),\n"
                + "    apellido varchar(30),\n"
                + "    dni varchar(30),\n"
                + "    telefono varchar(30),\n"
                + "    mail varchar(30),\n"
                + "    localidad varchar(30),\n"
                + "    direccion varchar(50),\n"
                + "    proveedor BOOLEAN,\n"
                + "    PRIMARY KEY(dni)"
                + ");");

        conexion.close();
        tablasCreadas += "\n\t Cliente";
        //System.out.println("tabla Cliente creada");
    }
    //crea la tabla productos en la base de datos

    public static void crearTablaProducto() throws SQLException {
        Connection conexion;

        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);

        Statement sentencia = conexion.createStatement();

        //System.out.println(" creando tabla Producto....");
        sentencia.executeUpdate("drop table if exists Producto");
        sentencia.executeUpdate("create table Producto(\n"
                + "    ref varchar(20),\n"
                + "    nombre varchar(30),\n"
                + "    pvp varchar(10),\n"
                + "    cantidad varchar(10),\n"
                + "    PRIMARY KEY(ref)\n"
                + "    "
                + "    "
                + "    );");

        conexion.close();
        tablasCreadas += "\n\t Producto";
        //System.out.println("tabla Producto creada");
    }

    //crea la tabla albaran en la base de datos
    public static void crearTablaAlbaran() throws SQLException {
        Connection conexion;

        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);

        Statement sentencia = conexion.createStatement();

        //System.out.println("creando tabla Albaran....");
        sentencia.executeUpdate("drop table if exists Albaran");
        sentencia.executeUpdate("create table Albaran(\n"
                + "    ID int(4),"
                + "    refAlbaran varchar(20),\n"
                + "    producto varchar(20),\n"
                + "    cantidad int(10),\n"
                + "    precio FLOAT(15,2),\n"
                + "    PRIMARY KEY(ID,refAlbaran,producto,precio),\n"
                + "    FOREIGN KEY(producto) REFERENCES producto(ref) ON DELETE CASCADE \n"
                + "    "
                + "    "
                + "    );");

        tablasCreadas += "\n\t Albaran";
        conexion.close();

        //System.out.println("tabla Albaran creada");
    }

    public static void crearTablaVenta() throws SQLException {
        Connection conexion;

        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);

        Statement sentencia = conexion.createStatement();

        //System.out.println("creando tabla Venta....");
        sentencia.executeUpdate("drop table if exists Venta");
        sentencia.executeUpdate("create table Venta(\n"
                + "    ID int(4),\n"
                + "    cliente varchar(30),\n"
                + "    producto varchar(20),\n"
                + "    cantidad int(10),\n"
                + "    precio FLOAT(15,2),\n"
                + "    fechaVenta Date,\n"
                + "    PRIMARY KEY (ID,producto,precio),\n"
                + "    FOREIGN KEY (producto) REFERENCES producto(ref) ON DELETE CASCADE,\n"
                + "    FOREIGN KEY (cliente) REFERENCES cliente(dni) on delete set null\n"
                + "    );");

        tablasCreadas += "\n\t Venta";
        conexion.close();
    }

    public static void crearTablaEvento() throws SQLException {
        Connection conexion;

        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);

        Statement sentencia = conexion.createStatement();

        //System.out.println("creando tabla Venta....");
        sentencia.executeUpdate("drop table if exists Evento");
        sentencia.executeUpdate("create table Evento(\n"
                + "    ID int(4),\n"
                + "    Nombre varchar(50),"
                + "    Descripcion varchar(200),"
                + "    hora varchar(10),"
                + "    fechaEvento Date,\n"
                + "    Prioritario BOOLEAN,"
                + "    PRIMARY KEY (ID)\n"
                + "    );");

        tablasCreadas += "\n\t Evento";
        conexion.close();
    }

    //metodo que actualiza los datos que hay en la base de datos mySql cogiendo los datos de la app
    public static void cargarDatosEnBDD() throws SQLException {
        int cont = 0;
        Connection conexion;
        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);
        PreparedStatement psCliente;
        psCliente = conexion.prepareStatement("insert into Cliente (id,nombre,apellido,dni,telefono,mail,localidad,direccion,proveedor)  values(?,?,?,?,?,?,?,?,?)");
        //cargamos Clientes
        System.out.println("\n\n\n\n=========Cargando Datos Clientes==========");
        Iterator<Cliente> itClientes = Clientes.getClientes().iterator();

        while (itClientes.hasNext()) {
            Cliente aux = itClientes.next();

            psCliente.setString(1, aux.getId());
            psCliente.setString(2, aux.getNombre());
            psCliente.setString(3, aux.getApellido());
            psCliente.setString(4, aux.getDni());
            psCliente.setString(5, aux.getTelefono());
            psCliente.setString(6, aux.getMail());
            psCliente.setString(7, aux.getLocalidad());
            psCliente.setString(8, aux.getDireccion());
            psCliente.setBoolean(9, aux.isProveedor());
            psCliente.execute();
            cont++;
        }
        System.out.println(cont + ": Registros insertados");

        System.out.println("===================");

        //cargamos Productos
        PreparedStatement psProducto;
        psProducto = conexion.prepareStatement("insert into producto (ref,nombre,pvp,cantidad) values(?,?,?,?)");
        cont = 0;
        System.out.println("=========Cargando Datos Productos==========");
        Iterator<Producto> itProcutos = Productos.getProductos().iterator();

        while (itProcutos.hasNext()) {
            Producto aux = itProcutos.next();
            psProducto.setString(1, aux.getRef());
            psProducto.setString(2, aux.getNombre());
            psProducto.setString(3, aux.getPvp());
            psProducto.setString(4, aux.getCantidad());

            psProducto.execute();
            cont++;
        }
        System.out.println(cont + ": Registros insertados");

        System.out.println("===================");

        //cargamos Albaran
        PreparedStatement psAlbaran;
        psAlbaran = conexion.prepareStatement("insert into Albaran (ID,refAlbaran,producto,cantidad,precio) values(?,?,?,?,?)");
        cont = 0;
        System.out.println("=========Cargando Datos Albaran==========");
        Iterator<Albaran> itAlbaranes = Albaranes.getAlbaranes().iterator();

        while (itAlbaranes.hasNext()) {
            int i = 0;
            Albaran aux = itAlbaranes.next();
            Iterator<Producto> itProductosAlbaran = aux.getProducto().iterator();
            while (itProductosAlbaran.hasNext()) {
                Producto p = itProductosAlbaran.next();
                psAlbaran.setInt(1, aux.getId());
                psAlbaran.setString(2, aux.getRefAlbaran());
                psAlbaran.setString(3, p.getRef());
                psAlbaran.setInt(4, aux.getCantidad().get(i));
                psAlbaran.setFloat(5, aux.getPrecio().get(i));
                i++;
                psAlbaran.execute();
            }

            cont++;
        }
        System.out.println(cont + ": Registros insertados");

        System.out.println("===================");

        //cargamos Venta
        PreparedStatement psVenta;
        psVenta = conexion.prepareStatement("insert into Venta (ID,cliente,producto,cantidad,precio,fechaVenta) values(?,?,?,?,?,?)");
        cont = 0;
        System.out.println("=========Cargando Datos Venta==========");
        Iterator<Venta> itVenta = Ventas.getVentas().iterator();

        while (itVenta.hasNext()) {
            int i = 0;
            Venta aux = itVenta.next();
            Iterator<Producto> itProductosVenta = aux.getProducto().iterator();

            while (itProductosVenta.hasNext()) {
                Producto p = itProductosVenta.next();
                psVenta.setInt(1, aux.getId());
                try {
                    psVenta.setString(2, aux.getCliente().getDni());
                } catch (java.lang.NullPointerException ex) {
                    psVenta.setString(2, null);
                    System.out.println(ex.getMessage());
                }

                psVenta.setString(3, p.getRef());
                psVenta.setInt(4, aux.getCantidad().get(i));
                psVenta.setFloat(5, aux.getPrecioProProducto().get(i));
                psVenta.setDate(6, Date.valueOf(aux.getFechaVenta()));
                i++;
                psVenta.execute();
            }

            cont++;
        }
        System.out.println(cont + ": Registros insertados");

        System.out.println("===================");

        //cargamos Eventos
        PreparedStatement psEvento;
        psEvento = conexion.prepareStatement("insert into Evento (ID,Nombre,Descripcion,hora,fechaEvento,Prioritario) values(?,?,?,?,?,?)");
        cont = 0;
        System.out.println("=========Cargando Datos Evento==========");
        Iterator<Evento> itEvento = Eventos.getEventos().iterator();

        while (itEvento.hasNext()) {
            int i = 0;
            Evento aux = itEvento.next();
            psEvento.setInt(1, aux.getID());
            psEvento.setString(2, aux.getNombre());
            psEvento.setString(3, aux.getDescripcion());
            psEvento.setString(4, aux.getHora());
            java.sql.Date fecha = new java.sql.Date(aux.getFecha().getTime());  //convertir Java.util.Date a Java.sql.Date
            psEvento.setDate(5, fecha);
            psEvento.setBoolean(6, aux.isPrioritario());
            psEvento.execute();
            cont++;

        }
        System.out.println(cont + ": Registros insertados");

        conexion.close();

    }

    //metodo principal que se ejecuta para obtener toda la estructura de datos
    public static void actualizarDatosEnBDD() throws SQLException {//guarda los datos en la bse de datos 
        crearBDD();

        crearTablaCliente();
        crearTablaProducto();
        crearTablaAlbaran();
        crearTablaVenta();
        crearTablaEvento();
        cargarDatosEnBDD();

    }

    //pemite guardar Cliente productos y albaranes de forma local
    public static void guardarEnLocal() {
        String LocalBDD = "Sublime_Management.db";
        if (db == null) {
            System.out.println("no se ha abierto la base de datos local");
        } else {
            try {
                db.close();
                Path path = Paths.get("./Sublime_Management.db");
                Files.delete(path);//elimina la antigua base de datos local
            } catch (Exception e) {
                System.out.println("No se ha eliminado le archivo local: " + e.getLocalizedMessage());
            }
            //guardarClientes();
            //guardarProductos();
            //guardarAlabranes();

            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), LocalBDD);//crea el archivo de base de datos
            db.store(new Datos());

        }
        System.out.println("==Guardado en Local==");
    }

    //obtiene los datos de la base de datos local y los carga en la app
    public static void cargarDeLocal2() {

        String LocalBDD = "Sublime_Management.db";
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), LocalBDD);
        ObjectSet<Datos> resultados = db.query(Datos.class);
        System.out.println("antes" + resultados.size());
        //        if(resultados.size()==0){
        //            Usuarios.getUsuarios().add(new Usuario("admin@admin.com", "admin"));
        //        }
        if (resultados.hasNext()) {
            Datos datos = resultados.next();
            if (!isLoggin) {
                //clientes
                Iterator<Cliente> itC = datos.getClientes().iterator();
                int idC = 0;
                while (itC.hasNext()) {
                    Cliente c = itC.next();
                    Clientes.addClienteToClientes(c);
                    if (Integer.valueOf(c.getId()) > idC) {
                        idC = Integer.valueOf(c.getId());
                    }
                }
                Cliente.setContadorClientes(idC + 1);

                //productos
                Productos.getProductos().addAll(datos.getProductos());
                //albaranes
                Albaranes.getAlbaranes().addAll(datos.getAlbaranes());
                int idA = 0;
                Iterator<Albaran> itA = datos.getAlbaranes().iterator();

                while (itA.hasNext()) {
                    Albaran a = itA.next();

                    if (a.getId() > idA) {
                        idA = a.getId();
                    }
                }

                Albaran.setContadorID(idA + 1);

                //ventas
                Ventas.getVentas().addAll(datos.getVentas());
                int idV = 0;
                Iterator<Venta> itV = datos.getVentas().iterator();

                while (itV.hasNext()) {
                    Venta a = itV.next();
                    if (a.getId() > idV) {
                        idV = a.getId();
                    }
                }

                Venta.setContadorID(idV + 1);

                //Eventos
                Eventos.getEventos().addAll(datos.getEventos());
                int idE = 0;
                Iterator<Evento> itE = datos.getEventos().iterator();

                while (itE.hasNext()) {
                    Evento e = itE.next();

                    if (e.getID() > idE) {
                        idE = e.getID();
                    }
                }

                Evento.setIDActualEvento(idE + 1);
            }
            if (isLoggin) {
                //usuarios
                boolean hasUserDefault = false;

                Usuarios.getUsuarios().addAll(datos.getUsuarios());
                System.out.println("Numero de Usuarios: " + Usuarios.getUsuarios().size());
                int idU = 0;
                Iterator<Usuario> itU = datos.getUsuarios().iterator();

                while (itU.hasNext()) {
                    Usuario u = itU.next();
                    if (u.getID() > idU) {
                        idU = u.getID();
                    }
                    if (u.getCorreo().equals("admin@admin.com")) {
                        hasUserDefault = true;
                    }
                }
                System.out.println("valor de has: " + hasUserDefault);
                if (!hasUserDefault) {
                    System.out.println("Crea usu en 1");
                    Usuarios.getUsuarios().add(new Usuario("admin@admin.com", "admin", true));
                }
                Usuario.setContadorID(idU + 1);
            }

            db.commit();
            db.close();

        } else {
            boolean crear = false;
            for (Usuario ej : Usuarios.getUsuarios()) {
                if (ej.getCorreo().equals("admin@admin.com")) {
                    crear = true;

                }
                System.out.println("aaaaa");
            }

            if (!crear) {
                System.out.println("Crea uso en 2");
                Usuarios.getUsuarios().add(new Usuario("admin@admin.com", "admin", true));
            }

        }

    }

    //carga los datos de la base de datos online a la app
    public static void cargarDeOnline2() throws SQLException {
        Connection conexion;
        conexion = DriverManager.getConnection(rutaBDD, usuarioBDD, passBDD);
        String leerClientes = "select * from Cliente";
        String leerAlbaranes = "select * from Albaran";
        String leerProductos = "select * from Producto";
        String leerVentas = "select * from Venta";
        String leerEventos = "select * from Evento";

        Statement sentencia = conexion.createStatement();
        ResultSet rs;
        //cliente
        rs = sentencia.executeQuery(leerClientes);
        while (rs.next()) {
            String nombre = rs.getString(2);
            String apellido = rs.getString(3);
            String dni = rs.getString(4);
            String telefono = rs.getString(5);
            String mail = rs.getString(6);
            String localidad = rs.getString(7);
            String direccion = rs.getString(8);
            boolean proveedor = rs.getBoolean(9);
            Cliente aux = new Cliente(nombre, apellido, dni, telefono, mail, localidad, direccion, proveedor);
            Clientes.addClienteToClientes(aux);
        }
        //producto
        rs = sentencia.executeQuery(leerProductos);
        while (rs.next()) {
            String nombre = rs.getString(1);
            String ref = rs.getString(2);
            String pvp = rs.getString(3);
            String cantidad = rs.getString(4);
            Producto aux = new Producto(nombre, ref, pvp, cantidad);
            Productos.addProductoToProductos(aux);
        }
        //albaranes
        rs = sentencia.executeQuery(leerAlbaranes);
        int IDActual = 0;
        String refActual = "";
        Float importet = 0f;
        List<Producto> listaP = new ArrayList<Producto>();
        List<Integer> listaC = new ArrayList<Integer>();
        List<Float> listaPre = new ArrayList<Float>();

        while (rs.next()) {
            int id = rs.getInt(1);
            String refA = rs.getString(2);

            if (id == IDActual) {
                Producto p = Productos.getProductoByReference2(rs.getString(3));

                Integer c = rs.getInt(4);
                Float pre = rs.getFloat(5);
                importet += c * pre;
                listaP.add(p);
                listaC.add(c);
                listaPre.add(pre);
                refActual = refA;
            } else {
                if (listaP.size() != 0) {
                    Albaran a = new Albaran(IDActual, listaP, listaC, listaPre, refActual, importet);
                    Albaranes.addAlbaranToAlbaranes(a);
                    listaP = new ArrayList<Producto>();
                    listaC = new ArrayList<Integer>();
                    listaPre = new ArrayList<Float>();
                    importet = 0f;
                }
                rs.previous();
                IDActual = id;
            }

        }
        if (listaP.size() != 0) {
            Albaran a = new Albaran(IDActual, listaP, listaC, listaPre, refActual, importet);
            Albaranes.addAlbaranToAlbaranes(a);
            listaP = new ArrayList<Producto>();
            listaC = new ArrayList<Integer>();
            listaPre = new ArrayList<Float>();
            Albaran.setContadorID(IDActual + 1);
        }
        //rs.pevious();

        //ventas
        rs = sentencia.executeQuery(leerVentas);
        int IDActualVenta = 0;

        LocalDate fechaVen = null;
        Cliente cli = null;
        listaP = new ArrayList<Producto>();
        listaC = new ArrayList<Integer>();
        listaPre = new ArrayList<Float>();

        while (rs.next()) {
            int id = rs.getInt(1);

            if (id == IDActualVenta) {
                cli = Clientes.getClientesByDNI(rs.getString(2));
                Producto p = Productos.getProductoByReference2(rs.getString(3));

                Integer c = rs.getInt(4);
                Float pre = rs.getFloat(5);
                Date auxiliar = new Date(rs.getDate(6).getTime());
                fechaVen = auxiliar.toLocalDate();

                listaP.add(p);
                listaC.add(c);
                listaPre.add(pre);
            } else {
                if (listaP.size() != 0) {
                    Venta a = new Venta(IDActualVenta, listaP, cli, listaC, listaPre, fechaVen);
                    Ventas.addVentaToVentas(a);
                    listaP = new ArrayList<Producto>();
                    listaC = new ArrayList<Integer>();
                    listaPre = new ArrayList<Float>();
                }
                rs.previous();
            }
            IDActualVenta = id;

        }
        if (listaP.size() != 0) {
            Venta a = new Venta(IDActualVenta, listaP, cli, listaC, listaPre, fechaVen);
            Ventas.addVentaToVentas(a);
            listaP = new ArrayList<Producto>();
            listaC = new ArrayList<Integer>();
            listaPre = new ArrayList<Float>();
            Venta.setContadorID(IDActualVenta + 1);
        }

        //rs.pevious();
        //Evento
        rs = sentencia.executeQuery(leerEventos);
        int IDActualEvento = 0;

        while (rs.next()) {
            int ID = rs.getInt(1);
            String nombre = rs.getString(2);
            String descripcion = rs.getString(3);
            String hora = rs.getString(4);
            Date fechaEvento = rs.getDate(5);
            boolean prio = rs.getBoolean(6);
            Evento aux = new Evento(ID, nombre, descripcion, fechaEvento, hora, prio);
            Eventos.getEventos().add(aux);
            if (ID > IDActualEvento) {
                IDActualEvento = ID;
            }

        }
        Evento.setIDActualEvento(IDActualEvento + 1);
        conexion.close();
    }

    //este tipo de dato lo creo para concentrar los LinkedList<> que tengo repartidas por las clases 
    //ya que estos son estaticos y da problemas al guardarlos
    public static class Datos {

        private LinkedList<Cliente> clientes;
        private LinkedList<Producto> productos;
        private List<Albaran> albaranes;
        private List<Venta> ventas;
        private List<Evento> eventos;
        private List<Usuario> usuarios;

        public Datos() {
            clientes = Clientes.getClientes();
            productos = Productos.getProductos();
            albaranes = Albaranes.getAlbaranes();
            ventas = Ventas.getVentas();
            eventos = Eventos.getEventos();
            usuarios = Usuarios.getUsuarios();

        }

        public LinkedList<Cliente> getClientes() {
            return clientes;
        }

        public void setClientes(LinkedList<Cliente> clientes) {
            this.clientes = clientes;
        }

        public LinkedList<Producto> getProductos() {
            return productos;
        }

        public void setProductos(LinkedList<Producto> productos) {
            this.productos = productos;
        }

        public List<Albaran> getAlbaranes() {
            return albaranes;
        }

        public void setAlbaranes(List<Albaran> albaranes) {
            this.albaranes = albaranes;
        }

        public List<Venta> getVentas() {
            return ventas;
        }

        public void setVentas(List<Venta> ventas) {
            this.ventas = ventas;
        }

        public List<Evento> getEventos() {
            return eventos;
        }

        public void setEventos(List<Evento> eventos) {
            this.eventos = eventos;
        }

        public List<Usuario> getUsuarios() {
            return usuarios;
        }

        public void setUsuarios(List<Usuario> usuarios) {
            this.usuarios = usuarios;
        }

    }

    //servicio que cada segundo guarda los cambios en la base de datos MySql
    public static class serviceComprobarCambios extends Thread {//permite hacer guardados en la base de datos Xampp

        public serviceComprobarCambios() {
        }

        public void run() {
            try {
                while (true) {
                    SqlManager.servicio = true;
                    actualizarDatosEnBDD();//coge los datos de los arraylist y los pasa a la base de datos en sql
                    //guardarEnLocal();
                    sleep(2000);
                }

            } catch (InterruptedException ex) {
                
            } catch (SQLException ex) {
                int i = JOptionPane.showConfirmDialog(null, "No se puedo establecer conexion con la base de datos Online\n"
                        + "Desea Continuar?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        System.exit(1);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
