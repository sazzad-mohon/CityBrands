package com.shehala.citybrands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.maps.MapActivity;
import com.shehala.citybrands.R;
import com.shehala.citybrands.holder.AllBrandsList;
import com.shehala.citybrands.holder.AllShopLists;
import com.shehala.citybrands.model.BrandsList;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.sectionindexlistview.Item;
import com.shehala.citybrands.sectionindexlistview.ItemsSections;
import com.shehala.citybrands.sectionindexlistview.NamesAdapter;
import com.shehala.citybrands.util.AllUrl;
import com.shehala.citybrands.util.Allconstants;
import com.shehala.citybrands.util.MyHTTPRequest;
import com.shehala.citybrands.util.PrintLog;
import com.shehala.citybrands.util.SharePreferenceUtil;
import com.shehala.citybrands.util.ToastShow;




public class AllBrandsActivity extends MapActivity implements OnClickListener {

	private boolean checkOrNot = false;
	private GestureDetector mGestureDetector;
	private Handler mHandler = new Handler();
	private static int duration = 1;
	// x and y coordinates within our side index
	private static float sideIndexX;
	private static float sideIndexY;

	// height of side index
	private int sideIndexHeight;

	// number of items in the side index
	private int indexListSize;

	// private int textSize = 0;

	// list with items for side index
	private ArrayList<Object[]> indexList = null;

	private Context con;
	ArrayList<Item> itemsSection = new ArrayList<Item>();
	private List<BrandsList> filterArray = new ArrayList<BrandsList>();
	NamesAdapter objAdapter = null;
	private String searchString;
	private ImageButton imgBtnBack, imgBtnBrandName, imgBtnFacebook,
			imgBtnTwitter, cityImgBtnBack, cityImgBtnBrandName,
			cityImgBtnFacebook, cityImgBtnTwitter;
	TextView tmpTV = null;
	private ProgressDialog pd;

	private ViewFlipper mainFliper;
	private EditText edtSearch;

	private Vector<BrandsList> temp = new Vector<BrandsList>();

