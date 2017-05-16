package com.williewheeler.t2.model;

import com.williewheeler.t2.util.MathUtil;

import java.util.List;
import java.util.ListIterator;

import static com.williewheeler.t2.T2Config.COLLISION_DISTANCE;

/**
 * Created by willie on 5/15/17.
 */
public class CollisionDetector {
	private GameState gameState;

	public CollisionDetector(GameState gameState) {
		this.gameState = gameState;
	}

	public void checkCollisions() {
		checkPlayerMissileCollisions();
		checkPlayerCollision();
	}

	private void checkPlayerMissileCollisions() {
		Player player = gameState.getPlayer();
		List<Grunt> grunts = gameState.getGrunts();
		List<PlayerMissile> playerMissiles = gameState.getPlayerMissiles();

		// TODO Use a spatial data structure (like k-d tree) to avoid linear search.
		ListIterator<PlayerMissile> playerMissileIt = playerMissiles.listIterator();

		processMissiles:
		while (playerMissileIt.hasNext()) {
			PlayerMissile playerMissile = playerMissileIt.next();
			ListIterator<Grunt> gruntIt = grunts.listIterator();
			while (gruntIt.hasNext()) {
				Grunt grunt = gruntIt.next();
				EntityState gruntState = grunt.getEntityState();
				if (gruntState == EntityState.ALIVE) {
					int dist = MathUtil.distance(playerMissile.getX(), playerMissile.getY(), grunt.getX(), grunt.getY());
					if (dist < COLLISION_DISTANCE) {
						playerMissileIt.remove();
						grunt.setEntityState(EntityState.DEAD);
						continue processMissiles;
					}
				} else if (gruntState == EntityState.BURIED) {
					gruntIt.remove();
					if (grunts.isEmpty()) {
						gameState.fireGameEvent(GameEvent.NEW_LEVEL);
					}
				}
			}
		}
	}

	private void checkPlayerCollision() {
		Player player = gameState.getPlayer();
		List<Grunt> grunts = gameState.getGrunts();
		List<PlayerMissile> playerMissiles = gameState.getPlayerMissiles();

		ListIterator<Grunt> gruntIt = grunts.listIterator();
		while (gruntIt.hasNext()) {
			Grunt grunt = gruntIt.next();
			if (grunt.getEntityState() == EntityState.ALIVE) {
				int dist = MathUtil.distance(player.getX(), player.getY(), grunt.getX(), grunt.getY());
				if (dist < 20) {
					player.setAlive(false);
					gameState.fireGameEvent(GameEvent.PLAYER_DEAD);
				}
			}
		}
	}

}
