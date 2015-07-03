/*
 * Copyright (C) 2010 Moduad Co., Ltd.
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
package org.androidpn.client.activity;

import org.androidpn.Request.YBRDataRequestHandler;
import org.androidpn.client.ServiceManager;
import org.androidpn.client.util.HttpRequest;
import org.androidpn.client.util.SysApplicationImpl;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.R.drawable;
import org.androidpn.demoapp.R.id;
import org.androidpn.demoapp.R.layout;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * This is an androidpn client demo application.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class DemoAppActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("DemoAppActivity", "onCreate()...");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Settings
		Button okButton = (Button) findViewById(R.id.btn_settings);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// ServiceManager.viewNotificationSettings(DemoAppActivity.this);
				HttpRequest
						.RequestToPostMessage(new YBRDataRequestHandler<Integer>() {

							@Override
							public void OnSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								System.out.println("OnSuccess");
								try {
									HttpRequest.RequestPostMessageTo(
											(String) (response
													.getJSONArray("data")
													.get(1)), null);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void OnFailure(int statusCode,
									Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								// TODO Auto-generated method stub
								System.out.println("OnFailure");
							}
						});
			}
		});

	}

}