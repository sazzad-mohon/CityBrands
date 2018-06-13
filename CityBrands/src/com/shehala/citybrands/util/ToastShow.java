package com.shehala.citybrands.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shehala.citybrands.R;

public class ToastShow {

//	public static void getMessage(Context con, String text) {
//		Toast.makeText(con, text, Toast.LENGTH_LONG).show();
//	}
	
	public static void getMessage(Activity act, String text) {

		try {

			LayoutInflater myInflator = act.getLayoutInflater();
			View myLayout = myInflator.inflate(R.layout.custom_toast_layout,
					(ViewGroup) act.findViewById(R.id.toastlayout));

			TextView myMessage = (TextView) myLayout
					.findViewById(R.id.txtvdisplay);
			myMessage.setText(text);

			Toast myToast = new Toast(act);
			myToast.setDuration(Toast.LENGTH_LONG);
			
			if (Allconstants.screenSize.equalsIgnoreCase("800x1232")
					|| Allconstants.screenSize.equalsIgnoreCase("800x1280")
					|| Allconstants.screenSize.equalsIgnoreCase("720x1280"))
			{
				myToast.setGravity(Gravity.BOTTOM, 0, 190);
			}
				
			
			myToast.setView(myLayout);
			myToast.show();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
