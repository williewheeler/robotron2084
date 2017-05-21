package com.williewheeler.robotron.model.event;

import java.util.EventObject;

/**
 * Created by willie on 5/14/17.
 */
public class GameEvent extends EventObject {
	public static final int FIRST_LEVEL = 0;
	public static final int NEW_LEVEL = 1;
	public static final int EXPLODE = 2;
	public static final int WALK = 3;
	public static final int SHOT = 4;
	public static final int PLAYER_DEAD = 5;
	public static final int HUMAN_RESCUED = 6;
	public static final int HUMAN_DIED = 7;

	private int type;

	// These aren't always relevant, but for now this is fine.
	private int x;
	private int y;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GameEvent(Object source, int type) {
		super(source);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
