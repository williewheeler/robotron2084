package com.williewheeler.t2.model;

import com.williewheeler.t2.T2Config;
import com.williewheeler.t2.model.entity.Grunt;
import com.williewheeler.t2.model.entity.Hulk;
import com.williewheeler.t2.model.entity.Player;
import com.williewheeler.t2.model.entity.PlayerMissile;
import com.williewheeler.t2.model.event.GameEvent;
import com.williewheeler.t2.model.event.GameListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

// TODO The game itself should be a state machine (e.g., title, credits, arena, transition, high scores, etc.)
// Player shouldn't be able to fire unless we're in the arena. [WLW]

/**
 * Created by willie on 5/12/17.
 */
public class GameState {
	private static final Logger log = LoggerFactory.getLogger(GameState.class);

	private CollisionDetector collisionDetector;

	private Player player;
	private int waveNumber;
	private final List<Grunt> grunts = new LinkedList<>();
	private final List<Hulk> hulks = new LinkedList<>();
	private final List<PlayerMissile> playerMissiles = new LinkedList<>();

	private List<GameListener> gameListeners = new ArrayList<>();

	public GameState() {
		this.collisionDetector = new CollisionDetector(this);
		this.player = new Player(this);
		this.waveNumber = 1;
		startWave();
	}

	public void addGameListener(GameListener listener) {
		gameListeners.add(listener);
	}

	public Player getPlayer() {
		return player;
	}

	public int getWaveNumber() {
		return waveNumber;
	}

	public void startWave() {
		Wave wave = WaveFactory.getWave(waveNumber);

		grunts.clear();
		hulks.clear();
		playerMissiles.clear();

		for (int i = 0; i < wave.getGrunts(); i++) {
			grunts.add(new Grunt(this));
		}
		for (int i = 0; i < wave.getHulks(); i++) {
			hulks.add(new Hulk(this));
		}
	}

	public void nextWave() {

		// FIXME Don't allow overflow
		this.waveNumber++;
		startWave();

		// TODO Enter state where player can't walk around, shoot, etc.

		fireGameEvent(GameEvent.NEW_LEVEL);
	}

	public List<PlayerMissile> getPlayerMissiles() {
		return playerMissiles;
	}

	public List<Grunt> getGrunts() {
		return grunts;
	}

	public List<Hulk> getHulks() {
		return hulks;
	}

	public void update() {
		if (player.isAlive()) {
			player.update();
			updateGrunts();
			updateHulks();
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

	private void updateHulks() {
		hulks.stream().forEach(hulk -> hulk.update());
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
