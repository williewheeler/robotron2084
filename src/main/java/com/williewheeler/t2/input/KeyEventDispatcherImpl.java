package com.williewheeler.t2.input;

import com.williewheeler.t2.model.GameState;
import com.williewheeler.t2.model.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/**
 * Created by willie on 5/13/17.
 */
public class KeyEventDispatcherImpl implements KeyEventDispatcher {
	private static final Logger log = LoggerFactory.getLogger(KeyEventDispatcherImpl.class);

	private GameState gameState;

	public KeyEventDispatcherImpl(GameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
//		log.trace("Dispatching: {}", e);
		int id = e.getID();
		int keyCode = e.getKeyCode();
		if (id == KeyEvent.KEY_PRESSED) {
			action(keyCode, true);
		} else if (id == KeyEvent.KEY_RELEASED) {
			action(keyCode, false);
		}
		return false;
	}

	private void action(int keyCode, boolean value) {
		Player player = gameState.getPlayer();

		// TODO Update this to WASD once this bug is resolved:
		// http://stackoverflow.com/questions/43192166/on-mac-in-java-keypressed-event-doesnt-fire-for-certain-keys/43960171
		switch (keyCode) {
			case KeyEvent.VK_T:
				player.setMoveUpIntent(value);
				break;
			case KeyEvent.VK_G:
				player.setMoveDownIntent(value);
				break;
			case KeyEvent.VK_F:
				player.setMoveLeftIntent(value);
				break;
			case KeyEvent.VK_H:
				player.setMoveRightIntent(value);
				break;
			case KeyEvent.VK_UP:
				player.setFireUpIntent(value);
				break;
			case KeyEvent.VK_DOWN:
				player.setFireDownIntent(value);
				break;
			case KeyEvent.VK_LEFT:
				player.setFireLeftIntent(value);
				break;
			case KeyEvent.VK_RIGHT:
				player.setFireRightIntent(value);
				break;
		}
	}
}
