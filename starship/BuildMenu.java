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
public class BuildMenu extends JFrame
{
    JPanel buildMenu = new JPanel();
    JButton test = new JButton();
    public BuildMenu(){ 
        add(buildMenu, BorderLayout.SOUTH);
        //buildMenu.add(test);
        buildMenu.setPreferredSize(new Dimension(200, 200));
        buildMenu.setBackground(Color.red);
    }
    public void drawMenu(Graphics g){
        int x = 20;
        int y = 600;
        int width = 80;
        int height = 50;
        g.setColor(Color.WHITE);
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        g.drawString("TextToDraw", x , y + height/2);
    }
}
