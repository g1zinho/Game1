package com.gibiris.graficos;

import java.awt.Color;
import java.awt.Graphics;

import com.gibirus.entities.Player;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(10, 4, 50, 8);
		g.setColor(Color.green);
		g.fillRect(10, 4,(int)((Player.life/Player.maxlife)*50), 8);
	}

}
