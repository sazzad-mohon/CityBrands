package com.shehala.citybrands.parser;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.util.PrintLog;

public class ShopListParser {


	public static boolean ParseQuery(Context con, String response)
			throws JSONException, IOException {

		
		final JSONArray holder = new JSONArray(response);
		PrintLog.getDebugLog(" holder.length() : ", holder.length() + "");
		AllShopLists.removeAll();
		ShopList mps;

		for (int i = 0; i < holder.length(); i++) {

			final JSONObject mObject = holder.getJSONObject(i);
			mps = new ShopList();
			String result = mObject.getString("Shop");
			final JSONObject finalObject = new JSONObject(result);

			try {
				mps.setId(finalObject.getString("id"));
				//PrintLog.getDebugLog("ID of Shop  : ", mps.getId() + "");
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setBrands_id(finalObject.getString("brands_id"));
				//PrintLog.getDebugLog("brands_id  : ", mps.getBrands_id() + "");
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			
			
			try {
				mps.setCvr_number(finalObject.getString("CVR_NUMBER"));
				//PrintLog.getWarnLog("CVR_NUMBER", mps.getCvr_number() +" <>");
			} catch (final Exception e) {
				// TODO: handle exception
			}

			try {
				mps.setShop_name(finalObject.getString("shop_name"));
				//PrintLog.getDebugLog("shop_name  : ", spl.getShop_name() + "");
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setShop_image(finalObject.getString("shop_image"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setAddress(finalObject.getString("address"));
				
				//PrintLog.getWarnLog("Address for getting lat lng", spl.getAddress());
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setZip_code(finalObject.getString("zip_code"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setPhone(finalObject.getString("phone"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setUrl(finalObject.getString("url"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setM_w(finalObject.getString("M_W"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setT(finalObject.getString("T"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setF(finalObject.getString("F"));
				//PrintLog.getDebugLog("F : ", spl.getF());
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setS(finalObject.getString("S"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setSn(finalObject.getString("Sn"));
				//PrintLog.getDebugLog("SN : ", spl.getSn());
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setEmail(finalObject.getString("email"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setHitcounter(finalObject.getString("hitcounter"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setCreated(finalObject.getString("created"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setModified(finalObject.getString("modified"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setDistance(finalObject.getString("distance"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setBrands_name(finalObject.getString("brands_name"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setIsFavourite(finalObject.getString("isFavourite"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setLatitude(finalObject.getString("latitude"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setLongitude(finalObject.getString("longitude"));
				
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			try {
				mps.setFacebookUrl(finalObject.getString("facebook"));
				PrintLog.getErrorLog("Facebook URL in parser : ", mps.getFacebookUrl()+"");
			} catch (final Exception e) {
				// TODO: handle exception
			}
			
			
			AllShopLists.setShopList(mps);
			mps = null;
		}

		
		return true;
	}

}
