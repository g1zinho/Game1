package com.gibirus.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gibiris.graficos.Spritesheet;
import com.gibirus.main.Game;
import com.gibirus.world.Camera;
import com.gibirus.world.World;

public class Player extends Entity {
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4 ;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3 ;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDMG;
	
	public int ammo = 0;
	
	public boolean isDMG = false;
	private int damageFrames = 0;
	
	public  double life = 100, maxlife = 100;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDMG = Game.spritesheet.getSprite(0, 32, 16, 16);
		for(int i =0; i < 4; i++ ){
		rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i =0; i < 4; i++){
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
			}
	}
	
	public void tick() {
		moved = false;
		
		if(right && World.isFree((int)(x+speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if(left  && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up  && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true;
			y-=speed;
			
		}else if(down  && World.isFree(this.getX(), (int)(y+speed))) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		checkCollisionLifePack();
		checkCollisionAmmo();
		
		if(isDMG) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDMG = false;
			}
		}
		
		if(life <= 0) {

			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32,0,16,16));
			Game.entities.add(Game.player);
			Game.world = new 	World("/map.png");
			return;
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH) ;
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT *16 - Game.HEIGHT);
	}

	public void checkCollisionAmmo() {
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity atual = Game.entities.get(i);
				if(atual instanceof Bullet) {
					if(Entity.isColliding(this, atual)) {
						ammo+=16;
						Game.entities.remove(atual);
					}
				}
			}
	}
		

	public boolean checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColliding(this, atual)) {
					life+=10;
					if(life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
		return down;
		
		}
		
	
	
	public void render(Graphics g) {
		if(!isDMG ) {
		if(dir == right_dir){
	      	g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY()  - Camera.y, null);
		}else if (dir == left_dir) {	
			g.drawImage(leftPlayer[index], this.getX()  - Camera.x, this.getY()  - Camera.y, null);
		}
		
	}else {
		g.drawImage(playerDMG, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	}

}
