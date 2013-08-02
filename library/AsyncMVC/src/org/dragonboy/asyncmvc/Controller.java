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

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Asynchronous Controller
 * 
 * @author dragonboyorg
 */
@SuppressLint("HandlerLeak")
public class Controller {
	static final String TAG = "Controller";

	private final HandlerThread mInboxHandlerThread;
	private final Handler mInboxHandler;

	private final CopyOnWriteArraySet<Handler> mOutboxHandlers = new CopyOnWriteArraySet<Handler>();

	protected Model mModel;

	private Controller() {
		mInboxHandlerThread = new HandlerThread("Controller Inbox: "
				+ this.getClass().getSimpleName());
		mInboxHandlerThread.start();

		mInboxHandler = new Handler(mInboxHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				Controller.this.handleMessage(msg);
			}
		};
	}

	public Controller(Model model) {
		this();
		mModel = model;
	}

	public void setModel(Model model) {
		mModel = model;
	}

	public Model getModel() {
		return mModel;
	}

	/**
	 * Implement this method.
	 * 
	 * @param msg
	 */
	protected void handleMessage(Message msg) {
	}

	public void registerObserver(Observer observer) {
		if (mModel != null) {
			mModel.registerObserver(observer);
		}
	}

	public void unregisterObserver(Observer observer) {
		if (mModel != null) {
			mModel.unregisterObserver(observer);
		}
	}

	public void unregisterObservers() {
		if (mModel != null) {
			mModel.unregisterAll();
		}
	}

	public void registerHandler(Handler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("The handler is null.");
		}
		mOutboxHandlers.add(handler);
	}

	public void unregisterHandler(Handler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("The handler is null.");
		}
		mOutboxHandlers.remove(handler);
	}

	public void unregisterHandlers() {
		mOutboxHandlers.clear();
	}

	public final void dispose() {
		mInboxHandlerThread.getLooper().quit();
	}

	public final Handler getInboxHandler() {
		return mInboxHandler;
	}

	public final void notifyOutboxHandlers(int what, int arg1, int arg2,
			Object obj) {
		for (Handler handler : mOutboxHandlers) {
			Message msg = Message.obtain(handler, what, arg1, arg2, obj);
			msg.sendToTarget();
		}
	}
}
