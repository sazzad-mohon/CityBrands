package com.shehala.citybrands.holder;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.util.PrintLog;

public class AllShopLists {

	private static Vector<ShopList> allShopList = new Vector<ShopList>();

	public static Vector<ShopList> getAllShopList() {
		return allShopList;
	}

	public static void setAllShopList(Vector<ShopList> allShopList) {
		AllShopLists.allShopList = allShopList;
	}

	public static ShopList getShopList(int pos) {
		return allShopList.elementAt(pos);
	}

	public static void setShopList(ShopList shopList) {
		AllShopLists.allShopList.addElement(shopList);
	}

	public static void removeAll() {
		allShopList.removeAllElements();
	}

	public static Vector<String> getShopNames() {

		final Vector<String> temp = new Vector<String>();
		for (ShopList spl : AllShopLists.allShopList) {
			temp.addElement(spl.getShop_name());
		}

		return temp;
	}

	public static Vector<String> getShopNamesByBrandsId(String brandId) {

		final Vector<String> temp = new Vector<String>();
		for (ShopList spl : AllShopLists.allShopList) {

			if (spl.getBrands_id().contains(brandId)) {
				temp.addElement(spl.getShop_name());
			}

		}
		PrintLog.getWarnLog("Size of Temp vector : ", temp.size()
				+ " in holder");
		return temp;

	}

	public static Vector<String> getShopNamesByBrandsName(String brandName) {

		final Vector<String> temp = new Vector<String>();
		for (ShopList spl : AllShopLists.allShopList) {

			if (spl.getBrands_name().contains(brandName)) {
				temp.addElement(spl.getShop_name());
			}

		}
		PrintLog.getWarnLog("Size of Temp vector : ", temp.size()
				+ " in holder");
		return temp;

	}

	public static String getShopIdByShopName(String name) {

		String id = "";

		for (ShopList spl : AllShopLists.allShopList) {

			if (spl.getShop_name().equals(name)) {
				id = spl.getId();
			}

		}
		PrintLog.getWarnLog("Id  : ", id + " in holder");
		return id;

	}

	public static String getShopDistanceByShopName(String name) {

		String distance = "";

		for (ShopList spl : AllShopLists.allShopList) {

			if (spl.getShop_name().equals(name)) {
				distance = spl.getDistance().trim();
			}

		}
		PrintLog.getWarnLog("Id  : ", distance + " in holder");
		return distance;

	}

	public static Vector<String> getShopAddress() {

		final Vector<String> temp = new Vector<String>();
		for (ShopList spl : AllShopLists.allShopList) {
			temp.addElement(spl.getAddress() + "," + spl.getZip_code());
		}

		return temp;
	}

	public static boolean isMached(String name) {
		boolean flag = false;
		for (ShopList spl : AllShopLists.allShopList) {
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

		for (ShopList spl : AllShopLists.allShopList) {
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

	public static Vector<String> getShopDistanceByBrandsId(String brandId) {

		final Vector<String> temp = new Vector<String>();
		for (ShopList mps : AllShopLists.allShopList) {

			if (mps.getBrands_id().contains(brandId)) {
				temp.addElement(mps.getDistance().trim());
			}

		}
		PrintLog.getWarnLog("Size of Temp vector : ", temp.size()
				+ " in holder");
		return temp;

	}

	public static List<ShopList> getAllShopsInfoByBrandName(String brandName) {
		List<ShopList> shoplist = new ArrayList<ShopList>();

		for (ShopList sl : AllShopLists.allShopList) {
			if (sl.getBrands_name().contains(brandName)) {

				ShopList spl = new ShopList();

				spl.setBrands_id(sl.getBrands_id());
				spl.setBrands_name(sl.getBrands_name());
				spl.setId(sl.getId());
				spl.setAddress(sl.getAddress());
				spl.setM_w(sl.getM_w());
				spl.setPhone(sl.getPhone());
				spl.setZip_code(sl.getZip_code());
				spl.setS(sl.getS());
				spl.setUrl(sl.getUrl());
				spl.setEmail(sl.getEmail());
				spl.setShop_name(sl.getShop_name());
				spl.setShop_image(sl.getShop_image());
				spl.setDistance(sl.getDistance());
				spl.setLatitude(sl.getLatitude());
				spl.setLongitude(sl.getLongitude());
				spl.setFacebookUrl(sl.getFacebookUrl());
				shoplist.add(spl);
			}
		}

		return shoplist;

	}
	
	public static List<ShopList> getAllShopsInfoByShopName(String shopName) {
		List<ShopList> shoplist = new ArrayList<ShopList>();

		for (ShopList sl : AllShopLists.allShopList) {
			if (sl.getShop_name().contains(shopName)) {

				ShopList spl = new ShopList();

				spl.setBrands_id(sl.getBrands_id());
				spl.setBrands_name(sl.getBrands_name());
				spl.setId(sl.getId());
				spl.setAddress(sl.getAddress());
				spl.setM_w(sl.getM_w());
				spl.setPhone(sl.getPhone());
				spl.setZip_code(sl.getZip_code());
				spl.setS(sl.getS());
				spl.setUrl(sl.getUrl());
				spl.setEmail(sl.getEmail());
				spl.setShop_name(sl.getShop_name());
				spl.setShop_image(sl.getShop_image());
				spl.setDistance(sl.getDistance());
				spl.setLatitude(sl.getLatitude());
				spl.setLongitude(sl.getLongitude());
				spl.setFacebookUrl(sl.getFacebookUrl());
				shoplist.add(spl);
			}
		}

		return shoplist;

	}

	
	
	
	public static ShopList getAllShopsInfoByShopId(String id) {
		ShopList spl = new ShopList();

		for (ShopList sl : AllShopLists.allShopList) {
			if (sl.getId().equalsIgnoreCase(id)) {

				

				spl.setBrands_id(sl.getBrands_id());
				spl.setBrands_name(sl.getBrands_name());
				spl.setId(sl.getId());
				spl.setAddress(sl.getAddress());
				spl.setM_w(sl.getM_w());
				spl.setPhone(sl.getPhone());
				spl.setZip_code(sl.getZip_code());
				spl.setS(sl.getS());
				spl.setUrl(sl.getUrl());
				spl.setEmail(sl.getEmail());
				spl.setShop_name(sl.getShop_name());
				spl.setShop_image(sl.getShop_image());
				spl.setDistance(sl.getDistance());
				spl.setLatitude(sl.getLatitude());
				spl.setLongitude(sl.getLongitude());
				spl.setFacebookUrl(sl.getFacebookUrl());
				
				PrintLog.getErrorLog("spl.getShop_name() in holder :", spl.getShop_name());
				
			}
		}

		return spl;

	}

}
