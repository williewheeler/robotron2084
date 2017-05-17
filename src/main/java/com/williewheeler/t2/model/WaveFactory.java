package com.williewheeler.t2.model;

/**
 * Created by willie on 5/14/17.
 */
public class WaveFactory {
	private static Wave[] waves = new Wave[] {
			new Wave(15, 5, 1, 1, 0, 0, 0, 0, 0),     // 1
			new Wave(17, 15, 1, 1, 1, 5, 0, 1, 0),    // 2
			new Wave(22, 25, 2, 2, 2, 6, 0, 3, 0),    // 3
			new Wave(34, 25, 2, 2, 2, 7, 0, 4, 0),    // 4
			new Wave(20, 20, 15, 0, 1, 0, 15, 1, 0),  // 5
			new Wave(32, 25, 3, 3, 3, 7, 0, 4, 0)     // 6
	};

	public static Wave getWave(int number) {
		return waves[number - 1];
	}
}
