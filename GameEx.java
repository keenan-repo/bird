

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;

import javax.imageio.ImageIO;

import java.io.*;
import java.awt.image.BufferedImage;

import javax.swing.WindowConstants;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GameEx extends JPanel {
	
	public VGTimerTask vgTask;
	public JFrame frame;
	public Rectangle screen, bird, wall, box, top, bot, left, right ;
	public Rectangle bounds ; 
	public Rectangle[] arr;
	public int x_pos=100 , y_pos=100, spdU, spdD, spdR, spdL ;
	public boolean[] keys = new boolean[256];
	public boolean keyL, keyR, keyU, keyD ;
	BufferedImage img;
	public int[][] map = new int[600][400];
	public int[][] unit = new int[50][48];	

	public GameEx()  {
		
		//System.out.println(Arrays.deepToString(getUnit(0,0)));

		KeyListener listener = new MyKeyListener();
		addKeyListener(listener);
		setFocusable(true);
		screen = new Rectangle(0, 0, 700, 500);
		bird = new Rectangle(x_pos,  y_pos, 10, 10);
		bounds = new Rectangle(50, 50, 600, 400);
		frame = new JFrame("Bird Game");
		box= new Rectangle(300,200,50,50);

		
		vgTask = new VGTimerTask();	
		
	    try {
	        img = ImageIO.read(new File("sprite.png"));
	    } catch (IOException e) {
	    }
		top= new Rectangle(getBirdX(),  getBirdY()-10, img.getWidth(), 10);
		bot= new Rectangle(getBirdX(),  getBirdY()+img.getHeight(), img.getWidth(), 10);
		left= new Rectangle(getBirdX()-10,  getBirdY(), 10, img.getHeight());
		right= new Rectangle(getBirdX()+img.getWidth(),  getBirdY(), 10, img.getHeight());
	}

	public void paint(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;


		
		//bounds = g.getClipBounds();


		g2d.clearRect(screen.x,  screen.y,  screen.width, screen.height);
		g2d.setColor(Color.blue);
		//g2d.fill(box);
		
		//bird is 50 wide by 48 height
		g2d.drawImage(img, getBirdX(),  getBirdY(), null);
		
		//top
		setTop(getBirdX(), getBirdY()-10, img.getWidth(), 10);
		g2d.draw(top);
		
		//bottom
		setBot(getBirdX(), getBirdY()+img.getWidth(), img.getWidth(), 10);
		g2d.draw(bot);
		
		//left
		setLeft(getBirdX()-10,  getBirdY(), 10, img.getHeight());
		g2d.draw(left);
		
		//right
		setRight(getBirdX()+img.getWidth(),  getBirdY(), 10, img.getHeight());
		g2d.draw(right);
		
		
		//box or any other stuff

		g2d.fill(box);
		
		g2d.draw(bounds);
		
		//System.out.println("x = " + getBirdX() + " y = " + getBirdY());
		}

	
	 void look() {
		 check(box);

		
	 }

	
	public void processInput() {
	    if(keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]){
	    	setBirdY(getBirdY()-spdU);
	    	System.out.println("moving");	        
	    } else {
	    	spdU=0;
	    }

	    if(keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]){
	        setBirdY(getBirdY()+spdD);
	    } else {
	    	spdD=0;
	    }

	    if(keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]){
	        setBirdX(getBirdX()-spdL);
	    } else {
	    	spdL=0;
	    }

	    if(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]){
	        setBirdX(getBirdX()+spdR);
	    } else {
	    	spdR=0;
	    }
	    /*if (spdU>0){
		    for (int i = 1 ; i<50 ; i++){
		    	setBirdY(getBirdY()+spdU/(5*i));
		    	System.out.println("slowing");
		    	//setBirdY(getBirdY()-spdD/(5*i));
		    	//setBirdY(getBirdX()+spdR/(5*i));
		    	//setBirdY(getBirdX()-spdL/(5*i));
		    }
	    }*/
	}
	
	
	//Below are where things are run, frame is repainted and update is run
	
	 class VGTimerTask extends TimerTask{
		 public void run() {
			 look();
			 processInput();
			 frame.repaint();

			 
		 }
		 
	 }
	

	public class MyKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
		    keys[e.getKeyCode()] = true;

		}

		public void keyReleased(KeyEvent e) {
		    keys[e.getKeyCode()] = false;
		    /*for (int i = 1 ; i<10 ; i++){
		    	setBirdY(getBirdY()+spdU/(5*i));
		    	setBirdY(getBirdY()-spdD/(5*i));
		    	setBirdY(getBirdX()+spdR/(5*i));
		    	setBirdY(getBirdX()-spdL/(5*i));
		    }
		    spdU=0; spdD=0 ; spdL=0; spdR=0; */
		}

		public void keyTyped(KeyEvent e) {
		}	
	}
	
	public static void main(String[] args) {
		java.util.Timer vgTimer = new java.util.Timer();
		GameEx panel = new GameEx();

	
		//Set up the frame
	    panel.frame.setSize(panel.screen.width, panel.screen.height);
        panel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

	    panel.frame.setContentPane(panel);     

        panel.frame.setVisible(true); 
		
		vgTimer.schedule(panel.vgTask, 0, 30);
		
	}
	
	int getBirdY(){
		return this.bird.y;
		}
	void setBirdY(int birdY){
		this.bird.y = birdY;
		}
	
	int getBirdX(){
		return this.bird.x;
	}
    void setBirdX(int birdX){
    	this.bird.x = birdX;
    	}
    
   /* void resetspd(){
    	spdU=0; spdD=0 ; spdL=0; spdR=0;
    }*/
    
    void setTop(int x, int y, int w, int h){
    	this.top.x = x;
    	this.top.y = y;
    	this.top.width = w;
    	this.top.height = h;
    	}
    void setBot(int x, int y, int w, int h){
    	this.bot.x = x;
    	this.bot.y = y;
    	this.bot.width = w;
    	this.bot.height = h;
    	}
    void setRight(int x, int y, int w, int h){
    	this.right.x = x;
    	this.right.y = y;
    	this.right.width = w;
    	this.right.height = h;
    	}
    void setLeft(int x, int y, int w, int h){
    	this.left.x = x;
    	this.left.y = y;
    	this.left.width = w;
    	this.left.height = h;
    	}
    
	 void check(Rectangle r) {
		 spdU=10; spdD=10; spdR=10; spdL=10 ;
			if (top.intersects(r)) {
				spdU=0;
			}  if(bot.intersects(r)) {
				spdD=0;
			}  if(right.intersects(r)) {
				spdR=0;			
			}  if(left.intersects(r)) {
				spdL=0;
			}
		 
	 }

}