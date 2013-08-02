package org.dragonboy.asyncmvcexample;

import org.dragonboy.asyncmvc.Model;
import org.dragonboy.asyncmvc.Observer;
import org.dragonboy.asyncmvcexample.controller.MyController;
import org.dragonboy.asyncmvcexample.model.MyModel;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	MyController mController;
	MyModel mModel;
	MyObserver mObserver;

	TextView mStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mStatusView = (TextView) findViewById(R.id.hello_world);

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mController.getInboxHandler().sendEmptyMessage(
								MyController.ACTION_RESET);
					}
				});
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mController.getInboxHandler().sendEmptyMessage(
								MyController.ACTION_SET_RANDOM);
					}
				});
		findViewById(R.id.button3).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mController.getInboxHandler().sendEmptyMessage(
								MyController.ACTION_ADD_ONE);
					}
				});

		mModel = new MyModel();
		mController = new MyController(mModel);
		mObserver = new MyObserver(new Handler(getMainLooper()));
		mModel.registerObserver(mObserver);

		mStatusView.setText(String.valueOf(mModel.getStatus()));
	}

	class MyObserver extends Observer {

		public MyObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChanged(Model model, Object data) {
			MyModel myModel = (MyModel) model;
			mStatusView.setText(String.valueOf(myModel.getStatus()));
		}

	}

}
