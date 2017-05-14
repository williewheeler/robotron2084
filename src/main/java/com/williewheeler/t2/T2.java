package com.williewheeler.t2;

import com.williewheeler.t2.model.GameState;
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
	private KeyEventDispatcher keyEventDispatcher;
	private TickHandler tickHandler;
	private Timer timer;

	public static void main(String[] args) {
		new T2().start();
	}

	public T2() {
		super("T2");
		registerFont();
		this.gameState = new GameState();
		this.gamePane = new GamePane(gameState);
		this.keyEventDispatcher = new KeyEventDispatcherImpl(gameState);
		this.tickHandler = new TickHandler();
		this.timer = new Timer(FRAME_PERIOD, tickHandler);
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

	private class TickHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			log.trace("focusOwner={}", getFocusOwner());
			gameState.update();

			// TODO For tighter control, consider using active rendering here.
			// See Killer Game Programming In Java, p. 21.
			gamePane.repaint();
		}
	}
}
