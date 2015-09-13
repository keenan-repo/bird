import java.awt.Graphics2D;
import javax.sound.sampled.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import javax.imageio.ImageIO;

import java.io.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.WindowConstants;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GameEx extends JPanel {
	
	public VGTimerTask vgTask;
	public JFrame frame;
	public Rectangle screen, bird, wall, box1, box2, box3, top, bot, left, right, box12, box22, box32 ;
	public Rectangle bounds ; 
	public Rectangle[] arr1, arr2;
	public Rectangle[][] maps;
	public int x_pos=550 , y_pos=100, spdU, spdD, spdR, spdL, Lvl=1 ;
	public boolean[] keys = new boolean[256];
	public boolean keyL, keyR, keyU, keyD, swap, got_seed ;
	BufferedImage img, seeds;

	public GameEx()  {
		
		//System.out.println(Arrays.deepToString(getUnit(0,0)));

		KeyListener listener = new MyKeyListener();
		addKeyListener(listener);
		setFocusable(true);
		screen = new Rectangle(0, 0, 700, 500);
		bird = new Rectangle(x_pos,  y_pos, 10, 10);
		bounds = new Rectangle(50, 50, 600, 400);
		frame = new JFrame("Bird Game");
		got_seed = false;
		
		//this is where I program the map. This needs to be cleaned up if I want to make a big game
		//level1
		arr1 = new Rectangle[10];
		box1= new Rectangle(10,200,300,50);
		box2= new Rectangle(350,300,50,50);
		box3= new Rectangle(400,350,50,50);
		arr1[1]=box1;
		arr1[2]=box2;
		arr1[3]=box3;
		
		//level2
		arr2 = new Rectangle[10];
		box12= new Rectangle(350,200,300,50);
		box22= new Rectangle(50,150,150,50);
		box32= new Rectangle(270,170,50,50);
		arr2[1]=box12;
		arr2[2]=box22;
		arr2[3]=box32;

		//Arrays.fill(maps[1], arr1);
		
		vgTask = new VGTimerTask();	
			    
	    
	    try {
	    	img = ImageIO.read(new File("sprite.png"));
	    	seeds = ImageIO.read(new File("seeds.png"));
	        // Open an audio input stream.
	    	File soundFile = new File("Shy-Animal2.wav");
	    	AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
	        // Get a sound clip resource.
	        Clip clip = AudioSystem.getClip();
	        // Open audio clip and load samples from the audio input stream.
	        clip.open(audioIn);
	        clip.start();
	     } catch (UnsupportedAudioFileException e) {
	        e.printStackTrace();
	     } catch (IOException e) {
	        e.printStackTrace();
	     } catch (LineUnavailableException e) {
	        e.printStackTrace();
	     }
	    
	    //hit box
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

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);

		if(swap && (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_RIGHT])){
			tx.translate(-img.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter(img, null);
			swap=false;

	    } 
		g2d.drawImage(img, getBirdX(),  getBirdY(), null);
		System.out.println("X= " + getBirdX() + " Y= " + getBirdY() + " Lvl= " + Lvl);
		
		if (getBirdX()==50 && getBirdY()==150 && Lvl==1){
			Lvl=2;
			setBirdX(590);
			setBirdY(150);
			

		}
		if (getBirdY()>400 && Lvl==2){
			Lvl=1;
			setBirdX(590);
			setBirdY(390);

		} else if(getBirdX()==600 && getBirdY()==150 && Lvl==2){
			Lvl=1;
			setBirdX(60);
			setBirdY(150);
		} else if(Lvl == 2 && !got_seed){
			g2d.drawImage(seeds, 50,  50, null);
			if (getBirdX()==50 && getBirdY() == 50){
				got_seed = true;
			}
		}
		
		//top		
		setTop(getBirdX(),  getBirdY()-10, img.getWidth(), 10);

		
		//bottom
		setBot(getBirdX(),  getBirdY()+img.getHeight(), img.getWidth(), 10);
		
		//left
		setLeft(getBirdX()-10,  getBirdY(), 10, img.getHeight());
		
		//right
		setRight(getBirdX()+img.getWidth(),  getBirdY(), 10, img.getHeight());
		g2d.draw(top);
		g2d.draw(bot);
		g2d.draw(left);
		g2d.draw(right);
		
		//box or any other stuff
		if (Lvl == 1){
			for(int i=1; i<=3 ; i++){
				g2d.fill(arr1[i]);
			}
		} else if (Lvl == 2){
			for(int i=1; i<=3 ; i++){
				g2d.fill(arr2[i]);
			}
		}
			//g2d.fill(box2);
		
		g2d.draw(bounds);
		
		//System.out.println("x = " + getBirdX() + " y = " + getBirdY());
		}

	
	 void look() {
		 spdU=10; spdD=10; spdR=10; spdL=10 ;
		 
			if (Lvl == 1){
				for(int i=1; i<=3 ; i++){
					check(arr1[i]);
				}
			} else if (Lvl == 2){
				for(int i=1; i<=3 ; i++){
					check(arr2[i]);
				}
			}
			
		 boundary(bounds);

		
	 }

	
	public void processInput() {
	    if(keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]){
	    	
	    	try {
		        // Open an audio input stream.
		    	File soundJump = new File("jump.wav");
		    	AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundJump);
		        // Get a sound clip resource.
		        Clip clip = AudioSystem.getClip();
		        // Open audio clip and load samples from the audio input stream.
		        clip.open(audioIn);
		        clip.start();
		     } catch (UnsupportedAudioFileException e) {
		        e.printStackTrace();
		     } catch (IOException e) {
		        e.printStackTrace();
		     } catch (LineUnavailableException e) {
		        e.printStackTrace();
		     }
	    	
		    	boolean jump = false;

		    	for(int i=1; i<=3; i++){
			    	if(bot.intersects(arr1[i]) || bot.intersects(arr2[i]) ) {
						jump = true;
			    	} else if (!bot.intersects(bounds)){
			    		jump = true;
			    	}
		    	}
		    	
		    	if (jump){
		    		setBirdY(getBirdY()-75);
		    	}		    	
		    }
	    keys[KeyEvent.VK_UP]=false;

	    if(keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]){
	        setBirdY(getBirdY()+spdD);
	    } else {
	    	//spdD=0;
	    }

	    if(keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]){
	        setBirdX(getBirdX()-spdL);
	        keyR=false;
	        keyL=true;
	    } else {
	    	spdL=0;
	    }

	    if(keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]){
	        setBirdX(getBirdX()+spdR);
	        keyR=true;
	        keyL=false;

	    } else {
	    	spdR=0;
	    }
	    /*if(keys[KeyEvent.VK_SPACE]){
	    	boolean jump = false;
	    	for(int i=1; i<=2; i++){
		    	if(bot.intersects(arr[i])) {
					jump = true;
					System.out.println("Jump!");
		    	}
	    	}
	    	if (jump){
	    		setBirdY(getBirdY()-10);
	    	}
	    	
	    }
	    */
	    /*if (spdU>0){
		    for (int i = 1 ; i<50 ; i++){
		    	setBirdY(getBirdY()+spdU/(5*i));
		    	System.out.println("slowing");
		    	//setBirdY(getBirdY()-spdD/(5*i));
		    	//setBirdY(getBirdX()+spdR/(5*i));
		    	//setBirdY(getBirdX()-spdL/(5*i));
		    }
	    }*/
	    setBirdY(getBirdY()+spdD);
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
		    swap=true;
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
		 //spdU=10; spdD=10; spdR=10; spdL=10 ;
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
	 void boundary(Rectangle r) {
		 //spdU=10; spdD=10; spdR=10; spdL=10 ;
			if (!top.intersects(r)) {
				spdU=0;
			}  if(!bot.intersects(r)) {
				spdD=0;
			}  if(!right.intersects(r)) {
				spdR=0;			
			}  if(!left.intersects(r)) {
				spdL=0;
			}
		 
	 }
	 Rectangle[][] getMap(){
		 for(int i = 0; i < maps.length/2; i++) {
			    Arrays.fill(maps[i], 1);
			}
		return this.maps;
	}

}