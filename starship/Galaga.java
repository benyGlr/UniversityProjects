 
import java.util.Iterator;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
public class Galaga extends JFrame
{
    
    boolean first=true;
    ImageIcon ourShip;
    ImageIcon enemyShip;
    ImageIcon winScreen;
    ImageIcon loseScreen;
    JLabel background;
    JLabel score;
    JLabel lives;
    JPanel swag;
    int shipX=281;
    int shipY=706;
    int bulletX=350;
    int bulletY=706;
    int dimensionX;
    Font font=new Font("Monotype Corsiva", Font.BOLD, 20);
    Color myColor=new Color(255, 255, 255, 0);
    KeyStuff selector=new KeyStuff();
    boolean laser=false;
    boolean collision=false;
    int countMoveX=0;
    int [] eX=new int[10];
    int [] eY=new int[10];
    boolean [] isDestroyed=new boolean[10];
    int points=0;
    int count=0;
    int amountShot=0;
    int bullet=8;
    int bCount;
    boolean win=false;
    int hitCount;
    
    public Galaga()
    {
        new JFrame();
        //JFrame myFrame=new JFrame();
        setTitle("Galaga");
        ourShip=new ImageIcon("spaceship.png");
        enemyShip=new ImageIcon("enemy.png");
        winScreen=new ImageIcon("winscreen.png");
        loseScreen=new ImageIcon("gameover.png");
        background=new JLabel(new ImageIcon("background.jpg"));
        lives=new JLabel("BULLETS:"+ bullet);
        score=new JLabel("Score: "+ points);
        swag=new JPanel();
        bCount=8;
        String name = JOptionPane.showInputDialog("Please input your name"); 
        //FileOutputStream(String name) 
        score.setOpaque(true);
        lives.setOpaque(true);
        swag.setOpaque(true);
        swag.setBackground(Color.BLACK);
        score.setBackground(Color.BLACK);
        lives.setBackground(Color.BLACK); 
        score.setForeground(Color.WHITE);
        lives.setForeground(Color.WHITE);
        lives.setFont(font);
        score.setFont(font);
        swag.add(score);
        swag.add(lives);
        add(swag, BorderLayout.NORTH);
        add(background); 
        setSize(900,860);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(selector); 
        
        for (count=0;count<5;count++)
        {
            eX[count]=0+(count*64);
            eY[count]=64;
        }
        for (count=0;count<5;count++)
        {
            eX[count+5]=25+(count*64);
            eY[count+5]=128;
        }
        for (count=0;count<10;count++)
        {
            isDestroyed[count]=false;
        }
    }   
    public Galaga(int x, int y)
    {
        
        new JFrame();
        //JFrame myFrame=new JFrame();
        setTitle("Galaga");
        ourShip=new ImageIcon("spaceship.png");
        enemyShip=new ImageIcon("enemy.png");
        winScreen=new ImageIcon("winscreen.png");
        loseScreen=new ImageIcon("gameover.png");
        background=new JLabel(new ImageIcon("background.jpg"));
        lives=new JLabel("BULLETS:"+ y);
        score=new JLabel("Score: "+ x);
        points=x;
        bullet=y;
        bCount=bullet;
        swag=new JPanel();
        
        score.setOpaque(true);
        lives.setOpaque(true);
        swag.setOpaque(true);
        swag.setBackground(Color.BLACK);
        score.setBackground(Color.BLACK);
        lives.setBackground(Color.BLACK); 
        score.setForeground(Color.WHITE);
        lives.setForeground(Color.WHITE);
        lives.setFont(font);
        score.setFont(font);
        swag.add(score);
        swag.add(lives);
        add(swag, BorderLayout.NORTH);
        add(background); 
        setSize(900,860);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(selector); 
        int randomX=(int)((Math.random()*400));
        int randomY=(int)((Math.random()*400));
        
        
          for (count=0;count<5;count++)
        {
            eX[count]=randomX+(count*64);
            eY[count]=randomY+64;
        }
        for (count=0;count<5;count++)
        {
            eX[count+5]=25+randomX+(count*64);
            eY[count+5]=randomY+128;
        }
        for (count=0;count<10;count++)
        {
            isDestroyed[count]=false;
        }
    }
    public void paint (Graphics g)
    {
        super.paint(g);
        dimensionX=getWidth();
        if ((shipX+137)>dimensionX)
        {
            shipX=dimensionX-137;
            bulletX=dimensionX-69;
            
        }
        else if (shipX<0)
        {
            shipX=0;
            bulletX=69;
        }
        g.drawImage(ourShip.getImage(),shipX, shipY, 137, 154, null);   
        drawLaser(g);
        drawEnemyShips(g);
        delay(20);
        repaint();
    }
    public void drawEnemyShips(Graphics g)
    {        
        if (countMoveX<65)
        {
            for (count=0;count<10;count++)
            {
                eX[count]=eX[count]+5;
            }
        }
        else 
        {
            for (count=0;count<10;count++)
            {
                eX[count]=eX[count]-5;
            }
        }
        countMoveX=countMoveX+5;
        if(countMoveX==130)
        {
            countMoveX=0;
        }
        for (count=0;count<10;count++)
        {
            if (isDestroyed[count]==false)
            {
                g.drawImage(enemyShip.getImage(),eX[count], eY[count], 64, 64, null);
            }
        }
        delay(100);         
    }
    public void drawLaser(Graphics g)
    {
        if (laser==true)
        {            
            if(hitCount>=3)
            {
                for (int i = bulletY; i>0; i-=20)
                {
                    g.setColor(Color.blue);
                    g.fillRect(bulletX-10,i,20,i-20);
                    g.drawImage(ourShip.getImage(),shipX, shipY, 137, 154, null);
                    for (count=0;count<10;count++)
                    {
                        g.drawImage(enemyShip.getImage(),eX[count], eY[count], 64, 64, null);
                    }
                    delay (10);  
                    for( count=0; count<10; count++)
                    {                   
                        if((bulletX-10)>(eX[count]) && (bulletX-10)<(eX[count]+64) || (bulletX+10)>(eX[count]) && (bulletX+10)<(eX[count]+64))
                        {
                            if(i>eY[count] && i<eY[count]+64)
                            {                                
                                bulletX=0;
                                bulletY=0;
                                drawCollision(g);                            
                            }
                        }                   
                    }
                    super.paint(g);
                    bulletX=shipX+69;    
                    bulletY=shipY;        
                }
                hitCount=0;
            }                      
            else if (hitCount<3)
            {
                for(int i=bulletY; i>0; i-=20)
                {                
                    g.setColor(Color.yellow);
                    g.drawLine(bulletX,i,bulletX,i-20);
                    g.drawImage(ourShip.getImage(),shipX, shipY, 137, 154, null);
                    for (count=0;count<10;count++)
                    {
                        g.drawImage(enemyShip.getImage(),eX[count], eY[count], 64, 64, null);
                    }
        
                    delay (10);   
                    for( count=0; count<10; count++)
                    {                    
                        if(bulletX>(eX[count]) && bulletX<(eX[count]+64))
                        {
                            if(i>eY[count] && i<eY[count]+64)
                            {                                
                                bulletX=0;
                                bulletY=0;
                                hitCount++;
                                drawCollision(g);  
                            
                            }
                        }                   
                    }
                    super.paint(g);
                    bulletX=shipX+69;    
                    bulletY=shipY;        
                }
            }
             bullet--; 

            lives.setText("BULLETS: "+bullet);
            if(bullet==0&& win==false)
            {                
                g.drawImage(loseScreen.getImage(), 0, 0, 900, 860, null);
                delay(5000);
                dispose();
            }
        }
    }
    public void drawWin(Graphics g)
    {
        g.drawImage(winScreen.getImage(), 0, 0, 900, 860, null);
        delay(5000);
        //pw.println(name+": "+points);
    }
    public void drawCollision(Graphics g)
    {   
        //System.out.println("hiiiiiii");
        int nuke =  (int)((Math.random()*200))+1;
        isDestroyed[count]=true;
        points=points+100; 
        score.setText("Score: "+points);
        eX[count]=-300;
        amountShot++;
        drawEnemyShips(g);
        if( nuke==1)
        {
            bCount--;
            points=points+2000;
            win=true;
            drawWin(g); 
            dispose();
            Galaga myGalaga=new Galaga(points, bCount);
        }     
        else if(amountShot==10)
        {
            bCount--;
            points=points+200*((int)Math.pow(2,bullet)-1);
            win=true;
            drawWin(g); 
            dispose();
            Galaga myGalaga=new Galaga(points, bCount);
            
        }
    }
    
