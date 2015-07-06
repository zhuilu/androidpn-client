package org.androidpn.client.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.androidpn.Request.YBRDataRequestHandler;
import org.androidpn.client.Constants;
import org.androidpn.client.adapter.UserAdapter;
import org.androidpn.client.bean.User;
import org.androidpn.client.util.HttpRequest;
import org.androidpn.demoapp.R;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class UserListActivity extends Activity {
	private ListView listview;
	private UserAdapter adapter;
	private User currentUser;
	private List<User> mData = new ArrayList<User>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mData.clear();
				mData.addAll((Collection<? extends User>) msg.obj);
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("DemoAppActivity", "onCreate()...");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		currentUser = (User) getIntent().getSerializableExtra(
				Constants.flag_user);
		// Settings
		Button okButton = (Button) findViewById(R.id.btn_settings);
		listview = (ListView) findViewById(R.id.listview);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

			}
		});

		adapter = new UserAdapter(mData, this, handler);
		listview.setAdapter(adapter);
		requestAllByUser();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				userItemClick(mData.get(position));
			}
		});
	}

	private void requestAllByUser() {
		HttpRequest.RequestToPostMessage(currentUser,
				new YBRDataRequestHandler<JSONObject>() {

					@Override
					public void OnSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						System.out.println("OnSuccess");
						System.out.println(response);
						List<User> data = new ArrayList<User>();
						try {
							JSONArray array = response.getJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								JSONObject temp = array.getJSONObject(i);
								User user = new User(temp);
								data.add(user);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						handler.obtainMessage(1, data).sendToTarget();
					}

					@Override
					public void OnFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						System.out.println("OnFailure");
					}
				});
	}

	private void userItemClick(User user) {
		try {
			HttpRequest.RequestPostMessageTo(user.getPhoto(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}