package com.shehala.citybrands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.shehala.citybrands.holder.AllMostPopularBrands;
import com.shehala.citybrands.holder.AllMostPopularShop;
import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.model.MostPopularBrand;
import com.shehala.citybrands.model.MostPopularShop;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.parser.MostPopularBrandParser;
import com.shehala.citybrands.parser.MostPopularShopParser;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteBrands;
import com.shehala.citybrands.sqlite.FavoriteShops;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.ListWaveAnimation;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;

@SuppressLint("DefaultLocale")
public class MostPopularActivity extends MapActivity implements OnClickListener {

	private ViewFlipper mainFliper;
	private Context con;
	private ImageButton imgBtnBrandName, imgBtnFacebook, imgBtnTwitter,
			imgBtnBack;
	private Switch mToggleBtn;
	private String strBrandOrShop = "Brand";
	private ListView mostPopularList;
	private ProgressDialog pd;
	private String response = "", res = "";
	private int pos = 0;
	private List<ShopList> shopList;
	private List<ShopList> shopListFinal;
	private String brandId = "", shopId = "";
	
	private List<FavoriteBrands> fb;
	private List<FavoriteShops> fs;
	private String strShopAndBrandTitle;
	

	private Handler mHandler = new Handler();
	private static int duration = 1;

	// private MostPolularBrandsAdapter adapter;

	private ImageButton cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;

	private DatabaseHandler dbHnadler;

	private GPSTracker gps;
	private double latitude = 0, longitude = 0, lat = 0, lon = 0;

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

		dbHnadler = new DatabaseHandler(con);

		Allconstants.strFavBack = "favMain";

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.w("Inisde onResume at ", this+"");
		
		if(SharePreferenceUtil.getEmailPressed(con)){
			SharePreferenceUtil.setEmailPressed(con, false);
			return;
		}

		if (SharePreferenceUtil.getLoginStatus(con).equalsIgnoreCase("logout")) {
			mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
			mainFliper.setDisplayedChild(18);
			mHandler.postDelayed(mRunnable, duration);
			
		} else {

			fb = dbHnadler.getAllContacts();
			fs = dbHnadler.getAllShops();
			initUI();
			Allconstants.strFavBack = "favMain";
		}

