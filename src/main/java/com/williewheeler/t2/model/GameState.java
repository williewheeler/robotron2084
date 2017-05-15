package com.williewheeler.t2.model;

import com.williewheeler.t2.T2Config;
import com.williewheeler.t2.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/12/17.
 */
public class GameState {
	private static final Logger log = LoggerFactory.getLogger(GameState.class);

	private Player player;

	// Use linked lists here for constant time removals.
	private final List<PlayerMissile> playerMissiles = new LinkedList<>();
	private final List<Grunt> grunts = new LinkedList<>();

	private List<GameListener> gameListeners = new ArrayList<>();

	public GameState() {
		this.player = new Player(this);
		for (int i = 0; i < 15; i++) {
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
			checkPlayerMissileCollisions();
			checkPlayerCollision();
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
//		log.trace("playerMissiles.size={}", playerMissiles.size());
	}

	private void checkPlayerMissileCollisions() {

		// TODO Use a spatial data structure (like k-d tree) to avoid linear search.
		ListIterator<PlayerMissile> playerMissileIt = playerMissiles.listIterator();

		processMissiles:
		while (playerMissileIt.hasNext()) {
			PlayerMissile playerMissile = playerMissileIt.next();

			// Process grunts
			ListIterator<Grunt> gruntIt = grunts.listIterator();
			while (gruntIt.hasNext()) {
				Grunt grunt = gruntIt.next();
				int dist = MathUtil.distance(playerMissile.getX(), playerMissile.getY(), grunt.getX(), grunt.getY());
				if (dist < COLLISION_DISTANCE) {
					playerMissileIt.remove();
					gruntIt.remove();
					player.incrementScore(GRUNT_SCORE_VALUE);
					fireGameEvent(GameEvent.EXPLODE);

					if (grunts.isEmpty()) {
						fireGameEvent(GameEvent.NEW_LEVEL);
					}

					continue processMissiles;
				}
			}
		}
	}

	private void checkPlayerCollision() {
		ListIterator<Grunt> gruntIt = grunts.listIterator();
		while (gruntIt.hasNext()) {
			Grunt grunt = gruntIt.next();
			int dist = MathUtil.distance(player.getX(), player.getY(), grunt.getX(), grunt.getY());
			if (dist < 20) {
				player.setAlive(false);
				fireGameEvent(GameEvent.PLAYER_DEAD);
			}
		}
	}
}
