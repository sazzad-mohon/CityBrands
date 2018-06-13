package com.shehala.citybrands.util;

public class AllUrl {

	public static String faceBookUrl = "https://www.facebook.com/pages/Citybrands/378518865574273?fref=ts";

	public static String twitterUrl = "https://twitter.com/CityBrands_dk";

	// Login url

	public static String loginUrl = BaseUrl.baseUrl
			+ "appUsers/login/index.php";

	// Registration url

	public static String registraionUrl = BaseUrl.baseUrl
			+ "appUsers/registration/index.php";

	// Edit Password url

	public static String editPasswordUrl = BaseUrl.baseUrl
			+ "appUsers/editpassword/index.php";

	// All shop url

	public static String allShopsUrl(String lat, String lon) {
		return BaseUrl.baseUrl + "shops/shopbydistance/index.php?lat=" + lat
				+ "&long=" + lon;
	}

	// All brands url

	public static String allBrandsUrl = BaseUrl.baseUrl
			+ "brands/showallbrands/index.php";

	// Popular brands

	public static String mostPopularBrands = BaseUrl.baseUrl
			+ "brands/showtopbrands/index.php";

	// Popular shop

	public static String getMostPopularShop(String lat, String lon) {
		return BaseUrl.baseUrl + "shops/showtopshops/index.php?lat=" + lat
				+ "&long=" + lon;
	}

	// News list url

	public static String newsListURL = BaseUrl.baseUrl + "news/showallnews/";

	// Delete user url

	public static String deleteUserURL = BaseUrl.baseUrl
			+ "appUsers/deleteappuser/";

	// Edit user url

	public static String editUserURL = BaseUrl.baseUrl
			+ "appUsers/appuseredit/";

	// increase shop hit url

	public static String increaseShopHitURL = BaseUrl.baseUrl
			+ "shops/increaseshophit/";

	// increase brand hit url

	public static String increaseBrandHitURL = BaseUrl.baseUrl
			+ "brands/increasebrandhit/";
}
