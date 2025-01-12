// 
// Decompiled by Procyon v0.5.36
// 

package Sublime_Management;

public class Usuario
{
    public static int contadorID;
    public String correo;
    public String password;
    public int ID;
    public boolean changePassword;
    
    public Usuario() {
    }
    
    public Usuario(final String nombre, final String password) {
        this.correo = nombre;
        this.password = password;
        this.ID = Usuario.contadorID;
        ++Usuario.contadorID;
        this.changePassword = false;
    }
    
    public Usuario(final String nombre, final String password, final boolean changepass) {
        this.correo = nombre;
        this.password = password;
        this.ID = Usuario.contadorID;
        ++Usuario.contadorID;
        this.changePassword = changepass;
    }
    
    public static int getContadorID() {
        return Usuario.contadorID;
    }
    
    public static void setContadorID(final int contadorID) {
        Usuario.contadorID = contadorID;
    }
    
    public int getID() {
        return this.ID;
    }
    
    public void setID(final int ID) {
        this.ID = ID;
    }
    
    public String getCorreo() {
        return this.correo;
    }
    
    public void setCorreo(final String correo) {
        this.correo = correo;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public boolean isChangePassword() {
        return this.changePassword;
    }
    
    public void setChangePassword(final boolean changePassword) {
        this.changePassword = changePassword;
    }
}
