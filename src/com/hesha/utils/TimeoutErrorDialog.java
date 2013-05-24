package com.hesha.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class TimeoutErrorDialog {
	/**
	 * 服务器超时错误提示
	 */
	public static void showTimeoutError(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("获取数据失败");
		builder.setMessage("链接服务器超时，请检查网络是否正确");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		
		builder.create().show();
	}
}
