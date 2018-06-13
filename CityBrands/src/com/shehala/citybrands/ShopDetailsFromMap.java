package com.shehala.citybrands;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.shehala.citybrands.R;
import com.shehala.citybrands.download.image.ImageLoader;
import com.shehala.citybrands.gpstracking.GPSTracker;
import com.shehala.citybrands.holder.AllMostPopularShop;
import com.shehala.citybrands.model.MostPopularShop;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteShops;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.BaseUrl;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;

public class ShopDetailsFromMap implements OnClickListener {

	private ViewFlipper mainFliper;
	private Activity ins;
	// shop details screen

	private Vector<String> allBrandsId = new Vector<String>();
	private Vector<String> brandsOfShop = new Vector<String>();

	private ImageButton shopDetailsImgBtnBack, shopDetailsImgBtnBrandName,
			shopDetailsImgBtnFacebook, shopDetailsImgBtnTwitter;
	private ImageView profile_photo;
	private ImageButton shopDetailsImgBtnWeb, shopDetailsImgBtnPhone,
			shopDetailsImgBtnFace, shopDetailsImgBtnEmail,
			shopDetailsImgBtnShowWay, shopDetailsImgBtnFavorite;
	
	private TextView txtAddress, txtZipCode, txtPhone, txtOpeningHourMonFri,
			txtOpeningHourSat, txtShopTitle, txtShopDistance;
	private MostPopularShop mps;
	private int pos = 0;

	private double lat, lng, latitude, longitude;
	private GPSTracker gps;

	private String strDistance = "";
	private int distanceInKm, distanceInM;
	private DatabaseHandler db;
	private List<FavoriteShops> fs;
	private String id = "";
	private ImageLoader loadImage;

	// Brands disclaimer screen

	private ImageButton cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;

	public ShopDetailsFromMap(Activity ins,
			ViewFlipper fliper, int i) {

		this.ins = ins;
		this.mainFliper = fliper;
		this.pos = i;
		mainFliper.setDisplayedChild(7);

		gps = new GPSTracker(ins);

		loadImage = new ImageLoader(ins);

		mps = AllMostPopularShop.getAllPopularShops().elementAt(pos);
		if (gps.canGetLocation())

		{
			lat = gps.getLatitude();
			lng = gps.getLongitude();

			// PrintLog.getWarnLog("Current Lat : ", lat + "<><>");
			// PrintLog.getWarnLog("Current Lon : ", lng + "<><>");
		}

	

		strDistance = mps.getDistance().trim();
		double dis = 0;
		try {
			dis = Double.parseDouble(strDistance);
			latitude = Double.parseDouble(mps.getLatitude().trim());
			longitude = Double.parseDouble(mps.getLongitude().trim());

			// PrintLog.getWarnLog("double dis : ", dis + "");
		} catch (Exception e) {
			// TODO: handle exception
		}

		distanceInKm = (int) Math.ceil(dis);
		distanceInM = distanceInKm * 1000;

		shopDetailsInfo(pos);

	}

