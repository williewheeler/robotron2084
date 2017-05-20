package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.model.GameModel;
import com.williewheeler.robotron.util.MathUtil;

import java.util.Random;

import static com.williewheeler.robotron.GameConfig.*;

/**
 * Created by willie on 5/16/17.
 */
public class Electrode extends AbstractEntity {
	private static Random random = new Random();

	private GameModel gameModel;

	public Electrode(GameModel gameModel) {
		this.gameModel = gameModel;

		// TODO Extract this since other enemies use it.
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

	@Override
	public void update() {
		// TODO No-op, though maybe we want to move the cyclic counter out of the GameModel into this class as a static
		// or at least shared field.
	}
}
