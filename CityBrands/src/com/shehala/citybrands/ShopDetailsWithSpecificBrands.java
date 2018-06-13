package com.shehala.citybrands;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.shehala.citybrands.R;
import com.shehala.citybrands.download.image.ImageLoader;
import com.shehala.citybrands.gpstracking.GPSTracker;
import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteShops;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.BaseUrl;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;

public class ShopDetailsWithSpecificBrands implements OnClickListener {

	private ViewFlipper mainFliper;
	private Activity ins;
	// shop details screen
	private String strShopAndBrandTitle, strShopAddress;
	private Vector<String> allBrandsId = new Vector<String>();
	private Vector<String> brandsOfShop = new Vector<String>();

	private ImageButton shopDetailsImgBtnBack, shopDetailsImgBtnBrandName,
			shopDetailsImgBtnFacebook, shopDetailsImgBtnTwitter;
	private ImageButton shopDetailsImgBtnWeb, shopDetailsImgBtnPhone,
			shopDetailsImgBtnFace, shopDetailsImgBtnEmail,
			shopDetailsImgBtnShowWay, shopDetailsImgBtnFavorite;
	private TextView shopDetailSpinner;
	private TextView txtAddress, txtZipCode, txtPhone, txtOpeningHourMonFri,
			txtOpeningHourSat, txtShopTitle, txtShopDistance;
	private ShopList spl;
	private String shop_id = "";
	private String whichScreen = "";
	private String brandName = "";

	private double lat, lng, latitude, longitude;
	private GPSTracker gps;
	private ImageLoader loadImage;
	private String strDistance = "";

	private ImageView profilePhoto;
	private List<FavoriteShops> fs;
	private DatabaseHandler db;
	private String id = "";
	private boolean flag;

	// Brands disclaimer screen

	private ImageButton cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;

	public ShopDetailsWithSpecificBrands(Activity ins, ViewFlipper fliper,
			String id, String screen) {

		this.ins = ins;
		this.mainFliper = fliper;
		this.shop_id = id;
		this.whichScreen = screen;

		mainFliper.setDisplayedChild(7);

		gps = new GPSTracker(ins);
		loadImage = new ImageLoader(ins);

		spl = AllShopLists.getAllShopsInfoByShopId(shop_id);
		PrintLog.getErrorLog("spl.getShop_name() in activity :",
				spl.getShop_name());
		if (gps.canGetLocation())

		{
			lat = gps.getLatitude();
			lng = gps.getLongitude();

			
		}


		shopDetailsInfo();

	}

	@SuppressLint("DefaultLocale")
	private void shopDetailsInfo() {

		profilePhoto = (ImageView) ins
				.findViewById(R.id.shop_details_profile_photo);

		if (spl.getShop_image().trim().length() != 0)

		{
			loadImage.displayImage(BaseUrl.baseUrl
					+ spl.getShop_image().trim().replaceAll(" ", "%20"),
					profilePhoto);
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
		shopDetailsImgBtnFavorite.setOnClickListener(this);

		db = new DatabaseHandler(ins);
		fs = db.getAllShops();
		for (int i = 0; i < fs.size(); i++) {
			id = fs.get(i).getShop_id();
			if (id.equals(spl.getId()))

			{
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_selected);
				flag = true;

				break;
			}

			else

			{
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_normal);
				flag = false;
			}

		}

		shopDetailsImgBtnShowWay = (ImageButton) ins
				.findViewById(R.id.shop_details_show_way);
		shopDetailsImgBtnShowWay.setOnClickListener(this);

		shopDetailsImgBtnWeb = (ImageButton) ins
				.findViewById(R.id.shop_details_web);
		if (spl.getUrl().length() == 0) {
			shopDetailsImgBtnWeb.setVisibility(View.INVISIBLE);
		} else {
			shopDetailsImgBtnWeb.setVisibility(View.VISIBLE);
			shopDetailsImgBtnWeb.setOnClickListener(this);
		}

