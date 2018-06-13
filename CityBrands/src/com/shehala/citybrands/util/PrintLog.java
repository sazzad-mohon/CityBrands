package com.shehala.citybrands.util;

import android.util.Log;


public class PrintLog {

	public static void getDebugLog(String tag, String msg) {
		//Log.d(tag, msg);
	}

	public static void getErrorLog(String tag, String msg) {
		//Log.e(tag, msg);
	}

	public static void getWarnLog(String tag, String msg) {
		Log.w(tag, msg);
	}
	
	public static void showHitCounterLog(String tag, String msg) {
		//Log.e(tag, msg);
	}
	public static void showLog(String msg) {
		Log.w("", msg);
	}
	public static void showFinalVersionLog(String msg) {
		Log.w("", msg);
	}
	
}
