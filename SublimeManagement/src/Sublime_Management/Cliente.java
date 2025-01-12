package Sublime_Management;


import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marce
 */
public class Cliente {
    private static int contadorId=0;
    
    private String id,nombre,apellido,dni,telefono,mail,localidad,direccion;
    private boolean proveedor;
    
    public Cliente(String nom, String ap, String dni, String tel,String mail,String loc,String dir, boolean pro){
        this.id=String.valueOf(Cliente.contadorId);
        this.nombre=nom;
        this.apellido=ap;
        this.dni=dni;
        this.telefono = tel;
        this.mail =mail;
        this.localidad = loc;
        this.direccion = dir;
        this.proveedor =pro;
        
        contadorId++;
        
    }
    public Cliente(){
        this.id=String.valueOf(Cliente.contadorId);
        contadorId++;
    }
    
    
    public String[] getDatos(){
        String datos[];
        if(proveedor){
            String [] s = {this.nombre,this.apellido, this.dni,this.telefono,"si"};
            datos = s;
        }else {
            String [] s = {this.nombre,this.apellido, this.dni,this.telefono,"no"};
            datos = s;
        }
        
        
        return datos;
    }
    public static void setContadorClientes(int c){
        contadorId = c;
    }
    public String[] getDatosWithId(){
        String datos[];
        if(proveedor){
            String [] s = {this.id,this.nombre,this.apellido, this.dni,this.telefono,this.mail,this.localidad,this.direccion,"si"};
            datos = s;
        }else {
            String [] s = {this.id,this.nombre,this.apellido, this.dni,this.telefono,this.mail,this.localidad,this.direccion,"no"};
            datos = s;
        }
        
        
        return datos;
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isProveedor() {
        return proveedor;
    }

    public void setProveedor(boolean proveedor) {
        this.proveedor = proveedor;
    }

    public static int getContadorId() {
        return contadorId;
    }

    public static void setContadorId(int aContadorId) {
        contadorId = aContadorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    

    
    
}
