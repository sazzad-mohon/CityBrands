package com.shehala.citybrands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.shehala.citybrands.R;
import com.shehala.citybrands.download.image.ImageLoader;
import com.shehala.citybrands.gpstracking.GPSTracker;
import com.shehala.citybrands.holder.AllBrandsList;
import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.mapview.CurrentMapPoint;
import com.shehala.citybrands.mapview.MapLocation;
import com.shehala.citybrands.mapview.MyItemizedOverlay;
import com.shehala.citybrands.model.Login;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.parser.LoginParser;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteShops;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.AllValidation;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.BaseUrl;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;
import com.shehala.citybrands.verticalseekbar.VerticalSeekBar;

@SuppressLint("DefaultLocale")
public class LoginActivity extends MapActivity implements OnClickListener {

	private Context con;
	private boolean flag = false;
	private boolean sFlag = false;
	private String strShopAndBrandTitle, strShopAddress;
	// Database handler

	private DatabaseHandler db;
	private List<FavoriteShops> temp;
	// private Bitmap fav_bitmap;
	// private int i = 0;
	private String id = "";

	// Map Screen
	@SuppressWarnings("unused")
	private Geocoder geocoder = null;
	private double latt, lon;
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

	// congratulation screen
	private TextView txtThanks, txtThanksMsg;
	private String strCongratulation = "";

	// New user and new pass

	private View mainview;
	private View submenu_popup;
	private PopupWindow popupwindow;
	private LayoutInflater inflater;

	private ImageButton imgBtnBrandName, imgBtnFacebook, imgBtnTwitter,
			imgBtnLogin;
	private ImageButton cityBrandLogo, cityBrandFacebook, cityBrandTwitter,
			cityBrandBack;
	private ImageButton newPassimgBtnBack, newPassimgBtnBrandName,
			newPassimgBtnFacebook, newPassimgBtnTwitter, newPassimgBtnSend;
	private ImageButton newUserimgBtnBack, newUserimgBtnBrandName,
			newUserimgBtnFacebook, newUserimgBtnTwitter, newUserimgBtnSend;
	private Button btnForgetPassword, btnNewUser;
	private Switch male_female;
	private ViewFlipper mainFliper;
	private ProgressDialog pd;
	private String response = "",
			strDeleteResponse = "";
	private String frScreen = "login";

	// Login screen

	private EditText edt_login_email, edt_login_password, edt_edit_email;
	private ImageButton img_login_email_cross, img_login_password_cross,
			img_edit_password_cross;

	private CheckBox chk_login_remember;
	// private String strEmail = "",strPass = "";

	// New user
	private ImageView imgInfo;
	private LinearLayout emptyWarning;
	private RelativeLayout new_user_male_female_layout;
	private EditText new_user_post_code, new_user_email, new_user_password,
			new_user_confirm_password;
	private ImageView img_post_code_miss, img_email_miss, img_password_miss,
			img_seekbar_miss_icon;
	private ImageButton new_user_post_code_cross, new_user_email_cross,
			new_user_password_cross, new_user_confirm_password_cross;
	private ImageButton popup_cross, setting_popup_cross, setting_popup_send;
	private CheckBox chk_new_user_remember;
	private String strAccept = "0", strSettingAccept = "0";
	private String age = "";
	private String setting_age = "";
	private Dialog dialog;
	// private AlertDialog.Builder builder;
	private SeekBar new_user_age_seekbar;
	private String strMaleFemale = "Female";
	private static int duration = 2000;
	private Handler mHandler = new Handler();
	private int popupWidth, popupHeight, popupPaddingTop, popupYXis;

	// Search Screen

	private Spinner distance_spinner;
	private ImageButton searchImgBtnBack, searchImgBtnBrandName,
			searchImgBtnFacebook, searchImgBtnTwitter, searchImgBtnSearchFinal,
			searchImgBtnEdtSearch, searchImgBtnEdtCross, searchImgBtnSetting;
	private AutoCompleteTextView searchEdtTxt;
	private TextView searchTxtLogout;
	private Switch searchToggleBtn;
	private String strBrandOrShop = "Brand";
	private ArrayAdapter<String> spr;
	private ArrayAdapter<String> bdr;
	private String spinnerDistance = "", spinnerFinalDistance = "";
	private double spinnerDistanceInKm = 0;
	private String strSearchItem = "";

	// all shop screen

	public double deviceHeight;
	private ListView all_shop_list;
	private ShowAllShopAdapter ad;
	private ImageButton allShopImgBtnBack, allShopImgBtnBrandName,
			allShopImgBtnFacebook, allShopImgBtnTwitter;
	//private int distanceInKm = 0, distanceInM = 0;

	private List<FavoriteShops> fs;
	private List<ShopList> shopList;
	private List<ShopList> shopListFinal;
	private String hitIncreaseUrl = AllUrl.increaseBrandHitURL;
	private String hitKeyUserOrBrandId = "BRAND_ID";
	private String hitUserOrBrandId = "";

	// shop details screen
	private ShopList slt;
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
	private double lat, lng, latitude, longitude;
	private GPSTracker gps;
	private ImageLoader loadImage;
	private ImageView shopProfileImage;
	private double verticalBarDistance = 0;
	private int topMarginValue = 0, progressValue = 0;
	private String brandName = "";

	// private int shopDetailsPos = 0;

	// setting screen

	private ImageButton setting_post_code_cross, setting_email_cross,
			setting_password_cross, setting_confirm_password_cross;

	private EditText setting_post_code, setting_email, setting_password,
			setting_confirm_password;

	private ImageButton settingimgBtnBack, settingimgBtnBrandName,
			settingimgBtnFacebook, settingimgBtnTwitter, settingimgBtnSave,
			settingimgBtnDelete;

