package com.shehala.citybrands;

import java.util.Stack;

import com.shehala.citybrands.R;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

/*
 * Tab activity class 
 */

@SuppressWarnings("deprecation")
public class MyTabActivity extends TabActivity {

	public static Context context;
	public static int tabIndex = 0;
	public static MyTabActivity instance = null;
	public static Stack<Integer> lifo;
	public static MyTabActivity getInstance() {
		return instance;
	}

	public static void setInstance(MyTabActivity instance) {
		MyTabActivity.instance = instance;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// FullScreen.makeFullScreen(MyTabActivity.this);
		instance = this;
		context = this;
		lifo = new Stack<Integer>();
		setContentView(R.layout.tab);
		
	
		setTabs(0);

	}

	public void openTab()

	{
		Integer a = (Integer) lifo.pop();
		System.out.println("stack pop: " + a);

		getTabHost().setCurrentTab(lifo.pop());

		//getTabHost().setCurrentTab(tabIndex);

	}
	
	public void pushIndex(int index) {
		lifo.push(index);
		System.out.println("stack: " + lifo);

	}

	
//	public void addTab(int i)
//
//	{
//
//		getTabHost().setCurrentTab(i);
//
//	}

	public  int getTabIndextInt()
	
	{
		return getTabHost().getCurrentTab();
	}
	
	/*
	 * set activity class into tab
	 */
	private void setTabs(final int activeTab) {

		/*
		 * 
		 * first tab
		 */
		Intent intent;

		intent = new Intent().setClass(this, LoginActivity.class);

		MyView view = null;
		view = new MyView(this, R.drawable.home_tab_normal,
				R.drawable.home_tab_hover, getString(R.string.home_tab));
		// view.setBackgroundDrawable(getResources().getDrawable(
		// R.layout.selecttabbackground));

		TabHost.TabSpec spec;

		spec = getTabHost().newTabSpec("listen").setIndicator(view)
				.setContent(intent);
		getTabHost().addTab(spec);

		/*
		 * 
		 * 2nd tab
		 */

		intent = new Intent().setClass(this, AllBrandsActivity.class);
		// Allconstants.mainTab = "allbrands";
		view = new MyView(this, R.drawable.all_brands_tab_normal,
				R.drawable.all_brands_tab_hover,
				getString(R.string.allbrands_tab));
		// view.setBackgroundDrawable(getResources().getDrawable(
		// R.layout.selecttabbackground));

		view.setFocusable(true);
		getTabHost().addTab(
				getTabHost().newTabSpec("artists").setIndicator(view)
						.setContent(intent));

		/*
		 * 
		 * third tab
		 */

		intent = new Intent().setClass(this, MostPopularActivity.class);

		view = new MyView(this, R.drawable.popular_normal,
				R.drawable.popular_brands_hover,
				getString(R.string.most_popular_tab));

		// view.setBackgroundDrawable(getResources().getDrawable(
		// R.layout.selecttabbackground));
		view.setFocusable(true);
		getTabHost().addTab(
				getTabHost().newTabSpec("chart").setIndicator(view)
						.setContent(intent));

		/*
		 * 
		 * fourth tab
		 */

		intent = new Intent().setClass(this, FavoriteActivity.class);

		view = new MyView(this, R.drawable.favorites_normal,
				R.drawable.favorites_hover, getString(R.string.favorite_tab));
		// view.setBackgroundDrawable(getResources().getDrawable(
		// R.layout.selecttabbackground));

		view.setFocusable(true);
		getTabHost().addTab(
				getTabHost().newTabSpec("stations").setIndicator(view)
						.setContent(intent));

		/*
		 * 
		 * fifth tab
		 */

		intent = new Intent().setClass(this, NewsActivity.class);
//		MyNewsView mv = new MyNewsView(this, R.drawable.news_tab_normal,
//				R.drawable.news_tab_hover, getString(R.string.news_tab), "3");
		 view = new MyView(this, R.drawable.news_tab_normal,
		 R.drawable.news_tab_hover, getString(R.string.news_tab));
//		 view.setBackgroundDrawable(getResources().getDrawable(
//		 R.layout.selecttabbackground));

		 view.setFocusable(true);
		getTabHost().addTab(
				getTabHost().newTabSpec("more").setIndicator(view)
						.setContent(intent));

		getTabHost().getTabWidget().getChildAt(0).getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		getTabHost().getTabWidget().getChildAt(1).getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		getTabHost().getTabWidget().getChildAt(2).getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		getTabHost().getTabWidget().getChildAt(3).getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		getTabHost().getTabWidget().getChildAt(4).getLayoutParams().height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

		
	}

	private class MyView extends LinearLayout {
		ImageView iv;
		TextView tv;
		TextView tv1;

		public MyView(final Context c, final int drawable,
				final int drawableselec, final String label) {
			super(c);
			iv = new ImageView(c);
			final StateListDrawable listDrawable = new StateListDrawable();
			listDrawable.addState(SELECTED_STATE_SET, getResources()
					.getDrawable(drawableselec));
			listDrawable.addState(ENABLED_STATE_SET, getResources()
					.getDrawable(drawable));
			iv.setImageDrawable(listDrawable);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setBackgroundColor(Color.TRANSPARENT);
			iv.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.0));

			tv = new TextView(c);
			tv.setText("");
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(2);
			tv.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					(float) 1.0));

			tv1 = new TextView(c);
			tv1.setText(label);
			tv1.setGravity(Gravity.CENTER);
			tv1.setBackgroundColor(Color.TRANSPARENT);
			tv1.setTextColor(this.getResources().getColorStateList(
					R.color.text_tab_indicator));
			tv1.setTextSize(10);
			tv1.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					(float) 1.0));

			setOrientation(LinearLayout.VERTICAL);
			setGravity(Gravity.CENTER);

			setPadding(0, 5, 3, 5);
			addView(tv);
			addView(iv);
			addView(tv1);
			// setBackgroundDrawable(getResources().getDrawable(
			// R.color.transparent));
		}
	}

	@SuppressWarnings("unused")
	private class MyNewsView extends LinearLayout {
		ImageView iv;
		TextView tv;
		TextView tv1;

		public MyNewsView(final Context c, final int drawable,
				final int drawableselec, final String label, String label2) {
			super(c);
			iv = new ImageView(c);
			final StateListDrawable listDrawable = new StateListDrawable();
			listDrawable.addState(SELECTED_STATE_SET, getResources()
					.getDrawable(drawableselec));
			listDrawable.addState(ENABLED_STATE_SET, getResources()
					.getDrawable(drawable));
			iv.setImageDrawable(listDrawable);
//			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setBackgroundColor(Color.TRANSPARENT);
			iv.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.0));

			tv = new TextView(c);
			tv.setText(label2);
			tv.setGravity(Gravity.CENTER);
			tv.setBackgroundResource(R.drawable.newslist);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(10);
			tv.setLayoutParams(new LayoutParams(
					26,
					26,
					(float) 1.0));

			tv1 = new TextView(c);
			tv1.setText(label);
			tv1.setGravity(Gravity.TOP);
			tv1.setBackgroundColor(Color.TRANSPARENT);
			tv1.setTextColor(this.getResources().getColorStateList(
					R.color.text_tab_indicator));
			tv1.setTextSize(10);
			tv1.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					(float) 1.0));

			setOrientation(LinearLayout.VERTICAL);
			setGravity(Gravity.TOP);

			setPadding(0, 5, 3, 5);
			addView(tv);
			addView(iv);
			addView(tv1);
			
		}
	}
}