package com.williewheeler.robotron.model.entity;

/**
 * Created by willie on 5/14/17.
 */
public class PlayerMissile extends AbstractEntity {
	private int deltaX;
	private int deltaY;

	public PlayerMissile(int x, int y, int deltaX, int deltaY) {
		setX(x);
		setY(y);
		this.deltaX = deltaX;
		this.deltaY = deltaY;

		// TODO Clean up all this if/else logic. Better to use bit ops.
		Direction dir = null;
		if (deltaX > 0) {
			if (deltaY > 0) {
				dir = Direction.DOWN_RIGHT;
			} else if (deltaY < 0) {
				dir = Direction.UP_RIGHT;
			} else {
				dir = Direction.RIGHT;
			}
		} else if (deltaX < 0) {
			if (deltaY > 0) {
				dir = Direction.DOWN_LEFT;
			} else if (deltaY < 0) {
				dir = Direction.UP_LEFT;
			} else {
				dir = Direction.LEFT;
			}
		} else {
			if (deltaY > 0) {
				dir = Direction.DOWN;
			} else if (deltaY < 0) {
				dir = Direction.UP;
			} else {
				dir = null;
			}
		}
		setDirection(dir);
	}

	@Override
	public void update() {
		incrX(deltaX);
		incrY(deltaY);
	}
}
