package com.shehala.citybrands.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.shehala.citybrands.holder.AllBrandsList;
import com.shehala.citybrands.model.BrandsList;

public class BrandsListParser {

	public static boolean connect(Context con, String response)
			throws JSONException, IOException {

		int i;
		final JSONArray holder = new JSONArray(response);
		AllBrandsList.removeAll();
		BrandsList brl;

		for (i = 0; i < holder.length(); i++) {

			final JSONObject mObject = holder.getJSONObject(i);
			brl = new BrandsList();
			String result = mObject.getString("brands");
			final JSONObject finalObject = new JSONObject(result);
			
			try {
				brl.setId(finalObject.getString("id"));
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				brl.setBrandName(finalObject.getString("brand_name"));
//				PrintLog.getDebugLog("Brand Name : ", brl.getBrandName()
//						+ " in parser");
			} catch (final Exception e) {
				// TODO: handle exception
			}
			AllBrandsList.setBrandsList(brl);
			brl = null;
		}

		return true;
	}

}