	private ListView listView;
	private List<ShopList> shopList;
	private List<ShopList> shopListFinal;
	private String brandName = "", brandId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.all_brand_flipper);
		con = this;
		mGestureDetector = new GestureDetector(this,
				new SideIndexGestureListener());
		Allconstants.strFavBack = "favMain";

	}

	private void initUI() {

		mainFliper = (ViewFlipper) findViewById(R.id.flpallbrand);
		mainFliper.setDisplayedChild(9);
		// Allconstants.strForBackButton = "all_brands_main";

		imgBtnBack = (ImageButton) findViewById(R.id.allbrands_back);
		imgBtnBack.setOnClickListener(this);

		imgBtnFacebook = (ImageButton) findViewById(R.id.allbrands_facebook);
		imgBtnFacebook.setOnClickListener(this);

		imgBtnTwitter = (ImageButton) findViewById(R.id.allbrands_twitter);
		imgBtnTwitter.setOnClickListener(this);

		imgBtnBrandName = (ImageButton) findViewById(R.id.allbrands_brand_names);
		imgBtnBrandName.setOnClickListener(this);

		cityImgBtnBack = (ImageButton) findViewById(R.id.city_brand_back);
		cityImgBtnBack.setOnClickListener(this);

		cityImgBtnFacebook = (ImageButton) findViewById(R.id.city_brand_facebook);
		cityImgBtnFacebook.setOnClickListener(this);

		cityImgBtnTwitter = (ImageButton) findViewById(R.id.city_brand_twitter);
		cityImgBtnTwitter.setOnClickListener(this);

		cityImgBtnBrandName = (ImageButton) findViewById(R.id.city_brand_names);
		cityImgBtnBrandName.setOnClickListener(this);

		edtSearch = (EditText) findViewById(R.id.allbrands_edt_search);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				filterArray.clear();
				searchString = edtSearch.getText().toString().trim()
						.replaceAll("\\s", "");

				if (temp.size() > 0 && searchString.length() > 0) {
					for (BrandsList name : temp) {
						if (name.getBrandName().toLowerCase()
								.startsWith(searchString.toLowerCase())) {

							filterArray.add(name);
						}
					}
					setAdapterToListview(filterArray);
				} else {
					filterArray.clear();
					setAdapterToListview(temp);
				}

			}
		});
		
		if(!SharePreferenceUtil.isOnline(con))
		{
			ToastShow.getMessage(AllBrandsActivity.this, "No internet available");
			return;
		}
		
		new ShowAllBrands().execute();

	}

	/***
	 * 
	 * Show all brand list
	 * 
	 */

	private class ShowAllBrands extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = ProgressDialog.show(con, "Please wait..", "Loading...", false,
					false);
		}

		@Override
		protected String doInBackground(String... aurl) {

			return null;

		}

		@Override
		protected void onPostExecute(String unused) {
			if (pd != null) {
				pd.dismiss();
			}

			temp = AllBrandsList.getAllbrands();
			// temp = AllBrandsList.getAllBrandsList();
			listView = (ListView) findViewById(R.id.allbrands_listview);

			setAdapterToListview(temp);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					Item item = itemsSection.get(position);

					if (v.getTag().getClass().getSimpleName()
							.equals("ViewHolderName")) {
						BrandsList objSchoolname = (BrandsList) item;
						brandName = objSchoolname.getBrandName();
						// ToastShow.getMessage(con, brandName+"<><>");
						brandId = objSchoolname.getId();
						shopList = AllShopLists
								.getAllShopsInfoByBrandName(brandName);
						PrintLog.getWarnLog("shopList.size()", shopList.size()
								+ "<><>");
						Allconstants.shopList = shopList;
						PrintLog.getWarnLog("Allconstants.shopList",
								Allconstants.shopList.size() + "<><>");

						shopListFinal = new ArrayList<ShopList>();

						for (int i = 0; i < shopList.size(); i++)

						{

							double final_distance = Double.parseDouble(shopList
									.get(i).getDistance());

							PrintLog.getWarnLog("final_distance",
									final_distance + "<><>");

							if (final_distance > 0)

							{

								ShopList sl = new ShopList();

								sl.setBrands_id(shopList.get(i).getBrands_id());
								sl.setBrands_name(shopList.get(i)
										.getBrands_name());
								sl.setShop_image(shopList.get(i)
										.getShop_image());
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
								sl.setFacebookUrl(shopList.get(i)
										.getFacebookUrl());
								shopListFinal.add(sl);

							}
						}

						if (shopListFinal.size() != 0)

						{
							Allconstants.shopList = shopList;
							Allconstants.mainFliper = mainFliper;
							Allconstants.activity = AllBrandsActivity.this;
							Allconstants.shopListFinal = shopListFinal;
							PrintLog.getWarnLog("Allconstants.shopList",
									Allconstants.shopList.size() + "<><>");
							new BrandsListWithSpecificBrands(
									AllBrandsActivity.this, mainFliper,
									shopListFinal);
							PrintLog.getErrorLog("brandName : ", brandName);
							Allconstants.strForBackButton = "specificBrandList";
							Allconstants.strFavBack = "favList";
							new BrandHitIncrease().execute();
						}

						else

						{
							Allconstants.shopList = shopList;
							Allconstants.mainFliper = mainFliper;
							Allconstants.activity = AllBrandsActivity.this;
							Allconstants.shopListFinal = shopListFinal;
							PrintLog.getWarnLog("Allconstants.shopList",
									Allconstants.shopList.size() + "<><>");
							new BrandsListWithSpecificBrands(
									AllBrandsActivity.this, mainFliper,
									shopListFinal);
							PrintLog.getErrorLog("brandName : ", brandName);
							Allconstants.strForBackButton = "specificBrandList";
							Allconstants.strFavBack = "favList";
							new BrandHitIncrease().execute();
						}

						PrintLog.getWarnLog("shopListFinal.size()",
								shopListFinal.size() + " in view click");

					} else {
						ItemsSections objSectionsName = (ItemsSections) item;
						ToastShow.getMessage(
								AllBrandsActivity.this,
								"Section :: "
										+ String.valueOf(objSectionsName
												.getSectionLetter()));
					}

				}
			});

		}
	}

	@Override
	public void onClick(View v) {

		// all_brand screen

		switch (v.getId()) {
		case R.id.allbrands_back:
			if (MyTabActivity.getInstance() != null) {
				MyTabActivity.getInstance().openTab();
			}
			break;

		case R.id.allbrands_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.allbrands_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

		case R.id.allbrands_brand_names:
			Allconstants.strAllBrands = "all_brands_main_br";
			Allconstants.strFavBack = "favBr";
			mainFliper.setDisplayedChild(8);
			break;

		// city_brand_logo screen

		case R.id.city_brand_back:
			mainFliper.setDisplayedChild(9);
			Allconstants.strFavBack = "favMain";
			break;

		case R.id.city_brand_facebook:
			Allconstants.browseUrl(con, AllUrl.faceBookUrl);
			break;

		case R.id.city_brand_names:
			mainFliper.setDisplayedChild(9);
			Allconstants.strFavBack = "favMain";
			break;

		case R.id.city_brand_twitter:
			Allconstants.browseUrl(con, AllUrl.twitterUrl);
			break;

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
				mainFliper.setDisplayedChild(9);
				Allconstants.strFavBack = "favMain";
				return true;
			}

			else if (Allconstants.strFavBack.equalsIgnoreCase("favList")) {
				mainFliper.setDisplayedChild(9);
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
					MyTabActivity.getInstance().pushIndex(1);
					MyTabActivity.getInstance().openTab();
					Allconstants.strFavBack = "";
					return true;
				}

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
			PrintLog.showHitCounterLog("Hit URL:", AllUrl.increaseBrandHitURL
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
			Allconstants.strFavBack = "favMain";

		}

		MyTabActivity.getInstance().pushIndex(1);

	}

	// setAdapter Here....

	public void setAdapterToListview(List<BrandsList> listForAdapter) {

		itemsSection.clear();

		if (null != listForAdapter && listForAdapter.size() != 0) {

			Collections.sort(listForAdapter);

			char checkChar = ' ';

			for (int index = 0; index < listForAdapter.size(); index++) {

				BrandsList objItem = (BrandsList) listForAdapter.get(index);

				char firstChar = objItem.getBrandName().charAt(0);

				if (' ' != checkChar) {
					if (checkChar != firstChar) {
						ItemsSections objSectionItem = new ItemsSections();
						objSectionItem.setSectionLetter(firstChar);
						itemsSection.add(objSectionItem);
					}
				} else {
					ItemsSections objSectionItem = new ItemsSections();
					objSectionItem.setSectionLetter(firstChar);
					itemsSection.add(objSectionItem);
				}

				checkChar = firstChar;
				itemsSection.add(objItem);
			}
		} else {
			// showAlertView();
		}

		if (null == objAdapter) {
			objAdapter = new NamesAdapter(AllBrandsActivity.this, itemsSection);
			listView.setAdapter(objAdapter);
		} else {
			objAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		} else {
			return false;
		}
	}

	private ArrayList<Object[]> createIndex() {
		// String[] strArr

		ArrayList<Object[]> tmpIndexList = new ArrayList<Object[]>();
		Object[] tmpIndexItem = null;

		int tmpPos = 0;
		String tmpLetter = "";
		String currentLetter = null;
		String strItem = null;

		for (int j = 0; j < temp.size(); j++) {
			strItem = temp.get(j).getBrandName();
			currentLetter = strItem.substring(0, 1);

			// every time new letters comes
			// save it to index list
			if (!currentLetter.equals(tmpLetter)) {
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = tmpLetter;
				tmpIndexItem[1] = tmpPos - 1;
				tmpIndexItem[2] = j - 1;

				tmpLetter = currentLetter;
				tmpPos = j + 1;

				tmpIndexList.add(tmpIndexItem);
				//
				// PrintLog.getErrorLog("tmpIndexList.size() b4",
				// tmpIndexList.size() + "<><><>");
			}
		}

		// save also last letter
		tmpIndexItem = new Object[3];
		tmpIndexItem[0] = tmpLetter;
		tmpIndexItem[1] = tmpPos - 1;
		tmpIndexItem[2] = temp.size() - 1;
		tmpIndexList.add(tmpIndexItem);

		// and remove first temporary empty entry
		if (tmpIndexList != null && tmpIndexList.size() > 0) {
			tmpIndexList.remove(0);
		}

		return tmpIndexList;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		// final ListView listView = (ListView)
		// findViewById(R.id.allbrands_listview);
		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndexHeight = sideIndex.getHeight();
		sideIndex.removeAllViews();

		// TextView for every visible item
		TextView tmpTV = null;

		// we'll create the index list
		indexList = createIndex();

		// number of items in the index List
		indexListSize = indexList.size();

		// maximal number of item, which could be displayed
		// int indexMaxSize = (int) Math.floor(sideIndex.getHeight());

		int tmpIndexListSize = indexListSize;
		double delta = 0;
		try {
			delta = indexListSize / tmpIndexListSize;
		} catch (Exception e) {
			// TODO: handle exception
		}

		String tmpLetter = null;
		Object[] tmpIndexItem = null;

		// show every m-th letter
		for (double i = 1; i <= indexListSize; i = i + delta) {
			tmpIndexItem = indexList.get((int) i - 1);
			tmpLetter = tmpIndexItem[0].toString();
			tmpTV = new TextView(this);
			tmpTV.setText(tmpLetter);
			tmpTV.setTextSize(9);
			tmpTV.setTextColor(Color.BLACK);
			tmpTV.setGravity(Gravity.CENTER);
			tmpTV.setPadding(0, 0, 0, 0);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			sideIndex.addView(tmpTV);
		}

		// and set a touch listener for it
		sideIndex.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// now you know coordinates of touch
				sideIndexX = event.getX();
				sideIndexY = event.getY();

				// and can display a proper item it country list
				displayListItem();

				return false;
			}
		});
	}

	class SideIndexGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// we know already coordinates of first touch
			// we know as well a scroll distance
			sideIndexX = sideIndexX - distanceX;
			sideIndexY = sideIndexY - distanceY;

			// when the user scrolls within our side index
			// we can show for every position in it a proper
			// item in the country list
			if (sideIndexX >= 0 && sideIndexY >= 0) {
				displayListItem();
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	private void displayListItem() {
		// compute number of pixels for every side index item
		double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

		// compute the item index for given event position belongs to
		int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

		// compute minimal position for the item in the list
		int minPosition = (int) (itemPosition * pixelPerIndexItem);

		// get the item (we can do it since we know item index)
		Object[] indexItem = indexList.get(itemPosition);

		// and compute the proper item in the country list
		int indexMin = Integer.parseInt(indexItem[1].toString());
		int indexMax = Integer.parseInt(indexItem[2].toString());
		int indexDelta = Math.max(1, indexMax - indexMin);

		double pixelPerSubitem = pixelPerIndexItem / indexDelta;
		int subitemPosition = (int) (indexMin + (sideIndexY - minPosition)
				/ pixelPerSubitem);

		ListView listView = (ListView) findViewById(R.id.allbrands_listview);
		listView.setSelection(subitemPosition);
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
