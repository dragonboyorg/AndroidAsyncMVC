/*
 * Copyright (C) 2013 dragonboyorg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dragonboy.asyncmvc;

import java.util.ArrayList;
import java.util.List;

import android.database.Observable;

/**
 * Asynchronous Model
 * 
 * @author dragonboyorg
 */
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

	public void notifyChanged() {
		notifyChanged(null);
	}

	public void notifyChanged(Object data) {
		List<Observer> snapshot = null;
		synchronized (mObservers) {
			snapshot = new ArrayList<Observer>(mObservers);
		}
		for (Observer observer : snapshot) {
			if (observer != null) {
				observer.notifyChanged(this, data);
			}
		}
	}
}
