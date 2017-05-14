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

	private int playerX = PLAYER_START_X;
	private int playerY = PLAYER_START_Y;

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

	public int getPlayerX() {
		return playerX;
	}

	public int getPlayerY() {
		return playerY;
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

		playerX += deltaX;
		playerY += deltaY;

		// Bounds check
		if (playerX < MIN_X) {
			playerX = MIN_X;
		}
		if (playerX > MAX_X) {
			playerX = MAX_X;
		}
		if (playerY < MIN_Y) {
			playerY = MIN_Y;
		}
		if (playerY > MAX_Y) {
			playerY = MAX_Y;
		}
	}
}
