package com.williewheeler.t2.model;

/**
 * Created by willie on 5/14/17.
 */
public class PlayerMissile {
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public void update() {
		this.x += deltaX;
		this.y += deltaY;
	}
}
