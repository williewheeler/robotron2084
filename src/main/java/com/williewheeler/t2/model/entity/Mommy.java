package com.williewheeler.t2.model.entity;

import com.williewheeler.t2.model.GameModel;
import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/17/17.
 */
public class Mommy implements Entity {
	private static Random random = new Random();

	private GameModel gameModel;
	private int x;
	private int y;
	private int moveCountdown = -1;

	// FIXME Hacky
	private int numMoves = random.nextInt(4);

	public Mommy(GameModel gameModel) {
		this.gameModel = gameModel;

		// TODO Extract this since other entities use it.
		Player player = gameModel.getPlayer();
		boolean tooClose = true;
		while (tooClose) {
			this.x = random.nextInt(ARENA_WIDTH);
			this.y = random.nextInt(ARENA_HEIGHT);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > PLAYER_CLEAR_RADIUS) {
				tooClose = false;
			}
		}
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public int getNumMoves() {
		return numMoves;
	}

	@Override
	public void update() {
		if (moveCountdown < 0) {
			this.moveCountdown = MOMMY_MOVE_PERIOD;
		} else {
			if (moveCountdown == 0) {
				numMoves++;
				move();
			}
			moveCountdown--;
		}
	}

	private void move() {
		Player player = gameModel.getPlayer();
		if (player.getX() > x) {
			x += MOMMY_MOVE_DISTANCE;
		}
		if (player.getX() < x) {
			x -= MOMMY_MOVE_DISTANCE;
		}
		if (player.getY() > y) {
			y += MOMMY_MOVE_DISTANCE;
		}
		if (player.getY() < y) {
			y -= MOMMY_MOVE_DISTANCE;
		}
	}
}
