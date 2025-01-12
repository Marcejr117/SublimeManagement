/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sublime_Management;


import java.util.LinkedList;


/**
 *
 * @author Marce_Lite
 */
public class Productos {
    private static LinkedList<Producto> productos = new LinkedList<>();
    public static void addProductoToProductos(Producto p){
        productos.add(p);
    }
    
    public static boolean isExists(String r){
        boolean exists=false;
        for (Producto aux : getProductos()){
            if(aux.getRef().equals(r)){
                exists=true;
            }
        }
        return exists;        
    }
    
    public static Producto getProductoByReference(String ref){
        Producto aux = null;
        for(Producto p : getProductos()){
            if(p.getRef().matches(".*"+ref+".*")){
                aux = p;
            }
        }
        return aux;
    }
    public static Producto getProductoByReference2(String ref){
        Producto aux = null;
        for(Producto p : getProductos()){
            if(p.getRef().equals(ref)){
                aux = p;
            }
        }
        return aux;
    }

    public static boolean RemoveProductoByReference(String referencia){
        boolean exito = false;
        for(Producto p : getProductos()){
            if(p.getRef().equals(referencia)){
                productos.remove(p);
                exito= true;
            }
        }
        return exito;
        
        
    }
    public static void setProductos(LinkedList aProductos) {
        Productos.productos = aProductos;
    }

    public static LinkedList<Producto> getProductos() {
        return productos;
    }
    public static LinkedList<Producto> getProductosConStock(){
        LinkedList<Producto> llp = new LinkedList<Producto>();
        
        for(Producto aux : Productos.getProductos()){
            if(Integer.valueOf(aux.getCantidad())>0){
                llp.add(aux);
            }
        }
        return llp;
    }
    public static LinkedList<Producto> getProductosSinStock(){
        LinkedList<Producto> llp = new LinkedList<Producto>();
        
        for(Producto aux : Productos.getProductos()){
            if(Integer.valueOf(aux.getCantidad())<=0){
                llp.add(aux);
            }
        }
        return llp;
    }
    
}
