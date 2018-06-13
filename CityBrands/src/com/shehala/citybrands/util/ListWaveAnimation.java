package com.shehala.citybrands.util;


import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;

import com.shehala.citybrands.R;

public class ListWaveAnimation {
	
	public static void showTheWave(Context c, ListView list)
	{

	final Animation animation = AnimationUtils
			.loadAnimation(c,
					R.anim.wave_scale);
	final LayoutAnimationController controller = new LayoutAnimationController(
			animation, 0.5f);
	list.setLayoutAnimation(controller);
	}

}
