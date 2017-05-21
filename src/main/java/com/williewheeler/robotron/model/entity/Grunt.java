package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.model.event.GameEvent;
import com.williewheeler.robotron.util.MathUtil;

import static com.williewheeler.robotron.GameConfig.*;
import static com.williewheeler.robotron.model.entity.EntityState.ALIVE;
import static com.williewheeler.robotron.model.entity.EntityState.GONE;

/**
 * Created by willie on 5/13/17.
 */
public class Grunt extends AbstractEntity {
	private GameModel gameModel;

	private int bornCountdown = -1;
	private int moveCountdown = -1;
	private int deadCountdown = -1;

	// Hacky
	private int numMoves = MathUtil.randomInt(4);

	public Grunt(GameModel gameModel) {
		this.gameModel = gameModel;
		setState(EntityState.BORN);

		// TODO Extract this since other enemies use it.
		Player player = gameModel.getPlayer();
		boolean tooClose = true;
		while (tooClose) {
			int x = MathUtil.randomInt(ARENA_WIDTH);
			int y = MathUtil.randomInt(ARENA_HEIGHT);
			setX(x);
			setY(y);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > PLAYER_CLEAR_RADIUS) {
				tooClose = false;
			}
		}
	}

	@Override
	public void setState(EntityState state) {
		super.setState(state);

		// Special handling for a couple cases
		Player player = gameModel.getPlayer();
		switch (state) {
			case BORN:
				this.bornCountdown = ENTITY_BORN_COUNTDOWN;
				break;
			case DEAD:
				this.deadCountdown = ENTITY_DEAD_COUNTDOWN;
				player.incrementScore(GRUNT_SCORE_VALUE);
				gameModel.fireGameEvent(GameEvent.EXPLODE);
				break;
		}
	}

	public int getBornCountdown() {
		return bornCountdown;
	}

	public int getDeadCountdown() {
		return deadCountdown;
	}

	public int getNumMoves() {
		return numMoves;
	}

	@Override
	public void update() {
		EntityState state = getState();
		switch (state) {
			case BORN:
				updateBorn();
				break;
			case ALIVE:
				updateAlive();
				break;
			case DEAD:
				updateDead();
				break;
			case GONE:
				// No-op. Update loop will reclaim buried grunts.
				break;
			default:
				throw new IllegalStateException("Illegal state: " + state);
		}
	}

	private void updateBorn() {
		if (--bornCountdown < 0) {
			setState(ALIVE);
		}
	}

	private void updateAlive() {
		if (moveCountdown < 0) {
			this.moveCountdown = MathUtil.randomInt(GRUNT_MOVE_MAX_PERIOD);
		} else {
			if (moveCountdown == 0) {
				numMoves++;
				moveTowardPlayer();
			}
			moveCountdown--;
		}
	}

	private void moveTowardPlayer() {
		Player player = gameModel.getPlayer();
		int x = getX();
		int y = getY();

		if (player.getX() > x) {
			incrX(GRUNT_MOVE_DISTANCE);
		}
		if (player.getX() < x) {
			incrX(-GRUNT_MOVE_DISTANCE);
		}
		if (player.getY() > y) {
			incrY(GRUNT_MOVE_DISTANCE);
		}
		if (player.getY() < y) {
			incrY(-GRUNT_MOVE_DISTANCE);
		}
	}

	private void updateDead() {
		this.deadCountdown--;
		if (deadCountdown < 0) {
			setState(GONE);
		}
	}
}
