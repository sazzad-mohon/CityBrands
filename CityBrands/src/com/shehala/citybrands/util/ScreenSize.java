package com.shehala.citybrands.util;

import android.app.Activity;

public class ScreenSize {

	@SuppressWarnings("deprecation")
	public static String getScreenSize(Activity activity) {
		String size = "";
		int width, height;

		width = activity.getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		height = activity.getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();
		size = width + "x" + height;

		return size;

	}
}
