package com.williewheeler.t2.model.entity;

import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/16/17.
 */
public class Hulk {
	private static Random random = new Random();

	private GameState gameState;
	private int x;
	private int y;
	private int moveCountdown = -1;
	private int numMoves = 0;

	public Hulk(GameState gameState) {
		this.gameState = gameState;

		// TODO Extract this since other enemies use it.
		Player player = gameState.getPlayer();
		boolean tooClose = true;
		while (tooClose) {
			this.x = random.nextInt(ARENA_WIDTH);
			this.y = random.nextInt(ARENA_HEIGHT);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > PLAYER_CLEAR_RADIUS) {
				tooClose = false;
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNumMoves() {
		return numMoves;
	}

	public void update() {
		if (moveCountdown < 0) {
			this.moveCountdown = HULK_MOVE_PERIOD;
		} else {
			if (moveCountdown == 0) {
				numMoves++;

				// FIXME This isn't really how the hulk moves, but we'll fix that.
				Player player = gameState.getPlayer();
				if (player.getX() > x) {
					x += HULK_MOVE_DISTANCE;
				}
				if (player.getX() < x) {
					x -= HULK_MOVE_DISTANCE;
				}
				if (player.getY() > y) {
					y += HULK_MOVE_DISTANCE;
				}
				if (player.getY() < y) {
					y -= HULK_MOVE_DISTANCE;
				}
			}
			moveCountdown--;
		}
	}
}
