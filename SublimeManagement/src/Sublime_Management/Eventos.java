/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sublime_Management;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Marce
 */
public class Eventos {
    private static List<Evento> eventos = new LinkedList<Evento>(); 
    

    public static List<Evento> getEventos() {
        return eventos;
    }

    public static void setEventos(List<Evento> eventos) {
        Eventos.eventos = eventos;
    }
    
    public static Evento getEventoByID(int id){
        Evento e = null;
        for (Evento aux : eventos){
            if(aux.getID() == id){
                e = aux;
            }
        }
        return e;
    }
    public static void removeEventoByID(int id){
        Evento e=null;
        for (Evento aux : eventos){
            if(aux.getID()==id)
                e = aux;
        }
        eventos.remove(e);
    }
    
}
