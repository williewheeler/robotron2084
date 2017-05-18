package com.williewheeler.t2.model.entity;

import com.williewheeler.t2.model.GameModel;
import com.williewheeler.t2.model.event.GameEvent;
import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/13/17.
 */
public class Grunt implements Entity {
	private static Random random = new Random();

	private GameModel gameModel;
	private EntityState entityState = EntityState.BORN;
	private int x;
	private int y;

	private int bornCountdown = -1;
	private int moveCountdown = -1;
	private int deadCountdown = -1;

	// Hacky
	private int numMoves = random.nextInt(4);

	public Grunt(GameModel gameModel) {
		this.gameModel = gameModel;
		setEntityState(EntityState.BORN);

		// TODO Extract this since other enemies use it.
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

	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState entityState) {
		this.entityState = entityState;

		// Special handling for a couple cases
		Player player = gameModel.getPlayer();
		switch (entityState) {
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
		switch (entityState) {
			case BORN:
				updateBorn();
				break;
			case ALIVE:
				updateAlive();
				break;
			case DEAD:
				updateDead();
				break;
			case BURIED:
				// No-op. Update loop will reclaim buried grunts.
				break;
			default:
				throw new IllegalStateException("Unknown entityState: " + entityState);
		}
	}

	private void updateBorn() {
		if (--bornCountdown < 0) {
			setEntityState(EntityState.ALIVE);
		}
	}

	private void updateAlive() {
		if (moveCountdown < 0) {
			this.moveCountdown = random.nextInt(GRUNT_MOVE_MAX_PERIOD);
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
		if (player.getX() > x) {
			x += GRUNT_MOVE_DISTANCE;
		}
		if (player.getX() < x) {
			x -= GRUNT_MOVE_DISTANCE;
		}
		if (player.getY() > y) {
			y += GRUNT_MOVE_DISTANCE;
		}
		if (player.getY() < y) {
			y -= GRUNT_MOVE_DISTANCE;
		}
	}

	private void updateDead() {
		this.deadCountdown--;
		if (deadCountdown < 0) {
			setEntityState(EntityState.BURIED);
		}
	}
}
