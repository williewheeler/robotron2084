package com.williewheeler.t2.model;

import java.util.EventListener;

/**
 * Created by willie on 5/14/17.
 */
public interface GameListener extends EventListener {

	void handleEvent(GameEvent event);
}
