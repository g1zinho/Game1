package com.gibirus.world;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gibirus.entities.Bullet;
import com.gibirus.entities.Enemy;
import com.gibirus.entities.Entity;
import com.gibirus.entities.Weapon;
import com.gibirus.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;

	
	public World(String path) {
	     try {
			BufferedImage	map = ImageIO.read(getClass().getResource(path));
			int [] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
					
					if(pixelAtual == 0xFF000000) {
						//floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);		
					}else if( pixelAtual == 0xFFFFFFFF) {
						//wall
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_WALL);
					}else if(pixelAtual == 0x2626FF) {
						//player
					}else if(pixelAtual == 0xFFFF0000) {
						//enemy
						Game.entities.add(new Enemy(xx*16,yy*16, 16 ,16, Entity.ENEMY_EN) );
					}else if(pixelAtual == 0xFF00FFFF) {
						//weapon
						Game.entities.add(new Weapon(xx*16,yy*16, 16 ,16, Entity.WEAPON_EN) );

					}else if(pixelAtual == 0xFF4CFF00) {
						//life pack
						Game.entities.add(new Enemy(xx*16,yy*16, 16 ,16, Entity.LIFEPACK_EN) );
					}else if(pixelAtual == 0xFFFFD800) {
						
						Game.entities.add(new Bullet(xx*16,yy*16, 16 ,16, Entity.BULLET_EN) );
					}
				}
			}
							
	     } catch (IOException e) {
			e.printStackTrace();
		}
	       
	     }
	 
	      public void render(Graphics g) {
	    	  int xstart = Camera.x >> 4;
	    	  int ystart = Camera.y >> 4;
	    	  
	    	  int xfinal = xstart + (Game.WIDTH >> 4);
	    	  int yfinal = ystart + (Game.HEIGHT >> 4);
	    	  
	    	  for(int xx = xstart; xx <  xfinal; xx++) {
	    		  for(int yy = ystart; yy <= yfinal;  yy++) {
	    			  if( xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
	    				  continue;
	    			  Tile tile = tiles[xx + (yy*WIDTH)];
	    			  tile.render(g);
	    		  }
	    		  }
	    	  }
	}
				
		
