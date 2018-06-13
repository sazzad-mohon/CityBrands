package com.shehala.citybrands;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.shehala.citybrands.R;
import com.shehala.citybrands.holder.AllBrandsList;
import com.shehala.citybrands.model.BrandsList;
import com.shehala.citybrands.sqlite.DatabaseHandler;
import com.shehala.citybrands.sqlite.FavoriteBrands;
import com.shehala.citybrands.util.PrintLog;

public class SimpleAdapter extends ArrayAdapter<BrandsList> implements
		SectionIndexer {

	

	private final Context con;
	private BrandsList brl;
	private DatabaseHandler db;
	private static String sections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private List<BrandsList> temp = new Vector<BrandsList>();


	public SimpleAdapter(Context context) {
		super(context, R.layout.row_all_brands, AllBrandsList
				.getAllBrandsList());
		con = context;
		db = new DatabaseHandler(con);
		temp = AllBrandsList.getAllBrandsList();
		// temp = AllBrandsList.getAllBrandsList();
		Collections.sort(temp);
		// TODO Auto-generated constructor stub

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			final LayoutInflater vi = (LayoutInflater) con
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_all_brands, null);
		}

		if (position < temp.size()) {
			brl = temp.get(position);

			final TextView brand_title = (TextView) v
					.findViewById(R.id.row_brand_name);

			brand_title.setText(brl.getBrandName().toString().trim());

			final ImageButton iv = (ImageButton) v
					.findViewById(R.id.row_all_brand_list_icon_normal);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					iv.setBackgroundResource(R.drawable.list_favorite_selected);
					db.addBrands(new FavoriteBrands(AllBrandsList
							.getAllBrandsList().elementAt(position).getId(),
							AllBrandsList.getAllBrandsList()
									.elementAt(position).getBrandName(),
							"2 km."));

					List<FavoriteBrands> fb = db.getAllContacts();

					PrintLog.getWarnLog("Size of db :", fb.size() + "<><>");

				}
			});
			
		}

		// TODO Auto-generated method stub
		return v;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) {
		Log.d("ListView", "Get position for section");
		for (int i = 0; i < temp.size(); i++) {
			String item = temp.get(i).getBrandName().toString().toUpperCase();
			if (item.charAt(0) == sections.charAt(section)) {
				return i;
			}

		}
		return 0;

		// for (int i = section; i >= 0; i--) {
		// for (int j = 0; j < getCount(); j++) {
		// if (i == 0) {
		// // For numeric section
		// for (int k = 0; k <= 9; k++) {
		// if (StringMatcher.match(String.valueOf(getItem(j).getBrandName()),
		// String.valueOf(k)))
		// return j;
		// }
		// } else {
		// if (StringMatcher.match(String.valueOf(getItem(j).getBrandName()),
		// String.valueOf(mSections.charAt(i))))
		// return j;
		// }
		// }
		// }
		// return 0;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		Log.d("ListView", "Get section");
		return 0;
	}

	@Override
	public Object[] getSections() {
		Log.d("ListView", "Get sections");
		String[] sectionsArr = new String[sections.length()];
		for (int i = 0; i < sections.length(); i++)
			sectionsArr[i] = "" + sections.charAt(i);

		return sectionsArr;

		// String[] sections = new String[mSections.length()];
		// for (int i = 0; i < mSections.length(); i++)
		// sections[i] = String.valueOf(mSections.charAt(i));
		// return sections;

	}

}
