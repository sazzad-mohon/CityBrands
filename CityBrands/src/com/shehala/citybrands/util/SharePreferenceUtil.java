package com.shehala.citybrands.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SharePreferenceUtil {

	private static final String LOG_TAG = "SharedPreferencesHelper";

	// Dialogs Id
	public static final int DIALOG_ABOUT = 0;
	public static final int DIALOG_NO_CONNECTION = 1;
	public static final int DIALOG_UPDATE_PROGRESS = 2;

	// Menu Groups Id
	public static final int CHANNEL_SUBMENU_GROUP = 0;
	public static final String REMEMBER = "remember";
	
	// Email pressed or not
	public static final String EMAILPRESSED = "false";

	// App Preferences
	private static final String PREFS_FILE_NAME = "AppPreferences";
	private static final String PREF_TAB_FEED_KEY = "tabFeed";

	private static final String USER = "user";
	private static final String PASS = "pass";

	private static final String RADIUS = "radius";
	private static final String ZOOM = "zoom";

	private static String ONLINE = "online";

	// App Preferences
	private static final String FILE_NAME = "AppPreferences";
	private static final String POSITION = "Position";

	// Getters for Application configuration attributes and preferences defined
	// in Android Manifest

	public static String getFlurryAnalyticsApiKey(final Context con) {
		String flurryAnalyticsApiKey = null;
		try {
			final ApplicationInfo ai = con.getPackageManager()
					.getApplicationInfo(con.getPackageName(),
							PackageManager.GET_META_DATA);
			flurryAnalyticsApiKey = ai.metaData.getString("FLURRY_API_KEY");
		} catch (final NameNotFoundException nnfe) {
			Log.e(SharePreferenceUtil.LOG_TAG, "", nnfe);
		}
		return flurryAnalyticsApiKey;
	}

	public static String getGoogleAnalyticsProfileId(final Context con) {
		String googleAnalyticsProfileId = null;
		try {
			final ApplicationInfo ai = con.getPackageManager()
					.getApplicationInfo(con.getPackageName(),
							PackageManager.GET_META_DATA);
			googleAnalyticsProfileId = ai.metaData
					.getString("GOOGLE_ANALYTICS_PROFILE_ID");
		} catch (final NameNotFoundException nnfe) {
			Log.e(SharePreferenceUtil.LOG_TAG, "", nnfe);
		}
		return googleAnalyticsProfileId;
	}

	public static String getMobclixApplicationId(final Context con) {
		String mobclixApplicationId = null;
		try {
			final ApplicationInfo ai = con.getPackageManager()
					.getApplicationInfo(con.getPackageName(),
							PackageManager.GET_META_DATA);
			mobclixApplicationId = ai.metaData
					.getString("com.mobclix.APPLICATION_ID");
		} catch (final NameNotFoundException nnfe) {
			Log.e(SharePreferenceUtil.LOG_TAG, "", nnfe);
		}
		return mobclixApplicationId;
	}

	public static String getPass(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString(SharePreferenceUtil.PASS, "");
	}

	public static long getPrefTabFeedId(final Context con,
			final long defaultTabFeedId) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getLong(
				SharePreferenceUtil.PREF_TAB_FEED_KEY, defaultTabFeedId);
	}

	public static boolean getRemember(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getBoolean(SharePreferenceUtil.REMEMBER,
				false);
	}
	
	public static boolean getEmailPressed(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getBoolean(SharePreferenceUtil.EMAILPRESSED,
				false);
	}

	public static int getSplashDuration(final Context con) {
		int splashScreenDuration = 2000;
		try {
			final ApplicationInfo ai = con.getPackageManager()
					.getApplicationInfo(con.getPackageName(),
							PackageManager.GET_META_DATA);
			splashScreenDuration = ai.metaData.getInt("splash_screen_duration");
		} catch (final NameNotFoundException nnfe) {
			Log.e(SharePreferenceUtil.LOG_TAG, "", nnfe);
		}
		return splashScreenDuration;
	}

	public static String getUser(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString(SharePreferenceUtil.USER, "");
	}

	public static String getRadius(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString(SharePreferenceUtil.RADIUS,
				"3KM");
	}

	public static String getZoom(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString(SharePreferenceUtil.ZOOM, "11");
	}

	public static CharSequence getVersionName(final Context con) {
		CharSequence version_name = "";
		try {
			final PackageInfo packageInfo = con.getPackageManager()
					.getPackageInfo(con.getPackageName(), 0);
			version_name = packageInfo.versionName;
		} catch (final NameNotFoundException nnfe) {
			Log.e(SharePreferenceUtil.LOG_TAG, "", nnfe);
		}
		return version_name;
	}

	// to check internet

	public static boolean isOnline(final Context con) {
		final ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			return ni.isConnectedOrConnecting();
		} else {
			return false;
		}
	}

	public static void setPass(final Context con, final String pass) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString(SharePreferenceUtil.PASS, pass);
		editor.commit();
	}

	public static void setPrefTabFeedId(final Context con, final long feedId) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putLong(SharePreferenceUtil.PREF_TAB_FEED_KEY, feedId);
		editor.commit();
	}

	public static void setRemember(final Context con, final boolean flag) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putBoolean(SharePreferenceUtil.REMEMBER, flag);
		editor.commit();
	}
	
	public static void setEmailPressed(final Context con, final boolean flag){
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putBoolean(SharePreferenceUtil.EMAILPRESSED, flag);
		editor.commit();
	}

	public static void setUser(final Context con, final String user) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString(SharePreferenceUtil.USER, user);
		editor.commit();
	}

	public static void setRadius(final Context con, final String rad) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString(SharePreferenceUtil.RADIUS, rad);
		editor.commit();
	}

	public static void setZoom(final Context con, final String zoom) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString(SharePreferenceUtil.ZOOM, zoom);
		editor.commit();
	}

	public static boolean trackFlurryAnalytics(final Context con) {
		boolean track = true;
		final String flurryAnalyticsApiKey = SharePreferenceUtil
				.getFlurryAnalyticsApiKey(con);

		if (flurryAnalyticsApiKey == null
				|| flurryAnalyticsApiKey
						.equalsIgnoreCase("xxxxxxxxxxxxxxxxxxxx")) {
			track = false;
		} else {
			track = true;
		}

		return track;
	}

	// Shared getter util methods

	public static boolean trackGoogleAnalytics(final Context con) {
		boolean track = true;
		final String googleAnalyticsProfileId = SharePreferenceUtil
				.getGoogleAnalyticsProfileId(con);

		if (googleAnalyticsProfileId == null
				|| googleAnalyticsProfileId.equalsIgnoreCase("UA-xxxxx-xx")) {
			track = false;
		} else {
			track = true;
		}

		return track;
	}

	public static boolean trackMobclixSession(final Context con) {
		boolean track = true;
		final String mobclixApplicationId = SharePreferenceUtil
				.getMobclixApplicationId(con);

		if (mobclixApplicationId == null
				|| mobclixApplicationId
						.equalsIgnoreCase("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")) {
			track = false;
		} else {
			track = true;
		}

		return track;
	}

	public static void setOnlineOrOffline(final Context con, final boolean flag) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putBoolean(SharePreferenceUtil.ONLINE, flag);
		editor.commit();
	}

	public static boolean getOnlineOrOffline(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getBoolean(SharePreferenceUtil.ONLINE,
				false);
	}

	/**
	 * 
	 * To set and get Position to share preference
	 * 
	 */

	public static void setPosition(final Context con, final String poss) {
		final SharedPreferences prefs = con.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString(POSITION, poss);
		editor.commit();
	}

	public static String getPosition(final Context con) {
		return con.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
				.getString(POSITION, "0");
	}

	public static void setNewsDate(final Context con, final String date) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("date", date);
		editor.commit();
	}

	public static String getNewsDate(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("date", "0000-00-00 00:00:00");
	}

	public static void setLoginStatus(final Context con, final String status) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("status", status);
		editor.commit();
	}

	public static String getLoginStatus(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("status", "logout");
	}

	// to get and set progress value
	public static void setAgeProgress(final Context con, final String status) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("progress", status);
		editor.commit();
	}

	public static String getAgeProgress(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("progress", "0");
	}

	// to get and set layout param value
	public static void setLayoutParam(final Context con, final String param) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("param", param);
		editor.commit();
	}

	public static String getLayoutParam(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("param", "0");
	}

	// to get and set post code value
	public static void setPostCode(final Context con, final String code) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("code", code);
		editor.commit();
	}

	public static String getPostCode(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("code", "0");
	}

	// to get and set user email value
	public static void setUserEmail(final Context con, final String mail) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("mail", mail);
		editor.commit();
	}

	public static String getUserEmail(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("mail", "0");
	}

	// to get and set user email value
	public static void setSex(final Context con, final String sex) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("sex", sex);
		editor.commit();
	}

	public static String getSex(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("sex", "female");
	}

	// to get and set user email value
	public static void setAge(final Context con, final String age) {
		final SharedPreferences prefs = con.getSharedPreferences(
				SharePreferenceUtil.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();
		editor.putString("age", age);
		editor.commit();
	}

	public static String getAge(final Context con) {
		return con.getSharedPreferences(SharePreferenceUtil.PREFS_FILE_NAME,
				Context.MODE_PRIVATE).getString("age", "0");
	}
}
