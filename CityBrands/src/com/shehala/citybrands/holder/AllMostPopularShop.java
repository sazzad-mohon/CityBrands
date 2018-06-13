package com.shehala.citybrands.holder;

import java.util.Vector;

import android.util.Log;

import com.shehala.citybrands.model.MostPopularShop;
import com.shehala.citybrands.util.PrintLog;

public class AllMostPopularShop {

	private static Vector<MostPopularShop> allPopularShops = new Vector<MostPopularShop>();

	public static Vector<MostPopularShop> getAllPopularShops() {
		return allPopularShops;
	}

	public static void setAllPopularShops(
			Vector<MostPopularShop> allPopularShops) {
		AllMostPopularShop.allPopularShops = allPopularShops;
	}

	public static MostPopularShop getPopularShops(int pos) {
		return allPopularShops.elementAt(pos);
	}

	public static void setPopularShops(MostPopularShop popularShops) {
		AllMostPopularShop.allPopularShops.addElement(popularShops);
	}

	public static void removeAll()

	{
		allPopularShops.removeAllElements();
	}
	
	

	public static Vector<String> getShopNamesByBrandsId(String brandId) {

		final Vector<String> temp = new Vector<String>();
		for (MostPopularShop mps : AllMostPopularShop.allPopularShops) {

			if (mps.getBrands_id().contains(brandId)) {
				temp.addElement(mps.getShop_name());
			}

		}
		PrintLog.getWarnLog("Size of Temp vector : ", temp.size()
				+ " in holder");
		return temp;

	}

	
	public static Vector<String> getShopDistanceByBrandsId(String brandId) {

		final Vector<String> temp = new Vector<String>();
		for (MostPopularShop mps : AllMostPopularShop.allPopularShops) {

			if (mps.getBrands_id().contains(brandId)) {
				temp.addElement(mps.getDistance().trim());
			}

		}
		PrintLog.getWarnLog("Size of Temp vector : ", temp.size()
				+ " in holder");
		return temp;

	}
	
	
	public static boolean isMached(String name) {
		boolean flag = false;
		for (MostPopularShop spl : AllMostPopularShop.allPopularShops) {
			if (spl.getShop_name().equals(name))

			{
				flag = true;
			}

		}
		return flag;
	}
	
	
	public static int shopIdToIndex(String name) {

		int i = 0;

		int pos = 0;

		for (MostPopularShop spl :  AllMostPopularShop.allPopularShops) {
			if (spl.getShop_name().equals(name)) {
				pos = i;
				Log.d("Pos", "" + pos);

				Log.d("I", "" + i);
				// return pos;
				break;
			}

			i++;
		}

		return pos;
	}


}
