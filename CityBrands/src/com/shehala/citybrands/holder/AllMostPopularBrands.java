package com.shehala.citybrands.holder;

import java.util.Vector;

import com.shehala.citybrands.model.MostPopularBrand;
import com.shehala.citybrands.util.PrintLog;

public class AllMostPopularBrands {

	private static Vector<MostPopularBrand> allPopularBrands = new Vector<MostPopularBrand>();

	public static Vector<MostPopularBrand> getAllPopularBrands() {
		return allPopularBrands;
	}

	public static void setAllPopularBrands(
			Vector<MostPopularBrand> allPopularBrands) {
		AllMostPopularBrands.allPopularBrands = allPopularBrands;
	}

	public static MostPopularBrand getPopularBrands(int pos) {
		return allPopularBrands.elementAt(pos);
	}

	public static void setAllPopularBrands(MostPopularBrand popularBrands) {
		AllMostPopularBrands.allPopularBrands.addElement(popularBrands);
	}

	public static void removeAll()

	{
		allPopularBrands.removeAllElements();

	}

	public static Vector<MostPopularBrand> getAllMostPopularBrandWithIsFavorite(
			String brandId) {
		Vector<MostPopularBrand> popularList = new Vector<MostPopularBrand>();

		for (MostPopularBrand sl : AllMostPopularBrands.allPopularBrands) {

			MostPopularBrand mpl = new MostPopularBrand();
			mpl.setId(sl.getId());
			mpl.setBrand_name(sl.getBrand_name());
			if (sl.getId().equalsIgnoreCase(brandId)) {
				mpl.setFavorite(true);
			} else {
				mpl.setFavorite(false);
			}
//			PrintLog.getErrorLog("Is favorite or not : ", mpl.isFavorite()
//					+ " <><>");
			popularList.add(mpl);

		}
		
		PrintLog.getErrorLog("getAllMostPopularBrandWithIsFavorite()", popularList.size()+"<>");

		return popularList;

	}
}
