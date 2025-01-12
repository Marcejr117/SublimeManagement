/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sublime_Management;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author Marce
 */
public class Config_Management {

    public static String config = "config.db";

    public static void GuardarDatosConfig(DataType_Config dataType_Config) {
        ObjectContainer db;
        Path fichero = Paths.get("./config.db");
        try {
            Files.delete(fichero);
            System.out.println("Borrado");
        } catch (IOException ex) {
            System.out.println("Error al borrar el archivo.");
        }

        

        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), config);//crea el archivo de base de datos
        db.store(dataType_Config);
        System.out.println("Configuracion Guardada");
        db.close();
    }

    public static DataType_Config ObtenerDatosConfig() {
        ObjectContainer db;
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), config);//crea el archivo de base de datos
        ObjectSet<DataType_Config> resultado = db.query(DataType_Config.class);

        DataType_Config configuracion = new DataType_Config();

        while (resultado.hasNext()) {
            configuracion = resultado.next();

        }
        db.close();
        return configuracion;
    }

    public static void AplicarTema(int op) {
        switch (op) {
            case 0:
                com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme.setup();
                UIManager.put("TextComponent.arc", 999);
                UIManager.put("Button.arc", 999);
                UIManager.put("Component.arc", 999);
                UIManager.put("ScrollBar.thumbArc", 999);
                break;
            case 1:
                com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme.setup();
                break;
            case 2:
                com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme.setup();
                break;
            case 3:
                com.formdev.flatlaf.FlatLightLaf.setup();
                break;
            case 4:
                com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme.setup();
                break;
            case 5:
                com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightContrastIJTheme.setup();
                break;
            case 6:
                com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme.setup();
                break;
            case 7:
                com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanContrastIJTheme.setup();
                break;

        }
    }
}
