package com.williewheeler.t2.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by willie on 5/14/17.
 */
public class SpriteFactory {
	private static final int SPRITE_SIZE = 15;

	private BufferedImage[] player;
	private BufferedImage[] grunt;
	private BufferedImage[] hulk;

	public SpriteFactory() {
		BufferedImage sheet;
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("sprites.png");
			sheet = ImageIO.read(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.player = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			player[i] = sheet.getSubimage(i * SPRITE_SIZE, 0 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		}

		this.grunt = new BufferedImage[4];
		grunt[0] = sheet.getSubimage(0 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		grunt[1] = sheet.getSubimage(1 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		grunt[2] = grunt[0];
		grunt[3] = sheet.getSubimage(2 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

		this.hulk = new BufferedImage[4];
		// FIXME The hulk is a little taller, so we are going to need to handle this more cleanly. [WLW]
		hulk[0] = sheet.getSubimage(0 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE + 1);
		hulk[1] = sheet.getSubimage(1 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE + 1);
		hulk[2] = hulk[0];
		hulk[3] = sheet.getSubimage(2 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE + 1);
	}

	public BufferedImage[] getPlayer() {
		return player;
	}

	public BufferedImage[] getGrunt() {
		return grunt;
	}

	public BufferedImage[] getHulk() {
		return hulk;
	}
}