		MyTabActivity.getInstance().pushIndex(2);

	}

	private void initUI() {

		mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
		mainFliper.setDisplayedChild(5);

		imgBtnBack = (ImageButton) findViewById(R.id.most_popular_back);
		imgBtnBack.setOnClickListener(this);

		imgBtnFacebook = (ImageButton) findViewById(R.id.most_popular_facebook);
		imgBtnFacebook.setOnClickListener(this);

		imgBtnTwitter = (ImageButton) findViewById(R.id.most_popular_twitter);
		imgBtnTwitter.setOnClickListener(this);

		imgBtnBrandName = (ImageButton) findViewById(R.id.most_popular_brand_names);
		imgBtnBrandName.setOnClickListener(this);

		mToggleBtn = (Switch) findViewById(R.id.most_popular_toggle_btn);
		mToggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (buttonView.isChecked())

				{
					strBrandOrShop = "Shop";
					mostPopularList = (ListView) findViewById(R.id.most_popular_listview);
					// mostPopularList.setDivider(null);
					// mostPopularList.setDividerHeight(0);
					mostPopularList.setAdapter(new MostPopularAdapter());
					//ListWaveAnimation.showTheWave(con, mostPopularList);
					PrintLog.getDebugLog("BrandOrShop in sliding?",
							strBrandOrShop);

				}

				else if (!buttonView.isChecked())

				{

					strBrandOrShop = "Brand";
					mostPopularList = (ListView) findViewById(R.id.most_popular_listview);
					// mostPopularList.setDivider(null);
					// mostPopularList.setDividerHeight(0);
					mostPopularList.setAdapter(new MostPopularAdapter());
					ListWaveAnimation.showTheWave(con, mostPopularList);
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
		
		if(!SharePreferenceUtil.isOnline(con))
		{
			ToastShow.getMessage(MostPopularActivity.this, "No internet available");
			return;
		}

		new ShowMostPopularBrands().execute();

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

		case R.id.most_popular_back:
			if (MyTabActivity.getInstance() != null) {
				MyTabActivity.getInstance().openTab();
			}
			break;

		case R.id.most_popular_facebook:

			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.most_popular_twitter:

			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.most_popular_brand_names:
			Allconstants.strFavBack = "favBr";
			mainFliper.setDisplayedChild(8);

			break;

		// city_brand_logo screen

		case R.id.city_brand_back:
			mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
			mainFliper.setDisplayedChild(5);
			Allconstants.strFavBack = "favMain";

			break;

		case R.id.city_brand_facebook:

			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_names:
			mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
			mainFliper.setDisplayedChild(5);
			Allconstants.strFavBack = "favMain";

			break;

		case R.id.city_brand_twitter:

			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;
		}

	}

	/***
	 * 
	 * Show most popular brands list
	 * 
	 */

	private class ShowMostPopularBrands extends
			AsyncTask<String, String, String> {
		HttpPost popularBrandHttpPost;
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {

			popularBrandHttpPost = new HttpPost(AllUrl.mostPopularBrands);
			try {
				res = MyHTTPRequest.getTextFromXmlOrJson(MyHTTPRequest
						.getInputStreamForGetRequest(AllUrl.getMostPopularShop(
								String.valueOf(latitude),
								String.valueOf(longitude))));

			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (URISyntaxException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			
			PrintLog.getDebugLog("Response of popular shop : ", res);

			try {
				response = MyHTTPRequest.getData(popularBrandHttpPost);
				// PrintLog.getDebugLog("Response of all brands : ", response);

			} catch (IOException e) {

				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				if (MostPopularShopParser.ParseQuery(con, res)) {

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				if (MostPopularBrandParser.connect(con, response)) {

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();

				// adapter = new MostPolularBrandsAdapter(con);

				mostPopularList = (ListView) findViewById(R.id.most_popular_listview);
				// mostPopularList.setDivider(null);
				// mostPopularList.setDividerHeight(0);
				mostPopularList.setAdapter(new MostPopularAdapter());
				//ListWaveAnimation.showTheWave(con, mostPopularList);

				
			}
		}
	}

	private class MostPopularAdapter extends BaseAdapter {
		private MostPopularBrand brl;
		private MostPopularShop spl;

		@Override
		public int getCount() {

			if (AllMostPopularBrands.getAllPopularBrands().size() >= 10) {
				return 10;
			}

			else {
				return (AllMostPopularBrands.getAllPopularBrands().size());
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
				v = vi.inflate(R.layout.row_most_popular, null);
			}

			
			if (strBrandOrShop.equalsIgnoreCase("Brand")) {
				if (position < AllMostPopularBrands.getAllPopularBrands()
						.size()) {
					
					brl = AllMostPopularBrands.getPopularBrands(position);

					final TextView number = (TextView) v
							.findViewById(R.id.row_most_popular_number);

					number.setText(position + 1 + "");

					final TextView brand_title = (TextView) v
							.findViewById(R.id.row_most_popular_title);
					brand_title.setTextSize(14);

					if (brl.getBrand_name().length() > 24) {
						strShopAndBrandTitle = brl.getBrand_name().toString()
								.substring(0, 24);
						brand_title.setText(strShopAndBrandTitle);

					}

					else {
						strShopAndBrandTitle = brl.getBrand_name().toString()
								.trim();
						brand_title.setText(strShopAndBrandTitle);
					}

					brand_title
							.setOnLongClickListener(new OnLongClickListener() {

								@Override
								public boolean onLongClick(View v) {
									// TODO Auto-generated method stub
									String textLeft = AllMostPopularBrands
											.getAllPopularBrands()
											.elementAt(position)
											.getBrand_name().trim();
									if (textLeft.length() > 24) {
										Animation animationToLeft = new TranslateAnimation(
												brand_title.getWidth(),
												-brand_title.getWidth(), 0, 0);
										animationToLeft.setDuration(5000);
										animationToLeft.setRepeatMode(0);
										animationToLeft.setRepeatCount(0);

										brand_title.setText(textLeft);
										brand_title
												.setAnimation(animationToLeft);
									
										return true;
									}
									return false;

								}
							});
					brand_title.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showShopList(position);
						}
					});

					final TextView distance = (TextView) v
							.findViewById(R.id.row_most_popular_distance);
					distance.setTextSize(2);
					distance.setVisibility(View.INVISIBLE);

					final ImageButton btn_fav = (ImageButton) v
							.findViewById(R.id.row_most_popular_favorite_icon);
					for (int j = 0; j < fb.size(); j++) {
						if (fb.get(j).getBrand_id()
								.equalsIgnoreCase(brl.getId())) {
							btn_fav.setBackgroundResource(R.drawable.list_favorite_selected);
						}

					}

					btn_fav.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							fb = dbHnadler.getAllContacts();

							String strId = "";
							for (int i = 0; i < fb.size(); i++) {

								if (fb.get(i)
										.getBrand_id()
										.equalsIgnoreCase(
												AllMostPopularBrands
														.getAllPopularBrands()
														.elementAt(position)
														.getId())) {
									strId = fb.get(i).getBrand_id();
									PrintLog.getErrorLog("ID: ", strId
											+ " from database in if block");
									break;
								} else {
									strId = "";
									PrintLog.getErrorLog("ID: ", strId
											+ " from database in else block");
								}

							}

							if (strId.equalsIgnoreCase(AllMostPopularBrands
									.getAllPopularBrands().elementAt(position)
									.getId())) {
								dbHnadler
										.deleteSingleFavoriteBrand(AllMostPopularBrands
												.getAllPopularBrands()
												.elementAt(position).getId());
								btn_fav.setBackgroundResource(R.drawable.list_favorite_normal);
								

							}

							else {
								btn_fav.setBackgroundResource(R.drawable.list_favorite_selected);
								dbHnadler.addBrands(new FavoriteBrands(
										AllMostPopularBrands
												.getAllPopularBrands()
												.elementAt(position).getId(),
										AllMostPopularBrands
												.getAllPopularBrands()
												.elementAt(position)
												.getBrand_name(),
										"Distance 2 km."));
								
							}

							

						}
					});
					

				}
			} else if (strBrandOrShop.equalsIgnoreCase("Shop")) {
				if (position < AllMostPopularShop.getAllPopularShops().size()) {
					spl = AllMostPopularShop.getPopularShops(position);

					final TextView number = (TextView) v
							.findViewById(R.id.row_most_popular_number);

					number.setText(position + 1 + "");

					final TextView brand_title = (TextView) v
							.findViewById(R.id.row_most_popular_title);

					if (spl.getShop_name().toString().length() > 24) {
						strShopAndBrandTitle = spl.getShop_name().toString()
								.substring(0, 24);
						brand_title.setText(strShopAndBrandTitle);

					}

					else {
						brand_title.setText(spl.getShop_name().toString()
								.trim());
					}

					brand_title
							.setOnLongClickListener(new OnLongClickListener() {

								@Override
								public boolean onLongClick(View v) {
									// TODO Auto-generated method stub
									String textLeft = AllMostPopularShop
											.getAllPopularShops()
											.elementAt(position).getShop_name()
											.trim();
									if (textLeft.length() > 24) {
										Animation animationToLeft = new TranslateAnimation(
												brand_title.getWidth(),
												-brand_title.getWidth(), 0, 0);
										animationToLeft.setDuration(5000);
										animationToLeft.setRepeatMode(0);
										animationToLeft.setRepeatCount(0);

										brand_title.setText(textLeft);
										brand_title
												.setAnimation(animationToLeft);
										// ToastShow.getMessage(ins,
										// shop_title.getWidth()+"<>");
										return true;
									}
									return false;

								}
							});

					brand_title.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showShopDetaisl(position);

						}
					});

					final TextView distance = (TextView) v
							.findViewById(R.id.row_most_popular_distance);
					double dis = 0;
					String strDistance = "";
					try {
						dis = Double.parseDouble(spl.getDistance().trim());
						strDistance = String.format("%.2f", dis);
						// PrintLog.getWarnLog("double dis : ", dis + "");
					} catch (Exception e) {
						// TODO: handle exception
					}

					distance.setText(getString(R.string.distance_calculation)
							+ " " + strDistance + " km.");

					final ImageButton showWay = (ImageButton) v
							.findViewById(R.id.row_most_popular_arrow_icon);
					showWay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MostPopularShop mps = AllMostPopularShop
									.getAllPopularShops().elementAt(position);

							try {
								lat = Double.parseDouble(mps.getLatitude()
										.trim());
								lon = Double.parseDouble(mps.getLongitude()
										.trim());
							} catch (Exception e) {
								// TODO: handle exception
							}

							if(!SharePreferenceUtil.isOnline(con))
							{
								ToastShow.getMessage(MostPopularActivity.this, "No internet available");
								return;
							}
							
							Allconstants.drawRoute(MostPopularActivity.this,
									lat, lon, latitude, longitude);
						}
					});

					final ImageButton btn_fav = (ImageButton) v
							.findViewById(R.id.row_most_popular_favorite_icon);
					for (int k = 0; k < fs.size(); k++) {
						if (fs.get(k).getShop_id()
								.equalsIgnoreCase(spl.getId())) {
							btn_fav.setBackgroundResource(R.drawable.list_favorite_selected);
						}
					}
					// btn_fav.setBackgroundResource(R.drawable.list_favorite_normal);
					btn_fav.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							fs = dbHnadler.getAllShops();

							String strId = "";
							for (int i = 0; i < fs.size(); i++) {

								if (fs.get(i)
										.getShop_id()
										.equalsIgnoreCase(
												AllMostPopularShop
														.getAllPopularShops()
														.elementAt(position)
														.getId())) {
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

							if (strId.equalsIgnoreCase(AllMostPopularShop
									.getAllPopularShops().elementAt(position)
									.getId())) {
								dbHnadler
										.deleteSingleFavoriteShop(AllMostPopularShop
												.getAllPopularShops()
												.elementAt(position).getId());
								btn_fav.setBackgroundResource(R.drawable.list_favorite_normal);
							
							}

							else {

								btn_fav.setBackgroundResource(R.drawable.list_favorite_selected);

								MostPopularShop mps = AllMostPopularShop
										.getAllPopularShops().elementAt(
												position);

								PrintLog.getWarnLog("Reading: ",
										"Reading all contacts..");
								dbHnadler.addShops(new FavoriteShops(mps
										.getId(), mps.getShop_name(), mps
										.getDistance().trim(), mps
										.getLatitude().trim(), mps
										.getLongitude().trim()));
								
							}

						}
					});

				}
			}

			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (strBrandOrShop.equalsIgnoreCase("Shop"))

					{
						showShopDetaisl(position);
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

	private void showShopList(int pos)

	{
		String b_id = AllMostPopularBrands.getAllPopularBrands().elementAt(pos)
				.getBrand_name().toString();
		brandId = AllMostPopularBrands.getAllPopularBrands().elementAt(pos)
				.getId();

		shopList = AllShopLists.getAllShopsInfoByBrandName(b_id);

		PrintLog.getWarnLog("shopList.size()", shopList.size() + "<Got><it>");
		shopListFinal = new ArrayList<ShopList>();

		for (int i = 0; i < shopList.size(); i++)

		{

			double final_distance = Double.parseDouble(shopList.get(i)
					.getDistance());

			PrintLog.getWarnLog("final_distance", final_distance + "<><>");

			if (final_distance >= 91)

			{

				ShopList sl = new ShopList();

				sl.setBrands_id(shopList.get(i).getBrands_id());
				sl.setBrands_name(shopList.get(i).getBrands_name());
				sl.setShop_image(shopList.get(i).getShop_image());
				sl.setS(shopList.get(i).getS());
				sl.setM_w(shopList.get(i).getM_w());
				sl.setPhone(shopList.get(i).getPhone());
				sl.setZip_code(shopList.get(i).getZip_code());
				// PrintLog.getWarnLog("shopList.get(i).getZip_code()",
				// shopList.get(i).getZip_code());
				sl.setAddress(shopList.get(i).getAddress());
				// PrintLog.getWarnLog("shopList.get(i).getAddress()",
				// shopList.get(i).getAddress());
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

		PrintLog.getWarnLog("shopListFinal.size()", shopListFinal.size()
				+ " in view click");
		// ShopList sl = shopListFinal.get(position);

		Allconstants.shopList = shopList;
		Allconstants.mainFliper = mainFliper;
		Allconstants.activity = MostPopularActivity.this;
		// Allconstants.spl = shopListFinal.get(position);
		Allconstants.shopListFinal = shopListFinal;

		PrintLog.getWarnLog("Allconstants.shopList",
				Allconstants.shopList.size() + "<><>");
		new PopularBrandsListWithSpecificBrands(MostPopularActivity.this,
				mainFliper, shopListFinal);
		new BrandHitIncrease().execute();
		PrintLog.getErrorLog("b_id : ", b_id);
		Allconstants.strForBackButton = "specificBrandList";
		Allconstants.strFavBack = "favList";

	}

	private void showShopDetaisl(int poss) {
		shopId = AllMostPopularShop.getAllPopularShops().elementAt(pos).getId()
				.trim();
		pos = poss;
		PrintLog.getWarnLog("Position : ", poss + "");

		new PopularBrandsDetailsWithSpecificBrands(MostPopularActivity.this,
				mainFliper, pos);
		Allconstants.strForBackButton = "specificBrandDetails";
		Allconstants.strFavBack = "favList";
		new ShopHitIncrease().execute();
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

			PrintLog.showHitCounterLog("Hit url :", AllUrl.increaseBrandHitURL
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

			// PrintLog.showLog("Brand Id for hit : " + shopId);

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
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (Allconstants.strFavBack.equalsIgnoreCase("favMain")) {
				if (MyTabActivity.getInstance() != null)

				{
					MyTabActivity.getInstance().openTab();
					Allconstants.strFavBack = "";
				}

				return true;
			}

			else if (Allconstants.strFavBack.equalsIgnoreCase("favBr")) {
				mainFliper.setDisplayedChild(5);
				Allconstants.strFavBack = "favMain";
				return true;
			}

			else if (Allconstants.strFavBack.equalsIgnoreCase("favList")) {
				mainFliper.setDisplayedChild(5);
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

			else

			{
				if (MyTabActivity.getInstance() != null)

				{
					MyTabActivity.getInstance().pushIndex(2);
					MyTabActivity.getInstance().openTab();
					Allconstants.strFavBack = "";
				}
				return true;

			}

		}

		return false;
	};

	private final Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (MyTabActivity.getInstance() != null) {
				MyTabActivity.getInstance().openTab();
			}

		}
	};
	
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

}
