package com.williewheeler.t2.view;

import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.model.Grunt;
import com.williewheeler.t2.model.Player;
import com.williewheeler.t2.model.PlayerMissile;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by willie on 5/12/17.
 */
public class GamePane extends JComponent {
	private static final int SPRITE_DISPLAY_SIZE = 30;

	private GameState gameState;
	private Font font;
	private SpriteFactory spriteFactory = new SpriteFactory();

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
		paintEnemies(g);
		paintPlayerMissiles(g);
	}

	private void paintHeader(Graphics g) {
		g.setFont(font);
		g.setColor(Color.CYAN);
		g.drawString(String.valueOf(getPlayer().getScore()), 20, 40);
	}

	private void paintPlayer(Graphics g) {
		Player player = getPlayer();
		BufferedImage[] sprites = spriteFactory.getPlayer();
		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
		int x = player.getX() - spriteOffset;
		int y = player.getY() - spriteOffset;
		int spriteIndex = (x + y) % sprites.length;
		g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
	}

	private void paintEnemies(Graphics g) {
		List<Grunt> grunts = gameState.getGrunts();

		BufferedImage[] sprites = spriteFactory.getGrunt();
		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;

		for (Grunt grunt : grunts) {
			int x = grunt.getX() - spriteOffset;
			int y = grunt.getY() - spriteOffset;
			int spriteIndex = (x + y) % sprites.length;
			g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
		}
	}

	private void paintPlayerMissiles(Graphics g) {
		List<PlayerMissile> missiles = gameState.getPlayerMissiles();

		g.setColor(Color.WHITE);
		for (PlayerMissile missile : missiles) {
			g.fillRect(missile.getX() - 2, missile.getY() - 2, 4, 4);
		}
	}

	private Player getPlayer() {
		return gameState.getPlayer();
	}
}
