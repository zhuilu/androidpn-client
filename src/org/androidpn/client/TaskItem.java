package org.androidpn.client;

import org.androidpn.Request.YBRDataRequestHandler;

import android.os.Handler;

public abstract class TaskItem implements Runnable {
	public Handler mHandler;
	public YBRDataRequestHandler mlListener;

	public TaskItem() {
	}

	public TaskItem(Handler handler) {
		mHandler = handler;
	}

	public <T> TaskItem(YBRDataRequestHandler<T> listener) {
		mlListener = listener;
	}

	public <T> TaskItem(Handler handler, YBRDataRequestHandler<T> listener) {
		mlListener = listener;
		mHandler = handler;
	}
}
