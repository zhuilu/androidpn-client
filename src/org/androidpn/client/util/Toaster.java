package org.androidpn.client.util;

import java.text.MessageFormat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Toaster单例实现
 * 
 * @author dai.sl
 * 
 */
public class Toaster {
	private static Toaster sInstance;

	private final Context mContext;
	private final Handler hToast;

	public static void init(Context context) {
		if (sInstance == null)
			sInstance = new Toaster(context);
		else
			throw new IllegalStateException("Toaster has been inited.");
	}

	public static Toaster getInstance() {
		if (sInstance == null)
			throw new IllegalStateException("Toaster has not been inited.");
		else
			return sInstance;
	}

	private Toaster(Context context) {
		this.mContext = context;
		this.hToast = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_LONG)
						.show();

			}
		};
	}

	public void displayToast(String str) {
		Message msg = Message.obtain(hToast, 0, str);
		msg.sendToTarget();
	}

	public void displayToast(int res) {
		displayToast(mContext.getString(res));
	}

	public void displayToast(int formatRes, Object[] params) {
		displayToast(MessageFormat
				.format(mContext.getString(formatRes), params));
	}
}
