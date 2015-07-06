package org.androidpn.client.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String photo;
	private String password;
	private boolean flag;

	public User(JSONObject temp) {
		// TODO Auto-generated constructor stub
		if (temp != null) {
			try {
				if (temp.has("username"))
					photo = temp.getString("username");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
