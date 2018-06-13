package com.shehala.citybrands.sqlite;

public class FavoriteShops {

	private String shop_id = "";
	private String shop_name = "";
	private String shop_distance = "";
	private String latitude = "";
	private String longitude = "";

	public FavoriteShops() {

	}

	public FavoriteShops(String shop_id, String shop_name,
			String shop_distance, String latitude, String longitude) {
		super();
		this.shop_id = shop_id;
		this.shop_name = shop_name;
		this.shop_distance = shop_distance;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_distance() {
		return shop_distance;
	}

	public void setShop_distance(String shop_distance) {
		this.shop_distance = shop_distance;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "FavoriteShops [shop_id=" + shop_id + ", shop_name=" + shop_name
				+ ", shop_distance=" + shop_distance + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

}
