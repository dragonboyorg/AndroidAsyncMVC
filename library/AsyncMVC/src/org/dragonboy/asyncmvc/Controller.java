package org.dragonboy.asyncmvc;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Controller {
	static final String TAG = "Controller";

	private final HandlerThread mInboxHandlerThread;
	private final Handler mInboxHandler;

	private final ArrayList<Handler> mOutboxHandlers = new ArrayList<Handler>();

	protected Model mModel;

	private Controller() {
		mInboxHandlerThread = new HandlerThread("Controller Inbox: "
				+ this.getClass().getSimpleName());
		mInboxHandlerThread.start();

		mInboxHandler = new MyHandler(mInboxHandlerThread.getLooper(), this);
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
		mInboxHandler.removeCallbacksAndMessages(null);
		mInboxHandlerThread.getLooper().quit();
		mOutboxHandlers.clear();
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

	static class MyHandler extends Handler {
		// WeakReference to the outer class's instance.
		private WeakReference<Controller> mOuter;

		public MyHandler(Looper looper, Controller controller) {
			super(looper);
			mOuter = new WeakReference<Controller>(controller);
		}

		@Override
		public void handleMessage(Message msg) {
			Controller controller = mOuter.get();
			if (controller != null) {
				controller.handleMessage(msg);
			}
		}
	}
}