    private class KeyStuff implements KeyListener
    {
         public void keyPressed(KeyEvent e)  
         {
             if (e.getKeyCode() == KeyEvent.VK_RIGHT )  
             {
                shipX=shipX+5;
                bulletX=bulletX+5;     
             } 
             else if (e.getKeyCode() == KeyEvent.VK_LEFT ) 
             {
                shipX=shipX-5;
                bulletX=bulletX-5;          
             }
             else if (e.getKeyCode() == KeyEvent.VK_SPACE)
             {
                 laser=true;
             }
             //repaint();
         }
         public void keyReleased(KeyEvent e)
         {
             if(e.getKeyCode() == KeyEvent.VK_SPACE)
             {
                 laser = false;
             }
             //repaint();
         }
         public void keyTyped(KeyEvent e)
         {
             
         }   
    }
    public void delay (int n)
    {
        long startDelay = System.currentTimeMillis();
        long endDelay=0;
        while (endDelay-startDelay < n)
            endDelay = System.currentTimeMillis();
    }
    public static void main(String[]args)throws Exception
    {
        new Galaga();
        BufferedReader br=new BufferedReader(new FileReader ("leaderboard.txt"));
        //PrintWriter pw=new PrintWriter(new FileWriter("leadboard.txt"));
    }
}
