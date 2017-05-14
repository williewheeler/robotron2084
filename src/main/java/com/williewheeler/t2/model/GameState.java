package com.williewheeler.t2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by willie on 5/12/17.
 */
public class GameState {
	private static final Logger log = LoggerFactory.getLogger(GameState.class);

	private Player player;

	public GameState() {
		this.player = new Player();
	}

	public Player getPlayer() {
		return player;
	}

	public void update() {
		player.update();
	}
}
