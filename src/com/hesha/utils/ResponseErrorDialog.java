package com.hesha.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ResponseErrorDialog {
	/**
	 * 服务返回错误提示
	 */
	public static void show(Context context, String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("获取数据失败");
		builder.setMessage("错误：" + content);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		
		builder.create().show();
	}
}