	private void shopDetailsInfo(final int pos) {

		profile_photo = (ImageView) ins
				.findViewById(R.id.shop_details_profile_photo);

		if (mps.getShop_image().length() != 0)

		{
			loadImage.displayImage(BaseUrl.baseUrl
					+ mps.getShop_image().trim().replaceAll(" ", "%20"),
					profile_photo);
		}

		cityImgBtnBack = (ImageButton) ins.findViewById(R.id.city_brand_back);
		cityImgBtnBack.setOnClickListener(this);

		cityImgBtnFacebook = (ImageButton) ins
				.findViewById(R.id.city_brand_facebook);
		cityImgBtnFacebook.setOnClickListener(this);

		cityImgBtnTwitter = (ImageButton) ins
				.findViewById(R.id.city_brand_twitter);
		cityImgBtnTwitter.setOnClickListener(this);

		cityImgBtnBrandName = (ImageButton) ins
				.findViewById(R.id.city_brand_names);
		cityImgBtnBrandName.setOnClickListener(this);

		shopDetailsImgBtnBack = (ImageButton) ins
				.findViewById(R.id.shop_details_back);
		shopDetailsImgBtnBack.setOnClickListener(this);

		shopDetailsImgBtnBrandName = (ImageButton) ins
				.findViewById(R.id.shop_details_brand_names);
		shopDetailsImgBtnBrandName.setOnClickListener(this);

		shopDetailsImgBtnFacebook = (ImageButton) ins
				.findViewById(R.id.shop_details_facebook);
		shopDetailsImgBtnFacebook.setOnClickListener(this);

		shopDetailsImgBtnTwitter = (ImageButton) ins
				.findViewById(R.id.shop_details_twitter);
		shopDetailsImgBtnTwitter.setOnClickListener(this);

		shopDetailsImgBtnFavorite = (ImageButton) ins
				.findViewById(R.id.shop_details_show_way_favorite_icon);

		db = new DatabaseHandler(ins);
		fs = db.getAllShops();
		for (int i = 0; i < fs.size(); i++) {
			id = fs.get(i).getShop_id();
			if (id.equals(mps.getId()))

			{
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_selected);

				break;
			}

			else

			{
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_normal);
			}

		}

		shopDetailsImgBtnFavorite.setOnClickListener(this);

		shopDetailsImgBtnShowWay = (ImageButton) ins
				.findViewById(R.id.shop_details_show_way);
		shopDetailsImgBtnShowWay.setOnClickListener(this);

		shopDetailsImgBtnWeb = (ImageButton) ins
				.findViewById(R.id.shop_details_web);
		shopDetailsImgBtnWeb.setOnClickListener(this);

		shopDetailsImgBtnPhone = (ImageButton) ins
				.findViewById(R.id.shop_details_phone_btn);
		shopDetailsImgBtnPhone.setOnClickListener(this);

		shopDetailsImgBtnEmail = (ImageButton) ins
				.findViewById(R.id.shop_details_message);
		shopDetailsImgBtnEmail.setOnClickListener(this);

		shopDetailsImgBtnFace = (ImageButton) ins
				.findViewById(R.id.shop_details_facebook_btn);
		shopDetailsImgBtnFace.setOnClickListener(this);

		txtAddress = (TextView) ins.findViewById(R.id.shop_details_address);
		txtAddress.setText(mps.getAddress().toString().trim() + "");

		txtZipCode = (TextView) ins.findViewById(R.id.shop_details_zip_code);
		txtZipCode.setText(mps.getZip_code().toString().trim() + "");

		txtPhone = (TextView) ins.findViewById(R.id.shop_details_phone);
		txtPhone.setText(mps.getPhone().toString().trim() + "");

		txtOpeningHourMonFri = (TextView) ins
				.findViewById(R.id.shop_details_opening_hour_fri_mon);
		txtOpeningHourMonFri.setText(ins.getString(R.string.mon_fri) + " "
				+ mps.getM_w().toString().trim() + "");

		txtOpeningHourSat = (TextView) ins
				.findViewById(R.id.shop_details_opening_hour_sat_day);
		txtOpeningHourSat.setText(ins.getString(R.string.sat) + " "
				+ mps.getS().toString().trim() + "");

		txtShopTitle = (TextView) ins
				.findViewById(R.id.shop_details_shop_title);
		txtShopTitle.setText(mps.getShop_name().toString().trim() + "");

		txtShopDistance = (TextView) ins
				.findViewById(R.id.shop_details_distance);

		if (distanceInM >= 1000) {
			int finalDistance = distanceInM / 1000;
			txtShopDistance.setText(ins
					.getString(R.string.distance_calculation)
					+ " "
					+ finalDistance + " km.");
		} else

		{
			txtShopDistance.setText(ins
					.getString(R.string.distance_calculation)
					+ " "
					+ distanceInM + " m.");
		}

		// To split comma and add to vector

		allBrandsId.removeAllElements();

		StringTokenizer st = new StringTokenizer(mps.getBrands_name(), ",");
		while (st.hasMoreTokens()) {

			if (allBrandsId.size() == 0)

			{
				brandsOfShop.addElement(ins.getString(R.string.see_allbrand));
			}
			allBrandsId.addElement(st.nextToken());

			// System.err.println(st.nextToken());
		}

		brandsOfShop.removeAllElements();

		for (int i = 0; i < allBrandsId.size(); i++)

		{
			String name = allBrandsId.elementAt(i);
			// PrintLog.getWarnLog("Name of brand ", name);
			if (brandsOfShop.size() == 0)

			{
				brandsOfShop.addElement(ins.getString(R.string.see_allbrand));
			}

			brandsOfShop.addElement(name);

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.shop_details_back:

			mainFliper.setDisplayedChild(5);

			break;

		case R.id.shop_details_brand_names:

			mainFliper.setDisplayedChild(8);
			break;

		case R.id.shop_details_facebook:
			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		case R.id.shop_details_twitter:
			Allconstants.browseUrl(ins, AllUrl.twitterUrl);
			break;

		case R.id.shop_details_show_way_favorite_icon:
			if (id.equals(mps.getId()))

			{

				// ToastShow.getMessage(ins, "Already favorite");
			}

			else if (!id.equals(mps.getId()))

			{
				db.addShops(new FavoriteShops(mps.getId().trim(), mps
						.getShop_name().trim(), mps.getDistance().trim(), mps
						.getLatitude().trim(), mps.getLongitude().trim()));
				// ToastShow.getMessage(ins, "added to favorite");
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_selected);
				shopDetailsImgBtnFavorite.setClickable(false);
			}
			break;

		case R.id.shop_details_show_way:
			// Allconstants.openMap(ins,
			// spl.getAddress()+"\n"+spl.getZip_code(), latitude, longitude);
			if(!SharePreferenceUtil.isOnline(ins))
			{
				ToastShow.getMessage(ins, "No internet available");
				return;
			}
			Allconstants.drawRoute(ins, latitude, longitude, lat, lng);
			break;

		case R.id.shop_details_web:
			Allconstants.browseUrl(ins, mps.getUrl().toString().trim()
					.replaceAll(" ", "%20"));
			break;

		case R.id.shop_details_phone_btn:
			final Dialog d = new Dialog(ins);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setContentView(R.layout.dialog_box);
			d.show();
			final Button yes = (Button) d.findViewById(R.id.dialog_yes);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Allconstants.makePhoneCall(ins, mps.getPhone().toString()
							.trim()
							+ "");
					d.dismiss();
				}
			});

			final Button no = (Button) d.findViewById(R.id.dialog_no);
			no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					d.dismiss();

				}
			});

			break;

		case R.id.shop_details_message:
			SharePreferenceUtil.setEmailPressed(ins, true);
			Allconstants.semdEmail(ins, mps.getEmail().toString().trim() + "");
			break;

		case R.id.shop_details_facebook_btn:
			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		// Brands disclaimer screen

		case R.id.city_brand_back:
			mainFliper.setDisplayedChild(7);
			break;

		case R.id.city_brand_facebook:

			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_names:
			mainFliper.setDisplayedChild(7);

			break;

		case R.id.city_brand_twitter:

			Allconstants.browseUrl(ins, AllUrl.twitterUrl);
			break;

		}

	}

}
