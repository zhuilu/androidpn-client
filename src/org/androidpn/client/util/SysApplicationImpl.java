/**
 * Date:2015年6月26日下午2:36:35
 * Copyright (c) 2015, daishulin@163.com All Rights Reserved.
 *
 */
package org.androidpn.client.util;

import java.util.Properties;

import org.androidpn.client.ServiceManager;
import org.androidpn.client.XmppManager;
import org.androidpn.demoapp.R;

import android.app.Application;
import android.util.Log;

/**
 * @author dai.sl
 *
 */
public class SysApplicationImpl extends Application {
	private XmppManager xmppManager;

	/**
	 * @return the xmppManager
	 */
	public XmppManager getXmppManager() {
		return xmppManager;
	}

	/**
	 * @param xmppManager
	 *            the xmppManager to set
	 */
	public void setXmppManager(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
	}

	private static SysApplicationImpl app;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		Toaster.init(getApplicationContext());
		// Start the service
		ServiceManager serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.notification);
		serviceManager.startService();
	}

	public static SysApplicationImpl getInstance() {
		return app;
	}

	public Properties loadProperties() {
		// InputStream in = null;
		// Properties props = null;
		// try {
		// in = getClass().getResourceAsStream(
		// "/org/androidpn/client/client.properties");
		// if (in != null) {
		// props = new Properties();
		// props.load(in);
		// } else {
		// Log.e(LOGTAG, "Could not find the properties file.");
		// }
		// } catch (IOException e) {
		// Log.e(LOGTAG, "Could not find the properties file.", e);
		// } finally {
		// if (in != null)
		// try {
		// in.close();
		// } catch (Throwable ignore) {
		// }
		// }
		// return props;

		Properties props = new Properties();
		try {
			int id = getApplicationContext().getResources().getIdentifier(
					"androidpn", "raw",
					getApplicationContext().getPackageName());
			props.load(getApplicationContext().getResources().openRawResource(
					id));
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("", "Could not find the properties file.", e);
			// e.printStackTrace();
		}
		return props;
	}
}
