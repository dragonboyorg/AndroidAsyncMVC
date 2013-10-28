package org.dragonboy.asyncmvcexample.model;

import org.dragonboy.asyncmvc.Model;

/**
 * @author dragonboy 2013-8-2
 */
public class MyModel extends Model {
	public static final int WHAT_STATUS1_CHANGE = 1;

	private int mStatus1;

	public void setStatus(int status) {
		mStatus1 = status;
		notifyChanged(WHAT_STATUS1_CHANGE);
	}

	public int getStatus() {
		return mStatus1;
	}

	public synchronized void add() {
		mStatus1++;
		notifyChanged(WHAT_STATUS1_CHANGE);
	}

}
