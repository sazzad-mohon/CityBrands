package com.shehala.citybrands.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.shehala.citybrands.holder.AllNewsList;
import com.shehala.citybrands.model.NewsList;
import com.shehala.citybrands.util.PrintLog;

public class NewsListParser {

	public static boolean connect(Context con, String response)
			throws JSONException, IOException {

		final JSONArray holder = new JSONArray(response);
		AllNewsList.removeAll();
		NewsList nsl;

		for (int i = 0; i < holder.length(); i++) {
			nsl = new NewsList();
			final JSONObject mObject = holder.getJSONObject(i);
			String result = mObject.getString("News");
			final JSONObject finalObject = new JSONObject(result);

			try {
				nsl.setId(finalObject.getString("id"));
				// PrintLog.getDebugLog("id : ", nsl.getId()
				// + " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				nsl.setHead_line(finalObject.getString("headline"));
				// PrintLog.getErrorLog("headline : ", nsl.getHead_line()
				// + " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				nsl.setDescription(finalObject.getString("description"));
				// PrintLog.getDebugLog("description : ", nsl.getDescription()
				// + " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				nsl.setImageurl(finalObject.getString("image_url"));
				// PrintLog.getDebugLog("image_url : ", nsl.getImageurl()
				// + " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				nsl.setVideo_url(finalObject.getString("video_url"));
				 PrintLog.getDebugLog("video_url : ", nsl.getVideo_url()
				 + " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				nsl.setCreated(finalObject.getString("created"));
				// PrintLog.getDebugLog("created : ", nsl.getCreated()
				// + " in parser");

			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				nsl.setModified(finalObject.getString("modified"));
			} catch (final Exception e) {
				// TODO: handle exception
			}

			AllNewsList.setNewsList(nsl);
			nsl = null;
		}

		return true;
	}

}
