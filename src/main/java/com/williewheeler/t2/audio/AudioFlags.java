package com.williewheeler.t2.audio;

/**
 * Created by willie on 5/14/17.
 */
public class AudioFlags {
	public static final int FIRST_LEVEL = 1 << 0;
	public static final int NEW_LEVEL = 1 << 1;
	public static final int EXPLODE = 1 << 2;
	public static final int WALK = 1 << 3;
	public static final int SHOT = 1 << 4;
	public static final int PLAYER_DEAD = 1 << 5;

	private int flags = 0;

	public boolean isFlagSet(int flag) {
		return (flags & flag) > 0;
	}

	public void setFlag(int flag) {
		flags |= flag;
	}

	public void clearFlags() {
		this.flags = 0;
	}
}
