package com.williewheeler.t2.model.entity;

import com.williewheeler.t2.model.GameModel;
import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/16/17.
 */
public class Electrode implements Entity {
	private static Random random = new Random();

	private GameModel gameModel;
	private int x;
	private int y;

	public Electrode(GameModel gameModel) {
		this.gameModel = gameModel;

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

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void update() {
		// TODO No-op, though maybe we want to move the cyclic counter out of the GameModel into this class as a static
		// or at least shared field.
	}
}
