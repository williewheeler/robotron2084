package com.williewheeler.robotron.model;

import com.williewheeler.robotron.model.entity.Electrode;
import com.williewheeler.robotron.model.entity.Entity;
import com.williewheeler.robotron.model.entity.EntityState;
import com.williewheeler.robotron.model.entity.Grunt;
import com.williewheeler.robotron.model.entity.Hulk;
import com.williewheeler.robotron.model.entity.Player;
import com.williewheeler.robotron.model.entity.PlayerMissile;
import com.williewheeler.robotron.model.event.GameEvent;
import com.williewheeler.robotron.util.MathUtil;

import java.util.List;
import java.util.ListIterator;

import static com.williewheeler.robotron.RobotronConfig.COLLISION_DISTANCE;
import static com.williewheeler.robotron.RobotronConfig.MOMMY_SCORE_VALUE;

/**
 * Created by willie on 5/15/17.
 */
public class CollisionDetector {
	private GameModel gameModel;

	public CollisionDetector(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	public void checkCollisions() {
		checkRescueHuman();
		checkHumanDied();
		checkPlayerShotEnemy();
		checkGruntHitElectrode();
		checkPlayerDied();
	}

	private void checkRescueHuman() {
		Player player = gameModel.getPlayer();
		int playerX = player.getX();
		int playerY = player.getY();

		List<Entity> humans = gameModel.getHumans();
		ListIterator<Entity> humanIt = humans.listIterator();

		while (humanIt.hasNext()) {
			Entity human = humanIt.next();
			int dist = MathUtil.distance(playerX, playerY, human.getX(), human.getY());
			if (dist < COLLISION_DISTANCE) {
				humanIt.remove();
				player.incrementScore(MOMMY_SCORE_VALUE);
				gameModel.fireGameEvent(GameEvent.HUMAN_RESCUED);
			}
		}
	}

	private void checkHumanDied() {
		List<Hulk> hulks = gameModel.getHulks();
		ListIterator<Hulk> hulkIt = hulks.listIterator();

		while (hulkIt.hasNext()) {
			Hulk hulk = hulkIt.next();
			int hulkX = hulk.getX();
			int hulkY = hulk.getY();

			List<Entity> humans = gameModel.getHumans();
			ListIterator<Entity> humanIt = humans.listIterator();

			while (humanIt.hasNext()) {
				Entity human = humanIt.next();

				int dist = MathUtil.distance(hulkX, hulkY, human.getX(), human.getY());
				if (dist < COLLISION_DISTANCE) {
					humanIt.remove();
					gameModel.fireGameEvent(GameEvent.HUMAN_DIED);
				}
			}
		}
	}

	/**
	 * @return boolean indicating whether to keep running checks
	 */
	private void checkPlayerShotEnemy() {
		List<PlayerMissile> playerMissiles = gameModel.getPlayerMissiles();

		// TODO Use a spatial data structure (like k-d tree) to avoid linear search.
		ListIterator<PlayerMissile> playerMissileIt = playerMissiles.listIterator();

		processMissiles:
		while (playerMissileIt.hasNext()) {
			PlayerMissile playerMissile = playerMissileIt.next();
			int missileX = playerMissile.getX();
			int missileY = playerMissile.getY();

			List<Grunt> grunts = gameModel.getGrunts();
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
				}
			}

			List<Electrode> electrodes = gameModel.getElectrodes();
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
			}
		}

		return true;
	}

	/**
	 * @return boolean indicating whether to keep running checks
	 */
	private void checkPlayerDied() {
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
				}
			}
		}

		List<Electrode> electrodes = gameModel.getElectrodes();
		for (Electrode electrode : electrodes) {
			int dist = MathUtil.distance(playerX, playerY, electrode.getX(), electrode.getY());
			if (dist < 20) {
				player.setAlive(false);
			}
		}

		List<Hulk> hulks = gameModel.getHulks();
		for (Hulk hulk : hulks) {
			int dist = MathUtil.distance(playerX, playerY, hulk.getX(), hulk.getY());
			if (dist < 20) {
				player.setAlive(false);
			}
		}
	}
}