		shopDetailsImgBtnPhone = (ImageButton) ins
				.findViewById(R.id.shop_details_phone_btn);
		if (spl.getPhone().length() == 0) {
			shopDetailsImgBtnPhone.setVisibility(View.INVISIBLE);
		} else {
			shopDetailsImgBtnPhone.setVisibility(View.VISIBLE);
			shopDetailsImgBtnPhone.setOnClickListener(this);
		}

		shopDetailsImgBtnEmail = (ImageButton) ins
				.findViewById(R.id.shop_details_message);
		if (spl.getEmail().length() == 0) {
			shopDetailsImgBtnEmail.setVisibility(View.INVISIBLE);
		}

		else {
			shopDetailsImgBtnEmail.setVisibility(View.VISIBLE);
			shopDetailsImgBtnEmail.setOnClickListener(this);
		}

		shopDetailsImgBtnFace = (ImageButton) ins
				.findViewById(R.id.shop_details_facebook_btn);
		if (spl.getFacebookUrl().length() == 0) {
			shopDetailsImgBtnFace.setVisibility(View.INVISIBLE);
		}

		else {
			shopDetailsImgBtnFace.setVisibility(View.VISIBLE);
			shopDetailsImgBtnFace.setOnClickListener(this);
		}

		txtAddress = (TextView) ins.findViewById(R.id.shop_details_address);
		if (spl.getAddress().length() > 18) {
			strShopAddress = spl.getAddress().toString().substring(0, 18);
			txtAddress.setText(strShopAddress);

		}

		else {
			txtAddress.setText(spl.getAddress().toString().trim() + "");
		}

		txtAddress.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Animation animationToLeft = new TranslateAnimation(400, -400,
						0, 0);
				animationToLeft.setDuration(8000);
				animationToLeft.setRepeatMode(0);
				animationToLeft.setRepeatCount(0);

