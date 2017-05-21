package com.williewheeler.robotron.util;

import java.util.Random;

/**
 * Created by willie on 5/14/17.
 */
public class MathUtil {
	private static final Random random = new Random();

	public static int distance(int x1, int y1, int x2, int y2) {
		int xDiff = x2 - x1;
		int yDiff = y2 - y1;
		return (int) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	public static int randomInt(int n) {
		return random.nextInt(n);
	}
}
