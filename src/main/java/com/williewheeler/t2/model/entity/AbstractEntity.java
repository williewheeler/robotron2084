package com.williewheeler.t2.model.entity;

/**
 * Created by willie on 5/17/17.
 */
public abstract class AbstractEntity implements Entity {
	private int x;
	private int y;

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
}
