/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sublime_Management;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Marce
 */
public class DataType_Config {

    private boolean habilitarBDDLocal, habilitarBDDOnline, habilitarDatosEmp;
    private String direccionBDD, puertoBDD, usuarioBDD, passBDD,nombreBDD, idioma, nombreEmp, DireccionEmp, tefEmp, emailEmp;
    private int tema;
    private File logo;
    public ArrayList<String[]> login;
    public ArrayList<String> correos;

    public DataType_Config() {
        habilitarBDDLocal = true;
        habilitarBDDOnline = false;
        direccionBDD = SqlManager.direccionBDD;
        puertoBDD = SqlManager.puertoBDD;
        usuarioBDD = SqlManager.usuarioBDD;
        passBDD = SqlManager.passBDD;
        nombreBDD = SqlManager.nombreBDD;

    }

    public DataType_Config(Boolean habiliarBDDLocal, Boolean habilitarBDDOnline, Boolean habilitarDatosEmp, String direccionBDD, String puertoBDD, String usuarioBDD, String passBDD, String nombreBDD, String idioma, String nombreEmp, String DireccionEmp, String tefEmp, String emailEmp, int tema, File logo, ArrayList<String[]> login, ArrayList<String> correos) {
        this.habilitarBDDLocal = habiliarBDDLocal;
        this.habilitarBDDOnline = habilitarBDDOnline;
        this.habilitarDatosEmp = habilitarDatosEmp;
        this.direccionBDD = direccionBDD;
        this.puertoBDD = puertoBDD;
        this.usuarioBDD = usuarioBDD;
        this.passBDD = passBDD;
        this.nombreBDD = nombreBDD;
        this.idioma = idioma;
        this.nombreEmp = nombreEmp;
        this.DireccionEmp = DireccionEmp;
        this.tefEmp = tefEmp;
        this.emailEmp = emailEmp;
        this.tema = tema;
        this.logo = logo;
        this.login = login;
        this.correos = correos;
    }

    public String getNombreBDD() {
        return nombreBDD;
    }

    public void setNombreBDD(String nombreBDD) {
        this.nombreBDD = nombreBDD;
    }
    
    public Boolean getHabilitarBDDLocal() {
        return habilitarBDDLocal;
    }

    public void setHabilitarBDDLocal(Boolean habilitarBDDLocal) {
        this.habilitarBDDLocal = habilitarBDDLocal;
    }

    public Boolean getHabilitarBDDOnline() {
        return habilitarBDDOnline;
    }

    public void setHabilitarBDDOnline(Boolean habilitarBDDOnline) {
        this.habilitarBDDOnline = habilitarBDDOnline;
    }

    public Boolean getHabilitarDatosEmp() {
        return habilitarDatosEmp;
    }

    public void setHabilitarDatosEmp(Boolean habilitarDatosEmp) {
        this.habilitarDatosEmp = habilitarDatosEmp;
    }

    public String getDireccionBDD() {
        return direccionBDD;
    }

    public void setDireccionBDD(String direccionBDD) {
        this.direccionBDD = direccionBDD;
    }

    public String getPuertoBDD() {
        return puertoBDD;
    }

    public void setPuertoBDD(String puertoBDD) {
        this.puertoBDD = puertoBDD;
    }

    public String getUsuarioBDD() {
        return usuarioBDD;
    }

    public void setUsuarioBDD(String usuarioBDD) {
        this.usuarioBDD = usuarioBDD;
    }

    public String getPassBDD() {
        return passBDD;
    }

    public void setPassBDD(String passBDD) {
        this.passBDD = passBDD;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getNombreEmp() {
        return nombreEmp;
    }

    public void setNombreEmp(String nombreEmp) {
        this.nombreEmp = nombreEmp;
    }

    public String getDireccionEmp() {
        return DireccionEmp;
    }

    public void setDireccionEmp(String DireccionEmp) {
        this.DireccionEmp = DireccionEmp;
    }

    public String getTefEmp() {
        return tefEmp;
    }

    public void setTefEmp(String tefEmp) {
        this.tefEmp = tefEmp;
    }

    public String getEmailEmp() {
        return emailEmp;
    }

    public void setEmailEmp(String emailEmp) {
        this.emailEmp = emailEmp;
    }

    public int getTema() {
        return tema;
    }

    public void setTema(int tema) {
        this.tema = tema;
    }

    public File getLogo() {
        return logo;
    }

    public void setLogo(File logo) {
        this.logo = logo;
    }

    public ArrayList<String[]> getLogin() {
        return login;
    }

    public void setLogin(ArrayList<String[]> login) {
        this.login = login;
    }

    public ArrayList<String> getCorreos() {
        return correos;
    }

    public void setCorreos(ArrayList<String> correos) {
        this.correos = correos;
    }

}
