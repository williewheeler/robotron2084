package com.williewheeler.t2;

/**
 * Created by willie on 5/12/17.
 */
public class T2Config {
	public static final int FRAMES_PER_SECOND = 30;
	public static final int FRAME_PERIOD = 1000 / FRAMES_PER_SECOND;

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static final int MIN_X = 10;
	public static final int MAX_X = WINDOW_WIDTH - 11;
	public static final int MIN_Y = 10;
	public static final int MAX_Y = WINDOW_HEIGHT - 21;

	public static final int PLAYER_START_X = WINDOW_WIDTH / 2;
	public static final int PLAYER_START_Y = WINDOW_HEIGHT / 2;

	public static final int PLAYER_MOVE_DISTANCE = 8;
	public static final int PLAYER_FIRE_DISTANCE = 5;
}
