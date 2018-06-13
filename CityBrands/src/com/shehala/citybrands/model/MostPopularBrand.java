package com.shehala.citybrands.model;

public class MostPopularBrand {
	private String id = "";
	private String brand_name = "";
	private boolean isFavorite = false;
	
	

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	@Override
	public String toString() {
		return "MostPopularBrand [id=" + id + ", brand_name=" + brand_name
				+ "]";
	}

}