				String textLeft = spl.getAddress().trim();
				txtAddress.setText(textLeft);
				txtAddress.setAnimation(animationToLeft);
				return true;
			}
		});

		txtZipCode = (TextView) ins.findViewById(R.id.shop_details_zip_code);
		txtZipCode.setText(spl.getZip_code().toString().trim() + "");

		txtPhone = (TextView) ins.findViewById(R.id.shop_details_phone);
		txtPhone.setText(spl.getPhone().toString().trim() + "");

		txtOpeningHourMonFri = (TextView) ins
				.findViewById(R.id.shop_details_opening_hour_fri_mon);
		txtOpeningHourMonFri.setText(ins.getString(R.string.mon_fri) + " "
				+ spl.getM_w().toString().trim() + "");

		txtOpeningHourSat = (TextView) ins
				.findViewById(R.id.shop_details_opening_hour_sat_day);
		txtOpeningHourSat.setText(ins.getString(R.string.sat) + " "
				+ spl.getS().toString().trim() + "");

		txtShopTitle = (TextView) ins
				.findViewById(R.id.shop_details_shop_title);
		if (spl.getShop_name().length() > 18) {
			strShopAndBrandTitle = spl.getShop_name().toString()
					.substring(0, 18);
			txtShopTitle.setText(strShopAndBrandTitle);

			txtShopTitle.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					txtShopTitle.setText(spl.getShop_name().toString().trim()
							+ "");

					return true;
				}
			});
		} else {
			txtShopTitle.setText(spl.getShop_name().toString().trim());
		}

		txtShopDistance = (TextView) ins
				.findViewById(R.id.shop_details_distance);

		strDistance = spl.getDistance().trim();
		double dis = 0;
		String strFinalDistance = "";
		try {
			dis = Double.parseDouble(strDistance);
			latitude = Double.parseDouble(spl.getLatitude().trim());
			longitude = Double.parseDouble(spl.getLongitude().trim());
			strFinalDistance = String.format("%.2f", dis);

		} catch (Exception e) {
			// TODO: handle exception
		}

		txtShopDistance.setText(ins.getString(R.string.distance_calculation)
				+ " " + strFinalDistance + " km.");

		// To split comma and add to vector

		allBrandsId.removeAllElements();

		StringTokenizer st = new StringTokenizer(spl.getBrands_name(), ",");
		while (st.hasMoreTokens()) {

			allBrandsId.addElement(st.nextToken());

			Collections.sort(allBrandsId);

			// System.err.println(st.nextToken());
		}

		brandsOfShop.removeAllElements();

		for (int i = 0; i < allBrandsId.size(); i++)

		{
			brandName = brandName + allBrandsId.elementAt(i) + "\n";
			PrintLog.getWarnLog("Name of brand ", brandName);

		}

	
		shopDetailSpinner = (TextView) ins
				.findViewById(R.id.shop_details_spinner);

		shopDetailSpinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(ins, R.style.ThemeDialogCustom);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.brand_of_shop_dialog);
				d.setCancelable(false);
				d.setCanceledOnTouchOutside(false);
				d.show();
				final TextView txtBrandName = (TextView) d
						.findViewById(R.id.shop_details_txt_brand_name);
				txtBrandName.setText(brandName);

				final ImageButton cross = (ImageButton) d
						.findViewById(R.id.shop_details_spinner_cross_brand);
				cross.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						d.dismiss();
					}
				});

			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.shop_details_back:

			if (whichScreen.equalsIgnoreCase("brand"))

			{

				mainFliper.setDisplayedChild(4);

				if (Allconstants.strScreen.equalsIgnoreCase("Favorite")) {
					Allconstants.strForBackButton = "FavoriteSpecificBrandList";

				}

				else if (Allconstants.strScreen.equalsIgnoreCase("Popular")) {
					Allconstants.strForBackButton = "specificBrandList";
				}

			}

			else if (whichScreen.equalsIgnoreCase("favorite_shop"))

			{
				mainFliper.setDisplayedChild(6);
				Allconstants.strFavBack = "favMain";
			}

			else if (whichScreen.equalsIgnoreCase("favoriteList"))

			{
				mainFliper.setDisplayedChild(4);
				Allconstants.strFavBack = "favList";
			}

			else if (whichScreen.equalsIgnoreCase("mapList")) {
				mainFliper.setDisplayedChild(12);
				Allconstants.strFavBack = "favListMap";
			}

			break;

		case R.id.shop_details_brand_names:
			// Allconstants.strFavBack = "favDetaislBr";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.shop_details_facebook:
			Allconstants.browseUrl(ins, AllUrl.faceBookUrl);
			break;

		case R.id.shop_details_twitter:
			Allconstants.browseUrl(ins, AllUrl.twitterUrl);
			break;

		case R.id.shop_details_show_way_favorite_icon:

			if (flag == false) {
				db.addShops(new FavoriteShops(spl.getId().trim(), spl
						.getShop_name().trim(), spl.getDistance().trim(), spl
						.getLatitude().trim(), spl.getLongitude().trim()));
			
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_selected);
				flag = true;
			}

			else {
				db.deleteSingleFavoriteShop(spl.getId());
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_normal);
				flag = false;
			}

			break;

		case R.id.shop_details_show_way:
			
			if(!SharePreferenceUtil.isOnline(ins))
			{
				ToastShow.getMessage(ins, "No internet available");
				return;
			}
			Allconstants.drawRoute(ins, latitude, longitude, lat, lng);
			break;

		case R.id.shop_details_web:
			Allconstants.browseUrl(ins, spl.getUrl().toString().trim()
					.replaceAll(" ", "%20"));
			break;

		case R.id.shop_details_phone_btn:
			final Dialog d = new Dialog(ins, R.style.ThemeDialogCustom);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setContentView(R.layout.dialog_box);
			d.show();
			final Button yes = (Button) d.findViewById(R.id.dialog_yes);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Allconstants.makePhoneCall(ins, spl.getPhone().toString()
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
			Allconstants.semdEmail(ins, spl.getEmail().toString().trim() + "");
			break;

		case R.id.shop_details_facebook_btn:
			Allconstants.browseUrl(ins,
					spl.getFacebookUrl().trim().replaceAll(" ", "%20"));
			PrintLog.showHitCounterLog("Facebook url : ", spl.getFacebookUrl()
					.trim());
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
