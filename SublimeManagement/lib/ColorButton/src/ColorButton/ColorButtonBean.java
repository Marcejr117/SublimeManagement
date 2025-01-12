/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ColorButton;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

/**
 *
 * @author rbaum
 */
public class ColorButtonBean extends JButton implements MouseListener{
    private Color colorHover = Color.BLACK;
    private Color colorNormal = Color.WHITE;
    private Color colorTextHover = Color.WHITE;
    private Color colorTextNormal = Color.BLACK;
    
    public ColorButtonBean(){
        this.setContentAreaFilled(true);
        this.setOpaque(true);
        this.setBackground(colorNormal);
        this.setForeground(colorTextNormal);
        
        addMouseListener(this);
    }

    public Color getColorHover() {
        return colorHover;
    }

    public void setColorHover(Color colorHover) {
        this.colorHover = colorHover;
    }

    public Color getColorNormal() {
        return colorNormal;
    }

    public void setColorNormal(Color colorNormal) {
        this.colorNormal = colorNormal;
        this.setBackground(this.colorNormal);
    }

    public Color getColorTextHover() {
        return colorTextHover;
    }

    public void setColorTextHover(Color colorTextHover) {
        this.colorTextHover = colorTextHover;
    }

    public Color getColorTextNormal() {
        return colorTextNormal;
    }

    public void setColorTextNormal(Color colorTextNormal) {
        this.colorTextNormal = colorTextNormal;
        this.setForeground(this.colorTextNormal);
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        this.setBackground(colorHover);
        this.setForeground(colorTextHover);
    }

    @Override
    public void mouseExited(MouseEvent me) {
        this.setBackground(colorNormal);
        this.setForeground(colorTextNormal);
    }
    
}
