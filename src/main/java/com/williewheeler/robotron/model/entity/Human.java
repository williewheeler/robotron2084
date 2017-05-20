package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.util.MathUtil;

import java.util.Random;

import static com.williewheeler.robotron.GameConfig.*;

/**
 * Created by willie on 5/17/17.
 */
public class Human extends AbstractEntity {
	private static final int TOLERANCE = 3;
	private static Random random = new Random();

	private HumanType type;
	private GameModel gameModel;
	private int moveCountdown = -1;

	// FIXME Hacky
	private int numMoves = random.nextInt(4);

	public Human(HumanType type, GameModel gameModel) {
		this.type = type;
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

	public HumanType getType() {
		return type;
	}

	public int getNumMoves() {
		return numMoves;
	}

	@Override
	public void update() {
		if (moveCountdown < 0) {
			this.moveCountdown = HUMAN_MOVE_PERIOD;
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

		int deltaX = player.getX() - getX();
		int deltaY = player.getY() - getY();

		int moveX = 0;
		int moveY = 0;

		if (deltaY < -TOLERANCE) {
			moveY = -HUMAN_MOVE_DISTANCE;
		}
		if (deltaY > TOLERANCE) {
			moveY = HUMAN_MOVE_DISTANCE;
		}
		if (deltaX < -TOLERANCE) {
			moveX = -HUMAN_MOVE_DISTANCE;
		}
		if (deltaX > TOLERANCE) {
			moveX = HUMAN_MOVE_DISTANCE;
		}

		move(moveX, moveY);
	}
}
