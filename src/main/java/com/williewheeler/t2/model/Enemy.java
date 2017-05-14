package com.williewheeler.t2.model;

import java.util.Random;

import static com.williewheeler.t2.T2Config.ENEMY_MOVE_DISTANCE;
import static com.williewheeler.t2.T2Config.ENEMY_MOVE_MAX_PERIOD;

/**
 * Created by willie on 5/13/17.
 */
public class Enemy {
	private static Random random = new Random();

	private GameState gameState;
	private int x = 100;
	private int y = 100;
	private int moveIn = -1;

	public Enemy(GameState gameState) {
		this.gameState = gameState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void update() {
		if (moveIn == -1) {
			this.moveIn = random.nextInt(ENEMY_MOVE_MAX_PERIOD);
		}

		if (moveIn == 0) {
			moveTowardPlayer();
		}

		moveIn--;
	}

	private void moveTowardPlayer() {
		Player player = gameState.getPlayer();
		if (player.getX() > x) {
			x += ENEMY_MOVE_DISTANCE;
		}
		if (player.getX() < x) {
			x -= ENEMY_MOVE_DISTANCE;
		}
		if (player.getY() > y) {
			y += ENEMY_MOVE_DISTANCE;
		}
		if (player.getY() < x) {
			y -= ENEMY_MOVE_DISTANCE;
		}
	}
}
