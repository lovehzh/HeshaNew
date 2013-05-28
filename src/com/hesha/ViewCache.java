package com.hesha;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {

    private View baseView;
    private TextView textView;
    private TextView tvProductName;
    private ImageView imageView;
    private TextView tvStatus;

    public ViewCache(View baseView) {
        this.baseView = baseView;
    }

    public TextView getTextView() {
        if (textView == null) {
            textView = (TextView) baseView.findViewById(R.id.text);
        }
        return textView;
    }

    public ImageView getImageView() {
        if (imageView == null) {
            imageView = (ImageView) baseView.findViewById(R.id.image);
        }
        return imageView;
    }

	public TextView getTvProductName() {
		if (tvProductName == null) {
			tvProductName = (TextView) baseView.findViewById(R.id.product_name);
        }
		return tvProductName;
	}

	public TextView getTvStatus() {
		if(tvStatus == null) {
			tvStatus = (TextView) baseView.findViewById(R.id.tvStatus);
		}
		return tvStatus;
	}
	
	

    
}