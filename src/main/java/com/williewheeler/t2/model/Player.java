package com.williewheeler.t2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/13/17.
 */
public class Player {
	private static final Logger log = LoggerFactory.getLogger(Player.class);

	private int score = 0;

	private int x = PLAYER_START_X;
	private int y = PLAYER_START_Y;

	private boolean moveUpIntent;
	private boolean moveDownIntent;
	private boolean moveLeftIntent;
	private boolean moveRightIntent;

	private boolean fireUpIntent;
	private boolean fireDownIntent;
	private boolean fireLeftIntent;
	private boolean fireRightIntent;

	public int getScore() {
		return score;
	}

	public void incrementScore(int delta) {
		this.score += delta;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setMoveUpIntent(boolean moveUpIntent) {
		this.moveUpIntent = moveUpIntent;
	}

	public void setMoveDownIntent(boolean moveDownIntent) {
		this.moveDownIntent = moveDownIntent;
	}

	public void setMoveLeftIntent(boolean moveLeftIntent) {
		this.moveLeftIntent = moveLeftIntent;
	}

	public void setMoveRightIntent(boolean moveRightIntent) {
		this.moveRightIntent = moveRightIntent;
	}

	public void setFireUpIntent(boolean fireUpIntent) {
		this.fireUpIntent = fireUpIntent;
	}

	public void setFireDownIntent(boolean fireDownIntent) {
		this.fireDownIntent = fireDownIntent;
	}

	public void setFireLeftIntent(boolean fireLeftIntent) {
		this.fireLeftIntent = fireLeftIntent;
	}

	public void setFireRightIntent(boolean fireRightIntent) {
		this.fireRightIntent = fireRightIntent;
	}

	public void update() {
//		log.trace("Updating game state");
		incrementScore(15);
		updatePlayer();
	}

	private void updatePlayer() {
		int deltaX = 0;
		int deltaY = 0;

		if (moveUpIntent) {
			deltaY -= PLAYER_MOVE_DISTANCE;
		}
		if (moveDownIntent) {
			deltaY += PLAYER_MOVE_DISTANCE;
		}
		if (moveLeftIntent) {
			deltaX -= PLAYER_MOVE_DISTANCE;
		}
		if (moveRightIntent) {
			deltaX += PLAYER_MOVE_DISTANCE;
		}

		x += deltaX;
		y += deltaY;

		// Bounds check
		if (x < MIN_X) {
			x = MIN_X;
		}
		if (x > MAX_X) {
			x = MAX_X;
		}
		if (y < MIN_Y) {
			y = MIN_Y;
		}
		if (y > MAX_Y) {
			y = MAX_Y;
		}
	}
}
