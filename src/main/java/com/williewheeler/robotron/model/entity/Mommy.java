package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.util.MathUtil;

import java.util.Random;

import static com.williewheeler.robotron.GameConfig.*;

/**
 * Created by willie on 5/17/17.
 */
public class Mommy extends AbstractEntity {
	private static Random random = new Random();

	private GameModel gameModel;
	private int moveCountdown = -1;

	// FIXME Hacky
	private int numMoves = random.nextInt(4);

	public Mommy(GameModel gameModel) {
		this.gameModel = gameModel;

		// TODO Extract this since other entities use it.
		Player player = gameModel.getPlayer();
		boolean tooClose = true;
		while (tooClose) {
			int x = random.nextInt(ARENA_WIDTH);
			int y = random.nextInt(ARENA_HEIGHT);
			setX(x);
			setY(y);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > PLAYER_CLEAR_RADIUS) {
				tooClose = false;
			}
		}
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
		final int tolerance = 3;

		Player player = gameModel.getPlayer();

		int deltaX = player.getX() - getX();
		int deltaY = player.getY() - getY();

		int moveX = 0;
		int moveY = 0;

		if (deltaY < -tolerance) {
			moveY = -MOMMY_MOVE_DISTANCE;
		}
		if (deltaY > tolerance) {
			moveY = MOMMY_MOVE_DISTANCE;
		}
		if (deltaX < -tolerance) {
			moveX = -MOMMY_MOVE_DISTANCE;
		}
		if (deltaX > tolerance) {
			moveX = MOMMY_MOVE_DISTANCE;
		}

		move(moveX, moveY);
	}
}
