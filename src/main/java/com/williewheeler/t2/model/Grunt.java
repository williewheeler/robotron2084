package com.williewheeler.t2.model;

import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/13/17.
 */
public class Grunt {
	private static Random random = new Random();

	private GameState gameState;
	private int x;
	private int y;
	private int moveIn = -1;

	public Grunt(GameState gameState) {
		this.gameState = gameState;
		Player player = gameState.getPlayer();

		boolean tooClose = true;
		while (tooClose) {
			this.x = random.nextInt(ARENA_WIDTH);
			this.y = random.nextInt(ARENA_HEIGHT);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > 200) {
				tooClose = false;
			}
		}

		// TODO Push the grunt away from the center so it doesn't automatically kill the player on startup. [WLW]
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void update() {
		if (moveIn == -1) {
			this.moveIn = random.nextInt(GRUNT_MOVE_MAX_PERIOD);
		}

		if (moveIn == 0) {
			moveTowardPlayer();
		}

		moveIn--;
	}

	private void moveTowardPlayer() {
		Player player = gameState.getPlayer();
		if (player.getX() > x) {
			x += GRUNT_MOVE_DISTANCE;
		}
		if (player.getX() < x) {
			x -= GRUNT_MOVE_DISTANCE;
		}
		if (player.getY() > y) {
			y += GRUNT_MOVE_DISTANCE;
		}
		if (player.getY() < y) {
			y -= GRUNT_MOVE_DISTANCE;
		}
	}
}
