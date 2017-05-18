package com.williewheeler.t2.model.entity;

/**
 * Created by willie on 5/14/17.
 */
public class PlayerMissile implements Entity {
	private int x;
	private int y;
	private int deltaX;
	private int deltaY;

	public PlayerMissile(int x, int y, int deltaX, int deltaY) {
		this.x = x;
		this.y = y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void update() {
		this.x += deltaX;
		this.y += deltaY;
	}
}
