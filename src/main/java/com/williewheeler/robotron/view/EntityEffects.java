package com.williewheeler.robotron.view;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by willie on 5/15/17.
 */
public class EntityEffects {

	public static BufferedImage spaghettify(BufferedImage source, int targetHeight) {
		final int sourceWidth = source.getWidth();
		final int sourceHeight = source.getHeight();

		AffineTransform scaleXform = AffineTransform.getScaleInstance(1.0f, (float) targetHeight / sourceHeight);
		BufferedImage spaghettified = new BufferedImage(sourceWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = spaghettified.createGraphics();
		g2.drawRenderedImage(source, scaleXform);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		for (int y = 0; y < targetHeight; y += 5) {
			g2.fillRect(0, y, sourceWidth, 4);
		}

		g2.dispose();
		return spaghettified;
	}
}
