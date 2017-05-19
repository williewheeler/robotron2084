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
	}

	@Override
	public void update() {
		incrX(deltaX);
		incrY(deltaY);
	}
}
