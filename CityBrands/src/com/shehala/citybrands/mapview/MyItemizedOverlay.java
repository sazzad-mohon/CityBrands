///***
// * Copyright (c) 2010 readyState Software Ltd
// * 
// * Licensed under the Apache License, Version 2.0 (the "License"); you may
// * not use this file except in compliance with the License. You may obtain
// * a copy of the License at
// * http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * 
// */
//
package com.shehala.citybrands.mapview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ViewFlipper;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.shehala.citybrands.BrandsDetailsWithSpecificBrands;
import com.shehala.citybrands.ShopDetailsWithSpecificBrands;
import com.shehala.citybrands.model.ShopList;
import com.shehala.citybrands.util.Allconstants;

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private final ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	@SuppressWarnings("unused")
	private final Context c;
	@SuppressWarnings("unused")
	private ViewFlipper flp;

	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(ItemizedOverlay.boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
	}

	public void addOverlay(OverlayItem overlay) {
		m_overlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		if (Allconstants.screenShopMap.equalsIgnoreCase("popular"))

		{
			ShopList spl = Allconstants.shopListFinal.get(index);
			new BrandsDetailsWithSpecificBrands(Allconstants.activity,
					Allconstants.mainFliper, spl,"map");
			Allconstants.strFavBack = "favDetailsFromMap";
			return true;

		}

		else if (Allconstants.screenShopMap.equalsIgnoreCase("favorite"))

		{
			ShopList spl = Allconstants.shopListFinal.get(index);
			new ShopDetailsWithSpecificBrands(Allconstants.activity,
					Allconstants.mainFliper, spl.getId().trim(), "mapList");
			Allconstants.strFavBack = "favDetailsFromMap";
			return true;

		}
		
		else 
		{
			ShopList spl = Allconstants.shopListFinal.get(index);
			new BrandsDetailsWithSpecificBrands(Allconstants.activity,
					Allconstants.mainFliper, spl,"map");
			Allconstants.strFavBack = "favDetailsFromMap";
			return true;
		}
		
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		if (!shadow) {
			super.draw(canvas, mapView, false);
		}

	}

}
