package com.williewheeler.t2.model.entity;

import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.util.MathUtil;

import java.util.Random;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/16/17.
 */
public class Electrode {
	private static Random random = new Random();

	private GameState gameState;
	private int x;
	private int y;

	public Electrode(GameState gameState) {
		this.gameState = gameState;

		// TODO Extract this since other enemies use it.
		Player player = gameState.getPlayer();
		boolean tooClose = true;
		while (tooClose) {
			this.x = random.nextInt(ARENA_WIDTH);
			this.y = random.nextInt(ARENA_HEIGHT);
			if (MathUtil.distance(x, y, player.getX(), player.getY()) > PLAYER_CLEAR_RADIUS) {
				tooClose = false;
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
