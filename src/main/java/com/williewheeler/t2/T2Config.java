package com.williewheeler.t2;

/**
 * Created by willie on 5/12/17.
 */
public class T2Config {
	public static final int FRAMES_PER_SECOND = 30;
	public static final int FRAME_PERIOD = 1000 / FRAMES_PER_SECOND;

	public static final int WINDOW_WIDTH = 1200;
	public static final int WINDOW_HEIGHT = 800;

	public static final int MIN_X = 10;
	public static final int MAX_X = WINDOW_WIDTH - 21;
	public static final int MIN_Y = 10;
	public static final int MAX_Y = WINDOW_HEIGHT - 41;

	public static final int PLAYER_START_X = WINDOW_WIDTH / 2;
	public static final int PLAYER_START_Y = WINDOW_HEIGHT / 2;

	public static final int PLAYER_MOVE_DISTANCE = 8;
	public static final int PLAYER_FIRE_DISTANCE = 5;
	public static final int PLAYER_RECHARGE_PERIOD = 4;

	public static final int GRUNT_MOVE_DISTANCE = 8;
	public static final int GRUNT_MOVE_MAX_PERIOD = 10;

	// https://strategywiki.org/wiki/Robotron:_2084/How_to_play
	public static final int GRUNT_SCORE_VALUE = 100;
}
