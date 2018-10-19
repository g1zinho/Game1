package com.gibiris.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gibirus.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(10, 4, 70, 8);
		g.setColor(Color.green);
		g.fillRect(10, 4,(int)((Game.player.life/Game.player.maxlife)*70), 8);
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.PLAIN, 9));
		g.drawString((int) Game.player.life+"/"+(int)Game.player.maxlife, 30, 12);
	}

}