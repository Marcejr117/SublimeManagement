/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sublime_Management;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Marce_Lite
 */
public class Ventas {
    private static List<Venta> ventas = new LinkedList<>();
    
    public static void addVentaToVentas(Venta venta){
        Ventas.ventas.add(venta);
    }
    public static boolean removeVentaFromVentasByID(int identificador){
        int contador=0;
        boolean resultado=false;
        for(Venta aux : getVentas()){
            if(aux.getId() == identificador){
                getVentas().remove(aux);
                resultado=true;
            }else{
                resultado=false;
            }
            contador++;
        }
        return resultado;
    }
    public static boolean removeVentaFromVentasByPosition(int posicion){
        boolean resultado=true;
        try{
            getVentas().remove(posicion);
            
        }catch(Exception ex){
            resultado=false;
        }
        return resultado;
    }

    public static List<Venta> getVentas() {
        return ventas;
    }

    public static void setVentas(List<Venta> aVentas) {
        ventas = aVentas;
    }

   
}
