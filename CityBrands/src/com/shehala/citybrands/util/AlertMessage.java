package com.shehala.citybrands.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.shehala.citybrands.MyTabActivity;
import com.shehala.citybrands.R;

public class AlertMessage {

	public static void showMessage(final Context c, final String title,
			final String s) {
		final AlertDialog.Builder aBuilder = new AlertDialog.Builder(c);
		aBuilder.setTitle("Alarma!");
		aBuilder.setIcon(R.drawable.app_icon);
		aBuilder.setMessage(s);
		
		aBuilder.setCancelable(false);
		

		aBuilder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.dismiss();
				
				if(MyTabActivity.getInstance() != null)
				{
					MyTabActivity.getInstance().openTab();
				}
			}

		});

		aBuilder.show();
	}

	public static void showProgress(final Context c,
			ProgressDialog progressDialog) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(c);
		}
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(c, "Please wait...",
					"Buffering...", true, true);
		}

	}

	public static void cancelProgress(final Context c,
			final ProgressDialog progressDialog) {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();

		}
	}
}
