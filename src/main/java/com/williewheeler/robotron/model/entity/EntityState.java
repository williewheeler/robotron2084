package com.williewheeler.robotron.model.entity;

/**
 * Created by willie on 5/15/17.
 */
public enum EntityState {
	BORN,
	ALIVE,
	DEAD,
	RESCUED,

	/**
	 * Indicates that the entity is gone and ready for garbage collection.
	 */
	GONE
}
