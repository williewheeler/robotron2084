package com.williewheeler.t2.view;

import com.williewheeler.t2.T2Config;
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
	private static final int X_OFFSET = 28;
	private static final int Y_OFFSET = 16 + T2Config.HEADER_HEIGHT;

	private GameState gameState;
	private Font font;
	private SpriteFactory spriteFactory = new SpriteFactory();
	private Transitions transitions = new Transitions();

	public GamePane(GameState gameState) {
		this.gameState = gameState;
		this.font = new Font("Robotron", Font.BOLD, 20);
	}

	public void displayTransition() {
		transitions.displayTransition();
	}

	@Override
	public void paint(Graphics g) {
		Dimension size = getSize();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, size.width, size.height);

		if (transitions.isDisplayingTransition()) {
			transitions.paintTransition(g);
		} else {
			paintHeader(g);
			paintArena(g);
			paintPlayer(g);
			paintEnemies(g);
			paintPlayerMissiles(g);
		}
	}

	private Player getPlayer() {
		return gameState.getPlayer();
	}

	private void paintHeader(Graphics g) {
		g.setFont(font);
		g.setColor(Color.CYAN);
		g.drawString(String.valueOf(getPlayer().getScore()), 20, T2Config.HEADER_HEIGHT - 8);
	}

	private void paintArena(Graphics g) {
		g.setColor(Color.RED);

		Dimension mySize = getSize();
		int extWidth = T2Config.ARENA_WIDTH + SPRITE_DISPLAY_SIZE + 2;
		int extHeight = T2Config.ARENA_HEIGHT + SPRITE_DISPLAY_SIZE + 2;
		int x = (mySize.width - extWidth) / 2;
		int y = T2Config.HEADER_HEIGHT;

		for (int i = 0; i < 3; i++) {
			g.drawRect(x - i, y - i, extWidth + 2 * i, extHeight + 2 * i);
		}
	}

	private void paintPlayer(Graphics g) {
		Player player = getPlayer();
		BufferedImage[] sprites = spriteFactory.getPlayer();
		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
		int x = X_OFFSET + player.getX() - spriteOffset;
		int y = Y_OFFSET + player.getY() - spriteOffset;
		int spriteIndex = (x + y) % sprites.length;
		g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
	}

	private void paintEnemies(Graphics g) {
		List<Grunt> grunts = gameState.getGrunts();

		BufferedImage[] sprites = spriteFactory.getGrunt();
		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;

		for (Grunt grunt : grunts) {
			int x = X_OFFSET + grunt.getX() - spriteOffset;
			int y = Y_OFFSET + grunt.getY() - spriteOffset;
			int spriteIndex = (x + y) % sprites.length;
			g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
		}
	}

	private void paintPlayerMissiles(Graphics g) {
		List<PlayerMissile> missiles = gameState.getPlayerMissiles();

		g.setColor(Color.WHITE);
		for (PlayerMissile missile : missiles) {
			g.fillRect(X_OFFSET + missile.getX() - 2, Y_OFFSET + missile.getY() - 2, 4, 4);
		}
	}
}
