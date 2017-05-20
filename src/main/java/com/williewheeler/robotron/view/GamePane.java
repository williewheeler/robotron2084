package com.williewheeler.robotron.view;

import com.williewheeler.robotron.GameConfig;
import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.model.entity.Direction;
import com.williewheeler.robotron.model.entity.Electrode;
import com.williewheeler.robotron.model.entity.Grunt;
import com.williewheeler.robotron.model.entity.Hulk;
import com.williewheeler.robotron.model.entity.Human;
import com.williewheeler.robotron.model.entity.HumanType;
import com.williewheeler.robotron.model.entity.Player;
import com.williewheeler.robotron.model.entity.PlayerMissile;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.List;

import static com.williewheeler.robotron.GameConfig.*;

/**
 * Created by willie on 5/12/17.
 */
public class GamePane extends JComponent {
	private static final int SPRITE_DISPLAY_SIZE = 30;
	private static final int XOFFSET = 28;
	private static final int YOFFSET = 16 + GameConfig.HEADER_HEIGHT;

	private GameModel gameModel;
	private Font font;
	private SpriteFactory spriteFactory = new SpriteFactory();
	private Transitions transitions = new Transitions();

	public GamePane(GameModel gameModel) {
		this.gameModel = gameModel;
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
			g.translate(XOFFSET, YOFFSET);
			paintEnemies(g);
			paintPlayer(g);
			paintPlayerMissiles(g);
			g.translate(-XOFFSET, -YOFFSET);
		}
	}

	private Player getPlayer() {
		return gameModel.getPlayer();
	}

	private void paintHeader(Graphics g) {
		g.setFont(font);
		g.setColor(Color.CYAN);
		g.drawString(String.valueOf(getPlayer().getScore()), 20, GameConfig.HEADER_HEIGHT - 8);
	}

	private void paintArena(Graphics g) {
		g.setColor(Color.RED);

		Dimension mySize = getSize();
		int extWidth = GameConfig.ARENA_WIDTH + SPRITE_DISPLAY_SIZE + 2;
		int extHeight = GameConfig.ARENA_HEIGHT + SPRITE_DISPLAY_SIZE + 2;
		int x = (mySize.width - extWidth) / 2;
		int y = GameConfig.HEADER_HEIGHT;

		for (int i = 0; i < 3; i++) {
			g.drawRect(x - i, y - i, extWidth + 2 * i, extHeight + 2 * i);
		}
	}

	private void paintPlayer(Graphics g) {
		Player player = getPlayer();
		BufferedImage[] sprites = spriteFactory.getPlayer();

		Direction direction = player.getDirection();
		int spriteBaseIndex = getSpriteBaseIndex(direction);
		int spriteIndex = spriteBaseIndex + (player.getNumMoves() % 4);

		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
		int x = player.getX() - spriteOffset;
		int y = player.getY() - spriteOffset;

		g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
	}

	private void paintEnemies(Graphics g) {
		paintElectrodes(g);
		paintHumans(g);
		paintGrunts(g);
		paintHulks(g);
	}

	private void paintElectrodes(Graphics g) {
		List<Electrode> electrodes = gameModel.getElectrodes();
		for (Electrode electrode : electrodes) {
//			BufferedImage sprite = spriteFactory.getElectrode()[0];
			BufferedImage sprite = createImage();
			ColorModel colorModel = createColorModel(gameModel.getCyclicCounter() / 20.0f);
			sprite = new BufferedImage(colorModel, sprite.getRaster(), false, null);

			int spriteOffset = 18 / 2;
			int x = electrode.getX() - spriteOffset;
			int y = electrode.getY() - spriteOffset;
			g.drawImage(sprite, x, y, 14, 18, null);
		}
	}

	private void paintHumans(Graphics g) {
		List<Human> humans = gameModel.getHumans();
		for (Human human : humans) {

			// TODO Consider polymorphism
			BufferedImage[] sprites = null;
			HumanType type = human.getType();
			if (type == HumanType.MOMMY) {
				sprites = spriteFactory.getMommy();
			} else if (type == HumanType.DADDY) {
				sprites = spriteFactory.getDaddy();
			} else {
				// TODO
			}
			
			int spriteBaseIndex = getSpriteBaseIndex(human.getDirection());
			int spriteIndex = spriteBaseIndex + human.getNumMoves() % 4;

			int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
			int x = human.getX() - spriteOffset;
			int y = human.getY() - spriteOffset;

			g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
		}
	}

	private void paintGrunts(Graphics g) {
		List<Grunt> grunts = gameModel.getGrunts();
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
		int dispX = gruntX - SPRITE_DISPLAY_SIZE / 2;
		int dispY = loY - SPRITE_DISPLAY_SIZE / 2;
		int height = Math.max(SPRITE_DISPLAY_SIZE, hiY - loY);

		BufferedImage spaghettified = EntityEffects.spaghettify(sprite, height);
		g.drawImage(spaghettified, dispX, dispY, SPRITE_DISPLAY_SIZE, height, null);
	}

	private void paintGrunt_alive(Grunt grunt, Graphics g) {
		BufferedImage[] sprites = spriteFactory.getGrunt();
		int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
		int x = grunt.getX() - spriteOffset;
		int y = grunt.getY() - spriteOffset;
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
		int dispX = gruntX - SPRITE_DISPLAY_SIZE / 2;
		int dispY = loY - SPRITE_DISPLAY_SIZE / 2;
		int height = Math.max(SPRITE_DISPLAY_SIZE, hiY - loY);

		BufferedImage spaghettified = EntityEffects.spaghettify(sprite, height);
		g.drawImage(spaghettified, dispX, dispY, SPRITE_DISPLAY_SIZE, height, null);
	}

	private void paintHulks(Graphics g) {
		List<Hulk> hulks = gameModel.getHulks();
		for (Hulk hulk : hulks) {
			BufferedImage[] sprites = spriteFactory.getHulk();
			int spriteOffset = SPRITE_DISPLAY_SIZE / 2;
			int x = hulk.getX() - spriteOffset;
			int y = hulk.getY() - spriteOffset;
			int spriteIndex = hulk.getNumMoves() % sprites.length;
			g.drawImage(sprites[spriteIndex], x, y, SPRITE_DISPLAY_SIZE, SPRITE_DISPLAY_SIZE, null);
		}
	}

	private void paintPlayerMissiles(Graphics g) {
		List<PlayerMissile> missiles = gameModel.getPlayerMissiles();

		g.setColor(Color.WHITE);
		for (PlayerMissile missile : missiles) {
			g.fillRect(missile.getX() - 2, missile.getY() - 2, 4, 4);
		}
	}

	private static BufferedImage createImage() {
		int width = SPRITE_DISPLAY_SIZE ;
		int height = SPRITE_DISPLAY_SIZE;

		// Generate the source pixels for our image
		// Lets just keep it to a simple blank image for now
		byte[] pixels = new byte[width * height];
		DataBuffer dataBuffer = new DataBufferByte(pixels, width*height, 0);
		SampleModel sampleModel =
				new SinglePixelPackedSampleModel(DataBuffer.TYPE_BYTE, width, height, new int[] { (byte) 0xf });
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, null);
		return new BufferedImage(createColorModel(0.0f), raster, false, null);
	}

	private static ColorModel createColorModel(float hue) {
		int rgb = Color.HSBtoRGB(hue, 1.0f, 0.5f);
		byte[] r = new byte[16];
		byte[] g = new byte[16];
		byte[] b = new byte[16];
		for (int i = 0; i < r.length; i++) {
			r[i] = (byte) ((rgb & 0xff000000) >> 24);
			g[i] = (byte) ((rgb & 0x00ff0000) >> 16);
			b[i] = (byte) ((rgb & 0x0000ff00) >> 8);
		}
		return new IndexColorModel(4, 16, r, g, b);
	}

	private int getSpriteBaseIndex(Direction direction) {
		int spriteBaseIndex = 0;
		switch (direction) {
			case UP:
				spriteBaseIndex = 4;
				break;
			case DOWN:
				spriteBaseIndex = 0;
				break;
			case LEFT:
			case UP_LEFT:
			case DOWN_LEFT:
				spriteBaseIndex = 12;
				break;
			case RIGHT:
			case UP_RIGHT:
			case DOWN_RIGHT:
				spriteBaseIndex = 8;
				break;
		}
		return spriteBaseIndex;
	}
}
