package com.hesha.gallery;

import java.util.List;

import com.hesha.R;
import com.hesha.SubjectDetailsActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	public static final BaseAdapter Adapter = null;
	private List<String> imageUrls; // 图片地址list
	private Context context;
	int mGalleryItemBackground;

	public ImageAdapter(List<String> imageUrls, Context context) {
		this.imageUrls = imageUrls;
		this.context = context;
		// /*
		// * 使用在res/values/attrs.xml中的<declare-styleable>定义 的Gallery属性.
		// */
		TypedArray a = context.obtainStyledAttributes(R.styleable.Gallery1);
		/* 取得Gallery属性的Index id */
		mGalleryItemBackground = a.getResourceId(
				R.styleable.Gallery1_android_galleryItemBackground, 0);
		/* 让对象的styleable属性能够反复使用 */
		a.recycle();
	}

	public int getCount() {
		return imageUrls.size();
	}

	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap image;
		ImageView view = new ImageView(context);
		image = SubjectDetailsActivity.imagesCache.get(imageUrls.get(position));
		// 从缓存中读取图片
		if (image == null) {
			image = SubjectDetailsActivity.imagesCache.get("background_non_load");
		}
		// 设置所有图片的资源地址
		view.setImageBitmap(image);
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new Gallery.LayoutParams(240, 240));
		view.setBackgroundResource(mGalleryItemBackground);
		/* 设置Gallery背景图 */
		
//		((SubjectDetailsActivity)context).changePointView(position);
		return view;
	}
}
