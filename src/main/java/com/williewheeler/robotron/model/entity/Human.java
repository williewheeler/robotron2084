package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.williewheeler.robotron.GameConfig.*;

/**
 * Created by willie on 5/17/17.
 */
public class Human extends AbstractEntity {
	private static final Logger log = LoggerFactory.getLogger(Human.class);

	private static final int INIT_RESCUED_COUNTDOWN = 30;
	private static final int INIT_DEAD_COUNTDOWN = 30;
	private static final int TOLERANCE = 3;

	private HumanType type;
	private GameModel gameModel;

	private int moveCountdown = -1;
	private int rescuedCountdown = -1;
	private int deadCountdown = -1;

	// FIXME Hacky
	private int numMoves = MathUtil.randomInt(4);

	public Human(HumanType type, GameModel gameModel) {
		this.type = type;
		this.gameModel = gameModel;

		// TODO Turn this into BORN
		setState(EntityState.ALIVE);

		placeInArena(gameModel);
	}

	@Override
	public void setState(EntityState state) {
		log.trace("Setting human state to {}", state);
		super.setState(state);
		switch (state) {
			case RESCUED:
				this.rescuedCountdown = INIT_RESCUED_COUNTDOWN;
				break;
			case DEAD:
				this.deadCountdown = INIT_DEAD_COUNTDOWN;
				break;
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
		switch (getState()) {
			case ALIVE:
				move();
				break;
			case RESCUED:
				if (rescuedCountdown == 0) {
					setState(EntityState.GONE);
				}
				rescuedCountdown--;
				break;
			case DEAD:
				if (deadCountdown == 0) {
					setState(EntityState.GONE);
				}
				deadCountdown--;
				break;
		}
	}

	private void placeInArena(GameModel gameModel) {
		// TODO Extract this since other entities use it.
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

	private void move() {
		if (moveCountdown < 0) {
			this.moveCountdown = HUMAN_MOVE_PERIOD;
		} else {
			if (moveCountdown == 0) {
				numMoves++;
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
			moveCountdown--;
		}
	}
}
