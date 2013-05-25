package com.hesha.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import com.hesha.R;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;
	private Context context;

	public AsyncImageLoader(Context context) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		this.context = context;
	}

	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};

		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				if(null == drawable) {
					Resources res = context.getResources();
					drawable = res.getDrawable(R.drawable.wating);
				}
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public static Drawable loadImageFromUrl(String url) {
		//System.out.println("url:" + url);//注意url为空的处理
		if(null == url || "".equals(url)){
			return null;
		}
		InputStream inputStream;
		try {
			inputStream = new URL(url).openStream();
		} catch (IOException e) {
			//throw new RuntimeException(e);
			return null;
		}
		return Drawable.createFromStream(inputStream, "src");
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}