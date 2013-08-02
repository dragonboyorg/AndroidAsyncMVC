package org.dragonboy.asyncmvcexample.controller;

import java.util.Random;

import org.dragonboy.asyncmvc.Controller;
import org.dragonboy.asyncmvcexample.model.MyModel;

import android.os.Message;

/**
 * @author dragonboy 2013-8-2
 */
public class MyController extends Controller {

	public MyController(MyModel model) {
		super(model);
	}

	public static final int ACTION_RESET = 0;
	public static final int ACTION_SET_RANDOM = 1;
	public static final int ACTION_ADD_ONE = 2;

	public static final Random RANDOM = new Random();

	@Override
	protected void handleMessage(Message msg) {
		MyModel model = getMyModel();
		switch (msg.what) {
		case ACTION_RESET:
			if (model != null) {
				model.setStatus(0);
			}
			break;
		case ACTION_SET_RANDOM:
			if (model != null) {
				model.setStatus(RANDOM.nextInt(10000));
			}
			break;
		case ACTION_ADD_ONE:
			if (model != null) {
				model.add();
			}
			break;

		default:
			break;
		}

	}

	private MyModel getMyModel() {
		MyModel model = null;
		if (mModel != null && mModel instanceof MyModel) {
			model = (MyModel) mModel;
		}
		return model;
	}

}
