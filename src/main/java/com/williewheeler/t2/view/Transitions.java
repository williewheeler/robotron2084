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

		// First half we expand the colored box to the outside.
		// Second half we expand a black box to the outside.

		double progress = 1.0 - ((double) transitionCounter / T2Config.TRANSITION_LENGTH);

		// Draw color box
		double colorProgress = Math.min(1.0, 2.0 * progress);
		int colorWidth = (int) (T2Config.WINDOW_WIDTH * colorProgress);
		int colorHeight = (int) (T2Config.WINDOW_HEIGHT * colorProgress);

		while (colorWidth > 0 && colorHeight > 0) {
			int x = (T2Config.WINDOW_WIDTH - colorWidth) / 2;
			int y = (T2Config.WINDOW_HEIGHT - colorHeight) / 2;

			g.setColor(TRANSITION_COLORS[colorWidth % TRANSITION_COLORS.length]);
			g.fillRect(x, y, colorWidth, colorHeight);

			colorWidth -= 8;
			colorHeight -= 8;

			x = (T2Config.WINDOW_WIDTH - colorWidth) / 2;
			y = (T2Config.WINDOW_HEIGHT - colorHeight) / 2;

			g.setColor(Color.BLACK);
			g.fillRect(x, y, colorWidth, colorHeight);

			colorWidth -= 4;
			colorHeight -= 4;
		}

		// Draw inner black box
		double blackProgress = Math.min(1.0, Math.max(0.0, 2.0 * progress - 1.0));
		int blackWidth = (int) (T2Config.WINDOW_WIDTH * blackProgress);
		int blackHeight = (int) (T2Config.WINDOW_HEIGHT * blackProgress);
		g.setColor(Color.BLACK);
		int blackX = (T2Config.WINDOW_WIDTH - blackWidth) / 2;
		int blackY = (T2Config.WINDOW_HEIGHT - blackHeight) / 2;
		g.fillRect(blackX, blackY, blackWidth, blackHeight);

		transitionCounter--;
	}
}
