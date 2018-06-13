package com.shehala.citybrands.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.shehala.citybrands.holder.AllMostPopularBrands;
import com.shehala.citybrands.model.MostPopularBrand;

public class MostPopularBrandParser {

	public static boolean connect(Context con, String response)
			throws JSONException, IOException {

		int i;
		final JSONArray holder = new JSONArray(response);
		AllMostPopularBrands.removeAll();
		MostPopularBrand mpb;

		for (i = 0; i < holder.length(); i++) {

			final JSONObject mObject = holder.getJSONObject(i);
			mpb = new MostPopularBrand();
			String result = mObject.getString("brands");
			final JSONObject finalObject = new JSONObject(result);
			
			try {
				mpb.setId(finalObject.getString("id"));
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				mpb.setBrand_name(finalObject.getString("brand_name"));
//				PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
//						+ " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}
			AllMostPopularBrands.setAllPopularBrands(mpb);
			mpb = null;
		}

		return true;
	}

}
