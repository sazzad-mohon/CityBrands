package com.shehala.citybrands.holder;

import java.util.Vector;

import android.util.Log;

import com.shehala.citybrands.model.NewsList;
import com.shehala.citybrands.util.PrintLog;

public class AllNewsList {

	private static Vector<NewsList> allNewsList = new Vector<NewsList>();

	public static Vector<NewsList> getAllNewsList()

	{
		return allNewsList;
	}

	public static void setAllNewsList(Vector<NewsList> allNewsList)

	{
		AllNewsList.allNewsList = allNewsList;
	}

	public static NewsList getNewsList(int pos)

	{
		return allNewsList.elementAt(pos);
	}

	public static void setNewsList(NewsList newsList)

	{
		AllNewsList.allNewsList.addElement(newsList);
	}

	public static void removeAll()

	{
		allNewsList.removeAllElements();
	}

	public static String getVideoUrlById(String id) {

		String url = "";

		for (NewsList newsList : AllNewsList.allNewsList) {

			if (newsList.getId().equals(id)) {
				url = newsList.getVideo_url();
			}

		}
		PrintLog.getWarnLog("url  : ", url + " in holder");
		Log.w("url  : ", url + " in holder");
		return url;

	}

	public static String getImageUrlById(String id) {

		String url = "";

		for (NewsList newsList : AllNewsList.allNewsList) {

			if (newsList.getId().equals(id)) {
				url = newsList.getImageurl().trim().replaceAll(" ", "%20");
			}

		}
		PrintLog.getWarnLog("url  : ", url + " in holder");
		return url;

	}

	public static Vector<NewsList> getAllNewsInfoExceptFirstId() {
		Vector<NewsList> newsList = new Vector<NewsList>();

		for (NewsList nl : AllNewsList.allNewsList) {
			if (nl.getId().equals("1")) {

				continue;
			}

			else

			{
				NewsList nsl = new NewsList();

				nsl.setId(nl.getId());
				nsl.setCreated(nl.getCreated());
				nsl.setDescription(nl.getDescription());
				nsl.setHead_line(nl.getHead_line());
				nsl.setImageurl(nl.getImageurl().trim().replaceAll(" ", "%20"));
				nsl.setModified(nl.getModified());
				nsl.setVideo_url(nl.getVideo_url().trim()
						.replaceAll(" ", "%20"));
				newsList.add(nsl);
			}
		}

		return newsList;

	}

}
