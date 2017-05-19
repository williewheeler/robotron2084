package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.util.MathUtil;

import java.util.Random;

import static com.williewheeler.robotron.RobotronConfig.*;

// TODO extend AbstractEntity

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
		Player player = gameModel.getPlayer();
		int x = getX();
		int y = getY();
		if (player.getX() > x) {
			incrX(MOMMY_MOVE_DISTANCE);
		}
		if (player.getX() < x) {
			incrX(-MOMMY_MOVE_DISTANCE);
		}
		if (player.getY() > y) {
			incrY(MOMMY_MOVE_DISTANCE);
		}
		if (player.getY() < y) {
			incrY(-MOMMY_MOVE_DISTANCE);
		}
	}
}
