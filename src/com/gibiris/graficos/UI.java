package com.gibiris.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gibirus.entities.Player;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(10, 4, 50, 8);
		g.setColor(Color.green);
		g.fillRect(10, 4,(int)((Player.life/Player.maxlife)*50), 8);
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 9));
		g.drawString((int) Player.life+"/"+(int)Player.maxlife, 10, 12);
	}

}
