package com.williewheeler.t2;

import com.williewheeler.t2.audio.AudioFactory;
import com.williewheeler.t2.audio.AudioFlags;
import com.williewheeler.t2.input.KeyEventDispatcherImpl;
import com.williewheeler.t2.model.GameEvent;
import com.williewheeler.t2.model.GameListener;
import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.view.GamePane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import static com.williewheeler.t2.T2Config.*;

/**
 * Created by willie on 5/12/17.
 */
public class T2 extends JFrame {
	private static final Logger log = LoggerFactory.getLogger(T2.class);

	private GameState gameState;
	private GamePane gamePane;
	private AudioFactory audioFactory;
	private AudioFlags audioFlags;
	private KeyEventDispatcher keyEventDispatcher;
	private TickHandler tickHandler;
	private GameHandler gameHandler;
	private Timer timer;

	public static void main(String[] args) {
		new T2().start();
	}

	public T2() {
		super("T2");
		registerFont();
		this.gameState = new GameState();
		this.gamePane = new GamePane(gameState);
		this.audioFactory = new AudioFactory();
		this.audioFlags = new AudioFlags();
		this.keyEventDispatcher = new KeyEventDispatcherImpl(gameState);
		this.tickHandler = new TickHandler();
		this.gameHandler = new GameHandler();
		this.timer = new Timer(FRAME_PERIOD, tickHandler);

		gameState.addGameListener(gameHandler);
	}

	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().add(gamePane);
		setVisible(true);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
		timer.start();
		gameState.fireGameEvent(GameEvent.FIRST_LEVEL);
	}

	private void registerFont() {
		InputStream fontIS = ClassLoader.getSystemResourceAsStream("Robotron.ttf");
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontIS));
		} catch (FontFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void playAudio() {
		if (audioFlags.isFlagSet(AudioFlags.FIRST_LEVEL)) {
			audioFactory.playSoundEffect("firstlevel");
		}
		if (audioFlags.isFlagSet(AudioFlags.NEW_LEVEL)) {
			audioFactory.playSoundEffect("newlevel");
		}
		if (audioFlags.isFlagSet(AudioFlags.EXPLODE)) {
			audioFactory.playSoundEffect("explode");
		}
		if (audioFlags.isFlagSet(AudioFlags.WALK)) {
			audioFactory.playSoundEffect("walk");
		}
		if (audioFlags.isFlagSet(AudioFlags.SHOT)) {
			audioFactory.playSoundEffect("shot");
		}
		if (audioFlags.isFlagSet(AudioFlags.PLAYER_DEAD)) {
			audioFactory.playSoundEffect("playerdead");
		}

		audioFlags.clearFlags();
	}

	private class TickHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			log.trace("focusOwner={}", getFocusOwner());
			gameState.update();

			// TODO For tighter control, consider using active rendering here.
			// See Killer Game Programming In Java, p. 21.
			gamePane.repaint();
			playAudio();
		}
	}

	private class GameHandler implements GameListener {

		@Override
		public void handleEvent(GameEvent event) {
			// TODO Consider implementing this in a way that doesn't require manually mapping events to flags.
			// For example we could use the event IDs to indicate the number of left shifts in the flag. But I don't
			// want to use IDs for that. [WLW]
			int type = event.getType();
			switch (type) {
				case GameEvent.FIRST_LEVEL:
					audioFlags.setFlag(AudioFlags.FIRST_LEVEL);
					break;
				case GameEvent.NEW_LEVEL:
					gamePane.displayTransition();
					audioFlags.setFlag(AudioFlags.NEW_LEVEL);
					break;
				case GameEvent.EXPLODE:
					audioFlags.setFlag(AudioFlags.EXPLODE);
					break;
				case GameEvent.WALK:
					audioFlags.setFlag(AudioFlags.WALK);
					break;
				case GameEvent.SHOT:
					audioFlags.setFlag(AudioFlags.SHOT);
					break;
				case GameEvent.PLAYER_DEAD:
					audioFlags.setFlag(AudioFlags.PLAYER_DEAD);
					break;
			}
		}
	}
}
