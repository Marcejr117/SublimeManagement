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
public class Albaranes {
    private static List<Albaran> albaranes = new LinkedList<>();
    public static void addAlbaranToAlbaranes(Albaran compra){
        Albaranes.albaranes.add(compra);
    }
    public static boolean removeAlbaranFromAlbaranesByID(int identificador){
        int contador=0;
        boolean resultado=false;
        for(Albaran aux : getAlbaranes()){
            if(aux.getId() == identificador){
                albaranes.remove(aux);
                resultado=true;
            }else{
                resultado=false;
            }
            contador++;
        }
        return resultado;
    }
    public static boolean removeAlbaranFromAlbaranesByPosition(int posicion){
        boolean resultado=true;
        try{
            getAlbaranes().remove(posicion);
            
        }catch(Exception ex){
            resultado=false;
        }
        return resultado;
    }

    public static List<Albaran> getAlbaranes() {
        return albaranes;
    }

    public static void setCompras(List<Albaran> aCompras) {
        albaranes = aCompras;
    }
    
    
}
