package com.shehala.citybrands.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shehala.citybrands.util.PrintLog;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "favorite";

	// Favorite_Brand Table name
	private static final String TABLE_FAVORITE_BRAND = "favorite_brands";
	private static final String TABLE_FAVORITE_SHOP = "favorite_shop";

	// Favorite_Shop Table Columns names
	private static final String SHOP_ID = "shop_id";
	private static final String SHOP_NAME = "shop_name";
	private static final String SHOP_DISTANCE = "shop_distance";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";

	// Favorite_Brand Table Columns names
	private static final String BRAND_ID = "brand_id";
	private static final String BRAND_NAME = "brand_name";
	private static final String DISTANCE = "distance";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FAVORITE_BRAND_TABLE = "CREATE TABLE "
				+ TABLE_FAVORITE_BRAND + "(" + BRAND_ID + " TEXT PRIMARY KEY,"
				+ BRAND_NAME + " TEXT," + DISTANCE + " TEXT" + ")";

		String CREATE_FAVORITE_SHOP_TABLE = "CREATE TABLE "
				+ TABLE_FAVORITE_SHOP + "(" + SHOP_ID + " TEXT PRIMARY KEY,"
				+ SHOP_NAME + " TEXT," + SHOP_DISTANCE + " TEXT," + LATITUDE
				+ " TEXT," + LONGITUDE + " TEXT" + ")";

		db.execSQL(CREATE_FAVORITE_BRAND_TABLE);
		db.execSQL(CREATE_FAVORITE_SHOP_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		PrintLog.getWarnLog("Old version : ", oldVersion + "<>");
		PrintLog.getWarnLog("New version : ", newVersion + "<>");

	}

	// Adding new brands
	public void addShops(FavoriteShops fs) {

		SQLiteDatabase db = this.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(SHOP_ID, fs.getShop_id());
			values.put(SHOP_NAME, fs.getShop_name());
			values.put(SHOP_DISTANCE, fs.getShop_distance());
			values.put(LATITUDE, fs.getLatitude());
			values.put(LONGITUDE, fs.getLongitude());

			// Inserting Row
			db.insert(TABLE_FAVORITE_SHOP, null, values);
			db.close(); // Closing database connection
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// Getting All Shops

	public List<FavoriteShops> getAllShops() {
		List<FavoriteShops> shoplist = new ArrayList<FavoriteShops>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE_SHOP;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				FavoriteShops fs = new FavoriteShops();
				fs.setShop_id(cursor.getString(0));
				fs.setShop_name(cursor.getString(1));
				fs.setShop_distance(cursor.getString(2));
				fs.setLatitude(cursor.getString(3));
				fs.setLongitude(cursor.getString(4));
				// Adding contact to list
				shoplist.add(fs);

			} while (cursor.moveToNext());
		}

		// return contact list
		db.close();
		return shoplist;

	}

	// Adding new brands
	public void addBrands(FavoriteBrands fv) {

		SQLiteDatabase db = this.getWritableDatabase();

		try {
			ContentValues values = new ContentValues();
			values.put(BRAND_ID, fv.getBrand_id());
			values.put(BRAND_NAME, fv.getBrand_name());
			values.put(DISTANCE, fv.getDistance());

			// Inserting Row
			db.insert(TABLE_FAVORITE_BRAND, null, values);
			db.close(); // Closing database connection
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// Getting All Brands
	public List<FavoriteBrands> getAllContacts() {
		List<FavoriteBrands> brandlist = new ArrayList<FavoriteBrands>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE_BRAND;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				FavoriteBrands fb = new FavoriteBrands();
				fb.setBrand_id(cursor.getString(0));
				fb.setBrand_name(cursor.getString(1));
				fb.setDistance(cursor.getString(2));
				// Adding contact to list
				brandlist.add(fb);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return brandlist;
	}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FAVORITE_BRAND;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		// return count
		return cursor.getCount();
	}

	// Deleting single favorite shop
	public void deleteSingleFavoriteShop(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FAVORITE_SHOP, SHOP_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	
	// Deleting single favorite brand
		public void deleteSingleFavoriteBrand(String fb) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_FAVORITE_BRAND, BRAND_ID + " = ?",
					new String[] { String.valueOf(fb) });
			db.close();
		}
}
