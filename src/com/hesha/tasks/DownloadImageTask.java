package com.hesha.tasks;

import java.io.IOException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Drawable>{
	private ImageView mImageView;
	public DownloadImageTask(ImageView mImageView) {
		this.mImageView = mImageView;
	}
	
	protected Drawable doInBackground(String... urls) {    
        return loadImageFromNetwork(urls[0]);    
    }    

    protected void onPostExecute(Drawable result) {    
        mImageView.setImageDrawable(result);    
    }
    
    private Drawable loadImageFromNetwork(String imageUrl)  
    {  
        Drawable drawable = null;  
        try {  
            // 可以在这里通过文件名来判断，是否本地有此图片  
            drawable = Drawable.createFromStream(  
                    new URL(imageUrl).openStream(), "image.jpg");  
        } catch (IOException e) {  
            Log.d("test", e.getMessage());  
        }  
        if (drawable == null) {  
            Log.d("test", "null drawable");  
        } else {  
            Log.d("test", "not null drawable");  
        }  
          
        return drawable ;  
    }  
}
