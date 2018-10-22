package com.gibirus.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.gibiris.graficos.Spritesheet;
import com.gibiris.graficos.UI;
import com.gibirus.entities.Enemy;
import com.gibirus.entities.Entity;
import com.gibirus.entities.Player;
import com.gibirus.entities.Shoot;
import com.gibirus.world.Camera;
import com.gibirus.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunnig = true;
	public final static int WIDTH = 240;
	public final static int HEIGHT = 160;
	public final int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL =2;
	private BufferedImage image; 
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shoot> bullets;
	public static Spritesheet spritesheet;
	
	
	public static  World world;
	
	public static Player player;

   public static Random rand;	
   
   public UI ui;
	 	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Shoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32,0,16,16));
		entities.add(player);
		world = new 	World("/level1.png");
		
		
		
		
	}
	
	public void initFrame() {
		frame = new JFrame("Gam3");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunnig = true;
		thread.start();
		
	}
	
    public synchronized void stop() {
    	isRunnig = false;
    	try {
    		thread.join();
    		
    	} catch (InterruptedException e){
    		e.printStackTrace();
    	}
    	
    }
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();		
		
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
		
		if (enemies.size()== 0) {
			CUR_LEVEL++ ;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
		
		}
		String newWorld = "level" + CUR_LEVEL +".png";
		System.out.println(newWorld);
		World.restartGame(newWorld);
		}
	}
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if( bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);

		/***/		
		g.dispose();
      	g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD,22));
		g.setColor(Color.WHITE);
		g.drawString("Ammo:" + player.ammo, 600, 32);
		bs.show();
	}
	
	
	public void run(){
		long lastTime =  System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
	    while(isRunnig) {
	    	long now = System.nanoTime();
	    	delta+= (now - lastTime) / ns;
	    	lastTime = now;
	    	if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
	    }
	    	if(System.currentTimeMillis() - timer >= 1000) {
	    		System.out.println("FPS:"+ frames);
	    		frames = 0;
	    		timer+=1000;
	    	}
	    		
	    	}
	    stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT  || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;	
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT  || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;	
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			
		}else if (e.getKeyCode() == KeyEvent.VK_DOWN  || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shooting = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		 player.mouseShoot = true;
		 //posição do mouse no mundo
		 player.mx = (e.getX()/3)+ Camera.x;
		 player.my = (e.getY()/3) + Camera.y ;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
