/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sublime_Management;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static jdk.nashorn.internal.objects.Global.print;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Marce
 */
public class GestionReportes {
    
    public static void reporte1(){
        int seleccion;
        Connection conn = null;
        
        // Opciones del menú principal
            String[] opciones = {"<html>1.- Generar informe de<br/>Todos los albaranes", "<html>2.- Salir"};
        // El menú principal se repite, mientras no pulse SALIR
                seleccion = JOptionPane.showOptionDialog(null, "Seleccione un opción", "Menú de opciones", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
                    switch (seleccion) {
                        case 0:
                            try {
                                
                                Class.forName("com.mysql.jdbc.Driver");
                                conn = DriverManager.getConnection(SqlManager.rutaBDD,SqlManager.usuarioBDD,SqlManager.passBDD);//esto hay que cambiarlo si queremos que funcionen los reportes con otras base de datos
                                
                                //especificamos sobre que jasper vamos a trabajar
                                JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("src/reportes/report1.jasper");
                                //creamos el print 
                                JasperPrint jasperPrint =  JasperFillManager.fillReport(reporte, null,conn);
                                //objeto exportador a pdf
                                Exporter exporter =new JRPdfExporter();
                                //agregamos cual sera la entrada en este caso el pint
                                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                                //agregamos la salida
                                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("./miReporte1.pdf"));
                                //extraemos le pdf
                                exporter.exportReport();
                                //permite ver desde la propia app
                                JasperViewer.viewReport(jasperPrint, false);
                            } catch (ClassNotFoundException ex) {
                                System.out.println("algo salio mal: "+ex.getLocalizedMessage());
                            } catch (JRException ex) {
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Para imprimir los informes se necesita\nuna base de datos online configurada","Error",JOptionPane.WARNING_MESSAGE);
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
            
    }
    public static void reporte2(){
        int seleccion;
        Connection conn = null;
        
         // Opciones del menú principal
            String[] opciones = {"<html>1.- Generar informe de<br/>Todos los clientes", "<html>2.- Salir"};
        // El menú principal se repite, mientras no pulse SALIR
            
                seleccion = JOptionPane.showOptionDialog(null, "Seleccione un opción", "Menú de opciones", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
                    switch (seleccion) {
                        case 0:
                            try {
                                
                                Class.forName("com.mysql.jdbc.Driver");
                                conn = DriverManager.getConnection(SqlManager.rutaBDD,SqlManager.usuarioBDD,SqlManager.passBDD);
                                
                                
                                JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("src/reportes/report2.jasper");
                                JasperPrint jasperPrint =  JasperFillManager.fillReport(reporte, null,conn);
                                Exporter exporter =new JRPdfExporter();
                                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("./miReporte2.pdf"));
                                exporter.exportReport();
                                JasperViewer.viewReport(jasperPrint, false);
                            } catch (ClassNotFoundException ex) {
                                System.out.println("algo salio mal"+ex.getLocalizedMessage());
                            } catch (JRException ex) {
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Para imprimir los informes se necesita\nuna base de datos online configurada","Error",JOptionPane.WARNING_MESSAGE);
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
    }
    public static void reporte3(){
        int seleccion;
        Connection conn = null;
        
         // Opciones del menú principal
            String[] opciones = {"<html>1.- Generar informe <br/>Grafico de los productos", "<html>2.- Salir"};
        // El menú principal se repite, mientras no pulse SALIR
            
                seleccion = JOptionPane.showOptionDialog(null, "Seleccione un opción", "Menú de opciones", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
                    switch (seleccion) {
                        case 0:
                            try {
                                
                                Class.forName("com.mysql.jdbc.Driver");
                                conn = DriverManager.getConnection(SqlManager.rutaBDD,SqlManager.usuarioBDD,SqlManager.passBDD);
                                
                                
                                JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("src/reportes/report3.jasper");
                                JasperPrint jasperPrint =  JasperFillManager.fillReport(reporte, null,conn);
                                Exporter exporter =new JRPdfExporter();
                                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("./miReporte3.pdf"));
                                exporter.exportReport();
                                JasperViewer.viewReport(jasperPrint, false);
                               
                            } catch (ClassNotFoundException ex) {
                                System.out.println("algo salio mal"+ex.getLocalizedMessage());
                            } catch (JRException ex) {
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Para imprimir los informes se necesita\nuna base de datos online configurada","Error",JOptionPane.WARNING_MESSAGE);
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
    }
    
    public static void reporte4(){
        int seleccion;
        Connection conn = null;
        String refAlb="";
         // Opciones del menú principal
            String[] opciones = {"<html>1.- Generar informe <br/>Grafico de los productos", "<html>2.- Salir"};
        // El menú principal se repite, mientras no pulse SALIR
            
                seleccion = JOptionPane.showOptionDialog(null, "Seleccione un opción", "Menú de opciones", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
                    switch (seleccion) {
                        case 0:
                            refAlb = JOptionPane.showInputDialog("Referencia de Albaran: ");
                            try {
                                
                                Class.forName("com.mysql.jdbc.Driver");
                                conn = DriverManager.getConnection(SqlManager.rutaBDD,SqlManager.usuarioBDD,SqlManager.passBDD);
                                
                                Map parametros = new HashMap();
                                parametros.put("pRefAlbaran",refAlb);
                                JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("src/reportes/report4.jasper");
                                JasperPrint jasperPrint =  JasperFillManager.fillReport(reporte, parametros,conn);
                                Exporter exporter =new JRPdfExporter();
                                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("./miReporte4.pdf"));
                                exporter.exportReport();
                                JasperViewer.viewReport(jasperPrint, false);
                               
                            } catch (ClassNotFoundException ex) {
                                System.out.println("algo salio mal"+ex.getLocalizedMessage());
                            } catch (JRException ex) {
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Para imprimir los informes se necesita\nuna base de datos online configurada","Error",JOptionPane.WARNING_MESSAGE);
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
    }
    
    public static void ImprimirVenta(int valor){
        int seleccion;
        Connection conn = null;
         // Opciones del menú principal
            String[] opciones = {"<html>1.- Generar informe <br/>Ticket Venta", "<html>2.- Salir"};
        // El menú principal se repite, mientras no pulse SALIR
            
                seleccion = JOptionPane.showOptionDialog(null, "Seleccione un opción", "Menú de opciones", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
                    switch (seleccion) {
                        case 0:
                            //refAlb = JOptionPane.showInputDialog("Referencia de Albaran: ");
                            try {
                                
                                Class.forName("com.mysql.jdbc.Driver");
                                conn = DriverManager.getConnection(SqlManager.rutaBDD,SqlManager.usuarioBDD,SqlManager.passBDD);
                                
                                Map parametros = new HashMap();
                                parametros.put("idVenta",valor);
                                //ahora vamos a poner los datos de la empresa
                                DataType_Config configuracion = Config_Management.ObtenerDatosConfig();
                                if(configuracion.getHabilitarDatosEmp()){
                                    parametros.put("PNombreEmpresa",configuracion.getNombreEmp());
                                    parametros.put("PDireccionEmpresa",configuracion.getDireccionEmp());
                                    parametros.put("PTelefonoEmpresa",configuracion.getTefEmp());
                                    parametros.put("PEmailEmpresa",configuracion.getEmailEmp());
                                    parametros.put("PLogoEmpresa", configuracion.getLogo());
                                }else{
                                    parametros.put("PNombreEmpresa"," ");
                                    parametros.put("PDireccionEmpresa"," ");
                                    parametros.put("PTelefonoEmpresa"," ");
                                    parametros.put("PEmailEmpresa"," ");
                                    parametros.put("PLogoEmpresa",null);
                                }
                                //
                                JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("src/reportes/RecivoVenta.jasper");
                                JasperPrint jasperPrint =  JasperFillManager.fillReport(reporte, parametros,conn);
                                Exporter exporter =new JRPdfExporter();
                                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("./RecivoVenta.pdf"));
                                exporter.exportReport();
                                JasperViewer.viewReport(jasperPrint, false);
                               
                            } catch (ClassNotFoundException ex) {
                                System.out.println("algo salio mal"+ex.getLocalizedMessage());
                            } catch (JRException ex) {
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Para imprimir los informes se necesita\nuna base de datos online configurada","Error",JOptionPane.WARNING_MESSAGE);
                                Logger.getLogger(GestionReportes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
    }
}
