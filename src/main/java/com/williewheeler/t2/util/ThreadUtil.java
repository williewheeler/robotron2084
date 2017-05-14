package com.williewheeler.t2.util;

/**
 * Created by willie on 5/13/17.
 */
public class ThreadUtil {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
}
