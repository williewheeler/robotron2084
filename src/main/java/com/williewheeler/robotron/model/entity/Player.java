package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.RobotronConfig;
import com.williewheeler.robotron.model.event.GameEvent;
import com.williewheeler.robotron.model.GameModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.williewheeler.robotron.RobotronConfig.*;

/**
 * Created by willie on 5/13/17.
 */
public class Player extends AbstractEntity {
	private static final Logger log = LoggerFactory.getLogger(Player.class);

	private GameModel gameModel;

	private int score = 0;

	private boolean alive = true;

	private boolean moveUpIntent;
	private boolean moveDownIntent;
	private boolean moveLeftIntent;
	private boolean moveRightIntent;

	private boolean fireUpIntent;
	private boolean fireDownIntent;
	private boolean fireLeftIntent;
	private boolean fireRightIntent;

	// Counters
	private int rechargeCounter = -1;
	private int walkCounter = -1;

	public Player(GameModel gameModel) {
		this.gameModel = gameModel;
		resetPosition();
	}

	public int getScore() {
		return score;
	}

	public void incrementScore(int delta) {
		this.score += delta;
	}

	public void resetPosition() {
		setX(ARENA_WIDTH / 2);
		setY(ARENA_HEIGHT / 2);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
		if (!alive) {
			gameModel.fireGameEvent(GameEvent.PLAYER_DEAD);
		}
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

	@Override
	public void update() {
//		log.trace("Updating game state");
		updatePlayer();
		checkFireMissile();
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

		incrX(deltaX);
		incrY(deltaY);

		if (deltaX != 0 || deltaY != 0) {
			if (walkCounter == -1) {
				walkCounter = PLAYER_WALK_PERIOD;
			}
			if (walkCounter-- == 0) {
				gameModel.fireGameEvent(GameEvent.WALK);
			}
		}

		// Bounds check
		int x = getX();
		int y = getY();

		if (x < 0) {
			setX(0);
		}
		if (x > RobotronConfig.ARENA_WIDTH) {
			setX(RobotronConfig.ARENA_WIDTH);
		}
		if (y < 0) {
			setY(0);
		}
		if (y > RobotronConfig.ARENA_HEIGHT) {
			setY(RobotronConfig.ARENA_HEIGHT);
		}
	}

	private void checkFireMissile() {
		if (rechargeCounter == -1) {
			rechargeCounter = PLAYER_RECHARGE_PERIOD;
		}

		if (rechargeCounter == 0) {
			int deltaX = 0;
			int deltaY = 0;

			if (fireUpIntent) {
				deltaY -= PLAYER_FIRE_DISTANCE;
			}
			if (fireDownIntent) {
				deltaY += PLAYER_FIRE_DISTANCE;
			}
			if (fireLeftIntent) {
				deltaX -= PLAYER_FIRE_DISTANCE;
			}
			if (fireRightIntent) {
				deltaX += PLAYER_FIRE_DISTANCE;
			}

			int x = getX();
			int y = getY();

			if (deltaX != 0 || deltaY != 0) {
				PlayerMissile missile = new PlayerMissile(x, y, deltaX, deltaY);
				gameModel.getPlayerMissiles().add(missile);
				gameModel.fireGameEvent(GameEvent.SHOT);
			}
		}

		rechargeCounter--;
	}
}
