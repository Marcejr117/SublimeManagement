package Sublime_Management;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import jdk.nashorn.internal.runtime.Context;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Marce
 */
public class Manager {

    public static boolean crearProducto(String n, String r, String pvp, String cantidadIni) {
        if (Productos.getProductoByReference(r) == null && !r.equals("")) {

            Producto aux = new Producto(n, r, pvp, cantidadIni);
            Productos.addProductoToProductos(aux);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Ya Existe un producto con esta Referencia o No hay referencia", "Error al crear producto", 2);
            return false;
        }

    }

    public static LinkedList<Producto> buscarProductoPorNombre(String NombreProducto) {
        /*
        LinkedList<Producto> datos=new LinkedList<Producto>();
        for(Producto aux : Productos.getProductos()){
            if(aux.getNombre().equals(NombreProducto)) {
                datos.add(aux);
            }
        }
        return datos;
         */
        LinkedList<Producto> datos = new LinkedList<Producto>();
        for (Producto aux : Productos.getProductos()) {
            if (aux.getNombre().matches(".*" + NombreProducto + ".*")) {//usar matches me permite ver los que contiene la palabra que he introducido
                datos.add(aux);
            }
        }
        return datos;
    }

    public static LinkedList<Producto> buscarProductoPorNombreConStock(String NombreProducto) {
        LinkedList<Producto> datos = new LinkedList<Producto>();
        for (Producto aux : Productos.getProductosConStock()) {
            if (aux.getNombre().matches(".*" + NombreProducto + ".*")) {
                datos.add(aux);
            }
        }
        return datos;
    }

    public static LinkedList<Producto> buscarProductoPorNombreSinStock(String NombreProducto) {
        LinkedList<Producto> datos = new LinkedList<Producto>();
        for (Producto aux : Productos.getProductosSinStock()) {
            if (aux.getNombre().matches(".*" + NombreProducto + ".*")) {
                datos.add(aux);
            }
        }
        return datos;
    }

    public static LinkedList<Producto> buscarProductoPorReferencia(String refProducto) {
        LinkedList<Producto> datos = new LinkedList<Producto>();
        for (Producto aux : Productos.getProductos()) {
            if (aux.getRef().matches(".*" + refProducto + ".*")) {
                datos.add(aux);

            }
        }

        return datos;
    }

    public static DefaultTableModel limpiarDefaultTableModel(DefaultTableModel tm) {
        System.out.println("rows: " + tm.getRowCount());
        for (int i = tm.getRowCount(); i > 0; i--) {
            tm.removeRow(i - 1);
        }
        return tm;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String Desencriptar(String textoEncriptado) throws Exception {

        String secretKey = "qualityinfosolutions"; //llave para desenciptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }
    
    public static String getStrongPassword(){
        String[] symbols = {"0", "1", "-", "*", "%", "$", "a", "b", "c"};
        int length = 10;
        Random random;
        String password="";
        try {
            random = SecureRandom.getInstanceStrong();
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                 int indexRandom = random.nextInt ( symbols.length );
                 sb.append( symbols[indexRandom] );
            }
            password = sb.toString();
            System.out.println(password);
          } catch (NoSuchAlgorithmException e){
              System.out.println(e.toString());
          }
        return password;
    }

}
