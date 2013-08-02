package org.dragonboy.asyncmvcexample.model;

import org.dragonboy.asyncmvc.Model;

/**
 * @author dragonboy 2013-8-2
 */
public class MyModel extends Model {

	private int mStatus1;

	public void setStatus(int status) {
		mStatus1 = status;
		notifyChanged();
	}

	public int getStatus() {
		return mStatus1;
	}

	public synchronized void add() {
		mStatus1++;
		notifyChanged();
	}

}
