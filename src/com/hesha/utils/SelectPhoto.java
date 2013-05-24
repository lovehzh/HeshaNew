package com.hesha.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class SelectPhoto {
	public static final int INTENT_FROM_CAMERA = 1;
	public static final int INTENT_FROM_PHOTO_ALBUM = 2;
	private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	private static File mCurrentPhotoFile;
	
	public static File getmCurrentPhotoFile() {
		return mCurrentPhotoFile;
	}

	public static void setmCurrentPhotoFile(File mCurrentPhotoFile) {
		SelectPhoto.mCurrentPhotoFile = mCurrentPhotoFile;
	}

	public static File showSelectPhotoDialog(final Context context) {
		String[] items = new String[2];
		items[0] = "手机拍照";
		items[1] = "手机相册";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("上传照片");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0) {
					PHOTO_DIR.mkdirs();
                    mCurrentPhotoFile = new File(PHOTO_DIR, SelectPhoto.getPhotoFileName());
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					// 下面这句指定调用相机拍照后的照片存储的路径
//					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//							Environment.getExternalStorageDirectory(), "product.jpg")));
					((Activity)context).startActivityForResult(intent, INTENT_FROM_CAMERA);
				}else {
					Intent intent1 = new Intent(Intent.ACTION_PICK, null);
					intent1.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					((Activity)context).startActivityForResult(intent1, INTENT_FROM_PHOTO_ALBUM);
				}
				
			}
		});
		
		builder.create().show();
		
		return mCurrentPhotoFile;
	}
	
	/**
     * Create a file name for the icon photo using current time.
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}
