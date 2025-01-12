// 
// Decompiled by Procyon v0.5.36
// 

package Sublime_Management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Usuarios
{
    public static List<Usuario> usuarios;
    
    public static boolean removeUsuariosByID(final int id) {
        boolean respuesta = false;
        Usuario sup = null;
        for (final Usuario aux : Usuarios.usuarios) {
            if (aux.getID() == id) {
                sup = aux;
                respuesta = true;
            }
        }
        Usuarios.usuarios.remove(sup);
        return respuesta;
    }
    
    public static Usuario getUsuarioByID(final int id) {
        Usuario aux = null;
        for (final Usuario u : Usuarios.usuarios) {
            if (u.getID() == id) {
                aux = u;
            }
        }
        return aux;
    }
    
    public static List<Usuario> getUsuarios() {
        return Usuarios.usuarios;
    }
    
    public static void setUsuarios(final List<Usuario> usuarios) {
        Usuarios.usuarios = usuarios;
    }
    
    static {
        Usuarios.usuarios = new ArrayList<Usuario>();
    }
}
