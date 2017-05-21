package com.williewheeler.robotron.view;

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
	private BufferedImage[] electrode;
	private BufferedImage[] mommy;
	private BufferedImage[] daddy;
	private BufferedImage[] mikey;
	private BufferedImage[] hulk;

	public SpriteFactory() {
		BufferedImage sheet;
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("sprites.png");
			sheet = ImageIO.read(input);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.player = buildSpriteArray(sheet, 0);

		this.grunt = new BufferedImage[4];
		grunt[0] = sheet.getSubimage(0 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		grunt[1] = sheet.getSubimage(1 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		grunt[2] = grunt[0];
		grunt[3] = sheet.getSubimage(2 * SPRITE_SIZE, 1 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

		this.electrode = new BufferedImage[1];
		electrode[0] = sheet.getSubimage(0 * SPRITE_SIZE, 4 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

		this.hulk = new BufferedImage[4];
		// FIXME The hulk is a little taller, so we are going to need to handle this more cleanly. [WLW]
		hulk[0] = sheet.getSubimage(0 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE + 1);
		hulk[1] = sheet.getSubimage(1 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE + 1);
		hulk[2] = hulk[0];
		hulk[3] = sheet.getSubimage(2 * SPRITE_SIZE, 2 * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE + 1);

		this.mommy = buildSpriteArray(sheet, 5);
		this.daddy = buildSpriteArray(sheet, 6);
		this.mikey = buildSpriteArray(sheet, 7);
	}

	private BufferedImage[] buildSpriteArray(BufferedImage sheet, int rowIndex) {
		BufferedImage[] spriteArr = new BufferedImage[16];

		// Iterate over the four directions
		for (int i = 0; i < 4; i++) {
			int baseArrIndex = 4 * i;
			int baseSprIndex = 3 * i;
			int y = rowIndex * SPRITE_SIZE;
			spriteArr[baseArrIndex] = sheet.getSubimage(baseSprIndex * SPRITE_SIZE, y, SPRITE_SIZE, SPRITE_SIZE);
			spriteArr[baseArrIndex + 1] = sheet.getSubimage((baseSprIndex + 1) * SPRITE_SIZE, y, SPRITE_SIZE, SPRITE_SIZE);
			spriteArr[baseArrIndex + 2] = spriteArr[baseArrIndex];
			spriteArr[baseArrIndex + 3] = sheet.getSubimage((baseSprIndex + 2) * SPRITE_SIZE, y, SPRITE_SIZE, SPRITE_SIZE);
		}

		return spriteArr;
	}

	public BufferedImage[] getPlayer() {
		return player;
	}

	public BufferedImage[] getGrunt() {
		return grunt;
	}

	public BufferedImage[] getElectrode() {
		return electrode;
	}

	public BufferedImage[] getMommy() {
		return mommy;
	}

	public BufferedImage[] getDaddy() {
		return daddy;
	}

	public BufferedImage[] getMikey() {
		return mikey;
	}

	public BufferedImage[] getHulk() {
		return hulk;
	}
}
