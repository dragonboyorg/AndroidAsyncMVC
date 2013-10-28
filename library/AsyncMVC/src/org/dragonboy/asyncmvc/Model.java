package org.dragonboy.asyncmvc;

import java.util.ArrayList;
import java.util.List;

import org.dragonboy.util.ArrayUtils;

import android.database.Observable;

public class Model extends Observable<Observer> {
	static final String TAG = "Model";

	@Override
	public void registerObserver(Observer observer) {
		super.registerObserver(observer);
	}

	@Override
	public void unregisterObserver(Observer observer) {
		super.unregisterObserver(observer);
	}

	@Override
	public void unregisterAll() {
		super.unregisterAll();
	}

	/**
	 * @deprecated
	 */
	public void notifyChanged() {
		notifyChanged(0, null);
	}

	public void notifyChanged(int what) {
		notifyChanged(what, null);
	}

	/**
	 * @deprecated
	 * @param data
	 */
	public void notifyChanged(Object data) {
		notifyChanged(0, data);
	}

	public void notifyChanged(int what, Object data) {
		List<Observer> snapshot = null;
		synchronized (mObservers) {
			snapshot = new ArrayList<Observer>(mObservers);
		}
		for (Observer observer : snapshot) {
			if (observer != null) {
				int[] whats = observer.getWhats();
				if ((whats != null) && (whats.length > 0)
						&& ArrayUtils.contains(whats, what)) {
					observer.notifyChanged(what, this, data);
				}
			}
		}
		snapshot.clear();
	}
}
