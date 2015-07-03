package org.androidpn.client.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 基类
 * 
 * @author dai.sl
 *
 */
public abstract class BaseActivity extends FragmentActivity {
	protected PopupWindow popupWindow;
	public String fargmentTag = "";

	/**
	 * @return the fargmentTag
	 */
	public String getFargmentTag() {
		return fargmentTag;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * @param fargmentTag
	 *            the fargmentTag to set
	 */
	public void setFargmentTag(String fargmentTag) {
		this.fargmentTag = fargmentTag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (getActionBar() != null) {
		// getActionBar().setBackgroundDrawable(
		// getResources()
		// .getDrawable(R.drawable.action_bar_background));
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// }

	}

	public abstract void myClickHandler(View view);

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @param view
	 */

}
