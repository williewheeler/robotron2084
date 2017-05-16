package com.williewheeler.t2.view;

import com.williewheeler.t2.T2Config;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by willie on 5/15/17.
 */
public class Transitions {

	private static final Color[] TRANSITION_COLORS = new Color[] {
			Color.RED,
			Color.ORANGE,
			Color.YELLOW,
			Color.GREEN,
			Color.BLUE
	};

	private int transitionCounter = -1;

	public void displayTransition() {
		this.transitionCounter = T2Config.TRANSITION_LENGTH;
	}

	public boolean isDisplayingTransition() {
		return transitionCounter > -1;
	}

	// FIXME This needs some work to make it more faithful to the original.
	// Also move this to its own class.
	public void paintTransition(Graphics g) {
		double progress = 1.0 - ((double) transitionCounter / T2Config.TRANSITION_LENGTH);

		// Draw outer colored box
		double outerProgress = Math.min(1.0, 2.0 * progress);
		double outerRegress = outerProgress;
		int colorIndex = 0;
		while (outerRegress > 0.0) {
			int outerWidth = (int) (T2Config.WINDOW_WIDTH * outerRegress);
			int outerHeight = (int) (T2Config.WINDOW_HEIGHT * outerRegress);
			int outerX = (T2Config.WINDOW_WIDTH - outerWidth) / 2;
			int outerY = (T2Config.WINDOW_HEIGHT - outerHeight) / 2;
			g.setColor(TRANSITION_COLORS[colorIndex++ % TRANSITION_COLORS.length]);
			g.fillRect(outerX, outerY, outerWidth, outerHeight);
			g.setColor(Color.BLACK);
			g.fillRect(outerX + 5, outerY + 5, Math.max(0, outerWidth - 10), Math.max(0, outerHeight - 10));
			outerRegress -= 0.02;
		}

		// Draw inner black box
		double innerProgress = Math.max(0.0, 2.0 * progress - 0.5);
		int innerWidth = (int) (T2Config.WINDOW_WIDTH * innerProgress);
		int innerHeight = (int) (T2Config.WINDOW_HEIGHT * innerProgress);
		int innerX = (T2Config.WINDOW_WIDTH - innerWidth) / 2;
		int innerY = (T2Config.WINDOW_HEIGHT - innerHeight) / 2;
		g.setColor(Color.BLACK);
		g.fillRect(innerX, innerY, innerWidth, innerHeight);

		transitionCounter--;
	}
}
