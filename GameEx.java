// I can't get it to run. Can't build ant file. Test commit

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
	public int x_pos=100 , y_pos=100 ;
	public boolean[] keys = new boolean[256];
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

	
	public void processInput(int spd) {
	    if(keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]){
	        setBirdY(getBirdY()-spd);
	        
	    }

	    if(keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]){
	        setBirdY(getBirdY()+spd);
	    }

	    if(keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]){
	        setBirdX(getBirdX()-spd);
	    }

	    if(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]){
	        setBirdX(getBirdX()+spd);
	    }
	}
	
	
	//Below are where things are run, frame is repainted and update is run
	
	 class VGTimerTask extends TimerTask{
		 public void run() {
			 look();
			 processInput(10);
			 frame.repaint();

			 
		 }
		 
	 }
	

	public class MyKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
		    keys[e.getKeyCode()] = true;

		}

		public void keyReleased(KeyEvent e) {
		    keys[e.getKeyCode()] = false;
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
    
    //build the map that we can test the unit against. This includes walls and stuff
	 int[][] getMap(){
		 for(int i = 0; i < map.length/2; i++) {
			    Arrays.fill(map[i], 1);
			}
		return this.map;
	}
	 
	 //the unit block
	 int getUnit(int i, int j){
		for (int[] row: unit) {
			Arrays.fill(row, 1);
		}
		return this.unit[i][j];
	}
	
	 void check(Rectangle r) {
		 
			if (top.intersects(r)) {
				keys[KeyEvent.VK_UP] = false ;
			}  if(bot.intersects(r)) {
				keys[KeyEvent.VK_DOWN] = false ;
			}  if(right.intersects(r)) {
				keys[KeyEvent.VK_RIGHT] = false ;					
			}  if(left.intersects(r)) {
				keys[KeyEvent.VK_LEFT] = false ;
			}
		 
	 }
}