package com.shehala.citybrands;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.shehala.citybrands.R;
import com.shehala.citybrands.download.image.ImageLoader;
import com.shehala.citybrands.holder.AllNewsList;
import com.shehala.citybrands.model.NewsList;
import com.shehala.citybrands.parser.NewsListParser;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.BaseUrl;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;

@SuppressLint({ "SimpleDateFormat", "SetJavaScriptEnabled" })
public class NewsActivity extends Activity implements OnClickListener {

	private Context con;
	private static NewsActivity instanc = null;

	/**
	 * @return the instanc
	 */
	public static NewsActivity getInstanc() {
		return instanc;
	}

	private String whichSreen = "newsList";
	private ImageButton imgBtnBrandName, imgBtnFacebook, imgBtnTwitter,
			imgBtnBack, cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;
	private ViewFlipper mainFliper;
	private ListView newsList;
	private String response = "";
	private ProgressDialog pd;
	private NewsListArrayAdapter adapter;
	private WebView vedioView;
	@SuppressWarnings("unused")
	private ImageView logo, video_pause;
	private Handler mHandler = new Handler();
	private static int duration = 2000;
	private View mainview;
	private View submenu_popup;
	private PopupWindow popupwindow;
	private LayoutInflater inflater;
	private ImageLoader loadImage;
	private Calendar dateTime;
	private Date currentDate;
	private Date modifiedDate;
	private SimpleDateFormat dateFormater;
	private String strCurrentDateTime = "", strModifiedDateTime = "";
	private Long longCurrentDate, longModifiedDate;
	private Vector<String> newsIndicatorVector = new Vector<String>();
	private int popupBottom = 0;
	private String strBack = "newsMain";
	private String current_url = "", youtubeId = "";
	@SuppressWarnings("unused")
	private boolean isPlaying = false;

	// news details screen

	private TextView txt_details, details_title;
	private ImageButton btn_next, btn_cross;
	private NewsList nsl;
	private int pos = 0;
	private ImageButton detailsBtnBrandName, detailsBtnFacebook,
			detailsBtnTwitter, detailsBtnBack;
	
