package com.williewheeler.t2;

import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.model.Player;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Created by willie on 5/12/17.
 */
public class GamePane extends JComponent {
	private GameState gameState;
	private Font font;

	public GamePane(GameState gameState) {
		this.gameState = gameState;
		this.font = new Font("Robotron", Font.BOLD, 24);
	}

	@Override
	public void paint(Graphics g) {
		Dimension size = getSize();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, size.width, size.height);
		paintHeader(g);
		paintPlayer(g);
	}

	private void paintHeader(Graphics g) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(getPlayer().getScore()), 20, 40);
	}

	private void paintPlayer(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(getPlayer().getPlayerX(), getPlayer().getPlayerY(), 15, 15);
	}

	private Player getPlayer() {
		return gameState.getPlayer();
	}
}
