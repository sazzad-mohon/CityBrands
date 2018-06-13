package com.shehala.citybrands.sectionindexlistview;

import java.util.ArrayList;
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

public class NamesAdapter extends ArrayAdapter<Item> implements SectionIndexer {

	private List<BrandsList> temp = new Vector<BrandsList>();
	private String sections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private ArrayList<Item> items;
	private LayoutInflater vi;
	private Item objItem;
	private String item;
	private List<FavoriteBrands> fb;
	private DatabaseHandler dbHnadler;
	private ArrayList<Item> itemsSection = new ArrayList<Item>();
	private ViewHolderSectionName holderSection;
	private ViewHolderName holderName;
	private BrandsList ei;
	private BrandsList objSchoolname;

	public NamesAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		temp = AllBrandsList.getAllbrands();
		this.items = items;
		dbHnadler = new DatabaseHandler(getContext());
		fb = dbHnadler.getAllContacts();
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		@SuppressWarnings("unused")
		View v = convertView;
		objItem = items.get(position);

		if (objItem.isSectionItem()) {
			ItemsSections si = (ItemsSections) objItem;

			if (convertView == null
					|| !convertView.getTag().equals(holderSection)) {
				convertView = vi.inflate(R.layout.alphabet_separator, null);

				holderSection = new ViewHolderSectionName();
				convertView.setTag(holderSection);
			} else {
				holderSection = (ViewHolderSectionName) convertView.getTag();
			}

			holderSection.section = (TextView) convertView
					.findViewById(R.id.alphabet_letter);
			holderSection.section
					.setText(String.valueOf(si.getSectionLetter()));

		} else {
			ei = (BrandsList) objItem;

			if (convertView == null || !convertView.getTag().equals(holderName)) {
				convertView = vi.inflate(R.layout.row_all_brands, null);

				holderName = new ViewHolderName();
				convertView.setTag(holderName);
				//v.setTag(holderName);
			} else {
				holderName = (ViewHolderName) convertView.getTag();
			}

			holderName.name = (TextView) convertView
					.findViewById(R.id.row_brand_name);

			if (holderName.name != null)
				holderName.name.setText(ei.getBrandName());

			holderName.fav_icon = (ImageButton) convertView
					.findViewById(R.id.row_all_brand_list_icon_normal);

			for (int m = 0; m < fb.size(); m++) {
				if (ei.getId().equalsIgnoreCase(fb.get(m).getBrand_id())) {
					holderName.fav_icon
							.setBackgroundResource(R.drawable.list_favorite_selected);
				}
			}

			holderName.fav_icon.setFocusable(false);
			holderName.fav_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					itemsSection.clear();

					holderName.fav_icon = (ImageButton) v
							.findViewById(R.id.row_all_brand_list_icon_normal);

					if (null != temp && temp.size() != 0) {

						Collections.sort(temp);

						char checkChar = ' ';

						for (int index = 0; index < temp.size(); index++) {

							BrandsList objItem = (BrandsList) temp.get(index);

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

					Item item = itemsSection.get(position);

					objSchoolname = (BrandsList) item;

					// ToastShow.getMessage(getContext(),
					// objSchoolname.getBrandName() + " from object");

					fb = dbHnadler.getAllContacts();

					String strId = "";
					for (int i = 0; i < fb.size(); i++) {

						if (fb.get(i).getBrand_id()
								.equalsIgnoreCase(objSchoolname.getId())) {
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

					if (strId.equalsIgnoreCase(objSchoolname.getId())) {
						dbHnadler.deleteSingleFavoriteBrand(objSchoolname
								.getId());

						holderName.fav_icon
								.setImageResource(R.drawable.list_favorite_normal);

						// flag = true;

					}

					else {
						holderName.fav_icon
								.setImageResource(R.drawable.list_favorite_selected);
						dbHnadler.addBrands(new FavoriteBrands(objSchoolname
								.getId(), objSchoolname.getBrandName(),
								"Distance 2 km."));

						// flag = false;

					}

					PrintLog.getWarnLog("Size of db :", fb.size() + "<><>");

				}
			});
		}
		return convertView;
	}

	public class ViewHolderName {
		public TextView name;
		public ImageButton fav_icon;
	}

	public class ViewHolderSectionName {
		public TextView section;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int arg0) {
		// TODO Auto-generated method stub
		Log.d("ListView", "Get position for section");
		for (int i = 0; i < temp.size(); i++) {
			item = temp.get(i).getBrandName().toString().toUpperCase();
			if (item.charAt(0) == sections.charAt(arg0)) {
				return i;
			}

		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		Log.d("ListView", "Get sections");
		String[] sectionsArr = new String[sections.length()];
		for (int i = 0; i < sections.length(); i++)
			sectionsArr[i] = "" + sections.charAt(i);

		return sectionsArr;
	}
}
