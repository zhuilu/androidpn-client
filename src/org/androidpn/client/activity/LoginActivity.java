/**
 * Date:2015年5月21日上午10:23:58
 * Copyright (c) 2015, daishulin@163.com All Rights Reserved.
 *
 */
package org.androidpn.client.activity;

import org.androidpn.Request.YBRDataRequestHandler;
import org.androidpn.client.Constants;
import org.androidpn.client.util.GlobalInputManager;
import org.androidpn.client.util.HttpRequest;
import org.androidpn.client.util.Toaster;
import org.androidpn.client.util.Util;
import org.androidpn.demoapp.R;
import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * 登录界面
 * 
 * @author dai.sl
 *
 */
public class LoginActivity extends BaseActivity {
	EditText photo;
	EditText password;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constants.Login_flag:
				Intent intent = new Intent(LoginActivity.this,
						UserListActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		};

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		photo = (EditText) findViewById(R.id.photo);
		password = (EditText) findViewById(R.id.password);

		// login
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	 * .Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.beidu.ybren.activity.BaseActivity#myClickHandler(android.view.View)
	 */
	@Override
	public void myClickHandler(View view) {
		GlobalInputManager.getInstance().hide(view);
		switch (view.getId()) {

		case R.id.login_commit:
			if (photo.getText().toString().trim().length() < 1) {
				Toaster.getInstance().displayToast(R.string.photo_empty);
				return;
			}
			if (!Util.checkMobileNO(photo.getText().toString().trim())) {
				Toaster.getInstance().displayToast(R.string.photo_fail);
				return;
			}
			if (password.getText().toString().trim().length() < 1) {
				Toaster.getInstance().displayToast(R.string.password_empty);
				return;
			}
			if (password.getText().toString().trim().length() < 6
					|| password.getText().toString().trim().length() > 12) {
				Toaster.getInstance().displayToast(R.string.password_hint);
				return;
			}

			HttpRequest.getInstance().loginTo(handler,
					photo.getText().toString(), password.getText().toString(),
					new YBRDataRequestHandler<JSONObject>() {

						@Override
						public void OnSuccess(int statusCode, Header[] headers,
								JSONObject response) {

						}

						@Override
						public void OnFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {

						}
					});

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

}
