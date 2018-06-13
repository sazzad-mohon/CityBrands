package com.shehala.citybrands.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.shehala.citybrands.model.Login;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;

public class LoginParser {

	public static boolean connect(Context con, String response)
			throws JSONException, IOException {

		final JSONArray holder = new JSONArray(response);

		PrintLog.getErrorLog("Size of login response : ", holder.length()
				+ response + "<>");

		if (holder.length() > 2)

		{

			try {
				Login.userID = holder.getString(0);

				PrintLog.getWarnLog("Id from model class : ", Login.userID
						+ "<><>");

			} catch (final Exception e) {

			}

			try {
				Login.loginStatus = holder.getString(1);
				PrintLog.getWarnLog("loginStatus from model class : ",
						Login.loginStatus + "<><>");
				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}

			try {
				Login.userEmail = holder.getString(2);
				SharePreferenceUtil.setUserEmail(con, holder.getString(2));
				PrintLog.getWarnLog("userEmail from model class : ",
						Login.userEmail + "<><>");
				PrintLog.getWarnLog("userEmail from preferecnce class : ",
						SharePreferenceUtil.getUserEmail(con) + "<><>");
				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}

			try {
				Login.gender = holder.getString(3);
				SharePreferenceUtil.setSex(con, holder.getString(3));
				PrintLog.getWarnLog("gender from model class : ", Login.gender
						+ "<><>");
				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}

			try {
				Login.age = holder.getString(4);
				SharePreferenceUtil.setAge(con,holder.getString(4));
				PrintLog.getWarnLog("age from model class : ", Login.age
						+ "<><>");
				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}

			try {
				Login.postalCode = holder.getString(5);
				SharePreferenceUtil.setPostCode(con, holder.getString(5));
				PrintLog.getWarnLog("postalCode from model class : ",
						Login.postalCode + "<><>");
				PrintLog.getWarnLog("postalCode from preference class : ",
						SharePreferenceUtil.getPostCode(con) + "<><>");

				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}

			try {
				Login.receiveNewsletter = holder.getString(6);
				PrintLog.getWarnLog("receiveNewsletter from model class : ",
						Login.receiveNewsletter + "<><>");
				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}
		}

		else

		{

			try {
				Login.userID = holder.getString(0);
				PrintLog.getWarnLog("Id from model class : ", Login.userID
						+ "<><>");

			} catch (final Exception e) {

			}

			try {
				Login.loginStatus = holder.getString(1);
				PrintLog.getWarnLog("loginStatus from model class : ",
						Login.loginStatus + "<><>");
				// PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
				// + " in parser");
			} catch (final Exception e) {

			}

		}

		return true;
	}
}
