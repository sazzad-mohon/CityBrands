package com.shehala.citybrands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.maps.MapActivity;
import com.shehala.citybrands.R;
import com.shehala.citybrands.gpstracking.GPSTracker;
import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteBrands;
import com.shehala.citybrands.sqlite.FavoriteShops;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;

@SuppressLint("DefaultLocale")
public class FavoriteActivity extends MapActivity implements OnClickListener {

	private ViewFlipper mainFliper;
	private Context con;
	private ImageButton imgBtnBrandName, imgBtnFacebook, imgBtnTwitter,
			imgBtnBack;
	private Switch mToggleBtn;
	private String strBrandOrShop = "Brand";
	private ListView favoriteListView;
	private FavoriteAdapter adapter;
	private ImageButton cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;
	private List<ShopList> shopList;
	private List<ShopList> shopListFinal;
	private GPSTracker gps;
	private double latitude = 0, longitude = 0, lat = 0, lon = 0;
	private DatabaseHandler db;
	private List<FavoriteShops> fs;
	private List<FavoriteBrands> fb;
	private String brandId = "";
	private ProgressDialog pd;
	private String name = "", shopId = "";
	private Handler mHandler = new Handler();
	private static int duration = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.all_brand_flipper);
		con = this;

		gps = new GPSTracker(con);

		if (gps.canGetLocation())

		{
			latitude = gps.getLatitude();
			PrintLog.getWarnLog("Latitude : ", latitude + "");
			longitude = gps.getLongitude();
			PrintLog.getWarnLog("longitude : ", longitude + "");

		}
		Allconstants.strFavBack = "favMain";

	}

	private void initUI() {
		mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
		mainFliper.setDisplayedChild(6);

		imgBtnBack = (ImageButton) findViewById(R.id.favorite_back);
		imgBtnBack.setOnClickListener(this);

		imgBtnFacebook = (ImageButton) findViewById(R.id.favorite_facebook);

		imgBtnFacebook.setOnClickListener(this);

		imgBtnTwitter = (ImageButton) findViewById(R.id.favorite_twitter);
		imgBtnTwitter.setOnClickListener(this);

		imgBtnBrandName = (ImageButton) findViewById(R.id.favorite_brand_names);
		imgBtnBrandName.setOnClickListener(this);

		mToggleBtn = (Switch) findViewById(R.id.favorite_toggle_btn);
		mToggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (buttonView.isChecked())

				{

					strBrandOrShop = "Shop";
					fs = db.getAllShops();
					fb = db.getAllContacts();
					favoriteListView = (ListView) findViewById(R.id.favorite_listview);
					favoriteListView.setDivider(null);
					favoriteListView.setDividerHeight(0);
					favoriteListView.setAdapter(adapter);
					//ListWaveAnimation.showTheWave(con, favoriteListView);
					PrintLog.getDebugLog("BrandOrShop in sliding ?",
							strBrandOrShop);

				}

				else if (!buttonView.isChecked())

				{
					strBrandOrShop = "Brand";
					fs = db.getAllShops();
					fb = db.getAllContacts();
					favoriteListView = (ListView) findViewById(R.id.favorite_listview);
					favoriteListView.setDivider(null);
					favoriteListView.setDividerHeight(0);
					favoriteListView.setAdapter(adapter);
					//ListWaveAnimation.showTheWave(con, favoriteListView);
					PrintLog.getDebugLog("BrandOrShop in sliding ?",
							strBrandOrShop);

				}
			}
		});

		PrintLog.getDebugLog("BrandOrShop ?", strBrandOrShop);

		cityImgBtnBack = (ImageButton) findViewById(R.id.city_brand_back);
		cityImgBtnBack.setOnClickListener(this);

		cityImgBtnFacebook = (ImageButton) findViewById(R.id.city_brand_facebook);
		cityImgBtnFacebook.setOnClickListener(this);

		cityImgBtnTwitter = (ImageButton) findViewById(R.id.city_brand_twitter);
		cityImgBtnTwitter.setOnClickListener(this);

		cityImgBtnBrandName = (ImageButton) findViewById(R.id.city_brand_names);
		cityImgBtnBrandName.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(SharePreferenceUtil.getEmailPressed(con)){
			SharePreferenceUtil.setEmailPressed(con, false);
			return;
		}

		if (SharePreferenceUtil.getLoginStatus(con).equalsIgnoreCase("logout"))

		{
			mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
			mainFliper.setDisplayedChild(18);
			mHandler.postDelayed(mRunnable, duration);
		} else {
			initUI();
			db = new DatabaseHandler(con);
			fs = db.getAllShops();
			PrintLog.getWarnLog("Size of fs: ", fs.size() + "<>");
			fb = db.getAllContacts();
			PrintLog.getWarnLog("Size of fb: ", fb.size() + "<>");
			mainFliper.setDisplayedChild(6);
			Allconstants.strFavBack = "favMain";

			new FavoriteList().execute();
		}
		MyTabActivity.getInstance().pushIndex(3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// Favorite screen
		case R.id.favorite_back:
			if (MyTabActivity.getInstance() != null)

			{
				MyTabActivity.getInstance().openTab();
			}

			break;

		case R.id.favorite_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.favorite_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.favorite_brand_names:
			Allconstants.strFavBack = "favBr";
			mainFliper.setDisplayedChild(8);
			break;

		// city_brand_logo screen

		case R.id.city_brand_back:
			Allconstants.strFavBack = "favMain";
			mainFliper.setDisplayedChild(6);
			break;

		case R.id.city_brand_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_names:
			Allconstants.strFavBack = "favMain";
			mainFliper.setDisplayedChild(6);

			break;

		case R.id.city_brand_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;
		}

	}

	/***
	 * 
	 * Show favorites list
	 * 
	 */

	private void showFav() {
		adapter = new FavoriteAdapter();
		if (fb.size() != 0) {
			favoriteListView = (ListView) findViewById(R.id.favorite_listview);
			favoriteListView.setDivider(null);
			favoriteListView.setDividerHeight(0);
			favoriteListView.setAdapter(adapter);
			//ListWaveAnimation.showTheWave(con, favoriteListView);
		}
	}

	@SuppressLint("DefaultLocale")
	private class FavoriteAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			if (strBrandOrShop.equalsIgnoreCase("brand")) {
				if (fb.size() >= 10) {
					return 10;
				}

				else {
					return fb.size();
				}
			}

			else if (strBrandOrShop.equalsIgnoreCase("shop"))

			{
				if (fs.size() >= 10) {
					return 10;
				}

				else {
					return fs.size();
				}
			}

			else {
				return 10;
			}

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_favorite, null);
			}

			if (strBrandOrShop.equalsIgnoreCase("Brand")) {

				final TextView title = (TextView) v
						.findViewById(R.id.row_favorite_title);

				if (fb.get(position).getBrand_name().length() > 24) {
					name = fb.get(position).getBrand_name().substring(0, 24);
					title.setText(name + "");
				}

				else {
					title.setText(fb.get(position).getBrand_name());
				}
				title.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub

						String textLeft = fb.get(position).getBrand_name();
						if (textLeft.length() > 24) {
							Animation animationToLeft = new TranslateAnimation(
									title.getWidth(), -title.getWidth(), 0, 0);
							animationToLeft.setDuration(5000);
							animationToLeft.setRepeatMode(0);
							animationToLeft.setRepeatCount(0);

							title.setText(textLeft);
							title.setAnimation(animationToLeft);

							return true;
						}

						return false;
					}
				});

				title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showShopList(position);

					}
				});

				final TextView distance = (TextView) v
						.findViewById(R.id.row_favorite_distance);
				distance.setVisibility(View.GONE);

				final ImageButton fav_icon = (ImageButton) v
						.findViewById(R.id.row_favorite_favorite_icon);
				fav_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						db.deleteSingleFavoriteBrand(fb.get(position)
								.getBrand_id());
						fav_icon.setBackgroundResource(R.drawable.list_favorite_normal);
						// ToastShow.getMessage(con, "Click on the button");

					}
				});

			} else if (strBrandOrShop.equalsIgnoreCase("Shop")) {

				// String name = "";

				final TextView title = (TextView) v
						.findViewById(R.id.row_favorite_title);

				if (fs.get(position).getShop_name().length() > 24) {
					name = fs.get(position).getShop_name().substring(0, 24);
					title.setText(name + "");
				}

				else {
					title.setText(fs.get(position).getShop_name());
				}

				title.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub

						String textLeft = fs.get(position).getShop_name();
						if (textLeft.length() > 24) {
							Animation animationToLeft = new TranslateAnimation(
									title.getWidth(), -title.getWidth(), 0, 0);
							animationToLeft.setDuration(5000);
							animationToLeft.setRepeatMode(0);
							animationToLeft.setRepeatCount(0);

							title.setText(textLeft);
							title.setAnimation(animationToLeft);

							return true;

						}

						return false;
					}
				});

				title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						shopId = fs.get(position).getShop_id().trim();
						showShopDetails(position);
						new ShopHitIncrease().execute();

					}
				});

				final TextView distance = (TextView) v
						.findViewById(R.id.row_favorite_distance);
				double dis = 0;
				String strFinalDistance = "";
				try {
					dis = Double.parseDouble(fs.get(position)
							.getShop_distance().trim());
					strFinalDistance = String.format("%.2f", dis);
				} catch (Exception e) {
					// TODO: handle exception
				}

				distance.setText(con.getString(R.string.distance_calculation)
						+ " " + strFinalDistance + " km.");

				final ImageButton fav_icon = (ImageButton) v
						.findViewById(R.id.row_favorite_favorite_icon);
				fav_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						db.deleteSingleFavoriteShop(fs.get(position)
								.getShop_id());
						fav_icon.setBackgroundResource(R.drawable.list_favorite_normal);

					}
				});

			}

			final ImageButton showWay = (ImageButton) v
					.findViewById(R.id.row_favorite_arrow_icon);
			showWay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						lat = Double.parseDouble(fs.get(position).getLatitude()
								.trim());
						lon = Double.parseDouble(fs.get(position)
								.getLongitude().trim());
					} catch (Exception e) {
						// TODO: handle exception
					}

					if(!SharePreferenceUtil.isOnline(con))
					{
						ToastShow.getMessage(FavoriteActivity.this, "No internet available");
						return;
					}
					
					Allconstants.drawRoute(FavoriteActivity.this, lat, lon,
							latitude, longitude);
				}
			});

			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (strBrandOrShop.equalsIgnoreCase("Shop"))

					{
						showShopDetails(position);
					}

					else if (strBrandOrShop.equalsIgnoreCase("Brand"))

					{

						showShopList(position);

					}
				}
			});

			return v;
		}
	}

	/***
	 * 
	 * Show Brand Hit Increasing
	 * 
	 */

	private class BrandHitIncrease extends AsyncTask<String, String, String> {

		HttpPost increaseHitPost;

		@Override
		protected String doInBackground(String... aurl) {

			increaseHitPost = new HttpPost(AllUrl.increaseBrandHitURL
					+ brandId.replaceAll(" ", "%20"));
			PrintLog.showHitCounterLog("Hit URL: ", AllUrl.increaseBrandHitURL
					+ brandId.replaceAll(" ", "%20"));

			PrintLog.showLog("Brand Id for hit : " + brandId);

			try {

				String res = MyHTTPRequest.getData(increaseHitPost);
				PrintLog.showLog("Increase hit response : " + res);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {

		}
	}

	/***
	 * 
	 * Show Shop Hit Increasing
	 * 
	 */

	private class ShopHitIncrease extends AsyncTask<String, String, String> {

		HttpPost increaseHitPost;

		@Override
		protected String doInBackground(String... aurl) {

			increaseHitPost = new HttpPost(AllUrl.increaseShopHitURL
					+ shopId.replaceAll(" ", "%20"));

			PrintLog.showHitCounterLog("Hit url :", AllUrl.increaseShopHitURL
					+ shopId.replaceAll(" ", "%20"));
			try {

				String res = MyHTTPRequest.getData(increaseHitPost);
				PrintLog.showLog("Increase hit response : " + res);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK)

		{

			if (Allconstants.strFavBack.equalsIgnoreCase("favMain")) {
				if (MyTabActivity.getInstance() != null)

				{
					MyTabActivity.getInstance().openTab();
					Allconstants.strFavBack = "";
				}

				return true;
			}

			else if (Allconstants.strFavBack.equalsIgnoreCase("favBr")) {
				mainFliper.setDisplayedChild(6);
				Allconstants.strFavBack = "favMain";
				return true;
			}

			else if (Allconstants.strFavBack.equalsIgnoreCase("favList")) {
				mainFliper.setDisplayedChild(6);
				Allconstants.strFavBack = "favMain";
				return true;
			}

			else if (Allconstants.strFavBack.equalsIgnoreCase("favListBr")) {
				mainFliper.setDisplayedChild(4);
				Allconstants.strFavBack = "favList";
				return true;
			} else if (Allconstants.strFavBack.equalsIgnoreCase("favListMap")) {
				mainFliper.setDisplayedChild(4);
				Allconstants.strFavBack = "favList";
				return true;
			} else if (Allconstants.strFavBack.equalsIgnoreCase("favListMapBr")) {
				mainFliper.setDisplayedChild(12);
				Allconstants.strFavBack = "favListMap";
				return true;
			} else if (Allconstants.strFavBack
					.equalsIgnoreCase("favDetailsFromMap")) {
				mainFliper.setDisplayedChild(12);
				Allconstants.strFavBack = "favListMap";
				return true;
			}

			else if (Allconstants.strFavBack
					.equalsIgnoreCase("favDetailsFromList")) {
				mainFliper.setDisplayedChild(4);
				Allconstants.strFavBack = "favList";
				return true;
			}

			else {

				if (MyTabActivity.getInstance() != null)

				{
					MyTabActivity.getInstance().pushIndex(3);
					MyTabActivity.getInstance().openTab();
					Allconstants.strFavBack = "";
				}

				return true;
			}

		}

		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		mHandler.removeCallbacksAndMessages(mRunnable);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mHandler.removeCallbacksAndMessages(mRunnable);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private class FavoriteList extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {
			return null;
		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd.isShowing()) {
				pd.dismiss();
			}

			showFav();
		}
	}

	private void showShopList(int pos) {
		String b_id = fb.get(pos).getBrand_name();
		brandId = fb.get(pos).getBrand_id().trim();

		shopList = AllShopLists.getAllShopsInfoByBrandName(b_id);
		PrintLog.getWarnLog("shopList.size()", shopList.size() + "<><>");
		Allconstants.shopList = shopList;
		PrintLog.getWarnLog("Allconstants.shopList",
				Allconstants.shopList.size() + "<><>");

		shopListFinal = new ArrayList<ShopList>();

		for (int i = 0; i < shopList.size(); i++)

		{

			double final_distance = Double.parseDouble(shopList.get(i)
					.getDistance());

			PrintLog.getWarnLog("final_distance", final_distance + "<><>");

			if (final_distance > 90)

			{

				ShopList sl = new ShopList();

				sl.setBrands_id(shopList.get(i).getBrands_id());
				sl.setBrands_name(shopList.get(i).getBrands_name());
				sl.setShop_image(shopList.get(i).getShop_image());
				sl.setS(shopList.get(i).getS());
				sl.setM_w(shopList.get(i).getM_w());
				sl.setPhone(shopList.get(i).getPhone());
				sl.setZip_code(shopList.get(i).getZip_code());
				sl.setAddress(shopList.get(i).getAddress());
				sl.setId(shopList.get(i).getId());
				sl.setShop_name(shopList.get(i).getShop_name());
				sl.setDistance(shopList.get(i).getDistance());
				sl.setLatitude(shopList.get(i).getLatitude());
				sl.setLongitude(shopList.get(i).getLongitude());
				sl.setUrl(shopList.get(i).getUrl());
				sl.setEmail(shopList.get(i).getEmail());
				sl.setFacebookUrl(shopList.get(i).getFacebookUrl());
				shopListFinal.add(sl);

			}
		}

		if (shopListFinal.size() != 0)

		{
			Allconstants.shopList = shopList;
			Allconstants.mainFliper = mainFliper;
			Allconstants.activity = FavoriteActivity.this;
			Allconstants.shopListFinal = shopListFinal;
			PrintLog.getWarnLog("Allconstants.shopList",
					Allconstants.shopList.size() + "<><>");
			new ShopListWithSpecificBrands(FavoriteActivity.this, mainFliper,
					shopListFinal);
			PrintLog.getErrorLog("b_id : ", b_id);
			Allconstants.strFavBack = "favList";
			new BrandHitIncrease().execute();
		}

		else

		{
			Allconstants.shopList = shopList;
			Allconstants.mainFliper = mainFliper;
			Allconstants.activity = FavoriteActivity.this;
			Allconstants.shopListFinal = shopListFinal;
			PrintLog.getWarnLog("Allconstants.shopList",
					Allconstants.shopList.size() + "<><>");
			new ShopListWithSpecificBrands(FavoriteActivity.this, mainFliper,
					shopListFinal);
			PrintLog.getErrorLog("b_id : ", b_id);
			Allconstants.strFavBack = "favList";
			new BrandHitIncrease().execute();
		}

		PrintLog.getWarnLog("shopListFinal.size()", shopListFinal.size()
				+ " in view click");
	}

	private void showShopDetails(int pos)

	{

		PrintLog.getWarnLog("Name of selected item : ", fs.get(pos)
				.getShop_name());

		FavoriteShops fshop = fs.get(pos);

		PrintLog.getWarnLog("fshop.getShop_name() : ", fshop.getShop_name());

		new ShopDetailsWithSpecificBrands(FavoriteActivity.this, mainFliper, fs
				.get(pos).getShop_id(), "favorite_shop");
		Allconstants.strFavBack = "favList";
	}

	private final Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (MyTabActivity.getInstance() != null) {
				MyTabActivity.getInstance().openTab();
			}

		}
	};

}
