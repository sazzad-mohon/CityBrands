package com.shehala.citybrands.util;

import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.shehala.citybrands.model.ShopList;

public class Allconstants {

	public static String appId = "123456789";
	public static String strForBackButton = "";
	public static String strScreen = "";
	public static String brandsBack = "";
	public static String strAllBrands = "";
	public static int previousTabValue = 0;
	public static int currentTabValue = 0;
	public static boolean isMap = false;
	public static int pos = 0;
	public static List<ShopList> shopList;
	public static ViewFlipper mainFliper;
	public static Activity activity;
	public static List<ShopList> shopListFinal;
	public static ShopList spl;
	public static String screenShopMap = "";
	
	public static String screenSize = "";
	public static String strFavBack = "";
	public static String favProxiBack = "";
	public static WebView webView;
	

	// To encode string to base 64

	public static String getBase64fromString(String text) {
		byte bytes[] = text.getBytes();
		return Base64.encodeToString(bytes, Base64.NO_WRAP);
	}

	public static void browseUrl(Context con, String url) {

		String mUrl = "";

		if (!url.contains("http")) {
			mUrl = "http://" + url;
		} else {
			mUrl = url;
		}

		try {

			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(mUrl));
			con.startActivity(i);

		} catch (ActivityNotFoundException e) {
		}

	}

	// To make a phone call

	public static void makePhoneCall(Context con, String number) {

		try {
			String url = "tel:" + number;
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			con.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
		}

	}

	// To send email

	
	public static void semdEmail(Context con, String email) {
		try {
			final String mail = email;
			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);

			emailIntent.setType("plain/text");

			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { mail });
			
			
			Log.w("Context while email",con+"");
			con.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			
			//con.startActivity(emailIntent);
		} catch (final ActivityNotFoundException anfe) {

		}
	}

	// Background change when click on button

	public static void pushHoverForTopBar(Context con,
			final ImageButton btnBack, final ImageButton btnBrands,
			final ImageButton btnTwitter, final ImageButton btnFacebook,
			final int backDrawable, final int brandDrawable,
			final int facebookDrawable, final int twitterDrawable) {

		btnBack.setBackgroundResource(backDrawable);
		btnBrands.setBackgroundResource(brandDrawable);
		btnTwitter.setBackgroundResource(twitterDrawable);
		btnFacebook.setBackgroundResource(facebookDrawable);

	}

	// Get latitude from address

	public static double getLatitude(Context con, String address)

	{

		double lat = 0;

		Geocoder geocoder = new Geocoder(con);
		try {

			List<Address> addressList = geocoder
					.getFromLocationName(address, 1);
			if (addressList != null && addressList.size() > 0) {
				lat = addressList.get(0).getLatitude();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lat;

	}

	// Get latitude from address

	public static double getLongitude(Context con, String address)

	{

		double lon = 0;

		Geocoder geocoder = new Geocoder(con);
		try {

			List<Address> addressList = geocoder
					.getFromLocationName(address, 1);
			if (addressList != null && addressList.size() > 0) {
				lon = addressList.get(0).getLongitude();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lon;

	}

	// Open map in browser

	public static void openMap(Activity act, String mapName, double lat,
			double lon)

	{
		try {
			final String label = "" + mapName;
			final String uriBegin = "geo:" + lat + "," + lon;
			final String query = lat + "," + lon + "(" + label + ")";
			final String encodedQuery = Uri.encode(query);
			final String uriString = uriBegin + "?q=" + encodedQuery;
			final Uri uri = Uri.parse(uriString);
			final Intent intent = new Intent(
					android.content.Intent.ACTION_VIEW, uri);
			act.startActivity(intent);

		} catch (Exception e) {

		}
	}

	// Show the from current position to destination

	public static void drawRoute(Activity act, double currentLat,
			double currentLon, double destinaionLat, double destinationLon) {
		final Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?" + "saddr="
						+ currentLat + "," + currentLon + "&daddr="
						+ "" + "" + ""));
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		
		
		act.startActivity(intent);
	}

}
