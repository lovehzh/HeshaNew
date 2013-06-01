package com.hesha.utils;


import com.hesha.R;
import com.hesha.tasks.CreateCollectionTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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
	
	public static void showCreateColDialog(final Context context, final String token) {
		final Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(R.layout.create_col_view);
		
		EditText etColName = (EditText)dialog.findViewById(R.id.et_col_name);
		final String colName = etColName.getText().toString().trim();
		final String colDes = "";
		
		Button btnOk = (Button)dialog.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new CreateCollectionTask(context, new ProgressDialog(context), token, colName, colDes).execute((Void)null);
			}
		});
		
		Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		
		dialog.setCancelable(true);
		dialog.show();
	}
}
