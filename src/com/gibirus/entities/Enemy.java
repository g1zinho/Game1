package com.gibirus.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gibirus.main.Game;
import com.gibirus.world.Camera;
import com.gibirus.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.5;
	
	private int maskx = 8, masky = 8, maskw = 6, maskh  = 6;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1 ;
	private BufferedImage[] sprites;
	private int life = 10;
   private boolean isDMG = false;
	private int damgeFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage[] sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[4];
        this.sprites[0] = sprite[0];
        this.sprites[1] = sprite[1];
	
	}


	public void tick() {
		//colisão do player com enimigo
		if(isCollidingWithPlayer() == false) {
			
		//Espaço da colisão entre enemigos
		if( (int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
				&& !isColliding((int)(x+speed), this.getY())) {
			x+=speed;
		}
		else if ((int)x > Game.player.getX()&& World.isFree((int)(x-speed), this.getY())
				&& !isColliding((int)(x-speed), this.getY())){
			x-=speed;
		}		
		if( (int)y < Game.player.getY()&& World.isFree( this.getX() ,(int)(y+speed))
				&& !isColliding(this.getX() ,(int)(y+speed))) {
			y+=speed;
		}
		else if ((int)y > Game.player.getY()&& World.isFree( this.getX() ,(int)(y-speed))
				&& !isColliding(this.getX() ,(int)(y-speed))){
			y-=speed;
		}
		}else {
			//estamos colidindo
			if(Game.rand.nextInt(100) < 10) {
			Game.player.life-= Game.rand.nextInt(3);
			Game.player.isDMG = true;
			//System.out.println("Vida : " + Player.life);
		}
			}
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
			collidingBullet();
			if(life <= 0) {
				destroySelf();
				return;
			}
			if(isDMG) {
				this.damageCurrent++;
				if(this.damageCurrent == this.damgeFrames) {
					this.damageCurrent = 0;
					this.isDMG = false;
					
				}
				
			}
		}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	public void collidingBullet() {
		for( int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);				
				if(Entity.isColliding(this, e)) {
					isDMG = true;
					life--;
					Game.bullets.remove(i);
					
					return;
				}
			}
		}
		 
	
		
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky ,  maskw, maskh );		
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16 ,16);
		return enemyCurrent.intersects(player);
		
	}
	
	public boolean isColliding(int  xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky ,  maskw, maskh );		
		
		for(int i =0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if( e == this) 
				continue;
				Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + maskx, maskw, maskh);		
				if(enemyCurrent.intersects(targetEnemy)) {
					return true;
				}
		}
		
		return false;
			
	}
    public void render(Graphics g) {
    	if(!isDMG)
    g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    	else 
    		g.drawImage(Entity.ENEMY_FREEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
	}

