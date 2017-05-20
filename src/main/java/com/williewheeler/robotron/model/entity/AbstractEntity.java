package com.williewheeler.robotron.model.entity;

import com.williewheeler.robotron.GameConfig;

/**
 * Created by willie on 5/17/17.
 */
public abstract class AbstractEntity implements Entity {
	private int x;
	private int y;
	private Direction direction = Direction.DOWN;

	@Override
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void incrX(int deltaX) {
		this.x += deltaX;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void incrY(int deltaY) {
		this.y += deltaY;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	protected void move(int deltaX, int deltaY) {
		updatePosition(deltaX, deltaY);
		updateDirection(deltaX, deltaY);
	}

	private void updatePosition(int deltaX, int deltaY) {
		incrX(deltaX);
		incrY(deltaY);

		if (x < 0) {
			setX(0);
		}
		if (x > GameConfig.ARENA_WIDTH) {
			setX(GameConfig.ARENA_WIDTH);
		}
		if (y < 0) {
			setY(0);
		}
		if (y > GameConfig.ARENA_HEIGHT) {
			setY(GameConfig.ARENA_HEIGHT);
		}
	}

	private void updateDirection(int deltaX, int deltaY) {
		boolean moveUp = deltaY < 0;
		boolean moveDown = deltaY > 0;
		boolean moveLeft = deltaX < 0;
		boolean moveRight = deltaX > 0;

		if (moveUp) {
			if (moveLeft) {
				this.direction = Direction.UP_LEFT;
			} else if (moveRight) {
				this.direction = Direction.UP_RIGHT;
			} else {
				this.direction = Direction.UP;
			}
		} else if (moveDown) {
			if (moveLeft) {
				this.direction = Direction.DOWN_LEFT;
			} else if (moveRight) {
				this.direction = Direction.DOWN_RIGHT;
			} else {
				this.direction = Direction.DOWN;
			}
		} else if (moveLeft) {
			this.direction = Direction.LEFT;
		} else if (moveRight) {
			this.direction = Direction.RIGHT;
		}
	}
}
