package com.williewheeler.robotron.model.entity;

// See http://www.robotron2084guidebook.com/technical/sourcecode/scotttunstall/enemy-design-psychology/
// for individual entity behavior.

/**
 * Created by willie on 5/17/17.
 */
public interface Entity {

	EntityState getState();

	int getX();

	int getY();

	Direction getDirection();

	void update();
}
