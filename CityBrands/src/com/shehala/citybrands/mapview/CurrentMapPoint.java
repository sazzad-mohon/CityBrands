package com.shehala.citybrands.mapview;

import java.util.Vector;

import com.google.android.maps.GeoPoint;

public class CurrentMapPoint {

	/**
	 * @return the allPoints
	 */
	public static Vector<MapLocation> getAllPoints(boolean isTest) {
		

	
		
		if (isTest) {


			final GeoPoint point = new GeoPoint((int) (51.5174723 * 1E6),
					(int) (-0.0899537 * 1E6));

			final MapLocation location1 = new MapLocation("first place", point);
			final MapLocation location2 = new MapLocation("2nd place",
					51.5274723, -0.0999537);
			final MapLocation location3 = new MapLocation("3rd place",
					51.5474723, -0.0999537);

			CurrentMapPoint.setPoint(location1);
			CurrentMapPoint.setPoint(location2);
			CurrentMapPoint.setPoint(location3);

			return CurrentMapPoint.allPoints;
		}
		
		

		return CurrentMapPoint.allPoints;
	}

	/**
	 * @param allPoints
	 *            the allPoints to set
	 */
	public static void setAllPoints(final Vector<MapLocation> allPoints1) {
		CurrentMapPoint.allPoints = allPoints1;
	}

	private static Vector<MapLocation> allPoints = new Vector<MapLocation>();

	/**
	 * @return the allPoints
	 */
	public static MapLocation getPoints(final int pos) {
		return CurrentMapPoint.allPoints.get(pos);
	}

	/**
	 * @param allPoints
	 *            the allPoints to set
	 */
	public static void setPoint(final MapLocation allPoints1) {
		CurrentMapPoint.allPoints.add(allPoints1);
	}

	public static void removeAllPoints() {

		CurrentMapPoint.allPoints.clear();
	}

}
