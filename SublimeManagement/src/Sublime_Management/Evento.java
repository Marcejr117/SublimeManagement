/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sublime_Management;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Marce
 */
public class Evento {
    private static int IDActualEvento;
    private String nombre,descripcion, hora;
    private Date fecha;
    private boolean prioritario;
    private int ID;

    public Evento(String nombre, String descripcion, Date fecha, String hora, boolean prioritario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.prioritario = prioritario;
        ID = IDActualEvento;
        IDActualEvento++;
    }

    public Evento( int ID, String nombre, String descripcion, Date fecha, String hora, boolean prioritario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.prioritario = prioritario;
        this.ID = ID;
    }

    public static int getIDActualEvento() {
        return IDActualEvento;
    }

    public static void setIDActualEvento(int IDActualEvento) {
        Evento.IDActualEvento = IDActualEvento;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
     
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isPrioritario() {
        return prioritario;
    }

    public void setPrioritario(boolean prioritario) {
        this.prioritario = prioritario;
    }
    public String[] getDatos(){
        String patron = "dd-MM-YYYY";
        SimpleDateFormat formato = new SimpleDateFormat(patron);
        String prio="";
        if(prioritario){
            prio = "SI";
        }else{
            prio = "NO";
        }
        String datos[] = new String[]{ID+"",nombre,descripcion,formato.format(fecha),hora,prio};
        return datos;
    }
}
