package com.williewheeler.t2.model;

/**
 * Created by willie on 5/16/17.
 */
public class Wave {
	private int electrodes;
	private int grunts;
	private int hulks;
	private int brains;
	private int spheroids;
	private int quarks;
	private int mommies;
	private int daddies;
	private int mikeys;

	public Wave(
			int grunts,
			int electrodes,
			int mommies,
			int daddies,
			int mikeys,
			int hulks,
			int brains,
			int spheroids,
			int quarks) {

		this.grunts = grunts;
		this.electrodes = electrodes;
		this.mommies = mommies;
		this.daddies = daddies;
		this.mikeys = mikeys;
		this.hulks = hulks;
		this.brains = brains;
		this.spheroids = spheroids;
		this.quarks = quarks;
	}

	public int getGrunts() {
		return grunts;
	}

	public int getElectrodes() {
		return electrodes;
	}

	public int getMommies() {
		return mommies;
	}

	public int getDaddies() {
		return daddies;
	}

	public int getMikeys() {
		return mikeys;
	}

	public int getHulks() {
		return hulks;
	}

	public int getBrains() {
		return brains;
	}

	public int getSpheroids() {
		return spheroids;
	}

	public int getQuarks() {
		return quarks;
	}
}
