package com.shehala.citybrands.mapview;

import com.google.android.maps.GeoPoint;


/** Class to hold our location information */
public class MapLocation {

	private final GeoPoint point;
	private final String name;

	public MapLocation(final String name, final double latitude,
			final double longitude) {
		this.name = name;
		point = new GeoPoint((int) (latitude * 1e6), (int) (longitude * 1e6));
	}

	public MapLocation(final String a, final GeoPoint point) {

		this.point = point;

		name = a;
	}

	public String getName() {
		return name;
	}

	public GeoPoint getPoint() {
		return point;
	}

}
