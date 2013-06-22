package com.hesha.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hesha.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SelectPhoto {
	public static final int INTENT_FROM_CAMERA = 11;
	public static final int INTENT_FROM_PHOTO_ALBUM = 12;
	public static final int INTENT_FROM_PHOTO_ZOOM = 13;
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
	
	public static Dialog getRiseUpDialog(final Context context) {
		final Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(R.layout.upload_photo_rise_up_dialog_view);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // set dialog display position here
		window.setWindowAnimations(R.style.rise_up);//set animation here
		dialog.show();
		
		Button btnFromShot = (Button)dialog.findViewById(R.id.btn_from_shot);
		btnFromShot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PHOTO_DIR.mkdirs();
                mCurrentPhotoFile = new File(PHOTO_DIR, SelectPhoto.getPhotoFileName());
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				// 下面这句指定调用相机拍照后的照片存储的路径
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//						Environment.getExternalStorageDirectory(), "product.jpg")));
				((Activity)context).startActivityForResult(intent, INTENT_FROM_CAMERA);
				dialog.dismiss();
			}
		});
		
		Button btnFromAlbums = (Button)dialog.findViewById(R.id.btn_from_albums);
		btnFromAlbums.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				((Activity)context).startActivityForResult(intent1, INTENT_FROM_PHOTO_ALBUM);
				dialog.dismiss();
			}
		});
		
		Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		return dialog;
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
