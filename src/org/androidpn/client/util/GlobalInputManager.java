package org.androidpn.client.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class GlobalInputManager {
	private static GlobalInputManager magager;
	private static InputMethodManager imm;

	/**
	 * @return the imm
	 */
	public static InputMethodManager getImm() {
		return imm;
	}

	/**
	 * @param imm
	 *            the imm to set
	 */
	public static void setImm(InputMethodManager imm) {
		GlobalInputManager.imm = imm;
	}

	private GlobalInputManager() {
		imm = (InputMethodManager) (SysApplicationImpl.getInstance()
				.getSystemService(Context.INPUT_METHOD_SERVICE));
	}

	public static GlobalInputManager getInstance() {
		if (magager != null) {
			return magager;
		} else {
			magager = new GlobalInputManager();
			return magager;
		}
	}

	public void hide(View view) {
		boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
		if (isOpen) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
		}
	}

	public void show(View view) {
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
