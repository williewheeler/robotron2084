package com.williewheeler.t2.view;

import com.williewheeler.t2.T2Config;
import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.model.entity.Grunt;
import com.williewheeler.t2.model.entity.Hulk;
import com.williewheeler.t2.model.entity.Player;
import com.williewheeler.t2.model.entity.PlayerMissile;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import static com.williewheeler.t2.T2Config.*;

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
			transitions.paintTransition(this, g);
		} else {
			paintHeader(g);
			paintArena(g);
			paintEnemies(g);
			paintPlayer(g);
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
		paintGrunts(g);
		paintHulks(g);
	}

	private void paintGrunts(Graphics g) {
		List<Grunt> grunts = gameState.getGrunts();
		for (Grunt grunt : grunts) {
			switch (grunt.getEntityState()) {
				case BORN:
					paintGrunt_born(grunt, g);
					break;
				case ALIVE:
					paintGrunt_alive(grunt, g);
					break;
				case DEAD:
					paintGrunt_dead(grunt, g);
					break;
			}
		}
	}

	// Do spaghettification effect
	private void paintGrunt_born(Grunt grunt, Graphics g) {
		BufferedImage sprite = spriteFactory.getGrunt()[0];

		int gruntX = grunt.getX();
		int gruntY = grunt.getY();
		double multiplier = (double) grunt.getBornCountdown() / ENTITY_BORN_COUNTDOWN;

		int loY = gruntY - (int) (ENTITY_BORN_SPREAD * multiplier);
		int hiY = gruntY + (int) (ENTITY_BORN_SPREAD * multiplier);
		int dispX = X_OFFSET + gruntX - SPRITE_DISPLAY_SIZE / 2;
		int dispY = Y_OFFSET + loY - SPRITE_DISPLAY_SIZE / 2;
		int height = Math.max(SPRITE_DISPLAY_SIZE, hiY - loY);

		BufferedImage spaghettified = EntityEffects.spaghettify(sprite, height);
		g.drawImage(spaghettified, dispX, dispY, SPRITE_DISPLAY_SIZE, height, null);
	}

	private void paintGrunt_alive(Grunt grunt, Graphics g) {
		BufferedImage[] sprites = spriteFactory.getGrunt();
		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
		int x = X_OFFSET + grunt.getX() - spriteOffset;
		int y = Y_OFFSET + grunt.getY() - spriteOffset;
		int spriteIndex = grunt.getNumMoves() % sprites.length;
		g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
	}

	// TODO
	// 1. Randomize the spread
	// 2. The spread direction should depend on the direction of the player missile
	// 3. Crop the image so that it doesn't go outside the arena
	private void paintGrunt_dead(Grunt grunt, Graphics g) {
		BufferedImage sprite = spriteFactory.getGrunt()[0];

		int gruntX = grunt.getX();
		int gruntY = grunt.getY();
		double multiplier = 1.0 - (double) grunt.getDeadCountdown() / ENTITY_DEAD_COUNTDOWN;

		int loY = gruntY - (int) (ENTITY_DEAD_SPREAD * multiplier);
		int hiY = gruntY + (int) (ENTITY_DEAD_SPREAD * multiplier);
		int dispX = X_OFFSET + gruntX - SPRITE_DISPLAY_SIZE / 2;
		int dispY = Y_OFFSET + loY - SPRITE_DISPLAY_SIZE / 2;
		int height = Math.max(SPRITE_DISPLAY_SIZE, hiY - loY);

		BufferedImage spaghettified = EntityEffects.spaghettify(sprite, height);
		g.drawImage(spaghettified, dispX, dispY, SPRITE_DISPLAY_SIZE, height, null);
	}

	private void paintHulks(Graphics g) {
		List<Hulk> hulks = gameState.getHulks();
		for (Hulk hulk : hulks) {
			BufferedImage[] sprites = spriteFactory.getHulk();
			int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
			int x = X_OFFSET + hulk.getX() - spriteOffset;
			int y = Y_OFFSET + hulk.getY() - spriteOffset;
			int spriteIndex = hulk.getNumMoves() % sprites.length;
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
