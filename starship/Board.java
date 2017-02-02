/*IDEAS:
   When a structure is being built have the dots be replaced 
   with a number that represents the amount of turns left
   
   When you click enter on a space, it shows what you can make on 
   bottom of the screen
   */
  
/*Class Description:
   This is the class the controls the board 
   */

 
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Dictionary;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
public class Board extends BuildMenu
{
    static int screenSizeX = 800;
    static int screenSizeY = 500;
    final int spaceBetweenPoints = 10;
    //List<Shape> gridOvals = new ArrayList<>();
    Shape[][] gridOvals = new Shape[Math.round(screenSizeX/spaceBetweenPoints)][Math.round(screenSizeY/spaceBetweenPoints)];
    // this is which oval is selected
    
    HashMap<String, Integer> selectedOval = new HashMap<> ();
    
    
    public Board()
    {
        super();
        setSize(Board.screenSizeX,Board.screenSizeY+200);
        setVisible(true);
        selectedOval.put("x",20);
        selectedOval.put("y",Math.round(screenSizeY/spaceBetweenPoints));   
    }
    public void drawGrid(Graphics g){
        
        // no idea what this does
        Graphics2D g2 = (Graphics2D) g;
        int x = 0;
        int y = 0;
        // puts the ovals in the gridovals array
        while (y < screenSizeY){
            while(x < screenSizeX){
                Shape oval = new Ellipse2D.Double(x,y,5,5);
                gridOvals[Math.round(x/spaceBetweenPoints)][Math.round(y/spaceBetweenPoints)] = oval;
                x = x + spaceBetweenPoints;     
            }
            x = 0;
            y = y + spaceBetweenPoints;
        }
        // draws out the gridOvals array
        x = 0;
        y = 0;
        while (y < screenSizeY){
            while(x < screenSizeX){
                g2.setColor(Color.black);
                if (Math.round(x/spaceBetweenPoints) == selectedOval.get("x") ) {
                    if (Math.round(y/spaceBetweenPoints) == selectedOval.get("y") ) {
                        g2.setColor(Color.red);
                    }
                }
                
                g2.fill(gridOvals[Math.round(x/spaceBetweenPoints)][Math.round(y/spaceBetweenPoints)]);
                x = x + spaceBetweenPoints;
            }
            x = 0;
            y = y + spaceBetweenPoints;
        }
    }
    public void drawBoard(Graphics g){
        //what does this do
        super.paint(g);
        drawGrid(g);
    }
    public void paint(Graphics g){
        super.paint(g);
        drawBoard(g);
        drawMenu(g);
    }
    
}
