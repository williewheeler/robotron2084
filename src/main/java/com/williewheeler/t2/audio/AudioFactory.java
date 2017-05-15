package com.williewheeler.t2.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by willie on 5/14/17.
 */
public class AudioFactory {
	private AudioLoader audioLoader;

	// TODO Consolidate these
	private final Map<String, ArrayDeque<Clip>> soundEffects = new HashMap<>();
	private final Map<String, Float> gainControlValues = new HashMap<>();

	public AudioFactory() {
		this.audioLoader = new AudioLoader();

		// Use the # of available processors since this is the most we can run at once anyway.
		int clipsPerId = Runtime.getRuntime().availableProcessors();
		initSoundEffects(clipsPerId);
	}

	public void playSoundEffect(String id) {
		synchronized (soundEffects) {
			ArrayDeque<Clip> buffer = soundEffects.get(id);
			Clip clip = buffer.poll();
			clip.setFramePosition(0);

			Float gainControlValue = gainControlValues.get(id);
			if (gainControlValue != null) {
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(gainControlValue);
			}

			clip.start();
			buffer.add(clip);
		}
	}

	// http://stackoverflow.com/questions/27208568/how-to-terminate-anonymous-threads-in-java
	// http://stackoverflow.com/questions/7266042/java-ring-buffer
	private void initSoundEffects(int clipsPerId) {
		// TODO Lazy load
		String[] ids = {
				"explode", "firstlevel", "newlevel", "playerdead", "shot", "walk"
		};

		// TODO Make this more generic, such as automatically loading all wavs in some directory or manifest. [WLW]
		for (String id : ids) {
			ArrayDeque<Clip> buffer = new ArrayDeque<Clip>();
			for (int i = 0; i < clipsPerId; i++) {
				buffer.add(audioLoader.loadSoundEffect(id));
			}
			soundEffects.put(id, buffer);
		}

		gainControlValues.put("walk", -10.0f);
	}
}
