package com.shehala.citybrands.holder;

import java.util.Vector;

import com.shehala.citybrands.model.BrandsList;
import com.shehala.citybrands.util.PrintLog;

public class AllBrandsList {

	private static Vector<BrandsList> allBrandsList = new Vector<BrandsList>();

	public static Vector<BrandsList> getAllBrandsList() {
		return allBrandsList;
	}

	public static void setAllBrandsList(Vector<BrandsList> allBrandsList) {
		AllBrandsList.allBrandsList = allBrandsList;
	}

	public static BrandsList getBrandsList(int pos) {
		return allBrandsList.elementAt(pos);
	}

	public static void setBrandsList(BrandsList brandsList) {
		AllBrandsList.allBrandsList.addElement(brandsList);
	}

	public static void removeAll() {
		allBrandsList.removeAllElements();
	}

	public static String getBrandIdByName(String name) {

		String id = "";

		for (BrandsList brl : allBrandsList) {

			if (name.equalsIgnoreCase(brl.getBrandName())) {

				id = brl.getId().toString();

				break;
			}
		}

		PrintLog.showLog("hitUserOrBrandId in holder : " + id);

		return id;
	}

	public static Vector<String> getAllBrandsName() {

		final Vector<String> temp = new Vector<String>();
		for (BrandsList bl : AllBrandsList.allBrandsList) {
			temp.addElement(bl.getBrandName());
		}
		return temp;
	}

	public static Vector<BrandsList> getAllbrands() {
		Vector<BrandsList> brandList = new Vector<BrandsList>();

		for (BrandsList sl : AllBrandsList.getAllBrandsList()) {
			if (sl.getBrandName().length() == 0)

			{
				continue;

			}

			else {
				BrandsList spl = new BrandsList();

				spl.setBrandName(sl.getBrandName());
				spl.setId(sl.getId());

				brandList.add(spl);
			}
		}

		return brandList;

	}

}
