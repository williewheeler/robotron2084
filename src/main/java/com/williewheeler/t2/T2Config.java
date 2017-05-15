package com.williewheeler.t2;

/**
 * Created by willie on 5/12/17.
 */
public class T2Config {
	public static final int FRAMES_PER_SECOND = 30;
	public static final int FRAME_PERIOD = 1000 / FRAMES_PER_SECOND;

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	public static final int HEADER_HEIGHT = 32;
	public static final int ARENA_WIDTH = WINDOW_WIDTH - 60;
	public static final int ARENA_HEIGHT = WINDOW_HEIGHT - (HEADER_HEIGHT + 70);

	public static final int PLAYER_START_X = ARENA_WIDTH / 2;
	public static final int PLAYER_START_Y = ARENA_HEIGHT / 2;

	public static final int PLAYER_MOVE_DISTANCE = 8;
	public static final int PLAYER_FIRE_DISTANCE = 20;
	public static final int PLAYER_RECHARGE_PERIOD = 3;
	public static final int PLAYER_WALK_PERIOD = 4;

	public static final int GRUNT_MOVE_DISTANCE = 8;
	public static final int GRUNT_MOVE_MAX_PERIOD = 10;

	// https://strategywiki.org/wiki/Robotron:_2084/How_to_play
	public static final int GRUNT_SCORE_VALUE = 100;

	public static final int COLLISION_DISTANCE = 10;
}
