package com.williewheeler.robotron.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Created by willie on 5/15/17.
 */
public class Transitions {
	private static final int TRANSITION_LENGTH = 50;

	private static final Color[] TRANSITION_COLORS = new Color[] {
			Color.RED,
			Color.ORANGE,
			Color.YELLOW,
			Color.GREEN,
			Color.BLUE
	};

	private int transitionCounter = -1;

	public void displayTransition() {
		this.transitionCounter = TRANSITION_LENGTH;
	}

	public boolean isDisplayingTransition() {
		return transitionCounter > -1;
	}

	// FIXME This needs some work to make it more faithful to the original.
	// Also move this to its own class.
	public void paintTransition(GamePane gamePane, Graphics g) {
		Dimension paneSize = gamePane.getSize();

		double progress = 1.0 - ((double) transitionCounter / TRANSITION_LENGTH);

		// Draw outer colored box
		double outerProgress = Math.min(1.0, 2.0 * progress);
		double outerRegress = outerProgress;
		int colorIndex = 0;
		while (outerRegress > 0.0) {
			int outerWidth = (int) (paneSize.width * outerRegress);
			int outerHeight = (int) (paneSize.height * outerRegress);
			int outerX = (paneSize.width - outerWidth) / 2;
			int outerY = (paneSize.height - outerHeight) / 2;
			g.setColor(TRANSITION_COLORS[colorIndex++ % TRANSITION_COLORS.length]);
			g.fillRect(outerX, outerY, outerWidth, outerHeight);
			g.setColor(Color.BLACK);
			g.fillRect(outerX + 5, outerY + 5, Math.max(0, outerWidth - 10), Math.max(0, outerHeight - 10));
			outerRegress -= 0.02;
		}

		// Draw inner black box
		double innerProgress = Math.max(0.0, 2.0 * progress - 0.5);
		int innerWidth = (int) (paneSize.width * innerProgress);
		int innerHeight = (int) (paneSize.height * innerProgress);
		int innerX = (paneSize.width - innerWidth) / 2;
		int innerY = (paneSize.height - innerHeight) / 2;
		g.setColor(Color.BLACK);
		g.fillRect(innerX, innerY, innerWidth, innerHeight);

		transitionCounter--;
	}
}
