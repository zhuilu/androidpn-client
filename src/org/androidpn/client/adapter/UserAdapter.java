package org.androidpn.client.adapter;

import java.util.List;

import org.androidpn.client.bean.User;
import org.androidpn.demoapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by dai.sl on 15/3/30.
 */
public class UserAdapter extends BaseAdapter {

	private List<User> users;
	private LayoutInflater inflater;
	private Context mContext;
	private Handler mHandler;
	static float screenWidth = 0;

	public UserAdapter(List<User> users, Context context, Handler handler) {
		this.users = users;
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		mHandler = handler;
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		UserViewHolder holderFoler = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.show_item, null);
			holderFoler = new UserViewHolder(convertView);
			convertView.setTag(holderFoler);
		} else {
			holderFoler = (UserViewHolder) convertView.getTag();
		}
		final User user = users.get(position);
		holderFoler.photo.setText(user.getPhoto());
		return convertView;
	}

	public static class UserViewHolder {
		TextView photo;

		public UserViewHolder(View convertView) {
			photo = (TextView) convertView.findViewById(R.id.photo);

		}
	}

}
