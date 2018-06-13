package com.shehala.citybrands.model;

import com.shehala.citybrands.sectionindexlistview.Item;

public class BrandsList implements Item ,Comparable<BrandsList>{

	private String id = "";
	private String brandName = "";

	
	
	public BrandsList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BrandsList(String brandName) {
		super();
		this.brandName = brandName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Override
	public String toString() {
		return "BrandsList [id=" + id + ", brandName=" + brandName + "]";
	}

	@Override
	public int compareTo(BrandsList another) {
		// TODO Auto-generated method stub
		return this.getBrandName().compareTo(another.getBrandName());
	}

	public char[] charAt(int i) {
		
		return null;
	}

	@Override
	public boolean isSectionItem() {
		// TODO Auto-generated method stub
		return false;
	}



}
