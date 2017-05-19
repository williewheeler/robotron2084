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
	public static final int RESCUE = 6;

	private int type;

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
}