	private int new_news_item=0;
	private Vector<NewsList> _allNewsList = new Vector<NewsList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.newsflipper);

		con = this;

		if (Allconstants.screenSize.equalsIgnoreCase("480x800")) {

			popupBottom = 80;
		}

		else if (Allconstants.screenSize.equalsIgnoreCase("320x480")) {
			popupBottom = 60;
		}

		else {
			popupBottom = 80;
		}

		dateTime = Calendar.getInstance();
		currentDate = new Date(); // Default Value.
		dateFormater = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
		strCurrentDateTime = String.valueOf(dateTime.get(Calendar.YEAR)
				+ "-"
				+ String.valueOf(dateTime.get(Calendar.MONTH)
						+ 1
						+ "-"
						+ String.valueOf(dateTime.get(Calendar.DATE) + " "
								+ dateTime.get(Calendar.HOUR_OF_DAY) + ":"
								+ dateTime.get(Calendar.MINUTE) + ":"
								+ dateTime.get(Calendar.SECOND))));
		PrintLog.getWarnLog("strDateTime", strCurrentDateTime + "");

	}

	private void initUI() {

		mainFliper = (ViewFlipper) findViewById(R.id.flpnews);
		mainFliper.setDisplayedChild(2);

		vedioView = (WebView) findViewById(R.id.news_vedio);

		imgBtnBack = (ImageButton) findViewById(R.id.news_list_back);
		imgBtnBack.setOnClickListener(this);

		imgBtnBrandName = (ImageButton) findViewById(R.id.news_list_brand_names);
		imgBtnBrandName.setOnClickListener(this);

		imgBtnFacebook = (ImageButton) findViewById(R.id.news_list_facebook);
		imgBtnFacebook.setOnClickListener(this);

		imgBtnTwitter = (ImageButton) findViewById(R.id.news_list_twitter);
		imgBtnTwitter.setOnClickListener(this);

		cityImgBtnBack = (ImageButton) findViewById(R.id.city_brand_back);
		cityImgBtnBack.setOnClickListener(this);

		cityImgBtnFacebook = (ImageButton) findViewById(R.id.city_brand_facebook);
		cityImgBtnFacebook.setOnClickListener(this);

		cityImgBtnTwitter = (ImageButton) findViewById(R.id.city_brand_twitter);
		cityImgBtnTwitter.setOnClickListener(this);

		cityImgBtnBrandName = (ImageButton) findViewById(R.id.city_brand_names);
		cityImgBtnBrandName.setOnClickListener(this);

		logo = (ImageView) findViewById(R.id.news_list_image);
		video_pause = (ImageView) findViewById(R.id.video_pause);
		// video_pause.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// video_pause.setVisibility(View.GONE);
		// // vedioView = (WebView) findViewById(R.id.news_vedio);
		// vedioView.loadData(getYouTubeUrl(youtubeId), "text/html",
		// "UTF-8");
		// }
		// });
		newsList = (ListView) findViewById(R.id.news_list_listview);

		if (!SharePreferenceUtil.isOnline(con)) {
			ToastShow.getMessage(NewsActivity.this, "No internet available");
			return;
		}

		new ShowNewsList().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 
	 * to show new news notification
	 */

	private final Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			if (newsIndicatorVector.size() != 0)

			{
				newsIndicator();
			}

		}
	};

	/**
	 * 
	 * to go back login screen
	 */

	private final Runnable loginRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (MyTabActivity.getInstance() != null) {
				MyTabActivity.getInstance().openTab();
			}

		}
	};

	// @Override
	// protected boolean isRouteDisplayed() {
	// // TODO Auto-generated method stub
	// return false;
	// }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.news_list_back:

			pauseVideo();

			if (MyTabActivity.getInstance() != null)

			{
				MyTabActivity.getInstance().openTab();

			}

			break;

		case R.id.news_list_brand_names:
			strBack = "news_br";
			mainFliper.setDisplayedChild(1);
			pauseVideo();
			break;

		case R.id.news_list_facebook:
			pauseVideo();
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.news_list_twitter:
			pauseVideo();
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		// city_brand_logo screen

		case R.id.city_brand_back:
			if (whichSreen.equalsIgnoreCase("newsList"))

			{
				pauseVideo();
				mainFliper.setDisplayedChild(2);
				strBack = "newsMain";
			} else if (whichSreen.equalsIgnoreCase("newsDetails"))

			{
				resumeVideo();
				strBack = "newsDetails";
				mainFliper.setDisplayedChild(3);
			}

			break;

		case R.id.city_brand_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_names:
			if (whichSreen.equalsIgnoreCase("newsList"))

			{
				resumeVideo();
				strBack = "newsMain";
				mainFliper.setDisplayedChild(2);
			} else if (whichSreen.equalsIgnoreCase("newsDetails"))

			{
				strBack = "newsDetails";
				mainFliper.setDisplayedChild(3);
			}
			break;

		case R.id.city_brand_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		// news details screen

		case R.id.news_detailsback:
			mainFliper.setDisplayedChild(2);
			whichSreen = "newsList";
			strBack = "newsMain";
			resumeVideo();
			// if (vedioView != null)
			//
			// {
			// video_pause.setVisibility(View.GONE);
			//
			// }

			break;

		case R.id.news_details_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.news_details_names:
			mainFliper.setDisplayedChild(1);
			break;

		case R.id.news_details_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.news_details_btn_cross:
			// resumeVideo();
			whichSreen = "newsList";
			mainFliper.setDisplayedChild(2);

			break;

		case R.id.news_details_next_btn:
			pos++;
			if (pos == AllNewsList.getAllNewsList().size())

			{
				SharePreferenceUtil.setNewsDate(con, strCurrentDateTime);
				if (popupwindow != null) {
					popupwindow.dismiss();
				}
				
				pos = 1;
				new_news_item = 0;
				
				
			}
			else
				new_news_item--;
			
			if(new_news_item>0){
				TextView pendingNews = (TextView) submenu_popup
						.findViewById(R.id.txtPendingNews);
				pendingNews.setText(String.valueOf(new_news_item));
			}
			
			nsl = AllNewsList.getAllNewsList().elementAt(pos);
			txt_details.setText(Html.fromHtml(nsl.getDescription().toString())
					+ "");
			details_title.setText(Html.fromHtml(nsl.getHead_line().toString())
					+ "");
			break;
		}

	}

	/***
	 * 
	 * Show all brand list
	 * 
	 */

	private class ShowNewsList extends AsyncTask<String, String, String> {
		HttpPost httppost;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", true,
					true);
		}

		@Override
		protected String doInBackground(String... aurl) {

			httppost = new HttpPost(AllUrl.newsListURL);

			try {
				response = MyHTTPRequest.getData(httppost);
			} catch (URISyntaxException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			try {

				if (NewsListParser.connect(con, response))

				{
					PrintLog.getWarnLog("AllNewsList.getAllNewsList().size()",
							AllNewsList.getAllNewsList().size() + "<><>");

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

			}

			adapter = new NewsListArrayAdapter(con);
			newsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			//ListWaveAnimation.showTheWave(con, newsList);

			if (AllNewsList.getVideoUrlById("1").length() == 0)

			{
				loadImage = new ImageLoader(con);
				logo = (ImageView) findViewById(R.id.news_list_image);
				logo.setVisibility(View.VISIBLE);
				loadImage.displayImage(
						BaseUrl.baseUrl + AllNewsList.getImageUrlById("1"),
						logo);


			}

			else

			{
				logo = (ImageView) findViewById(R.id.news_list_image);
				logo.setVisibility(View.GONE);
				showVideo();
			}

			/*
			for (int i = 1; i < AllNewsList.getAllNewsList().size(); i++){
				_allNewsList.set(i,AllNewsList.getAllNewsList().elementAt(i));
			}
			*/
		
			newsIndicatorVector.removeAllElements();
			for (int i = 1; i < AllNewsList.getAllNewsList().size(); i++)

			{
				strModifiedDateTime = AllNewsList.getAllNewsList().elementAt(i)
						.getModified();
				try {
					currentDate = dateFormater.parse(SharePreferenceUtil
							.getNewsDate(con));
					longCurrentDate = currentDate.getTime();
					currentDate.setTime(longCurrentDate);

					modifiedDate = dateFormater.parse(strModifiedDateTime);
					longModifiedDate = modifiedDate.getTime();
					modifiedDate.setTime(longModifiedDate);

					if (currentDate.before(modifiedDate)) {

						newsIndicatorVector.add(AllNewsList.getAllNewsList()
								.elementAt(i).getModified());

					}
				} catch (ParseException e) {
					// Do Something on Error.
				}
				
				new_news_item=newsIndicatorVector.size();

				PrintLog.getWarnLog("newsIndicator.size()",
						newsIndicatorVector.size() + "");

			}

			mHandler.postDelayed(mRunnable, duration);

		}
	}

	private void showVideo()

	{
		pd = ProgressDialog
				.show(con, "Please wait..", "Loading...", true, true);

		Thread d = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				current_url = AllNewsList.getVideoUrlById("1").trim()
						.replaceAll(" ", "%20");
				youtubeId = current_url.substring(25, 36);
				PrintLog.showFinalVersionLog(youtubeId);
				runOnUiThread(new Runnable() {

					@SuppressWarnings("deprecation")
					public void run() {

						if (pd.isShowing()) {
							pd.dismiss();
						}
						// viewVideo();

						vedioView.setVisibility(View.VISIBLE);
						vedioView.getSettings().setJavaScriptEnabled(true);
						vedioView.setWebViewClient(new HelloWebViewClient());
						vedioView.setWebChromeClient(new WebChromeClient());
						vedioView.getSettings().setPluginState(PluginState.ON);
						vedioView.getSettings().setUseWideViewPort(false);
						vedioView.getSettings().setSupportZoom(false);
						vedioView.setVerticalScrollBarEnabled(false);
						vedioView.setHorizontalScrollBarEnabled(false);
						vedioView.getSettings().setBuiltInZoomControls(false);
						vedioView.loadData(getYouTubeUrl(youtubeId),
								"text/html", "UTF-8");

						
					}
				});
			}
		});
		d.start();

	}

	private class HelloWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			// Log.e("Finishing loading url : ", url + "<><>");
			if (pd.isShowing()) {
				pd.dismiss();
			}

		}

		@SuppressWarnings("unused")
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url != null) {
				Log.e("view url:", url);
				view.loadData(getYouTubeUrl(youtubeId), "text/html", "UTF-8");
				return false;
			} else if (url == null) {
				view.loadData(getYouTubeUrl(youtubeId), "text/html", "UTF-8");
				return false;
			}

			return false;

		}
	}

	private class NewsListArrayAdapter extends ArrayAdapter<NewsList> {
		private final Context con;

		private NewsList nsl;

		public NewsListArrayAdapter(Context context) {
			super(context, R.layout.row_news_list, AllNewsList
					.getAllNewsInfoExceptFirstId());
			con = context;
			// TODO Auto-generated constructor stub

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_news_list, null);
			}

			if (position < AllNewsList.getAllNewsInfoExceptFirstId().size()) {
				nsl = AllNewsList.getAllNewsInfoExceptFirstId().elementAt(
						position);

				final TextView news_title = (TextView) v
						.findViewById(R.id.row_news_list_title);

				final TextView news_des = (TextView) v
						.findViewById(R.id.row_news_list_details);

				news_title.setText(Html.fromHtml(nsl.getHead_line().toString()
						.trim()));
				news_des.setText(Html.fromHtml(nsl.getDescription().toString()
						.trim()));

			}
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					vedioView = (WebView) findViewById(R.id.news_vedio);
					pauseVideo();
					whichSreen = "newsDetails";
					strBack = "newsDetails";
					pos = position + 1;
					showNewsDetails(pos);

				}
			});

			// TODO Auto-generated method stub
			return v;
		}

	}

	private void showNewsDetails(int pos)

	{
		nsl = AllNewsList.getAllNewsList().elementAt(pos);
		mainFliper.setDisplayedChild(3);

		details_title = (TextView) findViewById(R.id.details_title);
		details_title
				.setText(Html.fromHtml(nsl.getHead_line().toString()) + "");

		txt_details = (TextView) findViewById(R.id.news_details_txt);
		txt_details
				.setText(Html.fromHtml(nsl.getDescription().toString()) + "");

		btn_cross = (ImageButton) findViewById(R.id.news_details_btn_cross);
		btn_cross.setOnClickListener(this);

		btn_next = (ImageButton) findViewById(R.id.news_details_next_btn);
		btn_next.setOnClickListener(this);

		detailsBtnBack = (ImageButton) findViewById(R.id.news_detailsback);
		detailsBtnBack.setOnClickListener(this);

		detailsBtnBrandName = (ImageButton) findViewById(R.id.news_details_names);
		detailsBtnBrandName.setOnClickListener(this);

		detailsBtnFacebook = (ImageButton) findViewById(R.id.news_details_facebook);
		detailsBtnFacebook.setOnClickListener(this);

		detailsBtnTwitter = (ImageButton) findViewById(R.id.news_details_twitter);
		detailsBtnTwitter.setOnClickListener(this);
		
		new_news_item--;
		
		TextView pendingNews;
		try {
			submenu_popup = inflater.inflate(R.layout.news_indicator, null,
					false);
			pendingNews = (TextView) submenu_popup.findViewById(R.id.txtPendingNews);
			if(new_news_item>0){
				pendingNews.setText(String.valueOf(new_news_item));
			}
			else if(new_news_item<=0){
				pendingNews.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		
		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK)

		{

			if (strBack.equalsIgnoreCase("newsMain")) {
				if (MyTabActivity.getInstance() != null)

				{
					MyTabActivity.getInstance().openTab();
				}

				return true;

			}

			else if (strBack.equalsIgnoreCase("news_br")) {
				mainFliper.setDisplayedChild(2);
				strBack = "newsMain";
				return true;
			}

			else if (strBack.equalsIgnoreCase("newsDetails")) {
				mainFliper.setDisplayedChild(2);
				strBack = "newsMain";
				resumeVideo();

				return true;
			}

			else {

				if (MyTabActivity.getInstance() != null)

				{
					MyTabActivity.getInstance().pushIndex(4);
					MyTabActivity.getInstance().openTab();
					Allconstants.strFavBack = "";
				}
				return true;

			}

		}

		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(SharePreferenceUtil.getEmailPressed(con)){
			SharePreferenceUtil.setEmailPressed(con, false);
			return;
		}

		if (pd != null) {
			pd.dismiss();
		}

		if (SharePreferenceUtil.getLoginStatus(con).equalsIgnoreCase("logout"))

		{
			mainFliper = (ViewFlipper) findViewById(R.id.flpnews);
			mainFliper.setDisplayedChild(0);
			mHandler.postDelayed(loginRunnable, 1);

		} else {
			vedioView = (WebView) findViewById(R.id.news_vedio);
			resumeVideo();
			initUI();

		}
		MyTabActivity.getInstance().pushIndex(4);

	}

	private void newsIndicator() {

		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		try {

			mainview = findViewById(R.id.ff);
			submenu_popup = inflater.inflate(R.layout.news_indicator, null,
					false);

			popupwindow = new PopupWindow(submenu_popup,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, false);
			popupwindow.setOutsideTouchable(false);
			popupwindow.setTouchable(false);
			submenu_popup.setPadding(0, 0, 0, 0);
			popupwindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			
			popupwindow
					.showAtLocation(mainview, Gravity.BOTTOM, 0, popupBottom);

			TextView pendingNews = (TextView) submenu_popup
					.findViewById(R.id.txtPendingNews);
			pendingNews.setText(String.valueOf(newsIndicatorVector.size()));

		} catch (Exception e) {
			PrintLog.getWarnLog("Exception of popup :", e.toString());
		}

	}

	private static String getYouTubeUrl(String youtubeId) {
		String url = "";

		url = "<!DOCTYPE HTML><html><body style=\"padding: 0; margin: 0; \"><iframe id=\"ytplayer\" type=\"text/html\" width=\""
				+ "auto"
				+ "\" height=\""
				+ "auto"
				+ "\"src=\"http://www.youtube.com/embed/"
				+ youtubeId
				+ "?autoplay=1&fs=0&loop=1&color=red&theme=light&showinfo=0&rel=0\"frameborder=\"0\"></body></html>";

		return url;
	}

	private void pauseVideo() {
		try {
			Class.forName("android.webkit.WebView")
					.getMethod("onPause", (Class[]) null)
					.invoke(vedioView, (Object[]) null);
			vedioView.pauseTimers();
			// video_pause = (ImageView) findViewById(R.id.video_pause);
			// video_pause.setVisibility(View.VISIBLE);

		} catch (ClassNotFoundException cnfe) {

		} catch (NoSuchMethodException nsme) {

		} catch (InvocationTargetException ite) {

		} catch (IllegalAccessException iae) {

		}
	}

	private void resumeVideo() {
		try {
			Class.forName("android.webkit.WebView")
					.getMethod("onResume", (Class[]) null)
					.invoke(vedioView, (Object[]) null);
			vedioView.resumeTimers();
			// video_pause = (ImageView) findViewById(R.id.video_pause);
			// video_pause.setVisibility(View.GONE);

		} catch (ClassNotFoundException cnfe) {

		} catch (NoSuchMethodException nsme) {

		} catch (InvocationTargetException ite) {

		} catch (IllegalAccessException iae) {

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		if (vedioView != null) {
			try {
				Class.forName("android.webkit.WebView")
						.getMethod("onPause", (Class[]) null)
						.invoke(vedioView, (Object[]) null);
				vedioView.pauseTimers();
				// video_pause = (ImageView) findViewById(R.id.video_pause);
				// video_pause.setVisibility(View.VISIBLE);

			} catch (ClassNotFoundException cnfe) {

			} catch (NoSuchMethodException nsme) {

			} catch (InvocationTargetException ite) {

			} catch (IllegalAccessException iae) {

			}

		}

		mHandler.removeCallbacksAndMessages(mRunnable);
		mHandler.removeCallbacksAndMessages(loginRunnable);

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (vedioView != null) {
			try {
				Class.forName("android.webkit.WebView")
						.getMethod("onDestroy", (Class[]) null)
						.invoke(vedioView, (Object[]) null);
				vedioView.destroy();

			} catch (ClassNotFoundException cnfe) {

			} catch (NoSuchMethodException nsme) {

			} catch (InvocationTargetException ite) {

			} catch (IllegalAccessException iae) {

			}
		}

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if (vedioView != null) {
			try {
				Class.forName("android.webkit.WebView")
						.getMethod("onStop", (Class[]) null)
						.invoke(vedioView, (Object[]) null);
				vedioView.stopLoading();

			} catch (ClassNotFoundException cnfe) {

			} catch (NoSuchMethodException nsme) {

			} catch (InvocationTargetException ite) {

			} catch (IllegalAccessException iae) {

			}
		}

		mHandler.removeCallbacksAndMessages(mRunnable);
		mHandler.removeCallbacksAndMessages(loginRunnable);

	}

	// @Override
	// public void onInitializationSuccess(YouTubePlayer.Provider provider,
	// YouTubePlayer player, boolean wasRestored) {
	// // TODO Auto-generated method stub
	// if (!wasRestored) {
	// player.cueVideo("YMf3a7Cd84s");
	// }
	//
	// }
	//
	// @Override
	// protected YouTubePlayer.Provider getYouTubePlayerProvider() {
	// // TODO Auto-generated method stub
	// return (YouTubePlayerView) findViewById(R.id.youtube_view);
	// }
	//
	// private void viewVideo() {
	// YouTubePlayerView youTubeView = (YouTubePlayerView)
	// findViewById(R.id.youtube_view);
	// youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
	// }
	// "\<!DOCTYPE HTML><html><body style=\"padding: 0; margin: 0; \"><iframe
	// id=\"ytplayer\" type=\"text/html\" width=\"700\" height=\"480\"
	// src=\"http://www.youtube.com/embed/%@?rel=0&autoplay=1&playsinline=1\"
	// frameborder=\"0\" ></iframe></body></html>

}
