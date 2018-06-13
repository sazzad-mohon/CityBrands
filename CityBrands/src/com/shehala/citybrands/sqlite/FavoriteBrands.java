package com.shehala.citybrands.sqlite;

public class FavoriteBrands {
	private String brand_id = "";
	private String brand_name = "";
	private String distance = "";

	
	
	
	public FavoriteBrands(String brand_id, String brand_name, String distance) {
		super();
		this.brand_id = brand_id;
		this.brand_name = brand_name;
		this.distance = distance;
	}

	
	
	public FavoriteBrands() {
	
	}



	public String getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "FavoriteBrands [brand_id=" + brand_id + ", brand_name="
				+ brand_name + ", distance=" + distance + "]";
	}

}
