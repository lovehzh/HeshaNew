package com.hesha.utils;


import com.hesha.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class MyDialog {
	public static void showInfoDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.tips);
		builder.setMessage("系统正在维护，请稍后再试");
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();
	}
	
	public static void showInfoDialog(Context context, int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();
	}
	
	public static void showInfoDialog(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();
	}
}
