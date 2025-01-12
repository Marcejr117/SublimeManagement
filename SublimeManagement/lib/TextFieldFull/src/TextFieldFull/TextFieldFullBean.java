/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextFieldFull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import javax.swing.JTextField;

/**
 *
 * @author rbaum
 */
public class TextFieldFullBean extends JTextField implements Serializable, KeyListener {
    private Color colorNormal = Color.WHITE;
    private Color colorFull = new Color (153, 153, 153);
    private Color colorTextFull = Color.WHITE;
    
    public TextFieldFullBean() {
        this.setText(" ");
        this.setPreferredSize(new Dimension (100,30));
        this.setSize(70, 30);
        this.addKeyListener(this);
    }

    public Color getColorNormal() {
        return colorNormal;
    }

    public void setColorNormal(Color colorNormal) {
        this.colorNormal = colorNormal;
        this.setBackground(this.colorNormal);
    }

    public Color getColorFull() {
        return colorFull;
    }

    public void setColorFull(Color colorFull) {
        this.colorFull = colorFull;
    }

    public Color getColorTextFull() {
        return colorTextFull;
    }

    public void setColorTextFull(Color colorTextFull) {
        this.colorTextFull = colorTextFull;
        this.setForeground(this.colorTextFull);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        this.setBackground(colorFull);
        this.setForeground(colorTextFull);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        
    }
    
}
