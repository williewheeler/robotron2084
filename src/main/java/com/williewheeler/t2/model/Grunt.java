package com.williewheeler.t2.model;

import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/13/17.
 */
public class Grunt {
	private static Random random = new Random();

	private GameState gameState;
	private EntityState entityState = EntityState.BORN;
	private int x;
	private int y;

	private int bornCountdown = -1;
	private int moveCountdown = -1;
	private int deadCountdown = -1;

	public Grunt(GameState gameState) {
		this.gameState = gameState;
		setEntityState(EntityState.BORN);

		Player player = gameState.getPlayer();

		boolean tooClose = true;
		while (tooClose) {
			this.x = random.nextInt(ARENA_WIDTH);
			this.y = random.nextInt(ARENA_HEIGHT);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > 200) {
				tooClose = false;
			}
		}

		// TODO Push the grunt away from the center so it doesn't automatically kill the player on startup. [WLW]
	}

	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState entityState) {
		this.entityState = entityState;

		// Special handling for a couple cases
		Player player = gameState.getPlayer();
		switch (entityState) {
			case BORN:
				this.bornCountdown = ENTITY_BORN_COUNTDOWN;
				break;
			case DEAD:
				this.deadCountdown = ENTITY_DEAD_COUNTDOWN;
				player.incrementScore(GRUNT_SCORE_VALUE);
				gameState.fireGameEvent(GameEvent.EXPLODE);
				break;
		}
	}

	public int getBornCountdown() {
		return bornCountdown;
	}

	public int getDeadCountdown() {
		return deadCountdown;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

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
		if (--bornCountdown == -1) {
			setEntityState(EntityState.ALIVE);
		}
	}

	private void updateAlive() {
		if (moveCountdown == -1) {
			this.moveCountdown = random.nextInt(GRUNT_MOVE_MAX_PERIOD);
		}
		if (moveCountdown == 0) {
			moveTowardPlayer();
		}
		moveCountdown--;
	}

	private void moveTowardPlayer() {
		Player player = gameState.getPlayer();
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
