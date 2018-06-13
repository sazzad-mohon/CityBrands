package com.shehala.citybrands;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.google.android.maps.MapActivity;
import com.shehala.citybrands.R;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.ToastShow;

public class HomeActivity extends MapActivity {

	private Context con;
	private ImageButton imgBtnBrandName, imgBtnFacebook, imgBtnTwitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		con = this;//
		initUI();
		
	}

	private void initUI() {

		imgBtnBrandName = (ImageButton) findViewById(R.id.login_brand_names);
		imgBtnBrandName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ToastShow.getMessage(HomeActivity.this, "You have clicked on Brand Name Button");
			}
		});

		imgBtnFacebook = (ImageButton) findViewById(R.id.login_facebook);
		imgBtnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			}
		});

		imgBtnTwitter = (ImageButton) findViewById(R.id.login_twitter);
		imgBtnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Allconstants.browseUrl(con, AllUrl.twitterUrl);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
