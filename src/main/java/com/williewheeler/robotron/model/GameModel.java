package com.williewheeler.robotron.model;

import com.williewheeler.robotron.GameConfig;
import com.williewheeler.robotron.model.entity.Electrode;
import com.williewheeler.robotron.model.entity.Grunt;
import com.williewheeler.robotron.model.entity.Hulk;
import com.williewheeler.robotron.model.entity.Human;
import com.williewheeler.robotron.model.entity.HumanType;
import com.williewheeler.robotron.model.entity.Player;
import com.williewheeler.robotron.model.entity.PlayerMissile;
import com.williewheeler.robotron.model.event.GameEvent;
import com.williewheeler.robotron.model.event.GameListener;
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
public class GameModel {
	private static final Logger log = LoggerFactory.getLogger(GameModel.class);

	private CollisionDetector collisionDetector;

	private Player player;
	private int waveNumber;
	private final List<Grunt> grunts = new LinkedList<>();
	private final List<Electrode> electrodes = new LinkedList<>();
	private final List<Human> humans = new LinkedList<>();
	private final List<Hulk> hulks = new LinkedList<>();
	private final List<PlayerMissile> playerMissiles = new LinkedList<>();

	private int cyclicCounter = 0;

	private List<GameListener> gameListeners = new ArrayList<>();

	public GameModel() {
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

		player.resetPosition();
		grunts.clear();
		electrodes.clear();
		humans.clear();
		hulks.clear();
		playerMissiles.clear();

		for (int i = 0; i < wave.getGrunts(); i++) {
			grunts.add(new Grunt(this));
		}
		for (int i = 0; i < wave.getElectrodes(); i++) {
			electrodes.add(new Electrode(this));
		}
		for (int i = 0; i < wave.getMommies(); i++) {
			humans.add(new Human(HumanType.MOMMY, this));
		}
		for (int i = 0; i < wave.getDaddies(); i++) {
			humans.add(new Human(HumanType.DADDY, this));
		}
		for (int i = 0; i < wave.getMikeys(); i++) {
			humans.add(new Human(HumanType.MIKEY, this));
		}
		for (int i = 0; i < wave.getHulks(); i++) {
			hulks.add(new Hulk(this));
		}
	}

	public void nextWave() {
		this.cyclicCounter = 0;

		// FIXME Don't allow overflow
		this.waveNumber++;

		// FIXME Enter state where player can't walk around, shoot, etc.
		fireGameEvent(GameEvent.NEW_LEVEL);

		startWave();
	}

	public List<PlayerMissile> getPlayerMissiles() {
		return playerMissiles;
	}

	public List<Grunt> getGrunts() {
		return grunts;
	}

	public List<Electrode> getElectrodes() {
		return electrodes;
	}

	public List<Human> getHumans() {
		return humans;
	}

	public List<Hulk> getHulks() {
		return hulks;
	}

	public void update() {
		if (player.isAlive()) {
			this.cyclicCounter = (cyclicCounter + 1) % 20;
			player.update();
			updateGrunts();
			updateFamily();
			updateHulks();
			updatePlayerMissiles();
			collisionDetector.checkCollisions();
			checkNextWave();
		}
	}

	public void fireGameEvent(int type) {
		// TODO Consider caching events instead of creating a new one each time.
		GameEvent event = new GameEvent(this, type);
		for (GameListener listener : gameListeners) {
			listener.handleEvent(event);
		}
	}

	public int getCyclicCounter() {
		return cyclicCounter;
	}

	private void updateGrunts() {
		grunts.stream().forEach(grunt -> grunt.update());
	}

	private void updateFamily() {
		humans.stream().forEach(mommy -> mommy.update());
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
			if (x < 0 || x > GameConfig.ARENA_WIDTH || y < 0 || y > GameConfig.ARENA_HEIGHT) {
				playerMissileIt.remove();
			}
		}
	}

	private void checkNextWave() {
		if (grunts.isEmpty()) {
			nextWave();
		}
	}
}
