package com.williewheeler.robotron.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by willie on 5/14/17.
 */
public class AudioLoader {

	public Clip loadSoundEffect(String id) {
		String filename = "audio/" + id + ".wav";
		InputStream is = ClassLoader.getSystemResourceAsStream(filename);
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(is);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			return clip;
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			throw new RuntimeException(e);
		}
	}
}
