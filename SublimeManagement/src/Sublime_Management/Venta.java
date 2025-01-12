/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sublime_Management;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;

public class Venta {
    private static int contadorID;
    private int id;
    private List<Producto> producto;
    private Cliente cliente;
    private List<Integer> cantidad;
    private List<Float> precioProProducto;
    private LocalDate FechaVenta;
    
    public Venta(){
    }
    public Venta(List<Producto> producto, Cliente cliente, List<Integer> cantidad, List<Float> precioProProducto) {
        this.id = Venta.contadorID;
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.precioProProducto = precioProProducto;
        Venta.contadorID++;
        FechaVenta = LocalDate.now();
    }
    public Venta(int id, List<Producto> producto, Cliente cliente, List<Integer> cantidad, List<Float> precioProProducto, LocalDate FechaV) {
        this.id = id;
        this.producto = producto;
        this.cliente = cliente;
        this.cantidad = cantidad;
        this.precioProProducto = precioProProducto;
        this.FechaVenta = FechaV;
    }

    public LocalDate getFechaVenta() {
        return FechaVenta;
    }

    public void setFechaVenta(LocalDate FechaVenta) {
        this.FechaVenta = FechaVenta;
    }
    
    
    
    public static int getContadorID() {
        return contadorID;
    }

    public static void setContadorID(int contadorID) {
        Venta.contadorID = contadorID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Producto> getProducto() {
        return producto;
    }

    public void setProducto(List<Producto> producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Integer> getCantidad() {
        return cantidad;
    }

    public void setCantidad(List<Integer> cantidad) {
        this.cantidad = cantidad;
    }

    public List<Float> getPrecioProProducto() {
        return precioProProducto;
    }

    public void setPrecioProProducto(List<Float> precioProProducto) {
        this.precioProProducto = precioProProducto;
    }
    
    
    
}
