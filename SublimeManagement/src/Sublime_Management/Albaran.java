/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sublime_Management;

import java.util.List;

/**
 *
 * @author Marce_Lite
 */
public class Albaran {
    private static int contadorID;
    private int id;
    private List<Producto> producto;
    private List<Integer> cantidad;
    private List<Float> precio;
    private String refAlbaran;
    private float importeTotal;
    
    public Albaran(){
    }
    public Albaran(List<Producto> poducto,List<Integer> cantidad,List<Float> precio,String refAlbaran){
        this.id=Albaran.contadorID;
        this.producto=poducto;
        this.cantidad=cantidad;
        this.precio=precio;
        Albaran.contadorID++;
        this.refAlbaran = refAlbaran;
    }
    public Albaran(int id, List<Producto> poducto,List<Integer> cantidad,List<Float> precio,String refAlbaran){
        this.id=id;
        this.producto=poducto;
        this.cantidad=cantidad;
        this.precio=precio;
        if(id>contadorID){
            Albaran.contadorID = this.id;
        }
        
        this.refAlbaran = refAlbaran;
    }
    
    public Albaran(int id, List<Producto> poducto,List<Integer> cantidad,List<Float> precio,String refAlbaran,Float importeT){
        this.id=id;
        this.producto=poducto;
        this.cantidad=cantidad;
        this.precio=precio;
        if(id>contadorID){
            Albaran.contadorID = this.id;
        }
        
        this.refAlbaran = refAlbaran;
        this.importeTotal = importeT;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getContadorID() {
        return contadorID;
    }

    public static void setContadorID(int contadorID) {
        Albaran.contadorID = contadorID;
    }

    public List<Producto> getProducto() {
        return producto;
    }

    public void setProducto(List<Producto> producto) {
        this.producto = producto;
    }

    public List<Integer> getCantidad() {
        return cantidad;
    }

    public void setCantidad(List<Integer> cantidad) {
        this.cantidad = cantidad;
    }

    public List<Float> getPrecio() {
        return precio;
    }

    public void setPrecio(List<Float> precio) {
        this.precio = precio;
    }

    public String getRefAlbaran() {
        return refAlbaran;
    }

    public void setRefAlbaran(String refAlbaran) {
        this.refAlbaran = refAlbaran;
    }

    public float getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(float importeTotal) {
        this.importeTotal = importeTotal;
    }
    
    
    
}
