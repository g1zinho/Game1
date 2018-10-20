package com.gibirus.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gibirus.world.Camera;

public class Shoot  extends Entity{

	private int dx;
	private int dy;
	private double spd = 4;
	
	public Shoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy; 
	}

	public void tick() {
		
		x+=dx*spd;
		y+=dy*spd;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x , this.getY() - Camera.y, width, height);
		
	}
	
}
