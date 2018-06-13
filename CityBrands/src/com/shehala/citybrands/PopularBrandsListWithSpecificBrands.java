package com.shehala.citybrands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.shehala.citybrands.R;
import com.shehala.citybrands.gpstracking.GPSTracker;
import com.shehala.citybrands.mapview.CurrentMapPoint;
import com.shehala.citybrands.mapview.MapLocation;
import com.shehala.citybrands.mapview.MyItemizedOverlay;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteShops;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;
import com.shehala.citybrands.verticalseekbar.VerticalSeekBar;

public class PopularBrandsListWithSpecificBrands implements OnClickListener,
		OnSeekBarChangeListener {

	private ViewFlipper mainFliper;
	private ViewFlipper mFlp;
	private Activity ins;
	private ListView all_shop_list;
	private Adapter adapter;
	private DatabaseHandler db;
	private List<FavoriteShops> fs;
	private double latitude, longitude, lat, lon;
	private GPSTracker gps;

	
	int finalDistance = 0;

	private List<ShopList> shopListFinal;
	private ShopList spl;

	private ImageButton allShopImgBtnBack, allShopImgBtnBrandName,
			allShopImgBtnFacebook, allShopImgBtnTwitter;
	private Button showMap;
	private double verticalBarDistance = 0;
	private String shopIdForHit = "";

	private String strScreen = "";
	private VerticalSeekBar verticalBar;
	private TextView hits;
	private LinearLayout.LayoutParams param;

	// map screen

	private double maplat, maplon;
	private String address = "";
	private int size = 0;

	private MapView mapveiw;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private MyItemizedOverlay itemizedOverlay;
	private OverlayItem overlayItem = null;
	private MapController mc;

	private TableRow curl_to_lsit;
	private ImageButton mapImgBtnBack, mapImgBtnBrandName, mapImgBtnFacebook,
			mapImgBtnTwitter;

	// Brands disclaimer screen
	
	public double deviceHeight;

	private ImageButton cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;

	public PopularBrandsListWithSpecificBrands(Activity ins,
			ViewFlipper fliper, List<ShopList> shopListFinal) {

		this.ins = ins;
		this.mainFliper = fliper;
		this.shopListFinal = shopListFinal;
		PrintLog.getErrorLog("shopListFinal.size() in sublist  :",
				shopListFinal.size() + "");
		gps = new GPSTracker(ins);
		if (gps.canGetLocation())

		{
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

		}

		db = new DatabaseHandler(ins);
		fs = db.getAllShops();

		allShopInfo();
	}

	private void allShopInfo() {

		mainFliper.setDisplayedChild(4);

		allShopImgBtnBack = (ImageButton) ins.findViewById(R.id.all_shop_back);
		allShopImgBtnBack.setOnClickListener(this);

		allShopImgBtnBrandName = (ImageButton) ins
				.findViewById(R.id.all_shop_brand_names);
		allShopImgBtnBrandName.setOnClickListener(this);

		allShopImgBtnFacebook = (ImageButton) ins
				.findViewById(R.id.all_shop_facebook);
		allShopImgBtnFacebook.setOnClickListener(this);

		allShopImgBtnTwitter = (ImageButton) ins
				.findViewById(R.id.all_shop_twitter);
		allShopImgBtnTwitter.setOnClickListener(this);

		cityImgBtnBack = (ImageButton) ins.findViewById(R.id.city_brand_back_c);
		cityImgBtnBack.setOnClickListener(this);

		cityImgBtnFacebook = (ImageButton) ins
				.findViewById(R.id.city_brand_facebook_c);
		cityImgBtnFacebook.setOnClickListener(this);

		cityImgBtnTwitter = (ImageButton) ins
				.findViewById(R.id.city_brand_twitter_c);
		cityImgBtnTwitter.setOnClickListener(this);

		cityImgBtnBrandName = (ImageButton) ins
				.findViewById(R.id.city_brand_names_c);
		cityImgBtnBrandName.setOnClickListener(this);

		showMap = (Button) ins.findViewById(R.id.btnShowMap);
		showMap.setOnClickListener(this);

		verticalBar = (VerticalSeekBar) ins.findViewById(R.id.all_shop_seekbar);
		verticalBar.setOnSeekBarChangeListener(this);
		hits = (TextView) ins.findViewById(R.id.vertical_distance_counter);
		param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		WindowManager w = ins.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		deviceHeight = metrics.heightPixels;
		
		Log.w("deviceHeight", deviceHeight+"PX");
		
		param.topMargin = 397;  //<< why is that 397???
		
		if(deviceHeight>1100)
			param.topMargin += 107;
		
		hits.setLayoutParams(param);

		verticalBar.setProgress(9);

		if (shopListFinal.size() < 1)

		{
			hits.setText("0" + " hit");
		}

		else if (shopListFinal.size() == 1)

		{

			hits.setText(shopListFinal.size() + " hit");
		}

		if (shopListFinal.size() > 1)

		{

			hits.setText(shopListFinal.size() + " hits");
		}

		all_shop_list = (ListView) ins.findViewById(R.id.all_shop_listview);
		all_shop_list.setDivider(null);
		all_shop_list.setDividerHeight(0);
		adapter = new AllShopWithSpecificBrandAdapter(ins);
		all_shop_list.setAdapter((ListAdapter) adapter);
		all_shop_list.invalidate();
		//ListWaveAnimation.showTheWave(ins, all_shop_list);

		getMapInfo();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.all_shop_back:
			Allconstants.strFavBack = "favMain";
			mainFliper.setDisplayedChild(5);

			break;

		case R.id.all_shop_brand_names:
			Allconstants.strFavBack = "favListBr";
			mainFliper.setDisplayedChild(13);
			strScreen = "list";
			break;

		case R.id.all_shop_facebook:
			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		case R.id.all_shop_twitter:
			Allconstants.browseUrl(ins, AllUrl.twitterUrl);
			break;

		case R.id.btnShowMap:
			// mainFliper.setDisplayedChild(12);
			// ToastShow.getMessage(ins, "Show map");
			if (size == 0)

			{
				ToastShow.getMessage(ins, "No lat/lon found to show map");
			}

			else {
				Allconstants.screenShopMap = "popular";
				showMap();
				Allconstants.strFavBack = "favListMap";
			}

			break;

		// map screen
		case R.id.table_row_map_curl:
			Allconstants.strFavBack = "favList";
			mainFliper.setDisplayedChild(4);
			break;

		case R.id.map_view_back:
			Allconstants.strFavBack = "favList";
			mainFliper.setDisplayedChild(4);
			break;

		case R.id.map_view_brand_names:
			strScreen = "map";
			Allconstants.strFavBack = "favListMapBr";
			mainFliper.setDisplayedChild(13);
			break;

		case R.id.map_view_facebook:
			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		case R.id.map_view_twitter:
			Allconstants.browseUrl(ins, AllUrl.twitterUrl);
			break;

		// Brands disclaimer screen

		case R.id.city_brand_back_c:

			if (strScreen.equalsIgnoreCase("list"))

			{
				mFlp = (ViewFlipper) ins.findViewById(R.id.flpallbrand);
				mFlp.setDisplayedChild(4);
				Allconstants.strFavBack = "favList";
			}

			else if (strScreen.equalsIgnoreCase("map"))

			{
				Allconstants.strFavBack = "favListMap";
				mFlp = (ViewFlipper) ins.findViewById(R.id.flpallbrand);
				mFlp.setDisplayedChild(12);

			}

			break;

		case R.id.city_brand_facebook_c:
			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_names_c:

			if (strScreen.equalsIgnoreCase("list"))

			{
				mFlp = (ViewFlipper) ins.findViewById(R.id.flpallbrand);
				mFlp.setDisplayedChild(4);
				Allconstants.strFavBack = "favList";
			}

			else if (strScreen.equalsIgnoreCase("map"))

			{
				Allconstants.strFavBack = "favListMap";
				mFlp = (ViewFlipper) ins.findViewById(R.id.flpallbrand);
				mFlp.setDisplayedChild(12);

			}
			break;

		case R.id.city_brand_twitter:
			Allconstants.browseUrl(ins, AllUrl.twitterUrl);
			break;

		}

	}

	@SuppressLint("DefaultLocale")
	private class AllShopWithSpecificBrandAdapter extends BaseAdapter {

		public AllShopWithSpecificBrandAdapter(Context con) {

			con = ins;

		}

		@Override
		public int getCount() {

			return shopListFinal.size();

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
				final LayoutInflater vi = (LayoutInflater) ins
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_all_shops, null);
			}

			if (position < shopListFinal.size()) {
				double dis = 0;
				String strDistance = "";
				spl = shopListFinal.get(position);
				final TextView shop_title = (TextView) v
						.findViewById(R.id.row_all_shop_title);
				
				String name = "";

				
				if(spl.getShop_name().length()>24)
				{
					name = spl.getShop_name().toString();
					shop_title.setText(name);
				}
				else
				{
					shop_title.setText(spl.getShop_name());
				}

				shop_title.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						String textLeft = shopListFinal.get(position)
								.getShop_name().trim();
						if (textLeft.length() > 24) {
							Animation animationToLeft = new TranslateAnimation(
									shop_title.getWidth(), -shop_title
											.getWidth(), 0, 0);
							animationToLeft.setDuration(5000);
							animationToLeft.setRepeatMode(0);
							animationToLeft.setRepeatCount(0);

							shop_title.setText(textLeft);
							shop_title.setAnimation(animationToLeft);
							// ToastShow.getMessage(ins,
							// shop_title.getWidth()+"<>");
							return true;
						}
						return false;

					}
				});
				
				shop_title.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						spl = shopListFinal.get(position);
						// Allconstants.shopList = shopList;
						// Allconstants.mainFliper = mainFliper;
						// Allconstants.activity = MostPopularActivity.this;
						Allconstants.spl = shopListFinal.get(position);
						Allconstants.shopListFinal = shopListFinal;

						PrintLog.getWarnLog("Allconstants.shopList",
								Allconstants.shopList.size() + "<><>");

						shopIdForHit = spl.getId().trim();

						new BrandsDetailsWithSpecificBrands(ins, mainFliper, spl,
								"list");
						Allconstants.strFavBack = "favDetailsFromList";

						new ShopHitIncrease().execute();
					}
				});

				final TextView distance = (TextView) v
						.findViewById(R.id.row_all_shop_distance);
				try {
					dis = Double.parseDouble(spl.getDistance());
					strDistance = String.format("%.2f", dis);
					PrintLog.getWarnLog("double dis : ", dis + "");
				} catch (Exception e) {
					// TODO: handle exception
				}

				distance.setText(ins.getString(R.string.distance_calculation)
						+ " " + strDistance + " km.");


				final ImageButton showWay = (ImageButton) v
						.findViewById(R.id.row_all_shop_arrow_icon);
				showWay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							lat = Double.parseDouble(shopListFinal
									.get(position).getLatitude().trim());
							lon = Double.parseDouble(shopListFinal
									.get(position).getLongitude().trim());
						} catch (Exception e) {
							// TODO: handle exception
						}

						
						if(!SharePreferenceUtil.isOnline(ins))
						{
							ToastShow.getMessage(ins, "No internet available");
							return;
						}
						Allconstants.drawRoute(ins, lat,
								lon, latitude, longitude);
					}
				});

				final ImageButton fav_icon = (ImageButton) v
						.findViewById(R.id.row_all_shop_favorite_icon);
				for (int i = 0; i < fs.size(); i++) {
					if (fs.get(i).getShop_id()
							.equals(shopListFinal.get(position).getId())) {
						fav_icon.setBackgroundResource(R.drawable.list_favorite_selected);
					}
				}
				fav_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						fs = db.getAllShops();

						String strId = "";
						for (int i = 0; i < fs.size(); i++) {

							if (fs.get(i)
									.getShop_id()
									.equalsIgnoreCase(
											shopListFinal.get(position).getId())) {
								strId = fs.get(i).getShop_id();
								PrintLog.getErrorLog("ID: ", strId
										+ " from database in if block");
								break;
							} else {
								strId = "";
								PrintLog.getErrorLog("ID: ", strId
										+ " from database in else block");
							}

						}

						if (strId.equalsIgnoreCase(shopListFinal.get(position)
								.getId())) {
							db.deleteSingleFavoriteShop(shopListFinal.get(
									position).getId());
							fav_icon.setBackgroundResource(R.drawable.list_favorite_normal);
							// flag = true;

						}

						else {
							fav_icon.setBackgroundResource(R.drawable.list_favorite_selected);
							db.addShops(new FavoriteShops(shopListFinal
									.get(position).getId().toString(),
									shopListFinal.get(position).getShop_name()
											.toString(),
									shopListFinal.get(position).getDistance()
											.trim(),
									shopListFinal.get(position).getLatitude()
											.trim(), shopListFinal
											.get(position).getLongitude()
											.trim()));
							// flag = false;

						}

						// fav_icon.setBackgroundResource(R.drawable.list_favorite_selected);
						//
						// db.addShops(new FavoriteShops(spl.getId(), spl
						// .getShop_name(), spl.getDistance(), spl
						// .getLatitude().trim(), spl.getLongitude()
						// .trim()));

					}
				});

			}
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// mainFliper.setDisplayedChild(7);

					spl = shopListFinal.get(position);
					// Allconstants.shopList = shopList;
					// Allconstants.mainFliper = mainFliper;
					// Allconstants.activity = MostPopularActivity.this;
					Allconstants.spl = shopListFinal.get(position);
					Allconstants.shopListFinal = shopListFinal;

					PrintLog.getWarnLog("Allconstants.shopList",
							Allconstants.shopList.size() + "<><>");

					shopIdForHit = spl.getId().trim();

					new BrandsDetailsWithSpecificBrands(ins, mainFliper, spl,
							"list");
					Allconstants.strFavBack = "favDetailsFromList";

					new ShopHitIncrease().execute();

				}
			});

			return v;
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
					+ shopIdForHit.replaceAll(" ", "%20"));
			PrintLog.showHitCounterLog("Hit URL: ", AllUrl.increaseShopHitURL
					+ shopIdForHit.replaceAll(" ", "%20"));
			PrintLog.showLog("shopIdForHit : " + shopIdForHit);

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

	// Map view Screen

	private void showMap() {
		mainFliper.setDisplayedChild(12);
		mapveiw = (MapView) ins.findViewById(R.id.map_view_map);
		mapveiw.setBuiltInZoomControls(true);
		mapOverlays = mapveiw.getOverlays();

		curl_to_lsit = (TableRow) ins.findViewById(R.id.table_row_map_curl);
		curl_to_lsit.setOnClickListener(this);

		mapImgBtnBack = (ImageButton) ins.findViewById(R.id.map_view_back);
		mapImgBtnBack.setOnClickListener(this);

		mapImgBtnBrandName = (ImageButton) ins
				.findViewById(R.id.map_view_brand_names);
		mapImgBtnBrandName.setOnClickListener(this);

		mapImgBtnFacebook = (ImageButton) ins
				.findViewById(R.id.map_view_facebook);
		mapImgBtnFacebook.setOnClickListener(this);

		mapImgBtnTwitter = (ImageButton) ins
				.findViewById(R.id.map_view_twitter);
		mapImgBtnTwitter.setOnClickListener(this);

		// first overlay
		drawable = ins.getResources().getDrawable(R.drawable.list_indicator);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapveiw);
		if (size == 0) {

			ToastShow.getMessage(ins, "No lat/lon found to show map");
			return;

		} else {
			for (final MapLocation p : CurrentMapPoint.getAllPoints(false)) {

				overlayItem = new OverlayItem(p.getPoint(), p.getName(), "");
				itemizedOverlay.addOverlay(overlayItem);

			}
		}

		mapOverlays.add(itemizedOverlay);

		centerOverlays();

	}

	public void centerOverlays() {

		mc = mapveiw.getController();
		mc.setZoom(18);

		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;

		for (final MapLocation item : CurrentMapPoint.getAllPoints(false)) {
			// final int lat = item.getGeoPoint().getLatitudeE6();
			// final int lon = item.getGeoPoint().getLongitudeE6();

			final int lat = item.getPoint().getLatitudeE6();
			final int lon = item.getPoint().getLongitudeE6();

			maxLat = Math.max(lat, maxLat);
			minLat = Math.min(lat, minLat);
			maxLon = Math.max(lon, maxLon);
			minLon = Math.min(lon, minLon);
		}

		mc.zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon - minLon));
		mc.animateTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));

	}

	private void getMapInfo() {

		CurrentMapPoint.removeAllPoints();
		// Size should be AllShopLists.getShopAddress().size()
		for (int i = 0; i < shopListFinal.size(); i++) {

			try {

				address = shopListFinal.get(i).getShop_name() + "\n"
						+ shopListFinal.get(i).getAddress() + "\n"
						+ shopListFinal.get(i).getZip_code();
				maplat = Double.parseDouble(shopListFinal.get(i).getLatitude());
				maplon = Double
						.parseDouble(shopListFinal.get(i).getLongitude());
				// Log.w("Lat : ", latt + "<><><>");
				// Log.w("Lon : ", lon + "");

				MapLocation loc = null;

				try {
					loc = new MapLocation(address, maplat, maplon);

					CurrentMapPoint.setPoint(loc);
					size = CurrentMapPoint.getAllPoints(false).size();
					loc = null;
				} catch (Exception e) {
					// TODO: handle exception

					Log.w("error", e.toString());
				}
				// GeoPoint pt = new GeoPoint(lat, lng);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		size = CurrentMapPoint.getAllPoints(false).size();
		PrintLog.getWarnLog("Size of map vetor : ", size + " map vector");

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		shopListFinal = new ArrayList<ShopList>();

		for (int i = 0; i < Allconstants.shopList.size(); i++)

		{

			double final_distance = Double.parseDouble(Allconstants.shopList
					.get(i).getDistance());

			// PrintLog.getWarnLog("final_distance", final_distance +
			// "<><>");

			if (final_distance <= verticalBarDistance / 1000)

			{

				ShopList sl = new ShopList();

				sl.setBrands_id(Allconstants.shopList.get(i).getBrands_id());
				sl.setBrands_name(Allconstants.shopList.get(i).getBrands_name());
				sl.setShop_image(Allconstants.shopList.get(i).getShop_image());
				sl.setS(Allconstants.shopList.get(i).getS());
				sl.setM_w(Allconstants.shopList.get(i).getM_w());
				sl.setPhone(Allconstants.shopList.get(i).getPhone());
				sl.setZip_code(Allconstants.shopList.get(i).getZip_code());
				// PrintLog.getWarnLog("shopList.get(i).getZip_code()",
				// shopList.get(i).getZip_code());
				sl.setAddress(Allconstants.shopList.get(i).getAddress());
				// PrintLog.getWarnLog("shopList.get(i).getAddress()",
				// shopList.get(i).getAddress());
				sl.setId(Allconstants.shopList.get(i).getId());
				sl.setShop_name(Allconstants.shopList.get(i).getShop_name());
				sl.setDistance(Allconstants.shopList.get(i).getDistance());
				sl.setLatitude(Allconstants.shopList.get(i).getLatitude());
				sl.setLongitude(Allconstants.shopList.get(i).getLongitude());
				sl.setUrl(Allconstants.shopList.get(i).getUrl());
				sl.setEmail(Allconstants.shopList.get(i).getEmail());
				sl.setFacebookUrl(Allconstants.shopList.get(i).getFacebookUrl());
				shopListFinal.add(sl);

			}

		}

		Allconstants.shopListFinal = shopListFinal;

		getMapInfo();

		PrintLog.getWarnLog("ShoplistFinal.size() in seekbar progress ",
				shopListFinal.size() + "<22><22>");

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

		TextView hits = (TextView) ins
				.findViewById(R.id.vertical_distance_counter);
		hits.setLayoutParams(param);
		
		
		
		if (progress < 0) {
			param.topMargin = 445 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress == 0)

		{
			param.topMargin = 442 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress > 0)

		{
			param.topMargin = 442 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress >= 1)

		{
			param.topMargin = 440 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;

		}

		if (progress >= 2)

		{
			param.topMargin = 438 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress >= 3)

		{
			param.topMargin = 437 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress >= 4)

		{
			param.topMargin = 435 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress >= 5)

		{
			param.topMargin = 432 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;
		}

		if (progress >= 7)

		{
			param.topMargin = 425 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;

		}
		if (progress >= 9)

		{
			param.topMargin = 415 - 18;
			hits.setLayoutParams(param);
			verticalBarDistance = 10000 * 1000;

		}
		if (progress >= 11)

		{
			param.topMargin = 405 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 80 * 1000;
		}
		if (progress >= 13)

		{
			param.topMargin = 393 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 80 * 1000;
		}
		if (progress >= 15)

		{
			param.topMargin = 382 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 80 * 1000;
		}
		if (progress >= 17)

		{
			param.topMargin = 370 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 80 * 1000;
		}
		if (progress >= 19)

		{
			param.topMargin = 362 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 70 * 1000;
		}
		if (progress >= 21)

		{
			param.topMargin = 353 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 70 * 1000;

		}
		if (progress >= 23)

		{
			param.topMargin = 345 - 15;
			hits.setLayoutParams(param);
			verticalBarDistance = 70 * 1000;
		}
		if (progress >= 25)

		{
			param.topMargin = 335 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 60 * 1000;
		}
		if (progress >= 27)

		{
			param.topMargin = 320 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 60 * 1000;

		}
		if (progress >= 29)

		{
			param.topMargin = 307 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 60 * 1000;

		}
		if (progress >= 31)

		{
			param.topMargin = 300 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 50 * 1000;
		}
		if (progress >= 33)

		{
			param.topMargin = 292 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 50 * 1000;
		}
		if (progress >= 35)

		{
			param.topMargin = 282 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 50 * 1000;
		}
		if (progress >= 37)

		{
			param.topMargin = 275 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 40 * 1000;
		}
		if (progress >= 39)

		{
			param.topMargin = 268 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 40 * 1000;

		}
		if (progress >= 41)

		{
			param.topMargin = 260 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 40 * 1000;
		}
		if (progress >= 43)

		{
			param.topMargin = 250 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 30 * 1000;
		}
		if (progress >= 45)

		{
			param.topMargin = 240 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 30 * 1000;
		}
		if (progress >= 47)

		{
			param.topMargin = 230 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 30 * 1000;
		}
		if (progress >= 49)

		{
			param.topMargin = 220 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 20 * 1000;
		}
		if (progress >= 51)

		{
			param.topMargin = 210 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 20 * 1000;
		}
		if (progress >= 53)

		{
			param.topMargin = 200 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 20 * 1000;
		}
		if (progress >= 55)

		{
			param.topMargin = 190 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 10 * 1000;
		}
		if (progress >= 57)

		{
			param.topMargin = 180 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 10 * 1000;
		}
		if (progress >= 59)

		{
			param.topMargin = 170 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 10 * 1000;
		}
		if (progress >= 61)

		{
			param.topMargin = 160 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 9 * 1000;
		}
		if (progress >= 63)

		{
			param.topMargin = 150 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 8 * 1000;
		}
		if (progress >= 65)

		{
			param.topMargin = 143 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 7 * 1000;
		}
		if (progress >= 67)

		{
			param.topMargin = 133 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 6 * 1000;
		}
		if (progress >= 71)

		{
			param.topMargin = 120 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 5 * 1000;
		}
		if (progress >= 73)

		{
			param.topMargin = 110 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 5 * 1000;
		}
		if (progress >= 75)

		{
			param.topMargin = 100 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 3 * 1000;
		}
		if (progress >= 77)

		{
			param.topMargin = 90 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 2 * 1000;
		}
		if (progress >= 79)

		{
			param.topMargin = 80 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 1.5 * 1000;
		}
		if (progress >= 81)

		{
			param.topMargin = 70 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 1.5 * 1000;
		}
		if (progress >= 83)

		{
			param.topMargin = 60 - 10;
			hits.setLayoutParams(param);
			verticalBarDistance = 1 * 1000;
		}
		if (progress >= 85)

		{
			param.topMargin = 50 - 10;
			verticalBarDistance = 900;
		}
		if (progress >= 87)

		{
			param.topMargin = 43 - 10;
			verticalBarDistance = 800;
		}
		if (progress >= 89)

		{
			param.topMargin = 33 - 10;
			verticalBarDistance = 700;
		}
		if (progress >= 91)

		{
			param.topMargin = 15 - 10;
			verticalBarDistance = 600;
		}
		if (progress >= 93)

		{
			param.topMargin = 0;
			hits.setLayoutParams(param);
			verticalBarDistance = 500;
		}
		if (progress >= 95)

		{
			param.topMargin = 0;
			verticalBarDistance = 400;
		}

		if (progress >= 97)

		{
			param.topMargin = 0;
			verticalBarDistance = 300;
		}

		if (progress >= 98)

		{
			param.topMargin = -5;
			verticalBarDistance = 200;
		}

		if (progress >= 99)

		{
			param.topMargin = -3;
			verticalBarDistance = 100;
		}
		
		
		Log.w("PROGRESS", progress+" PX");
		
		if(progress <= 10 && deviceHeight>1100)
			param.topMargin += 120;
		else if(progress <= 27 && deviceHeight>1100)
			param.topMargin += 100;
		else if(progress <= 43 && deviceHeight>1100)
			param.topMargin += 70;
		else if(progress <= 70 && deviceHeight>1100)
			param.topMargin += 50;
		else if(progress <= 90 && deviceHeight>1100)
			param.topMargin += 20;
		else if(progress <= 100 && deviceHeight>1100)
			param.topMargin -= 10;
		
		
		hits.setLayoutParams(param);

		if (shopListFinal.size() < 1)

		{
			hits.setText("0" + " hit");
		}

		else if (shopListFinal.size() == 1)

		{

			hits.setText(shopListFinal.size() + " hit");
		}

		else

		{

			hits.setText(shopListFinal.size() + " hits");
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

		// ToastShow.getMessage(ins, "start");
		TextView mh = (TextView) ins
				.findViewById(R.id.vertical_distance_counter);

		if (verticalBarDistance / 1000 >= 90)

		{
			mh.setText("90>" + " km.");
		}

		else {
			mh.setText(verticalBarDistance / 1000 + " km.");
		}

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// ToastShow.getMessage(ins, "stop");
		// TextView mh =
		// (TextView)ins.findViewById(R.id.vertical_distance_counter);
		// mh.setText(verticalBarDistance + " m.");
	}

}
