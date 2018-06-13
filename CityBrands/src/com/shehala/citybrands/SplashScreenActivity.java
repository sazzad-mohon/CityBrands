package com.shehala.citybrands;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.shehala.citybrands.R;
import com.shehala.citybrands.gpstracking.GPSTracker;
import com.shehala.citybrands.holder.AllBrandsList;
import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.parser.BrandsListParser;
import com.shehala.citybrands.parser.ShopListParser;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.ScreenSize;

public class SplashScreenActivity extends Activity {
	private Handler mHandler = new Handler();
	private static int duration = 2000;
	private Context con;
	private String strBrand = "", strShop = "";
	private GPSTracker gps;
	private double latitude = 0, longitude = 0;
	private ImageView splash_needle;
	private String strLat;
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
		con = this;
		splash_needle = (ImageView) findViewById(R.id.splash_compass_needle);
		animation = AnimationUtils.loadAnimation(this,
				R.anim.rotate_around_center_point);
		splash_needle.startAnimation(animation);
		Allconstants.screenSize = ScreenSize.getScreenSize(this);
		PrintLog.getWarnLog("Allconstants.screenSize", Allconstants.screenSize);
		new AllShopAndBrand().execute();
		gps = new GPSTracker(con);

		if (gps.canGetLocation())

		{
			latitude = gps.getLatitude();
			strLat = String.valueOf(latitude);
			PrintLog.getWarnLog("strLat : ", strLat + "");
			longitude = gps.getLongitude();
			PrintLog.getWarnLog("longitude : ", longitude + "");

		}

		mHandler.postDelayed(mRunnable, duration);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		mHandler.removeCallbacksAndMessages(mHandler);
	}

	private final Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub	
			SplashScreenActivity.this.finish();
			Intent goToLoginScreen = new Intent(con, MyTabActivity.class);
			goToLoginScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(goToLoginScreen);
			
			//overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

		}
	};

	class AllShopAndBrand extends AsyncTask<String, String, String> {
		HttpPost http_all_brand;
		HttpPost http_all_shop;

		@Override
		protected String doInBackground(String... aurl) {

			 http_all_brand = new HttpPost(AllUrl.allBrandsUrl);
			 try {
				strBrand = MyHTTPRequest.getData(http_all_brand);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		

			try {
				
				strShop = MyHTTPRequest.getTextFromXmlOrJson(MyHTTPRequest
						.getInputStreamForGetRequest(AllUrl.allShopsUrl(
								strLat, String.valueOf(longitude))));
				
				PrintLog.getDebugLog("strShop", strShop + "");
			

				if (ShopListParser.ParseQuery(con, strShop)) {
					PrintLog.getDebugLog(
							"AllShopLists.getAllShopList().size()",
							AllShopLists.getAllShopList().size() + "");
				}
				
				if (BrandsListParser.connect(con, strBrand))

				{
					PrintLog.getDebugLog(
							"AllBrandsList.getAllBrandsList().size()",
							AllBrandsList.getAllBrandsList().size() + "");
				}

			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			} catch (URISyntaxException e) {

				e.printStackTrace();
			} catch (JSONException e) {

				e.printStackTrace();
			}

			return null;

		}
	}

}
