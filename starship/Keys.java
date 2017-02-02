/* TO DO: set make it so x and y cant increase out of bounds */
import java.awt.*;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class Keys
{
    KeyStuff selector=new KeyStuff();
    Board gameBoard = new Board();
    
    public Keys()
    {
        gameBoard.addKeyListener(selector); 
    }
    
    private class KeyStuff implements KeyListener
    {
         
         public void keyPressed(KeyEvent e)  
         {
             int oldX = gameBoard.selectedOval.get("x");
             int oldY = gameBoard.selectedOval.get("y");
             if (e.getKeyCode() == KeyEvent.VK_RIGHT )  
             {
                 gameBoard.selectedOval.put("x",oldX + 1);
             } 
             else if (e.getKeyCode() == KeyEvent.VK_LEFT ) 
             {
                gameBoard.selectedOval.put("x",oldX - 1);         
             }
             else if (e.getKeyCode() == KeyEvent.VK_UP ) 
             {
                // INFO: for y, lower y is higher on the grid
                gameBoard.selectedOval.put("y",oldY - 1);
             }
             else if (e.getKeyCode() == KeyEvent.VK_DOWN ) 
             {
                // INFO: for y, lower y is higher on the grid
                gameBoard.selectedOval.put("y",oldY + 1);
             }
             else if (e.getKeyCode() == KeyEvent.VK_SPACE)
             {
                 
             }
             gameBoard.repaint();
         }
         public void keyReleased(KeyEvent e)
         {
             
         }
         public void keyTyped(KeyEvent e)
         {
             
         }   
    }
    public static void main(String arg[]){ 
        new Keys();
    }
}

