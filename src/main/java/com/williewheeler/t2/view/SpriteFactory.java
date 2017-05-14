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
			player[i] = sheet.getSubimage(i * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		}

		this.grunt = new BufferedImage[3];
		for (int i = 0; i < 3; i++) {
			grunt[i] = sheet.getSubimage(i * SPRITE_SIZE, 0 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		}
	}

	public BufferedImage[] getPlayer() {
		return player;
	}

	public BufferedImage[] getGrunt() {
		return grunt;
	}
}
