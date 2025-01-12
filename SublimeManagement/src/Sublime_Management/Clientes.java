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
public class Clientes {
    //uso "LinkedList por que es mas eficiente"
    private static LinkedList<Cliente> clientes= new LinkedList<Cliente>();
    
    public static void addClienteToClientes(Cliente aux){
        getClientes().add(aux);
    }
    
    public static boolean RemoveClienteFromCLientesByID(int identificador){
        int contador=0;
        boolean resultado=false;
        for(Cliente aux : getClientes()){
            if(Integer.parseInt(aux.getId())==identificador){
                getClientes().remove(aux);
                resultado=true;
            }else{
                resultado=false;
            }
            contador++;
        }
        return resultado;
    }
    public static boolean RemoveClienteFromCLientesByPosition(int posicion){
        
        boolean resultado=true;
        try{
            getClientes().remove(posicion);
            
        }catch(Exception ex){
            resultado=false;
        }
        return resultado;
    }
    

    public static LinkedList<Cliente> getClientes() {
        return clientes;
    }

    public static void setClientes(LinkedList<Cliente> aClientes) {
        clientes = aClientes;
    }
    public static Cliente findClientByID(String id){
        Cliente aux = null;
        for(Cliente c : getClientes()){
            if(aux.getId().equals(id)){
                aux = c;
            }
        }
        return aux;
        
    }
    public static Cliente getClientesByDNI(String DNI){
        Cliente client= null;
        
        for (Cliente aux : clientes){
            if(aux.getDni().equals(DNI) ){
                client=aux;
            }
        }
        return client;
    }
    
    
}
