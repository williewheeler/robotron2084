package com.williewheeler.robotron.model;

import com.williewheeler.robotron.model.entity.Electrode;
import com.williewheeler.robotron.model.entity.EntityState;
import com.williewheeler.robotron.model.entity.Grunt;
import com.williewheeler.robotron.model.entity.Hulk;
import com.williewheeler.robotron.model.entity.Mommy;
import com.williewheeler.robotron.model.entity.Player;
import com.williewheeler.robotron.model.entity.PlayerMissile;
import com.williewheeler.robotron.model.event.GameEvent;
import com.williewheeler.robotron.util.MathUtil;

import java.util.List;
import java.util.ListIterator;

import static com.williewheeler.robotron.RobotronConfig.COLLISION_DISTANCE;
import static com.williewheeler.robotron.RobotronConfig.MOMMY_SCORE_VALUE;

// FIXME There is a bug where if you shoot a single shot to kill the last grunt, the level doesn't advance
// til you fire one more shot. It's because the next level check occurs only in the context of the collision detection
// process. [WLW]

/**
 * Created by willie on 5/15/17.
 */
public class CollisionDetector {
	private GameModel gameModel;

	public CollisionDetector(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	// TODO Need a way to signal game over
	public void checkCollisions() {
		checkRescue();

		if (!checkPlayerShotEnemy()) {
			return;
		}
		if (!checkGruntHitElectrode()) {
			return;
		}
		if (!checkPlayerDied()) {
			return;
		}
	}

	private void checkRescue() {
		Player player = gameModel.getPlayer();
		int playerX = player.getX();
		int playerY = player.getY();

		List<Mommy> mommies = gameModel.getMommies();
		ListIterator<Mommy> mommyIt = mommies.listIterator();

		while (mommyIt.hasNext()) {
			Mommy mommy = mommyIt.next();
			int dist = MathUtil.distance(playerX, playerY, mommy.getX(), mommy.getY());
			if (dist < COLLISION_DISTANCE) {
				mommyIt.remove();
				player.incrementScore(MOMMY_SCORE_VALUE);
				gameModel.fireGameEvent(GameEvent.RESCUE);
			}
		}
	}

	/**
	 * @return boolean indicating whether to keep running checks
	 */
	private boolean checkPlayerShotEnemy() {
		List<PlayerMissile> playerMissiles = gameModel.getPlayerMissiles();

		List<Grunt> grunts = gameModel.getGrunts();
		List<Electrode> electrodes = gameModel.getElectrodes();

		// TODO Use a spatial data structure (like k-d tree) to avoid linear search.
		ListIterator<PlayerMissile> playerMissileIt = playerMissiles.listIterator();

		processMissiles:
		while (playerMissileIt.hasNext()) {
			PlayerMissile playerMissile = playerMissileIt.next();
			int missileX = playerMissile.getX();
			int missileY = playerMissile.getY();

			ListIterator<Grunt> gruntIt = grunts.listIterator();
			while (gruntIt.hasNext()) {
				Grunt grunt = gruntIt.next();
				EntityState gruntState = grunt.getEntityState();
				if (gruntState == EntityState.ALIVE) {
					int dist = MathUtil.distance(missileX, missileY, grunt.getX(), grunt.getY());
					if (dist < COLLISION_DISTANCE) {
						playerMissileIt.remove();
						grunt.setEntityState(EntityState.DEAD);
						continue processMissiles;
					}
				} else if (gruntState == EntityState.BURIED) {
					gruntIt.remove();
					if (grunts.isEmpty()) {
						gameModel.nextWave();

						// TODO Move this to the end of the method once we have other entities
						return false;
					}
				}
			}

			ListIterator<Electrode> electrodeIt = electrodes.listIterator();
			while (electrodeIt.hasNext()) {
				Electrode electrode = electrodeIt.next();
				int dist = MathUtil.distance(missileX, missileY, electrode.getX(), electrode.getY());
				if (dist < 11) {
					electrodeIt.remove();
					continue processMissiles;
				}
			}
		}

		return true;
	}

	private boolean checkGruntHitElectrode() {
		List<Grunt> grunts = gameModel.getGrunts();
		ListIterator<Grunt> gruntIt = grunts.listIterator();

		processGrunts:
		while (gruntIt.hasNext()) {
			Grunt grunt = gruntIt.next();
			EntityState gruntState = grunt.getEntityState();
			if (gruntState == EntityState.ALIVE) {
				int gruntX = grunt.getX();
				int gruntY = grunt.getY();

				List<Electrode> electrodes = gameModel.getElectrodes();
				ListIterator<Electrode> electrodeIt = electrodes.listIterator();
				while (electrodeIt.hasNext()) {
					Electrode electrode = electrodeIt.next();

					int dist = MathUtil.distance(gruntX, gruntY, electrode.getX(), electrode.getY());
					if (dist < COLLISION_DISTANCE) {
						electrodeIt.remove();
						grunt.setEntityState(EntityState.DEAD);
						continue processGrunts;
					}
				}
			} else if (gruntState == EntityState.BURIED) {
				gruntIt.remove();
				if (grunts.isEmpty()) {
					gameModel.nextWave();

					// TODO Move this to the end of the method once we have other entities
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @return boolean indicating whether to keep running checks
	 */
	private boolean checkPlayerDied() {
		Player player = gameModel.getPlayer();
		int playerX = player.getX();
		int playerY = player.getY();

		List<Grunt> grunts = gameModel.getGrunts();
		ListIterator<Grunt> gruntIt = grunts.listIterator();
		while (gruntIt.hasNext()) {
			Grunt grunt = gruntIt.next();
			if (grunt.getEntityState() == EntityState.ALIVE) {
				int dist = MathUtil.distance(playerX, playerY, grunt.getX(), grunt.getY());
				if (dist < 20) {
					player.setAlive(false);
					return false;
				}
			}
		}

		List<Electrode> electrodes = gameModel.getElectrodes();
		for (Electrode electrode : electrodes) {
			int dist = MathUtil.distance(playerX, playerY, electrode.getX(), electrode.getY());
			if (dist < 20) {
				player.setAlive(false);
				return false;
			}
		}

		List<Hulk> hulks = gameModel.getHulks();
		for (Hulk hulk : hulks) {
			int dist = MathUtil.distance(playerX, playerY, hulk.getX(), hulk.getY());
			if (dist < 20) {
				player.setAlive(false);
				return false;
			}
		}

		return true;
	}
}
