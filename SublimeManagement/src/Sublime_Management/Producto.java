package Sublime_Management;


import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marce
 */
public class Producto {
    private String nombre;
    private String Ref;
    private String pvp;
    private String cantidad="0";
    public Producto(){
    }
    public Producto(String n,String r,String pvp, String cantidad){
        
        this.nombre = n;
        this.Ref =r;
        this.pvp =pvp;
        this.cantidad = cantidad;
        
    }
    public Producto(String datos[]){
        
        this.nombre = datos[0];
        this.Ref =datos[1];
        this.pvp =datos[2];
        this.cantidad = datos[3];
        
    }
    
    
    
    public String[] getDatos(){
        String aux[] = {this.nombre,this.Ref,this.cantidad,this.pvp};
        return  aux;
    }
    
    
    

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRef() {
        return Ref;
    }

    public void setRef(String Ref) {
        this.Ref = Ref;
    }

    public String getPvp() {
        return pvp;
    }

    public void setPvp(String pvp) {
        this.pvp = pvp;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    
    
    
}