	private CheckBox settingChkNews;
	private SeekBar settingSeekbar;
	@SuppressWarnings("unused")
	private String layoutParam = "", strProgress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainflipper);
		con = this;

		// String screenSize = ScreenSize.getScreenSize(this);
		//
		// ToastShow.getMessage(con, "Device's screen size is " + screenSize);
		db = new DatabaseHandler(con);
		temp = db.getAllShops();
		geocoder = new Geocoder(con);

		// PrintLog.getWarnLog("Size of temp in on create : ", temp.size()
		// + "<><>");
		// fav_bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.list_favorite_selected);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (Allconstants.screenSize.equalsIgnoreCase("480x800")) {
			popupWidth = 480;
			popupHeight = 650;
			popupPaddingTop = 40;
			popupYXis = 80;

		}

		else if (Allconstants.screenSize.equalsIgnoreCase("320x480")) {
			popupWidth = 320;
			popupHeight = 370;
			popupPaddingTop = 30;
			popupYXis = 50;
		}

		else {
			popupWidth = 720;
			popupHeight = 970;
			popupPaddingTop = 30;
			popupYXis = 90;
		}

		if (SharePreferenceUtil.getLoginStatus(con).equalsIgnoreCase("logout")) {
			initUI();
		}

		else if (SharePreferenceUtil.getLoginStatus(con).equalsIgnoreCase(
				"login")) {

			getSearchScreenInfo();
		}

		// String st = String.format("%.2f", 1.1);

	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(SharePreferenceUtil.getEmailPressed(con)){
			SharePreferenceUtil.setEmailPressed(con, false);
			return;
		}

		if (SharePreferenceUtil.getLoginStatus(con).equalsIgnoreCase("login")) {

			new SearchInfo().execute();

		}

		else {
			Allconstants.strForBackButton = "login";
		}

		PrintLog.getWarnLog("strForBackButton : ",
				Allconstants.strForBackButton + "");
		MyTabActivity.getInstance().pushIndex(0);
	}

	private class SearchInfo extends AsyncTask<String, String, String> {

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

			getSearchScreenInfo();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// MyTabActivity.tabIndex = 0;
		// ToastShow.getMessage(con, "I am in on pause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// ToastShow.getMessage(con, "I am in on restart");
	}

	private void initUIForResume() {
		edt_login_email = (EditText) findViewById(R.id.login_email);
		edt_login_password = (EditText) findViewById(R.id.login_password);

		img_login_email_cross = (ImageButton) findViewById(R.id.login_email_cross);
		img_login_email_cross.setOnClickListener(this);

		img_login_password_cross = (ImageButton) findViewById(R.id.login_password_cross);
		img_login_password_cross.setOnClickListener(this);

		imgBtnBrandName = (ImageButton) findViewById(R.id.login_brand_names);
		imgBtnBrandName.setOnClickListener(this);

		imgBtnFacebook = (ImageButton) findViewById(R.id.login_facebook);
		imgBtnFacebook.setOnClickListener(this);

		imgBtnTwitter = (ImageButton) findViewById(R.id.login_twitter);
		imgBtnTwitter.setOnClickListener(this);

		imgBtnLogin = (ImageButton) findViewById(R.id.m_login_btn);
		imgBtnLogin.setOnClickListener(this);

		btnForgetPassword = (Button) findViewById(R.id.login_btn_forget_password);
		btnForgetPassword.setOnClickListener(this);

		btnNewUser = (Button) findViewById(R.id.login_btn_new_user);
		btnNewUser.setOnClickListener(this);

		cityBrandBack = (ImageButton) findViewById(R.id.city_brand_back);
		cityBrandBack.setOnClickListener(this);

		cityBrandTwitter = (ImageButton) findViewById(R.id.city_brand_twitter);
		cityBrandTwitter.setOnClickListener(this);

		cityBrandLogo = (ImageButton) findViewById(R.id.city_brand_names);
		cityBrandLogo.setOnClickListener(this);

		cityBrandFacebook = (ImageButton) findViewById(R.id.city_brand_facebook);
		cityBrandFacebook.setOnClickListener(this);

		chk_login_remember = (CheckBox) findViewById(R.id.login_remember);
		chk_login_remember.setChecked(false);
		chk_login_remember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (chk_login_remember.isChecked()) {
					// strEmail = edt_login_email.getText().toString();
					SharePreferenceUtil.setUser(con, edt_login_email.getText()
							.toString() + "");
					SharePreferenceUtil.setPass(con, edt_login_password
							.getText().toString() + "");
				}

				else if (!chk_login_remember.isChecked())

				{
					SharePreferenceUtil.setPass(con, "");
					SharePreferenceUtil.setUser(con, "");

				}

			}
		});

		if (SharePreferenceUtil.getUser(con).length() != 0
				&& SharePreferenceUtil.getPass(con).length() != 0) {

			edt_login_email.setText(SharePreferenceUtil.getUser(con));
			edt_login_password.setText(SharePreferenceUtil.getPass(con));
			// mChkRemember.setChecked(true);
		} else {
			edt_login_email.setText("");
			edt_login_password.setText("");
			// mChkRemember.setChecked(false);
		}
	}

	private void initUI() {

		mainFliper = (ViewFlipper) findViewById(R.id.flpmain);
		mainFliper.setDisplayedChild(0);

		Allconstants.strForBackButton = "login";

		edt_login_email = (EditText) findViewById(R.id.login_email);
		edt_login_password = (EditText) findViewById(R.id.login_password);

		img_login_email_cross = (ImageButton) findViewById(R.id.login_email_cross);
		img_login_email_cross.setOnClickListener(this);

		img_login_password_cross = (ImageButton) findViewById(R.id.login_password_cross);
		img_login_password_cross.setOnClickListener(this);

		imgBtnBrandName = (ImageButton) findViewById(R.id.login_brand_names);
		imgBtnBrandName.setOnClickListener(this);

		imgBtnFacebook = (ImageButton) findViewById(R.id.login_facebook);
		imgBtnFacebook.setOnClickListener(this);

		imgBtnTwitter = (ImageButton) findViewById(R.id.login_twitter);
		imgBtnTwitter.setOnClickListener(this);

		imgBtnLogin = (ImageButton) findViewById(R.id.m_login_btn);
		imgBtnLogin.setOnClickListener(this);

		btnForgetPassword = (Button) findViewById(R.id.login_btn_forget_password);
		btnForgetPassword.setOnClickListener(this);

		btnNewUser = (Button) findViewById(R.id.login_btn_new_user);
		btnNewUser.setOnClickListener(this);

		cityBrandBack = (ImageButton) findViewById(R.id.city_brand_back);
		cityBrandBack.setOnClickListener(this);

		cityBrandTwitter = (ImageButton) findViewById(R.id.city_brand_twitter);
		cityBrandTwitter.setOnClickListener(this);

		cityBrandLogo = (ImageButton) findViewById(R.id.city_brand_names);
		cityBrandLogo.setOnClickListener(this);

		cityBrandFacebook = (ImageButton) findViewById(R.id.city_brand_facebook);
		cityBrandFacebook.setOnClickListener(this);

		chk_login_remember = (CheckBox) findViewById(R.id.login_remember);
		chk_login_remember.setChecked(false);
		chk_login_remember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (chk_login_remember.isChecked()) {
					// strEmail = edt_login_email.getText().toString();
					SharePreferenceUtil.setUser(con, edt_login_email.getText()
							.toString() + "");
					SharePreferenceUtil.setPass(con, edt_login_password
							.getText().toString() + "");
				}

				else if (!chk_login_remember.isChecked())

				{
					SharePreferenceUtil.setPass(con, "");
					SharePreferenceUtil.setUser(con, "");

				}

			}
		});

		if (SharePreferenceUtil.getUser(con).length() != 0
				&& SharePreferenceUtil.getPass(con).length() != 0) {

			edt_login_email.setText(SharePreferenceUtil.getUser(con));
			edt_login_password.setText(SharePreferenceUtil.getPass(con));
			// mChkRemember.setChecked(true);
		} else {
			edt_login_email.setText("");
			edt_login_password.setText("");
			// mChkRemember.setChecked(false);
		}

	}

	private void createNewUser() {
		mainFliper.setDisplayedChild(2);

		// new_user_main_bg.setBackgroundResource((R.drawable.main_bg));

		emptyWarning = (LinearLayout) findViewById(R.id.linear_required);
		emptyWarning.setVisibility(View.INVISIBLE);

		new_user_male_female_layout = (RelativeLayout) findViewById(R.id.new_user_male_female_layout);
		new_user_male_female_layout.setVisibility(View.VISIBLE);

		male_female = (Switch) findViewById(R.id.new_user_toggle);
		PrintLog.getDebugLog("MaleOrFemale :", strMaleFemale);
		male_female.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (buttonView.isChecked())

				{
					strMaleFemale = "Male";
					PrintLog.getDebugLog("MaleOrFemale in slide :",
							strMaleFemale);
				}

				else if (!buttonView.isChecked())

				{
					strMaleFemale = "Female";
					PrintLog.getDebugLog("MaleOrFemale in slide :",
							strMaleFemale);
				}
			}
		});

		new_user_age_seekbar = (SeekBar) findViewById(R.id.new_user_seekbar);
		new_user_age_seekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// dialog.dismiss();

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						TextView txtProgress = (TextView) findViewById(R.id.dialog_age_counter);
						txtProgress.setVisibility(View.VISIBLE);
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						int marginValue = 0;

						TextView txtProgress = (TextView) findViewById(R.id.dialog_age_counter);
						txtProgress.setText("0-9 " + getString(R.string.year));
						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
								android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
								android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

						param.leftMargin = marginValue;
						
						
						WindowManager w = LoginActivity.this.getWindowManager();
						Display d = w.getDefaultDisplay();
						DisplayMetrics metrics = new DisplayMetrics();
						d.getMetrics(metrics);
						double deviceWidth = metrics.widthPixels;
						
						//Log.w("deviceWidth", deviceWidth+"PX");
						
						if(deviceWidth>700)
							param.leftMargin += 50;
						
						txtProgress.setLayoutParams(param);

						SharePreferenceUtil.setAgeProgress(con,
								String.valueOf(progress));

						if (Allconstants.screenSize.equalsIgnoreCase("320x480"))

						{
							if (progress >= 0) {
								param.leftMargin = 15;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "15");
							}
							if (progress >= 3) {
								param.leftMargin = 20;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "20");
							}

							if (progress >= 6) {
								param.leftMargin = 25;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "25");
							}

							if (progress >= 9) {
								param.leftMargin = progress * 2 + 12;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "30");
							}

							if (progress >= 10) {
								param.leftMargin = progress * 2 + 12;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("10-13 "
										+ getString(R.string.year));
								age = "10-13";
								SharePreferenceUtil.setLayoutParam(con, "32");
							}

							if (progress >= 14) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("14-17 "
										+ getString(R.string.year));
								age = "14-17";
								SharePreferenceUtil.setLayoutParam(con, "42");
							}

							if (progress >= 18) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("18-21 "
										+ getString(R.string.year));
								age = "18-21";
								SharePreferenceUtil.setLayoutParam(con, "54");
							}

							if (progress >= 22) {

								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("22-25 "
										+ getString(R.string.year));
								age = "22-25";
								SharePreferenceUtil.setLayoutParam(con, "66");
							}

							if (progress >= 26) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("26-30 "
										+ getString(R.string.year));
								age = "26-30";
								SharePreferenceUtil.setLayoutParam(con, "78");
							}

							if (progress >= 31) {
								param.leftMargin = progress * 3 - 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("31-40 "
										+ getString(R.string.year));
								age = "31-40";
								SharePreferenceUtil.setLayoutParam(con, "88");
							}

							if (progress >= 41) {
								param.leftMargin = progress * 3 - 10;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("41-50 "
										+ getString(R.string.year));
								age = "41-50";
								SharePreferenceUtil.setLayoutParam(con, "113");
							}

							if (progress >= 51) {
								param.leftMargin = progress * 3 - 25;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("51-60 "
										+ getString(R.string.year));
								age = "51-60";
								SharePreferenceUtil.setLayoutParam(con, "128");
							}

							if (progress >= 61) {
								param.leftMargin = progress * 3 - 25;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("61-70 "
										+ getString(R.string.year));
								age = "61-70";
								SharePreferenceUtil.setLayoutParam(con, "154");
							}

							if (progress >= 71) {
								param.leftMargin = progress * 3 - 35;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("71> "
										+ getString(R.string.year));
								age = "71>";
								SharePreferenceUtil.setLayoutParam(con, "178");
							}

							if (progress >= 97) {
								param.leftMargin = progress * 3 - 52;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("71> "
										+ getString(R.string.year));
								age = "71>";
								SharePreferenceUtil.setLayoutParam(con, "239");
							}
						}

						else {
							if (progress >= 0) {
								param.leftMargin = -15;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "-15");
							}
							if (progress >= 3) {
								param.leftMargin = -5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "-5");
							}

							if (progress >= 6) {
								param.leftMargin = progress;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "6");
							}

							if (progress >= 9) {
								param.leftMargin = progress * 2;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								age = "0-9";
								SharePreferenceUtil.setLayoutParam(con, "18");
							}

							if (progress >= 10) {
								param.leftMargin = progress * 2 + 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("10-13 "
										+ getString(R.string.year));
								age = "10-13";
								SharePreferenceUtil.setLayoutParam(con, "25");
							}

							if (progress >= 14) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("14-17 "
										+ getString(R.string.year));
								age = "14-17";
								SharePreferenceUtil.setLayoutParam(con, "42");
							}

							if (progress >= 18) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("18-21 "
										+ getString(R.string.year));
								age = "18-21";
								SharePreferenceUtil.setLayoutParam(con, "54");
							}

							if (progress >= 22) {

								param.leftMargin = progress * 3 + 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("22-25 "
										+ getString(R.string.year));
								age = "22-25";
								SharePreferenceUtil.setLayoutParam(con, "71");
							}

							if (progress >= 26) {
								param.leftMargin = progress * 3 + 7;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("26-30 "
										+ getString(R.string.year));
								age = "26-30";
								SharePreferenceUtil.setLayoutParam(con, "85");
							}

							if (progress >= 31) {
								param.leftMargin = progress * 3 + 15;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("31-40 "
										+ getString(R.string.year));
								age = "31-40";
								SharePreferenceUtil.setLayoutParam(con, "108");
							}

							if (progress >= 41) {
								param.leftMargin = progress * 4 - 20;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("41-50 "
										+ getString(R.string.year));
								age = "41-50";
								SharePreferenceUtil.setLayoutParam(con, "144");
							}

							if (progress >= 51) {
								param.leftMargin = progress * 4 - 20;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("51-60 "
										+ getString(R.string.year));
								age = "51-60";
								SharePreferenceUtil.setLayoutParam(con, "184");
							}

							if (progress >= 61) {

								txtProgress.setText("61-70 "
										+ getString(R.string.year));
								age = "61-70";
								SharePreferenceUtil.setLayoutParam(con, "184");
							}

							if (progress >= 71) {

								txtProgress.setText("71> "
										+ getString(R.string.year));
								age = "71>";
								SharePreferenceUtil.setLayoutParam(con, "184");
							}

							if (progress >= 97) {
								param.leftMargin = progress * 4 - 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("71> "
										+ getString(R.string.year));
								age = "71>";
								SharePreferenceUtil.setLayoutParam(con, "184");
							}
							
							
							Log.w("PROGRESS", progress+" PX");
							
							
							if(progress <= 10 && deviceWidth>600)
								param.leftMargin += 10;
							else if(progress <= 30 && deviceWidth>600)
								param.leftMargin += 50;
							else if(progress <= 40 && deviceWidth>600)
								param.leftMargin += 70;
							else if(progress <= 50 && deviceWidth>600)
								param.leftMargin += 90;
							else if(progress <= 70 && deviceWidth>600)
								param.leftMargin += 120;
							else if(progress <= 90 && deviceWidth>600)
								param.leftMargin += 150;
							else if(progress <= 100 && deviceWidth>600)
								param.leftMargin += 170;
							
							
							txtProgress.setLayoutParams(param);
							SharePreferenceUtil.setLayoutParam(con, param.leftMargin+"");

						}

					}
				});

		new_user_email = (EditText) findViewById(R.id.new_user_edt_email);
		new_user_password = (EditText) findViewById(R.id.new_user_edt_code);
		new_user_post_code = (EditText) findViewById(R.id.new_user_postcode);
		new_user_confirm_password = (EditText) findViewById(R.id.new_user_edt_confirm_code);

		img_email_miss = (ImageView) findViewById(R.id.new_user_edt_email_miss_icon);
		img_password_miss = (ImageView) findViewById(R.id.new_user_edt_code_miss_icon);
		img_post_code_miss = (ImageView) findViewById(R.id.new_user_postcode_miss_icon);
		img_seekbar_miss_icon = (ImageView) findViewById(R.id.new_user_seekbar_miss_icon);

		new_user_email_cross = (ImageButton) findViewById(R.id.new_user_email_cross);
		new_user_email_cross.setOnClickListener(this);

		new_user_password_cross = (ImageButton) findViewById(R.id.new_user_edt_code_cross);
		new_user_password_cross.setOnClickListener(this);

		new_user_confirm_password_cross = (ImageButton) findViewById(R.id.new_user_edt_confirm_code_cross);
		new_user_confirm_password_cross.setOnClickListener(this);

		new_user_post_code_cross = (ImageButton) findViewById(R.id.new_user_btn_postcode_cross);
		new_user_post_code_cross.setOnClickListener(this);

		newUserimgBtnBack = (ImageButton) findViewById(R.id.new_user_back);
		newUserimgBtnBack.setOnClickListener(this);

		newUserimgBtnBrandName = (ImageButton) findViewById(R.id.new_user_brand_names);
		newUserimgBtnBrandName.setOnClickListener(this);

		newUserimgBtnFacebook = (ImageButton) findViewById(R.id.new_user_facebook);
		newUserimgBtnFacebook.setOnClickListener(this);

		newUserimgBtnTwitter = (ImageButton) findViewById(R.id.new_user_twitter);
		newUserimgBtnTwitter.setOnClickListener(this);

		newUserimgBtnSend = (ImageButton) findViewById(R.id.new_user_btn_send);
		newUserimgBtnSend.setOnClickListener(this);

		imgInfo = (ImageView) findViewById(R.id.new_user_info_btn);
		imgInfo.setOnClickListener(this);

		chk_new_user_remember = (CheckBox) findViewById(R.id.new_user_remember);
		chk_new_user_remember.setChecked(false);
		PrintLog.getWarnLog("AcceptOrNot : ", strAccept);
		chk_new_user_remember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (chk_new_user_remember.isChecked())

				{
					strAccept = "1";
					PrintLog.getWarnLog("AcceptOrNot : ", strAccept);
				}

				else if (!chk_new_user_remember.isChecked())

				{
					strAccept = "0";
					PrintLog.getWarnLog("AcceptOrNot : ", strAccept);
				}
			}
		});

	}

	private void getNewPassword() {
		mainFliper.setDisplayedChild(1);

		edt_edit_email = (EditText) findViewById(R.id.new_pasword_email);
		img_edit_password_cross = (ImageButton) findViewById(R.id.new_pasword_email_cross);
		img_edit_password_cross.setOnClickListener(this);

		newPassimgBtnBack = (ImageButton) findViewById(R.id.new_pasword_back);
		newPassimgBtnBack.setOnClickListener(this);

		newPassimgBtnBrandName = (ImageButton) findViewById(R.id.new_pasword_brand_names);
		newPassimgBtnBrandName.setOnClickListener(this);

		newPassimgBtnFacebook = (ImageButton) findViewById(R.id.new_pasword_facebook);
		newPassimgBtnFacebook.setOnClickListener(this);

		newPassimgBtnTwitter = (ImageButton) findViewById(R.id.new_pasword_twitter);
		newPassimgBtnTwitter.setOnClickListener(this);

		newPassimgBtnSend = (ImageButton) findViewById(R.id.new_pasword_btn);
		newPassimgBtnSend.setOnClickListener(this);

	}

	private void getSearchScreenInfo() {

		Allconstants.strForBackButton = "search";
		mainFliper = (ViewFlipper) findViewById(R.id.flpmain);
		mainFliper.setDisplayedChild(3);
		distance_spinner = (Spinner) findViewById(R.id.distance_spinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.m_dis, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		distance_spinner.setAdapter(adapter);
		distance_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub

						spinnerDistance = parent.getItemAtPosition(position)
								.toString();
						PrintLog.getWarnLog("Spinner Distance : ",
								spinnerDistance);
						if (spinnerDistance.contains(">km.")) {
							spinnerFinalDistance = spinnerDistance.substring(0,
									spinnerDistance.length() - 4);
							try {

								double parseDistance = Double
										.parseDouble(spinnerFinalDistance
												.trim());

								spinnerDistanceInKm = (parseDistance * 1000);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}

						else if (spinnerDistance.contains("km."))

						{
							spinnerFinalDistance = spinnerDistance.substring(0,
									spinnerDistance.length() - 3);
							try {

								double parseDistance = Double
										.parseDouble(spinnerFinalDistance
												.trim());

								spinnerDistanceInKm = (parseDistance * 1000);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}

						else if (spinnerDistance.contains("m."))

						{
							spinnerFinalDistance = spinnerDistance.substring(0,
									spinnerDistance.length() - 2);

							try {
								double parseDistance = Double
										.parseDouble(spinnerFinalDistance
												.trim());

								spinnerDistanceInKm = (parseDistance);

							} catch (Exception e) {
								// TODO: handle exception
							}

						}

						if (spinnerDistanceInKm >= 90000.0) {
							spinnerDistanceInKm = 90000000000000000.0;
						}

						PrintLog.getWarnLog("spinnerFinalDistance : ",
								spinnerFinalDistance);

						PrintLog.getWarnLog("spinnerDistanceInKm : ",
								spinnerDistanceInKm + "");

						// SeekBarProgressCalculation.getProgressValue(
						// progressValue, topMarginValue,
						// spinnerDistanceInKm);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});

		searchImgBtnBack = (ImageButton) findViewById(R.id.search_back_btn);
		searchImgBtnBack.setOnClickListener(this);

		searchImgBtnBrandName = (ImageButton) findViewById(R.id.search_brand_names);
		searchImgBtnBrandName.setOnClickListener(this);

		searchImgBtnFacebook = (ImageButton) findViewById(R.id.search_facebook);
		searchImgBtnFacebook.setOnClickListener(this);

		searchImgBtnTwitter = (ImageButton) findViewById(R.id.search_twitter);
		searchImgBtnTwitter.setOnClickListener(this);

		searchImgBtnSearchFinal = (ImageButton) findViewById(R.id.search_final_search);
		searchImgBtnSearchFinal.setOnClickListener(this);

		searchImgBtnEdtSearch = (ImageButton) findViewById(R.id.search_search_btn);
		searchImgBtnEdtSearch.setOnClickListener(this);

		searchImgBtnEdtCross = (ImageButton) findViewById(R.id.search_edt_search_text_cross);
		searchImgBtnEdtCross.setOnClickListener(this);

		searchToggleBtn = (Switch) findViewById(R.id.search_toggle_btn);
		// searchToggleBtn.setChecked(true);
		// searchToggleBtn.setBackgroundResource(R.drawable.switch_brand_2);
		// searchToggleBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// if (searchToggleBtn.isChecked())
		//
		// {
		//
		// searchToggleBtn
		// .setBackgroundResource(R.drawable.switch_shop);
		// strBrandOrShop = "Shop";
		// searchEdtTxt = (AutoCompleteTextView)
		// findViewById(R.id.search_edt_search_text);
		// searchEdtTxt.setThreshold(11);
		// searchEdtTxt.setAdapter(bdr);
		//
		// PrintLog.getDebugLog("BrandOrShop in sliding ?",
		// strBrandOrShop);
		//
		// }
		//
		// else if (!searchToggleBtn.isChecked())
		//
		// {
		// searchToggleBtn
		// .setBackgroundResource(R.drawable.switch_brand_2);
		// strBrandOrShop = "Brand";
		// searchEdtTxt = (AutoCompleteTextView)
		// findViewById(R.id.search_edt_search_text);
		// searchEdtTxt.setThreshold(1);
		// searchEdtTxt.setAdapter(spr);
		//
		// PrintLog.getDebugLog("BrandOrShop in sliding ?",
		// strBrandOrShop);
		//
		// }
		// }
		// });
		// PrintLog.getDebugLog("BrandOrShop ?", strBrandOrShop);
		// PrintLog.getDebugLog("BrandOrShop?", strBrandOrShop);
		searchToggleBtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (buttonView.isChecked())

						{
							strBrandOrShop = "Shop";
							searchEdtTxt = (AutoCompleteTextView) findViewById(R.id.search_edt_search_text);
							searchEdtTxt.setThreshold(1);
							searchEdtTxt.setAdapter(spr);

							PrintLog.getDebugLog("BrandOrShop in sliding ?",
									strBrandOrShop);
						}

						else if (!buttonView.isChecked())

						{
							strBrandOrShop = "Brand";
							searchEdtTxt = (AutoCompleteTextView) findViewById(R.id.search_edt_search_text);
							searchEdtTxt.setThreshold(1);
							searchEdtTxt.setAdapter(bdr);

							PrintLog.getDebugLog("BrandOrShop in sliding ?",
									strBrandOrShop);
						}

					}
				});

		searchImgBtnSetting = (ImageButton) findViewById(R.id.search_setting_btn);
		searchImgBtnSetting.setOnClickListener(this);

		searchTxtLogout = (TextView) findViewById(R.id.search_logout);
		searchTxtLogout.setOnClickListener(this);

		bdr = new ArrayAdapter<String>(con, R.layout.row_search_autocomplete,
				R.id.autocomplete_row_txt, AllBrandsList.getAllBrandsName());

		spr = new ArrayAdapter<String>(con, R.layout.row_search_autocomplete,
				R.id.autocomplete_row_txt, AllShopLists.getShopNames());

		searchEdtTxt = (AutoCompleteTextView) findViewById(R.id.search_edt_search_text);
		searchEdtTxt.setThreshold(1);
		searchEdtTxt.setAdapter(bdr);

		// PrintLog.getWarnLog("Size of shop names vector : ", AllShopLists
		// .getShopNames().size() + "");

		initUIForResume();

	}

	private void allShopInfo() {

		mainFliper.setDisplayedChild(4);

		// allShopWithSpecificBrand = AllShopLists
		// .getShopNamesByBrandsName(strSearchItem);

		if (spinnerDistanceInKm >= 100.0)

		{

			progressValue = 99;
			topMarginValue = -3;

		}

		if (spinnerDistanceInKm >= 200.0)

		{

			progressValue = 5;
			topMarginValue = 5;

		}

		if (spinnerDistanceInKm >= 300.0)

		{
			progressValue = 97;
			topMarginValue = 0;

		}

		if (spinnerDistanceInKm >= 400.0)

		{
			progressValue = 95;
			topMarginValue = 0;

		}

		if (spinnerDistanceInKm >= 500.0)

		{
			progressValue = 93;
			topMarginValue = 0;

		}

		if (spinnerDistanceInKm >= 600.0)

		{
			progressValue = 91;
			topMarginValue = 5;

		}

		if (spinnerDistanceInKm >= 700.0)

		{
			progressValue = 89;
			topMarginValue = 23;

		}

		if (spinnerDistanceInKm >= 800.0)

		{
			progressValue = 87;
			topMarginValue = 33;

		}

		if (spinnerDistanceInKm >= 900.0)

		{
			progressValue = 85;
			topMarginValue = 40;

		}

		if (spinnerDistanceInKm >= 1000.0)

		{
			progressValue = 83;
			topMarginValue = 50;

		}

		if (spinnerDistanceInKm >= 1500.0)

		{
			progressValue = 81;
			topMarginValue = 60;

		}

		if (spinnerDistanceInKm >= 2000.0)

		{
			progressValue = 77;
			topMarginValue = 80;

		}

		if (spinnerDistanceInKm >= 3000.0)

		{
			progressValue = 75;
			topMarginValue = 90;

		}

		if (spinnerDistanceInKm >= 5000.0)

		{
			progressValue = 73;
			topMarginValue = 100;

		}

		if (spinnerDistanceInKm >= 6000.0)

		{
			progressValue = 67;
			topMarginValue = 123;

		}

		if (spinnerDistanceInKm >= 7000.0)

		{
			progressValue = 65;
			topMarginValue = 133;

		}

		if (spinnerDistanceInKm >= 8000.0)

		{
			progressValue = 63;
			topMarginValue = 140;

		}

		if (spinnerDistanceInKm >= 9000.0)

		{
			progressValue = 61;
			topMarginValue = 150;

		}

		if (spinnerDistanceInKm >= 10000.0)

		{
			progressValue = 59;
			topMarginValue = 160;

		}

		if (spinnerDistanceInKm >= 20000.0)

		{
			progressValue = 53;
			topMarginValue = 190;

		}

		if (spinnerDistanceInKm >= 30000.0)

		{
			progressValue = 47;
			topMarginValue = 220;

		}

		if (spinnerDistanceInKm >= 40000.0)

		{
			progressValue = 41;
			topMarginValue = 250;

		}

		if (spinnerDistanceInKm >= 50000.0)

		{
			progressValue = 35;
			topMarginValue = 272;

		}

		if (spinnerDistanceInKm >= 60000.0)

		{
			progressValue = 29;
			topMarginValue = 297;

		}

		if (spinnerDistanceInKm >= 70000.0)

		{
			progressValue = 22;
			topMarginValue = 330;

		}

		if (spinnerDistanceInKm >= 80000.0)

		{
			progressValue = 17;
			topMarginValue = 355;

		}

		if (spinnerDistanceInKm >= 90000.0)

		{
			progressValue = 9;
			topMarginValue = 397;

		}

		allShopImgBtnBack = (ImageButton) findViewById(R.id.all_shop_back);
		allShopImgBtnBack.setOnClickListener(this);

		allShopImgBtnBrandName = (ImageButton) findViewById(R.id.all_shop_brand_names);
		allShopImgBtnBrandName.setOnClickListener(this);

		allShopImgBtnFacebook = (ImageButton) findViewById(R.id.all_shop_facebook);
		allShopImgBtnFacebook.setOnClickListener(this);

		allShopImgBtnTwitter = (ImageButton) findViewById(R.id.all_shop_twitter);
		allShopImgBtnTwitter.setOnClickListener(this);

		VerticalSeekBar verticalBar = (VerticalSeekBar) findViewById(R.id.all_shop_seekbar);
		TextView hits = (TextView) findViewById(R.id.vertical_distance_counter);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		
		param.topMargin = topMarginValue;

		WindowManager w = LoginActivity.this.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		deviceHeight = metrics.heightPixels;
		
		Log.w("progressValue", progressValue+" PX");
		
		if(progressValue <= 10 && deviceHeight>1100)
			param.topMargin += 120;
		else if(progressValue <= 27 && deviceHeight>1100)
			param.topMargin += 100;
		else if(progressValue <= 43 && deviceHeight>1100)
			param.topMargin += 70;
		else if(progressValue <= 70 && deviceHeight>1100)
			param.topMargin += 50;
		else if(progressValue <= 90 && deviceHeight>1100)
			param.topMargin += 20;
		else if(progressValue <= 100 && deviceHeight>1100)
			param.topMargin -= 10;
		
		
		hits.setLayoutParams(param);
		Log.w("deviceHeight", deviceHeight+"PX");
		
		//if(deviceHeight>1100)
			//param.topMargin += 107;
		
		hits.setLayoutParams(param);
		
		verticalBar.setProgress(progressValue);

		if (shopListFinal.size() < 1)

		{
			hits.setText("0" + " hit");
		} else if (shopListFinal.size() == 1)

		{

			hits.setText(shopListFinal.size() + " hit");
		}

		if (shopListFinal.size() > 1)

		{

			hits.setText(shopListFinal.size() + " hits");
		}

		verticalBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// PrintLog.getWarnLog("Vertical progress : ", "stop");
				// TextView hits = (TextView)
				// findViewById(R.id.vertical_distance_counter);
				// hits.setVisibility(View.GONE);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				TextView mh = (TextView) findViewById(R.id.vertical_distance_counter);

				if (verticalBarDistance / 1000 >= 90)

				{
					mh.setText("90>" + " km.");
				}

				else {
					mh.setText(verticalBarDistance / 1000 + " km.");
				}

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				shopListFinal = new ArrayList<ShopList>();

				for (int i = 0; i < shopList.size(); i++)

				{

					double final_distance = Double.parseDouble(shopList.get(i)
							.getDistance());

					// PrintLog.getWarnLog("final_distance", final_distance +
					// "<><>");

					if (final_distance <= verticalBarDistance / 1000)

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

				Allconstants.shopListFinal = shopListFinal;
				getLat();

				PrintLog.getWarnLog("ShoplistFinal.size() ",
						shopListFinal.size() + "<77><77>");

				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

				TextView hits = (TextView) findViewById(R.id.vertical_distance_counter);
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
					hits.setLayoutParams(param);
					verticalBarDistance = 900;
				}
				if (progress >= 87)

				{
					param.topMargin = 43 - 10;
					hits.setLayoutParams(param);
					verticalBarDistance = 800;
				}
				if (progress >= 89)

				{
					param.topMargin = 33 - 10;
					hits.setLayoutParams(param);
					verticalBarDistance = 700;
				}
				if (progress >= 91)

				{
					param.topMargin = 15 - 10;
					hits.setLayoutParams(param);
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
					hits.setLayoutParams(param);
					verticalBarDistance = 400;
				}

				if (progress >= 97)

				{
					param.topMargin = 0;
					hits.setLayoutParams(param);
					verticalBarDistance = 300;
				}

				if (progress >= 98)

				{
					param.topMargin = -5;
					hits.setLayoutParams(param);
					verticalBarDistance = 200;
				}

				if (progress >= 99)

				{
					param.topMargin = -3;
					hits.setLayoutParams(param);
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

				if (shopListFinal.size() < 1) {
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
		});

		// img_curl = (ImageButton) findViewById(R.id.img_curl);
		// img_curl.setOnClickListener(this);

		all_shop_list = (ListView) findViewById(R.id.all_shop_listview);
		all_shop_list.setDivider(null);
		all_shop_list.setDividerHeight(0);
		ad = new ShowAllShopAdapter();
		ad.notifyDataSetChanged();
		all_shop_list.setAdapter(ad);
		// all_shop_list.invalidate();
		// all_shop_list.invalidate();
		//ListWaveAnimation.showTheWave(con, all_shop_list);
		new ShopsBrandHitIncrease().execute();

		// progressValue = 0;
		// topMarginValue = 0;
	}

	private void shopDetailsInfo(final int pos) {
		slt = shopListFinal.get(pos);

		String ad = slt.getAddress();
		PrintLog.getWarnLog("adress :", ad + "<><>");

		gps = new GPSTracker(con);
		loadImage = new ImageLoader(con);
		if (gps.canGetLocation())

		{
			lat = gps.getLatitude();
			lng = gps.getLongitude();

			// PrintLog.getWarnLog("Current Lat : ", lat + "<><>");
			// PrintLog.getWarnLog("Current Lon : ", lng + "<><>");
		}

		shopProfileImage = (ImageView) findViewById(R.id.shop_details_profile_photo);
		if (slt.getShop_image().length() != 0)

		{
			loadImage.displayImage(
					BaseUrl.baseUrl
							+ slt.getShop_image().toString().trim()
									.replaceAll(" ", "%20"), shopProfileImage);
		}

		shopDetailsImgBtnBack = (ImageButton) findViewById(R.id.shop_details_back);
		shopDetailsImgBtnBack.setOnClickListener(this);

		shopDetailsImgBtnBrandName = (ImageButton) findViewById(R.id.shop_details_brand_names);
		shopDetailsImgBtnBrandName.setOnClickListener(this);

		shopDetailsImgBtnFacebook = (ImageButton) findViewById(R.id.shop_details_facebook);
		shopDetailsImgBtnFacebook.setOnClickListener(this);

		shopDetailsImgBtnTwitter = (ImageButton) findViewById(R.id.shop_details_twitter);
		shopDetailsImgBtnTwitter.setOnClickListener(this);

		shopDetailsImgBtnFavorite = (ImageButton) findViewById(R.id.shop_details_show_way_favorite_icon);
		shopDetailsImgBtnFavorite.setOnClickListener(this);

		db = new DatabaseHandler(con);
		fs = db.getAllShops();
		for (int i = 0; i < fs.size(); i++) {
			id = fs.get(i).getShop_id();
			if (id.equals(slt.getId()))

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

		shopDetailsImgBtnShowWay = (ImageButton) findViewById(R.id.shop_details_show_way);
		shopDetailsImgBtnShowWay.setOnClickListener(this);

		shopDetailsImgBtnWeb = (ImageButton) findViewById(R.id.shop_details_web);
		if (slt.getUrl().length() == 0) {
			shopDetailsImgBtnWeb.setVisibility(View.INVISIBLE);
		} else {
			shopDetailsImgBtnWeb.setVisibility(View.VISIBLE);
			shopDetailsImgBtnWeb.setOnClickListener(this);
		}

		shopDetailsImgBtnPhone = (ImageButton) findViewById(R.id.shop_details_phone_btn);
		if (slt.getPhone().length() == 0) {
			shopDetailsImgBtnPhone.setVisibility(View.INVISIBLE);
		} else {
			shopDetailsImgBtnPhone.setVisibility(View.VISIBLE);
			shopDetailsImgBtnPhone.setOnClickListener(this);
		}

		shopDetailsImgBtnEmail = (ImageButton) findViewById(R.id.shop_details_message);
		if (slt.getEmail().length() == 0) {
			shopDetailsImgBtnEmail.setVisibility(View.INVISIBLE);
		}

		else {
			shopDetailsImgBtnEmail.setVisibility(View.VISIBLE);
			shopDetailsImgBtnEmail.setOnClickListener(this);
		}

		shopDetailsImgBtnFace = (ImageButton) findViewById(R.id.shop_details_facebook_btn);
		if (slt.getFacebookUrl().length() == 0) {
			shopDetailsImgBtnFace.setVisibility(View.INVISIBLE);
		}

		else {
			shopDetailsImgBtnFace.setVisibility(View.VISIBLE);
			shopDetailsImgBtnFace.setOnClickListener(this);
		}

		txtAddress = (TextView) findViewById(R.id.shop_details_address);
		if (slt.getAddress().toString().trim().length() == 0) {
			txtAddress.setVisibility(View.GONE);
		}

		else {
			txtAddress.setVisibility(View.VISIBLE);

			if (slt.getAddress().length() > 18) {
				strShopAddress = slt.getAddress().toString().substring(0, 18);
				txtAddress.setText(strShopAddress);
			}

			else {
				txtAddress.setText(slt.getAddress().toString().trim() + "");
			}

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

				String textLeft = slt.getAddress().trim();
				txtAddress.setText(textLeft);
				txtAddress.setAnimation(animationToLeft);
				return false;
			}
		});

		txtZipCode = (TextView) findViewById(R.id.shop_details_zip_code);
		if (slt.getZip_code().toString().trim().length() == 0) {
			txtZipCode.setVisibility(View.GONE);
		}

		else {
			txtZipCode.setVisibility(View.VISIBLE);
			txtZipCode.setText(slt.getZip_code().toString().trim() + "");
		}

		txtPhone = (TextView) findViewById(R.id.shop_details_phone);
		if (slt.getPhone().toString().trim().length() == 0) {
			txtPhone.setVisibility(View.GONE);
		}

		else {
			txtPhone.setVisibility(View.VISIBLE);
			txtPhone.setText(slt.getPhone().toString().trim() + "");
		}

		txtOpeningHourMonFri = (TextView) findViewById(R.id.shop_details_opening_hour_fri_mon);
		if (slt.getM_w().toString().trim().length() == 0) {
			txtOpeningHourMonFri.setVisibility(View.GONE);
		}

		else {
			txtOpeningHourMonFri.setVisibility(View.VISIBLE);
			txtOpeningHourMonFri.setText(getString(R.string.mon_fri) + " "
					+ slt.getM_w().toString().trim() + "");
		}

		txtOpeningHourSat = (TextView) findViewById(R.id.shop_details_opening_hour_sat_day);
		if (slt.getS().toString().trim().length() == 0) {
			txtOpeningHourSat.setVisibility(View.GONE);
		}

		else {
			txtOpeningHourSat.setVisibility(View.VISIBLE);
			txtOpeningHourSat.setText(getString(R.string.sat) + " "
					+ slt.getS().toString().trim() + "");
		}

		txtShopTitle = (TextView) findViewById(R.id.shop_details_shop_title);

		if (slt.getShop_name().length() > 18) {
			strShopAndBrandTitle = slt.getShop_name().toString()
					.substring(0, 18);
			txtShopTitle.setText(strShopAndBrandTitle);

			txtShopTitle.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					txtShopTitle.setText(slt.getShop_name().toString().trim()
							+ "");
					return false;
				}
			});
		}

		else {
			txtShopTitle.setText(slt.getShop_name().toString().trim() + "");
		}

		txtShopDistance = (TextView) findViewById(R.id.shop_details_distance);

		double dis = 0;
		String strFinalDistance = "";
		try {
			dis = Double.parseDouble(slt.getDistance().trim());
			latitude = Double.parseDouble(slt.getLatitude().trim());
			longitude = Double.parseDouble(slt.getLongitude().trim());
			strFinalDistance = String.format("%.2f", dis);
			// PrintLog.getWarnLog("double dis : ", dis + "");
		} catch (Exception e) {
			// TODO: handle exception
		}

		txtShopDistance.setText(con.getString(R.string.distance_calculation)
				+ " " + strFinalDistance + " km.");

		// distanceInKm = (int) Math.ceil(dis);
		// distanceInM = distanceInKm * 1000;
		//
		// if (distanceInM >= 1000)
		//
		// {
		// int finalDistance = distanceInM / 1000;
		// txtShopDistance.setText(con
		// .getString(R.string.distance_calculation)
		// + " "
		// + finalDistance + " km.");
		// } else
		//
		// {
		// txtShopDistance.setText(con
		// .getString(R.string.distance_calculation)
		// + " "
		// + distanceInM + " m.");
		// }

		// To split comma and add to vector

		allBrandsId.removeAllElements();

		StringTokenizer st = new StringTokenizer(slt.getBrands_name(), ",");
		while (st.hasMoreTokens()) {

			allBrandsId.addElement(st.nextToken());
			Collections.sort(allBrandsId);
			// System.err.println(st.nextToken());
		}

		// To add data in spinner

		brandsOfShop.removeAllElements();

		for (int i = 0; i < allBrandsId.size(); i++)

		{
			brandName = brandName + allBrandsId.elementAt(i) + "\n";
			PrintLog.getWarnLog("Name of brand ", brandName);

		}

		shopDetailSpinner = (TextView) findViewById(R.id.shop_details_spinner);

		shopDetailSpinner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(con, R.style.ThemeDialogCustom);
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

		new ShopsBrandHitIncrease().execute();

	}

	private void settingInfo() {

		setting_email = (EditText) findViewById(R.id.setting_edt_email);
		setting_email.setText(SharePreferenceUtil.getUserEmail(con) + "");

		setting_password = (EditText) findViewById(R.id.setting_edt_code);
		setting_post_code = (EditText) findViewById(R.id.setting_postcode);
		setting_post_code.setText(SharePreferenceUtil.getPostCode(con) + "");

		setting_confirm_password = (EditText) findViewById(R.id.setting_edt_confirm_code);

		setting_email_cross = (ImageButton) findViewById(R.id.setting_email_cross);
		setting_email_cross.setOnClickListener(this);

		setting_password_cross = (ImageButton) findViewById(R.id.setting_edt_code_cross);
		setting_password_cross.setOnClickListener(this);

		setting_confirm_password_cross = (ImageButton) findViewById(R.id.setting_edt_confirm_code_cross);
		setting_confirm_password_cross.setOnClickListener(this);

		setting_post_code_cross = (ImageButton) findViewById(R.id.setting_btn_postcode_cross);
		setting_post_code_cross.setOnClickListener(this);

		settingimgBtnBack = (ImageButton) findViewById(R.id.setting_back);
		settingimgBtnBack.setOnClickListener(this);

		settingimgBtnBrandName = (ImageButton) findViewById(R.id.setting_brand_names);
		settingimgBtnBrandName.setOnClickListener(this);

		settingimgBtnFacebook = (ImageButton) findViewById(R.id.setting_facebook);
		settingimgBtnFacebook.setOnClickListener(this);

		settingimgBtnTwitter = (ImageButton) findViewById(R.id.setting_twitter);
		settingimgBtnTwitter.setOnClickListener(this);

		settingimgBtnSave = (ImageButton) findViewById(R.id.setting_btn_send);
		settingimgBtnSave.setOnClickListener(this);

		settingimgBtnDelete = (ImageButton) findViewById(R.id.setting_btn_delete_profile);
		settingimgBtnDelete.setOnClickListener(this);

		settingChkNews = (CheckBox) findViewById(R.id.setting_remember);
		settingChkNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (settingChkNews.isChecked())

				{
					strSettingAccept = "1";
				}

				else if (!settingChkNews.isChecked())

				{
					strSettingAccept = "0";
				}

			}
		});

		settingSeekbar = (SeekBar) findViewById(R.id.setting_seekbar);
		TextView txtProgress = (TextView) findViewById(R.id.setting_age_counter);
		txtProgress.setText(SharePreferenceUtil.getAge(con) + " "
				+ getString(R.string.year));
		txtProgress.setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		param.leftMargin = Integer.parseInt(SharePreferenceUtil
				.getLayoutParam(con));
		txtProgress.setLayoutParams(param);

		try {
			settingSeekbar.setProgress(Integer.parseInt(SharePreferenceUtil
					.getAgeProgress(con).toString()));
		} catch (Exception e) {
			// TODO: handle exception
		}

		settingSeekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						
						TextView txtProgress = (TextView) findViewById(R.id.setting_age_counter);
						txtProgress.setText("0-9 " + getString(R.string.year));
						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
								android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
								android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

						
						
						WindowManager w = LoginActivity.this.getWindowManager();
						Display d = w.getDefaultDisplay();
						DisplayMetrics metrics = new DisplayMetrics();
						d.getMetrics(metrics);
						double deviceWidth = metrics.widthPixels;
						
						//Log.w("deviceWidth", deviceWidth+"PX");
						
						if(deviceWidth>600)
							param.leftMargin += 80;
						
						txtProgress.setLayoutParams(param);

						if (Allconstants.screenSize.equalsIgnoreCase("320x480"))

						{
							if (progress >= 0) {
								param.leftMargin = 15;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "20";

							}
							if (progress >= 3) {
								param.leftMargin = 20;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "20";
							}

							if (progress >= 6) {
								param.leftMargin = 25;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "25";
							}

							if (progress >= 9) {
								param.leftMargin = progress * 2 + 12;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "30";
							}

							if (progress >= 10) {
								param.leftMargin = progress * 2 + 12;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("10-13 "
										+ getString(R.string.year));
								setting_age = "10-13";
								layoutParam = "32";
							}

							if (progress >= 14) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("14-17 "
										+ getString(R.string.year));
								setting_age = "14-17";
								layoutParam = "42";
							}

							if (progress >= 18) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("18-21 "
										+ getString(R.string.year));
								setting_age = "18-21";
								layoutParam = "54";
							}

							if (progress >= 22) {

								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("22-25 "
										+ getString(R.string.year));
								setting_age = "22-25";
								layoutParam = "66";
							}

							if (progress >= 26) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("26-30 "
										+ getString(R.string.year));
								setting_age = "26-30";
								layoutParam = "78";
							}

							if (progress >= 31) {
								param.leftMargin = progress * 3 - 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("31-40 "
										+ getString(R.string.year));
								setting_age = "31-40";
								layoutParam = "88";
							}

							if (progress >= 41) {
								param.leftMargin = progress * 3 - 10;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("41-50 "
										+ getString(R.string.year));
								setting_age = "41-50";
								layoutParam = "113";

							}

							if (progress >= 51) {
								param.leftMargin = progress * 3 - 25;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("51-60 "
										+ getString(R.string.year));
								setting_age = "51-60";
								layoutParam = "128";

							}

							if (progress >= 61) {
								param.leftMargin = progress * 3 - 25;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("61-70 "
										+ getString(R.string.year));
								setting_age = "61-70";
								layoutParam = "154";

							}

							if (progress >= 71) {
								param.leftMargin = progress * 3 - 35;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("71> "
										+ getString(R.string.year));
								setting_age = "71>";
								layoutParam = "178";

							}

							if (progress >= 97) {
								param.leftMargin = progress * 3 - 52;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("71> "
										+ getString(R.string.year));
								setting_age = "71>";
								layoutParam = "239";

							}
						}

						else {
							if (progress >= 0) {
								param.leftMargin = -15;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "-15";

							}
							if (progress >= 3) {
								param.leftMargin = -5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "-5";

							}

							if (progress >= 6) {
								param.leftMargin = progress;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "6";

							}

							if (progress >= 9) {
								param.leftMargin = progress * 2;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("0-9 "
										+ getString(R.string.year));
								setting_age = "0-9";
								layoutParam = "18";

							}

							if (progress >= 10) {
								param.leftMargin = progress * 2 + 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("10-13 "
										+ getString(R.string.year));
								setting_age = "10-13";
								layoutParam = "25";
							}

							if (progress >= 14) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("14-17 "
										+ getString(R.string.year));
								setting_age = "14-17";
								layoutParam = "42";

							}

							if (progress >= 18) {
								param.leftMargin = progress * 3;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("18-21 "
										+ getString(R.string.year));
								setting_age = "18-21";
								layoutParam = "54";

							}

							if (progress >= 22) {

								param.leftMargin = progress * 3 + 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("22-25 "
										+ getString(R.string.year));
								setting_age = "22-25";
								layoutParam = "71";

							}

							if (progress >= 26) {
								param.leftMargin = progress * 3 + 7;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("26-30 "
										+ getString(R.string.year));
								setting_age = "26-30";
								layoutParam = "85";

							}

							if (progress >= 31) {
								param.leftMargin = progress * 3 + 15;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("31-40 "
										+ getString(R.string.year));
								setting_age = "31-40";
								layoutParam = "108";

							}

							if (progress >= 41) {
								param.leftMargin = progress * 4 - 20;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("41-50 "
										+ getString(R.string.year));
								setting_age = "41-50";
								layoutParam = "144";

							}

							if (progress >= 51) {
								param.leftMargin = progress * 4 - 20;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("51-60 "
										+ getString(R.string.year));
								setting_age = "51-60";
								layoutParam = "184";
							}

							if (progress >= 61) {

								txtProgress.setText("61-70 "
										+ getString(R.string.year));
								setting_age = "61-70";
								layoutParam = "184";
							}

							if (progress >= 71) {

								txtProgress.setText("71> "
										+ getString(R.string.year));
								setting_age = "71>";
								layoutParam = "184";

							}

							if (progress >= 97) {
								param.leftMargin = progress * 4 - 5;
								txtProgress.setLayoutParams(param);
								txtProgress.setText("71> "
										+ getString(R.string.year));
								setting_age = "71>";
								layoutParam = "184";

							}
							
							Log.w("PROGRESS", progress+" PX");
							
							
							if(progress <= 10 && deviceWidth>600)
								param.leftMargin += 10;
							else if(progress <= 30 && deviceWidth>600)
								param.leftMargin += 50;
							else if(progress <= 40 && deviceWidth>600)
								param.leftMargin += 70;
							else if(progress <= 50 && deviceWidth>600)
								param.leftMargin += 90;
							else if(progress <= 70 && deviceWidth>600)
								param.leftMargin += 120;
							else if(progress <= 90 && deviceWidth>600)
								param.leftMargin += 150;
							else if(progress <= 100 && deviceWidth>600)
								param.leftMargin += 170;
							
							
							txtProgress.setLayoutParams(param);
							SharePreferenceUtil.setLayoutParam(con, param.leftMargin+"");

						}

						strProgress = String.valueOf(progress);

					}
				});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// city_brand_logo screen
		case R.id.city_brand_back:

			if (frScreen.equalsIgnoreCase("login")) {
				mainFliper.setDisplayedChild(0);
				Allconstants.strForBackButton = "login";

			}

			else if (frScreen.equalsIgnoreCase("edtscreen")) {
				mainFliper.setDisplayedChild(1);
				Allconstants.strForBackButton = "edit_pass";

			}

			else if (frScreen.equalsIgnoreCase("newuser")) {
				mainFliper.setDisplayedChild(2);
				Allconstants.strForBackButton = "new_user";

			}

			else if (frScreen.equalsIgnoreCase("shopdetails")) {
				mainFliper.setDisplayedChild(7);
				Allconstants.strForBackButton = "shop_details";
			}

			else if (frScreen.equalsIgnoreCase("allshop")) {
				Allconstants.strForBackButton = "all_shop";
				mainFliper.setDisplayedChild(4);
			}

			else if (frScreen.equalsIgnoreCase("search")) {
				mainFliper.setDisplayedChild(3);
				Allconstants.strForBackButton = "search";
			}

			else if (frScreen.equalsIgnoreCase("search_setting")) {
				mainFliper.setDisplayedChild(10);
				Allconstants.strForBackButton = "setting";
			}

			else if (frScreen.equalsIgnoreCase("map")) {
				mainFliper.setDisplayedChild(12);
				Allconstants.strForBackButton = "map";
			}

			break;

		case R.id.city_brand_names:
			if (frScreen.equalsIgnoreCase("login")) {
				mainFliper.setDisplayedChild(0);
			}

			else if (frScreen.equalsIgnoreCase("edtscreen")) {
				mainFliper.setDisplayedChild(1);
				Allconstants.strForBackButton = "edit_pass";
			}

			else if (frScreen.equalsIgnoreCase("newuser")) {
				mainFliper.setDisplayedChild(2);
				Allconstants.strForBackButton = "new_user";

			}

			else if (frScreen.equalsIgnoreCase("shopdetails")) {
				mainFliper.setDisplayedChild(7);
				Allconstants.strForBackButton = "shop_details";
			}

			else if (frScreen.equalsIgnoreCase("allshop")) {
				Allconstants.strForBackButton = "all_shop";
				mainFliper.setDisplayedChild(4);
			}

			else if (frScreen.equalsIgnoreCase("search")) {
				mainFliper.setDisplayedChild(3);
				Allconstants.strForBackButton = "search";
			}

			else if (frScreen.equalsIgnoreCase("search_setting")) {
				mainFliper.setDisplayedChild(10);
				Allconstants.strForBackButton = "setting";
			}

			else if (frScreen.equalsIgnoreCase("map")) {
				mainFliper.setDisplayedChild(12);
				Allconstants.strForBackButton = "map";
			}

			break;

		case R.id.city_brand_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_twitter:

			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		// Login layout

		case R.id.login_brand_names:
			frScreen = "login";
			Allconstants.strForBackButton = "login_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.login_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.login_twitter:
			
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.m_login_btn:
			if(!SharePreferenceUtil.isOnline(con))
			{
				ToastShow.getMessage(LoginActivity.this, "No internet available");
				return;
			}
			new LoginParse().execute();
			break;

		case R.id.login_btn_forget_password:
			Allconstants.strForBackButton = "edit_pass";
			getNewPassword();
			break;

		case R.id.login_email_cross:
			edt_login_email.setText("");
			break;

		case R.id.login_password_cross:
			edt_login_password.setText("");
			break;

		case R.id.login_btn_new_user:
			Allconstants.strForBackButton = "new_user";
			createNewUser();
			break;

		// Edit password layout

		case R.id.new_pasword_back:
			mainFliper.setDisplayedChild(0);
			Allconstants.strForBackButton = "login";

			break;

		case R.id.new_pasword_brand_names:
			frScreen = "edtscreen";
			Allconstants.strForBackButton = "edit_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.new_pasword_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.new_pasword_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.new_pasword_email_cross:
			edt_edit_email.setText("");
			break;

		case R.id.new_pasword_btn:
			
			if(!SharePreferenceUtil.isOnline(con))
			{
				ToastShow.getMessage(LoginActivity.this, "No internet available");
				return;
			}
			new EditPassword().execute();
			break;

		// New user layout

		case R.id.new_user_back:

			mainFliper.setDisplayedChild(0);
			Allconstants.strForBackButton = "login";
			if (popupwindow != null) {
				popupwindow.dismiss();
			}
			break;

		case R.id.new_user_brand_names:

			frScreen = "newuser";
			Allconstants.strForBackButton = "new_user_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.new_user_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.new_user_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.new_user_email_cross:
			new_user_email.setText("");
			break;

		case R.id.new_user_btn_postcode_cross:
			new_user_post_code.setText("");
			break;

		case R.id.new_user_edt_code_cross:
			new_user_password.setText("");
			break;

		case R.id.new_user_edt_confirm_code_cross:
			new_user_confirm_password.setText("");
			break;

		case R.id.new_user_btn_send:
			
			if(!SharePreferenceUtil.isOnline(con))
			{
				ToastShow.getMessage(LoginActivity.this, "No internet available");
				return;
			}

			if (dialog != null) {
				dialog.dismiss();
			}

			if (new_user_post_code.getText().toString().length() == 0
					|| new_user_email.getText().toString().length() == 0
					|| new_user_password.getText().toString().length() == 0
					|| new_user_confirm_password.getText().toString().length() == 0
					|| age.length() == 0) {
				emptyFieldNotification();
			} else if (!AllValidation.isValidEmail(LoginActivity.this, new_user_email
					.getText().toString())) {
				return;

			}

			else {

				if (!new_user_password.getText().toString()
						.equals(new_user_confirm_password.getText().toString()))

				{
					emptyFieldNotification();
					ToastShow.getMessage(LoginActivity.this,
							"Password and confirm password did not match.");
				}

				else

				{
					emptyFieldNotification();
					new NewUserRegistration().execute();
				}

			}

			break;

		case R.id.new_user_info_btn:

			new_user_male_female_layout.setVisibility(View.INVISIBLE);
			newUserimgBtnSend.setClickable(false);
			new_user_age_seekbar.setClickable(false);

			mainview = findViewById(R.id.new_user_main_bg);
			submenu_popup = inflater.inflate(R.layout.submenu_popup, null,
					false);

			submenu_popup.getBackground().setAlpha(200);

			popupwindow = new PopupWindow(submenu_popup, popupWidth,
					popupHeight, false);
			popupwindow.setOutsideTouchable(true);
			popupwindow.setTouchable(true);
			submenu_popup.setPadding(0, popupPaddingTop, 0, 0);

			popupwindow.showAtLocation(mainview, Gravity.TOP, 0, popupYXis);

			popup_cross = (ImageButton) submenu_popup
					.findViewById(R.id.new_user_info_img_button);
			popup_cross.setOnClickListener(this);

			break;

		case R.id.new_user_info_img_button:
			popupwindow.dismiss();
			new_user_male_female_layout.setVisibility(View.VISIBLE);
			newUserimgBtnSend.setClickable(true);
			new_user_age_seekbar.setClickable(true);
			break;

		// setting page

		case R.id.setting_back:
			mainFliper.setDisplayedChild(3);
			Allconstants.strForBackButton = "search";
			if (popupwindow != null) {
				popupwindow.dismiss();
			}
			break;

		case R.id.setting_brand_names:
			frScreen = "search_setting";
			Allconstants.strForBackButton = "setting_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.setting_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.setting_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.setting_email_cross:
			setting_email.setText("");
			break;

		case R.id.setting_btn_postcode_cross:
			setting_post_code.setText("");
			break;

		case R.id.setting_edt_code_cross:
			setting_password.setText("");
			break;

		case R.id.setting_edt_confirm_code_cross:
			setting_confirm_password.setText("");
			break;

		case R.id.setting_btn_delete_profile:

			mainview = findViewById(R.id.m_lin);
			submenu_popup = inflater.inflate(R.layout.submenu_setting_popup,
					null, false);

			submenu_popup.getBackground().setAlpha(200);

			popupwindow = new PopupWindow(submenu_popup, popupWidth,
					popupHeight, false);
			popupwindow.setOutsideTouchable(true);
			popupwindow.setTouchable(true);
			submenu_popup.setPadding(0, popupPaddingTop, 0, 0);

			popupwindow.showAtLocation(mainview, Gravity.TOP, 0, popupYXis);

			setting_popup_cross = (ImageButton) submenu_popup
					.findViewById(R.id.setting_popup_cross_btn);
			setting_popup_cross.setOnClickListener(this);

			setting_popup_send = (ImageButton) submenu_popup
					.findViewById(R.id.setting_popup_send);
			setting_popup_send.setOnClickListener(this);

			break;

		case R.id.setting_popup_cross_btn:
			if (popupwindow != null) {
				popupwindow.dismiss();
			}
			break;

		case R.id.setting_popup_send:

			
			
			if(!SharePreferenceUtil.isOnline(con))
			{
				ToastShow.getMessage(LoginActivity.this, "No internet available");
				return;
			}
			popupwindow.dismiss();
			new DeleteUser().execute();

			break;

		case R.id.setting_btn_send:
			
			if(!SharePreferenceUtil.isOnline(con))
			{
				ToastShow.getMessage(LoginActivity.this, "No internet available");
				return;
			}
			

			if (setting_password.getText().toString().length() != 0
					&& setting_confirm_password.getText().toString().length() != 0
					&& setting_post_code.getText().toString().length() != 0
					&& setting_email.getText().toString().length() != 0)

			{
				if (!setting_password.getText().toString()
						.equals(setting_confirm_password.getText().toString()))

				{
					ToastShow.getMessage(LoginActivity.this,
							"Password and confirm password did not match.");
				}

				else {

					new EditProfile().execute();
					// SharePreferenceUtil.setAgeProgress(con,
					// strProgress);
					// SharePreferenceUtil.setLayoutParam(con, layoutParam);

				}
			}

			else {
				ToastShow.getMessage(LoginActivity.this, "All fields must be filled up.");
			}

			break;

		// search screen

		case R.id.search_back_btn:
			mainFliper.setDisplayedChild(0);
			SharePreferenceUtil.setLoginStatus(con, "logout");
			Allconstants.strForBackButton = "login";
			break;

		case R.id.search_brand_names:
			frScreen = "search";
			Allconstants.strForBackButton = "search_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.search_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.search_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.search_final_search:
			temp = db.getAllShops();
			strSearchItem = searchEdtTxt.getText().toString().trim();
			hitIncreaseUrl = AllUrl.increaseBrandHitURL;
			hitUserOrBrandId = AllBrandsList.getBrandIdByName(strSearchItem);
			PrintLog.showLog("hitUserOrBrandId from holder : "
					+ hitUserOrBrandId);

			PrintLog.getWarnLog("progressValue : ", progressValue + " Value");

			PrintLog.getWarnLog("topMargin : ", topMarginValue + " topMargin");

			Allconstants.strForBackButton = "all_shop";

			PrintLog.getWarnLog("strSearchItem", strSearchItem);

			if (strBrandOrShop.equalsIgnoreCase("Brand")) {
				shopList = AllShopLists
						.getAllShopsInfoByBrandName(strSearchItem);
			}

			else if (strBrandOrShop.equalsIgnoreCase("Shop")) {
				shopList = AllShopLists
						.getAllShopsInfoByShopName(strSearchItem);
			}

			shopListFinal = new ArrayList<ShopList>();

			for (int i = 0; i < shopList.size(); i++)

			{

				double final_distance = Double.parseDouble(shopList.get(i)
						.getDistance());

				// PrintLog.getWarnLog("final_distance", final_distance +
				// "<><>");

				if (final_distance <= spinnerDistanceInKm / 1000)

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

			if (shopListFinal.size() != 0)

			{
				// new ShowAllShopsList().execute();
				Allconstants.shopList = shopList;
				Allconstants.mainFliper = mainFliper;
				Allconstants.activity = LoginActivity.this;
				Allconstants.shopListFinal = shopListFinal;

				PrintLog.getWarnLog("Allconstants.shopList",
						Allconstants.shopList.size() + "<><>");
				allShopInfo();
				getLat();
			}

			else

			{
				// ToastShow.getMessage(con,
				// "There is no brand with this name and radius");
				Allconstants.shopList = shopList;
				Allconstants.mainFliper = mainFliper;
				Allconstants.activity = LoginActivity.this;
				Allconstants.shopListFinal = shopListFinal;
				allShopInfo();
				getLat();
			}

			break;

		case R.id.search_search_btn:

			break;

		case R.id.search_edt_search_text_cross:
			searchEdtTxt.setText("");
			break;

		case R.id.search_setting_btn:
			Allconstants.strForBackButton = "setting";
			mainFliper.setDisplayedChild(10);
			settingInfo();
			break;

		case R.id.search_logout:
			SharePreferenceUtil.setLoginStatus(con, "logout");
			mainFliper.setDisplayedChild(0);
			Allconstants.strForBackButton = "login";
			break;

		// all shop

		case R.id.all_shop_back:
			Allconstants.strForBackButton = "search";
			mainFliper.setDisplayedChild(3);
			break;

		case R.id.all_shop_brand_names:
			frScreen = "allshop";
			Allconstants.strForBackButton = "all_shop_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.all_shop_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.all_shop_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		// shop details screen

		case R.id.shop_details_back:
			mainFliper.setDisplayedChild(4);
			Allconstants.strForBackButton = "shop_details";
			break;

		case R.id.shop_details_brand_names:
			frScreen = "shopdetails";
			Allconstants.strForBackButton = "shop_details_br";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.shop_details_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.shop_details_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.shop_details_show_way_favorite_icon:
		
			if (flag == false) {
				db.addShops(new FavoriteShops(slt.getId().trim(), slt
						.getShop_name().trim(), slt.getDistance().trim(), slt
						.getLatitude().trim(), slt.getLongitude().trim()));
				// ToastShow.getMessage(ins, "added to favorite");
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_selected);
				flag = true;
			}

			else {
				db.deleteSingleFavoriteShop(slt.getId());
				shopDetailsImgBtnFavorite
						.setBackgroundResource(R.drawable.list_favorite_normal);
				flag = false;
			}
			break;

		case R.id.shop_details_show_way:
			
			if(!SharePreferenceUtil.isOnline(con))
			{
				ToastShow.getMessage(LoginActivity.this, "No internet available");
				return;
			}
			
			Allconstants.drawRoute(LoginActivity.this,  latitude,
					longitude,lat, lng);

			break;

		case R.id.shop_details_web:
			Allconstants.browseUrl(con, slt.getUrl().toString().trim()
					.replaceAll(" ", "%20"));
			break;

		case R.id.shop_details_phone_btn:
			final Dialog d = new Dialog(con, R.style.ThemeDialogCustom);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setContentView(R.layout.dialog_box);
			d.show();
			final Button yes = (Button) d.findViewById(R.id.dialog_yes);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Allconstants.makePhoneCall(con, slt.getPhone().toString()
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
			SharePreferenceUtil.setEmailPressed(con, true);
			Allconstants.semdEmail(con, slt.getEmail().toString().trim() + "");
			break;

		case R.id.shop_details_facebook_btn:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		// map screen

		case R.id.table_row_map_curl:
			Allconstants.strForBackButton = "all_shop";
			mainFliper.setDisplayedChild(4);
			break;

		case R.id.map_view_back:
			Allconstants.strForBackButton = "all_shop";
			mainFliper.setDisplayedChild(4);
			break;

		case R.id.map_view_brand_names:
			Allconstants.strForBackButton = "map_br";
			frScreen = "map";
			mainFliper.setDisplayedChild(8);
			break;

		case R.id.map_view_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.map_view_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		}

	}

	/**
	 * 
	 * To Edit Password via email
	 */

	class EditPassword extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (edt_edit_email.getText().toString().length() == 0) {
				ToastShow.getMessage(LoginActivity.this, "You should enter an email address");
				return;
			}

			if (!AllValidation.isValidEmail(LoginActivity.this, edt_edit_email.getText()
					.toString())) {
				return;
			}
			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {

			HttpPost httppostt = new HttpPost(AllUrl.editPasswordUrl);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("email",
						edt_edit_email.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("app_id",
						Allconstants.appId));

				httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = MyHTTPRequest.getData(httppostt);

				PrintLog.getDebugLog("Response : ", response + "<><>");

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();

				mainFliper.setDisplayedChild(11);
				txtThanks = (TextView) findViewById(R.id.txt_thanks);
				txtThanksMsg = (TextView) findViewById(R.id.txt_thanks_msg);

				if (response.equalsIgnoreCase("email_send")) {
					txtThanksMsg
							.setText("A new password has been submitted by mail");
					mHandler.postDelayed(mRunnable, duration);

				}

				else if (response.equalsIgnoreCase("email_not_exist")) {
					strCongratulation = response;
					txtThanksMsg.setText("" + response);
					mHandler.postDelayed(mRunnable, duration);
				}

			}
		}
	}

	private final Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (strCongratulation.equalsIgnoreCase("editinfo"))

			{
				mainFliper.setDisplayedChild(3);

				strCongratulation = "";
			}

			else if (strCongratulation.equalsIgnoreCase("email_not_exist"))

			{
				mainFliper.setDisplayedChild(1);
				strCongratulation = "";
			}

			else if (strCongratulation.equalsIgnoreCase("not success"))

			{
				mainFliper.setDisplayedChild(0);
				strCongratulation = "";
			}
			//
			// else if (strCongratulation.equalsIgnoreCase("user_not_exist"))
			//
			// {
			// mainFliper.setDisplayedChild(10);
			// strCongratulation = "";
			// }
			//
			// else if (strCongratulation
			// .equalsIgnoreCase("user_authentication_fail"))
			//
			// {
			// mainFliper.setDisplayedChild(10);
			// strCongratulation = "";
			// }
			//
			// else if (strCongratulation.equalsIgnoreCase("Security Error!!!"))
			//
			// {
			// mainFliper.setDisplayedChild(10);
			// strCongratulation = "";
			// }

			else

			{
				mainFliper.setDisplayedChild(0);
			}

		}
	};

	/***
	 * 
	 * To login
	 * 
	 */
	// findlogin
	class LoginParse extends AsyncTask<String, String, String> {
		HttpPost http_login;

		HttpPost http_all_brand;

		// HttpPost http_all_shop;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (edt_login_email.getText().toString().length() == 0) {
				ToastShow.getMessage(LoginActivity.this, "You should enter an email address");
				return;
			}

			if (edt_login_password.getText().toString().length() == 0) {
				ToastShow.getMessage(LoginActivity.this, "You should enter password");
				return;
			}
			pd = ProgressDialog.show(con, "Please wait..", "Loading...", false,
					false);
		}

		@Override
		protected String doInBackground(String... aurl) {

			http_login = new HttpPost(AllUrl.loginUrl);
			// http_all_brand = new HttpPost(AllUrl.allBrandsUrl);
			// http_all_shop = new HttpPost(AllUrl.allShopsUrl);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("email",
						edt_login_email.getText().toString() + ""));
				nameValuePairs.add(new BasicNameValuePair("password",
						edt_login_password.getText().toString() + ""));
				nameValuePairs.add(new BasicNameValuePair("app_id",
						Allconstants.appId));

				http_login.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = MyHTTPRequest.getData(http_login);
				PrintLog.getDebugLog("Login Response : ", response + "");
				// strBrandsResponse = MyHTTPRequest.getData(http_all_brand);
				// strShopResponse = MyHTTPRequest.getData(http_all_shop);

				if (LoginParser.connect(con, response))

				{
					PrintLog.getWarnLog("Login status in activity : ",
							Login.loginStatus + "");
				}

				// if (BrandsListParser.connect(con, strBrandsResponse))
				//
				// {
				// PrintLog.getDebugLog(
				// "AllBrandsList.getAllBrandsList().size()",
				// AllBrandsList.getAllBrandsList().size() + "");
				// }

				// if (ShopListParser.ParseQuery(con, strShopResponse)) {
				// PrintLog.getDebugLog(
				// "AllShopLists.getAllShopList().size()",
				// AllShopLists.getAllShopList().size() + "");
				// }

			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			} catch (URISyntaxException e) {

				e.printStackTrace();
			} catch (JSONException e) {

				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();

				if (Login.loginStatus.equalsIgnoreCase("Login successful.")) {
					Allconstants.strForBackButton = "search";

					if (chk_login_remember.isChecked()) {

						SharePreferenceUtil.setUser(con, edt_login_email
								.getText().toString() + "");
						SharePreferenceUtil.setPass(con, edt_login_password
								.getText().toString() + "");
					}

					else if (!chk_login_remember.isChecked())

					{
						SharePreferenceUtil.setPass(con, "");
						SharePreferenceUtil.setUser(con, "");
					}

					SharePreferenceUtil.setLoginStatus(con, "login");

					getSearchScreenInfo();

				} else {

					ToastShow.getMessage(LoginActivity.this, Login.loginStatus);

					if (chk_login_remember.isChecked()) {

						SharePreferenceUtil.setUser(con, edt_login_email
								.getText().toString() + "");
						SharePreferenceUtil.setPass(con, edt_login_password
								.getText().toString() + "");
					}

					else if (!chk_login_remember.isChecked())

					{
						SharePreferenceUtil.setPass(con, "");
						SharePreferenceUtil.setUser(con, "");
					}
				}

			}
		}
	}

	/***
	 * 
	 * New user registraion
	 * 
	 */

	class EditProfile extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {

			HttpPost httppostt = new HttpPost(AllUrl.editUserURL + Login.userID);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("email",
						setting_email.getText().toString() + ""));

				nameValuePairs.add(new BasicNameValuePair("password",
						setting_password.getText().toString() + ""));

				nameValuePairs.add(new BasicNameValuePair("gender",
						Login.gender));

				nameValuePairs.add(new BasicNameValuePair("age", setting_age
						+ ""));

				nameValuePairs.add(new BasicNameValuePair("postcode",
						setting_post_code.getText().toString() + ""));

				nameValuePairs.add(new BasicNameValuePair("accept",
						strSettingAccept));

				nameValuePairs.add(new BasicNameValuePair("status", "1"));

				nameValuePairs.add(new BasicNameValuePair("app_id",
						Allconstants.appId));

				httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = MyHTTPRequest.getData(httppostt);

				PrintLog.getDebugLog("Response : ", response + "<><>");

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();

				strCongratulation = "editinfo";
				mainFliper.setDisplayedChild(11);
				txtThanks = (TextView) findViewById(R.id.txt_thanks);
				txtThanks.setText(getString(R.string.thanks) + "");

				txtThanksMsg = (TextView) findViewById(R.id.txt_thanks_msg);
				txtThanksMsg.setText(getString(R.string.edit_info));

				mHandler.postDelayed(mRunnable, duration);

			}
		}
	}

	/***
	 * 
	 * New user registraion
	 * 
	 */

	class NewUserRegistration extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {

			HttpPost httppostt = new HttpPost(AllUrl.registraionUrl);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("email",
						new_user_email.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("password",
						new_user_password.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("gender",
						strMaleFemale));

				nameValuePairs.add(new BasicNameValuePair("age", age));

				nameValuePairs.add(new BasicNameValuePair("postcode",
						new_user_post_code.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("accept", strAccept));

				nameValuePairs.add(new BasicNameValuePair("status", "1"));

				nameValuePairs.add(new BasicNameValuePair("app_id",
						Allconstants.appId));

				httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = MyHTTPRequest.getData(httppostt);

				PrintLog.getDebugLog("Response : ", response + "<><>");

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();

				PrintLog.getErrorLog("new_user_email.getText().toString()",
						new_user_email.getText().toString() + "");

				txtThanks = (TextView) findViewById(R.id.txt_thanks);
				txtThanksMsg = (TextView) findViewById(R.id.txt_thanks_msg);
				
			Log.e("r response: ", response);

				if (response.equalsIgnoreCase("success")) {
					mainFliper.setDisplayedChild(11);

					txtThanksMsg
							.setText(getString(R.string.registration_success)
									+ "");
					mHandler.postDelayed(mRunnable, duration);

				}

				else {
					strCongratulation = "not success";
					mainFliper.setDisplayedChild(11);
					mHandler.postDelayed(mRunnable, duration);

				}

			}
		}
	}

	/***
	 * 
	 * Delete profile
	 * 
	 */

	private class DeleteUser extends AsyncTask<String, String, String> {

		private HttpPost httppostt;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {

			httppostt = new HttpPost(AllUrl.deleteUserURL + Login.userID);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("USER_ID", Login.userID
					+ ""));
			nameValuePairs.add(new BasicNameValuePair("email", Login.userEmail
					.toString().trim() + ""));

			nameValuePairs.add(new BasicNameValuePair("app_id",
					Allconstants.appId + ""));

			try {
				httppostt.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				strDeleteResponse = MyHTTPRequest.getData(httppostt);

				PrintLog.getWarnLog("strDeleteResponse", strDeleteResponse);

			} catch (IOException e) {

				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();
			}

			mainFliper.setDisplayedChild(11);

			txtThanks = (TextView) findViewById(R.id.txt_thanks);
			txtThanks.setText(getString(R.string.delete_profile_implemented)
					+ "");

			txtThanksMsg = (TextView) findViewById(R.id.txt_thanks_msg);
			txtThanksMsg
					.setText(getString(R.string.delete_profile_implemented_msg));

			mHandler.postDelayed(mRunnable, duration);

		}
	}

	/***
	 * 
	 * Show Brand Hit Increasing
	 * 
	 */

	private class ShopsBrandHitIncrease extends
			AsyncTask<String, String, String> {

		HttpPost increaseHitPost;

		@Override
		protected String doInBackground(String... aurl) {

			increaseHitPost = new HttpPost(hitIncreaseUrl
					+ hitUserOrBrandId.replaceAll(" ", "%20"));
			PrintLog.showHitCounterLog("Hit URL: ", hitIncreaseUrl
					+ hitUserOrBrandId.replaceAll(" ", "%20"));

			// List<NameValuePair> nameValuePairs = new
			// ArrayList<NameValuePair>();
			//
			// nameValuePairs.add(new BasicNameValuePair(hitKeyUserOrBrandId,
			// hitUserOrBrandId));

			PrintLog.showLog("hitIncreaseUrl : " + hitIncreaseUrl);
			PrintLog.showLog("hitUserOrBrandId : " + hitUserOrBrandId);
			PrintLog.showLog("hitKeyIncreaseBrandOrShop : "
					+ hitKeyUserOrBrandId);

			try {
				// increaseHitPost = new HttpPost(hitIncreaseUrl
				// + hitUserOrBrandId);
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

	private class ShowAllShopAdapter extends BaseAdapter {

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

		@SuppressLint("DefaultLocale")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_all_shops, null);
			}

			if (position < shopListFinal.size()) {
				// spl = AllShopLists.getShopList(position);

				double dis = 0;
				String strFinalDistance = "";
				try {
					dis = Double.parseDouble(shopListFinal.get(position)
							.getDistance().trim());
					strFinalDistance = String.format("%.2f", dis);

					// PrintLog.getWarnLog("double dis : ", dis + "");
				} catch (Exception e) {
					// TODO: handle exception
				}

				final TextView shop_title = (TextView) v
						.findViewById(R.id.row_all_shop_title);
				String name = "";

				if (shopListFinal.get(position).getShop_name().length() > 24) {
					name = shopListFinal.get(position).getShop_name()
							.substring(0, 24);
					shop_title.setText(name);
				} else {
					shop_title.setText(shopListFinal.get(position)
							.getShop_name().trim());
				}

				// shop_title.setText(spl.getShop_name().toString().trim());

				shop_title.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						String textLeft = shopListFinal.get(position)
								.getShop_name().trim();
						if(textLeft.length()>24)
						{
							Animation animationToLeft = new TranslateAnimation(
									shop_title.getWidth(), -shop_title.getWidth(),
									0, 0);
							animationToLeft.setDuration(5000);
							animationToLeft.setRepeatMode(0);
							animationToLeft.setRepeatCount(0);
							shop_title.setText(textLeft);
							shop_title.setAnimation(animationToLeft);
							
							return true;

						}
						
						return false;
					}
				});

				shop_title.setOnClickListener(new  OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mainFliper.setDisplayedChild(7);
						Allconstants.strForBackButton = "shop_details";
						hitIncreaseUrl = AllUrl.increaseShopHitURL;
						hitUserOrBrandId = shopListFinal.get(position).getId()
								.trim();
						hitKeyUserOrBrandId = "SHOP_ID";
						Allconstants.spl = shopListFinal.get(position);
						shopDetailsInfo(position);
					}
				});
				
				final TextView distance = (TextView) v
						.findViewById(R.id.row_all_shop_distance);

				distance.setText(con.getString(R.string.distance_calculation)
						+ " " + strFinalDistance + " km.");

			

				final ImageButton f_icon = (ImageButton) v
						.findViewById(R.id.row_all_shop_favorite_icon);

				for (int k = 0; k < temp.size(); k++) {

					if (temp.get(k)
							.getShop_id()
							.equalsIgnoreCase(
									shopListFinal.get(position).getId())) {
						f_icon.setBackgroundResource(R.drawable.list_favorite_selected);
					}
				}

				f_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ShopList sl =
						// AllShopLists.getAllShopList().elementAt(
						// position);

						String strId = "";
						for (int i = 0; i < temp.size(); i++) {

							if (temp.get(i)
									.getShop_id()
									.equalsIgnoreCase(
											shopListFinal.get(position).getId())) {
								strId = temp.get(i).getShop_id();
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
								.getId()) || sFlag == false) {
							db.deleteSingleFavoriteShop(shopListFinal.get(
									position).getId());
							f_icon.setBackgroundResource(R.drawable.list_favorite_normal);
							sFlag = true;

						}

						else {
							f_icon.setBackgroundResource(R.drawable.list_favorite_selected);
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
							sFlag = false;

						}

					}
				});

			}

			final ImageButton arrow_icon = (ImageButton) v
					.findViewById(R.id.row_all_shop_arrow_icon);
			arrow_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					gps = new GPSTracker(con);

					double lati = 0, lon = 0;

					if (gps.canGetLocation())

					{
						lat = gps.getLatitude();
						lng = gps.getLongitude();
					}

					try {

						lati = Double.parseDouble(shopListFinal.get(position)
								.getLatitude().trim());
						lon = Double.parseDouble(shopListFinal.get(position)
								.getLongitude()

								.trim());

						// PrintLog.getWarnLog("double dis : ", dis + "");
					} catch (Exception e) {
						// TODO: handle exception
					}

					
					if(!SharePreferenceUtil.isOnline(con))
					{
						ToastShow.getMessage(LoginActivity.this, "No internet available");
						return;
					}
					
					Allconstants.drawRoute(LoginActivity.this,  lati,
							lon,lat, lng);

				}
			});

			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mainFliper.setDisplayedChild(7);
					Allconstants.strForBackButton = "shop_details";
					hitIncreaseUrl = AllUrl.increaseShopHitURL;
					hitUserOrBrandId = shopListFinal.get(position).getId()
							.trim();
					hitKeyUserOrBrandId = "SHOP_ID";
					Allconstants.spl = shopListFinal.get(position);
					shopDetailsInfo(position);

				}
			});

			// TODO Auto-generated method stub
			return v;

		}


	}

	// To notify whether any field is empty

	private void emptyFieldNotification() {

		if (new_user_email.getText().toString().length() == 0) {
			img_email_miss.setVisibility(View.VISIBLE);

		} else {
			img_email_miss.setVisibility(View.INVISIBLE);

		}

		if (new_user_password.getText().toString().length() == 0) {
			img_password_miss.setVisibility(View.VISIBLE);

		} else {
			img_password_miss.setVisibility(View.INVISIBLE);

		}

		if (new_user_confirm_password.getText().toString().length() == 0) {
			img_password_miss.setVisibility(View.VISIBLE);

		} else {
			img_password_miss.setVisibility(View.INVISIBLE);

		}

		if (new_user_post_code.getText().toString().length() == 0) {
			img_post_code_miss.setVisibility(View.VISIBLE);

		} else {
			img_post_code_miss.setVisibility(View.INVISIBLE);

		}

		if (new_user_post_code.getText().toString().length() == 0
				|| new_user_email.getText().toString().length() == 0
				|| new_user_password.getText().toString().length() == 0
				|| new_user_confirm_password.getText().toString().length() == 0
				|| age.length() == 0) {

			emptyWarning.setVisibility(View.VISIBLE);
		} else {

			emptyWarning.setVisibility(View.INVISIBLE);
		}

		if (age.length() == 0) {
			img_seekbar_miss_icon.setVisibility(View.VISIBLE);
		} else {
			img_seekbar_miss_icon.setVisibility(View.INVISIBLE);
		}

	}

	// Back from device back button

	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Allconstants.strForBackButton.equalsIgnoreCase("new_user")) {

				Allconstants.strForBackButton = "login";
				mainFliper.setDisplayedChild(0);
			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("edit_pass")) {

				Allconstants.strForBackButton = "login";
				mainFliper.setDisplayedChild(0);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("login_br")) {

				Allconstants.strForBackButton = "login";
				mainFliper.setDisplayedChild(0);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("login")) {

				showDialog(1);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("edit_br")) {
				Allconstants.strForBackButton = "edit_pass";
				mainFliper.setDisplayedChild(1);
			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("new_user_br")) {
				Allconstants.strForBackButton = "new_user";
				mainFliper.setDisplayedChild(2);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("search")) {
				Allconstants.strForBackButton = "login";
				mainFliper.setDisplayedChild(0);
				SharePreferenceUtil.setLoginStatus(con, "logout");
			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("search_br")) {
				Allconstants.strForBackButton = "search";
				mainFliper.setDisplayedChild(3);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("setting")) {
				Allconstants.strForBackButton = "search";
				if (popupwindow != null) {
					popupwindow.dismiss();
				}
				mainFliper.setDisplayedChild(3);

			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("setting_br")) {
				Allconstants.strForBackButton = "setting";

				if (popupwindow != null) {
					popupwindow.dismiss();
				}
				mainFliper.setDisplayedChild(10);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("all_shop")) {
				Allconstants.strForBackButton = "search";
				mainFliper.setDisplayedChild(3);
			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("all_shop_br")) {
				Allconstants.strForBackButton = "all_shop";
				mainFliper.setDisplayedChild(4);
			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("shop_details")) {
				Allconstants.strForBackButton = "all_shop";
				mainFliper.setDisplayedChild(4);
			}

			else if (Allconstants.strForBackButton
					.equalsIgnoreCase("shop_details_br")) {
				Allconstants.strForBackButton = "shop_details";
				mainFliper.setDisplayedChild(7);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("map")) {
				Allconstants.strForBackButton = "all_shop";
				mainFliper.setDisplayedChild(4);
			}

			else if (Allconstants.strForBackButton.equalsIgnoreCase("map_br")) {
				Allconstants.strForBackButton = "map";
				mainFliper.setDisplayedChild(12);
			}

			return true;

		}
		return false;

	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(final int id) {
		switch (id) {

		case 1: {
			final AlertDialog alertbuilder = new AlertDialog.Builder(
					LoginActivity.this).create();
			// alertbuilder.setTitle(Splash.title);
			alertbuilder
					.setMessage("Are you sure you want to exit from the app?");
			alertbuilder.setButton(DialogInterface.BUTTON_POSITIVE, "No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(final DialogInterface dialog,
								final int which) {

						}
					});
			alertbuilder.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(final DialogInterface dialog,
								final int which) {

							finish();

							SharePreferenceUtil.setLoginStatus(con, "logout");

						}
					});

			return alertbuilder;
		}
		}
		return super.onCreateDialog(id);
	}

	// Map view Screen

	private void showMap() {
		mainFliper.setDisplayedChild(12);
		mapveiw = (MapView) findViewById(R.id.map_view_map);
		mapveiw.setBuiltInZoomControls(true);
		mapOverlays = mapveiw.getOverlays();

		curl_to_lsit = (TableRow) findViewById(R.id.table_row_map_curl);
		curl_to_lsit.setOnClickListener(this);

		mapImgBtnBack = (ImageButton) findViewById(R.id.map_view_back);
		mapImgBtnBack.setOnClickListener(this);

		mapImgBtnBrandName = (ImageButton) findViewById(R.id.map_view_brand_names);
		mapImgBtnBrandName.setOnClickListener(this);

		mapImgBtnFacebook = (ImageButton) findViewById(R.id.map_view_facebook);
		mapImgBtnFacebook.setOnClickListener(this);

		mapImgBtnTwitter = (ImageButton) findViewById(R.id.map_view_twitter);
		mapImgBtnTwitter.setOnClickListener(this);

		// first overlay
		drawable = getResources().getDrawable(R.drawable.list_indicator);
		itemizedOverlay = new MyItemizedOverlay(drawable, mapveiw);
		if (size == 0) {

			ToastShow.getMessage(LoginActivity.this, "No lat/lon found");
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

	private void getLat() {

		CurrentMapPoint.removeAllPoints();
		// Size should be AllShopLists.getShopAddress().size()
		for (int i = 0; i < shopListFinal.size(); i++) {

			try {

				address = shopListFinal.get(i).getShop_name() + "\n"
						+ shopListFinal.get(i).getAddress() + "\n"
						+ shopListFinal.get(i).getZip_code();
				latt = Double.parseDouble(shopListFinal.get(i).getLatitude());
				lon = Double.parseDouble(shopListFinal.get(i).getLongitude());
				// Log.w("Lat : ", latt + "<><><>");
				// Log.w("Lon : ", lon + "");

				MapLocation loc = null;

				try {
					loc = new MapLocation(address, latt, lon);

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

	// action of show map button

	public void showMap(View v)

	{
		if (size == 0)

		{
			ToastShow.getMessage(LoginActivity.this, "No lat/lon found to show map");
		}

		else {
			Allconstants.strForBackButton = "map";
			Allconstants.screenShopMap = "allshop";
			showMap();
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyTabActivity.tabIndex = 0;
	}
}
