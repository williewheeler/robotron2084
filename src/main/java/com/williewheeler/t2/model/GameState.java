package com.williewheeler.t2.model;

import com.williewheeler.t2.T2Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by willie on 5/12/17.
 */
public class GameState {
	private static final Logger log = LoggerFactory.getLogger(GameState.class);

	private CollisionDetector collisionDetector;

	private Player player;
	private final List<PlayerMissile> playerMissiles = new LinkedList<>();
	private final List<Grunt> grunts = new LinkedList<>();

	private List<GameListener> gameListeners = new ArrayList<>();

	public GameState() {
		this.collisionDetector = new CollisionDetector(this);

		this.player = new Player(this);

		for (int i = 0; i < 100; i++) {
			grunts.add(new Grunt(this));
		}
	}

	public void addGameListener(GameListener listener) {
		gameListeners.add(listener);
	}

	public Player getPlayer() {
		return player;
	}

	public List<PlayerMissile> getPlayerMissiles() {
		return playerMissiles;
	}

	public List<Grunt> getGrunts() {
		return grunts;
	}

	public void update() {
		if (player.isAlive()) {
			player.update();
			updateGrunts();
			updatePlayerMissiles();
			collisionDetector.checkCollisions();
		}
	}

	public void fireGameEvent(int type) {
		// TODO Consider caching events instead of creating a new one each time.
		GameEvent event = new GameEvent(this, type);
		for (GameListener listener : gameListeners) {
			listener.handleEvent(event);
		}
	}

	private void updateGrunts() {
		grunts.stream().forEach(grunt -> grunt.update());
	}

	private void updatePlayerMissiles() {
		ListIterator<PlayerMissile> playerMissileIt = playerMissiles.listIterator();
		while (playerMissileIt.hasNext()) {
			PlayerMissile playerMissile = playerMissileIt.next();
			playerMissile.update();

			int x = playerMissile.getX();
			int y = playerMissile.getY();
			if (x < 0 || x > T2Config.ARENA_WIDTH || y < 0 || y > T2Config.ARENA_HEIGHT) {
				playerMissileIt.remove();
			}
		}
	}
}
