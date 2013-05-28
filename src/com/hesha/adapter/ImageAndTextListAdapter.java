package com.hesha.adapter;

import java.util.List;

import com.hesha.R;
import com.hesha.ViewCache;
import com.hesha.bean.BaseItem;
import com.hesha.constants.Constants;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageAndTextListAdapter extends ArrayAdapter<BaseItem> {

        private GridView gridView;
        private AsyncImageLoader asyncImageLoader;
        private String baseUrl;
        public ImageAndTextListAdapter(Activity activity, List<BaseItem> imageAndTexts, GridView gridView1) {
            super(activity, 0, imageAndTexts);
            this.gridView = gridView1;
            asyncImageLoader = new AsyncImageLoader(activity);
            baseUrl = Constants.IMAGE_BASE_URL;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Activity activity = (Activity) getContext();

            // Inflate the views from XML
            View rowView = convertView;
            ViewCache viewCache;
            if (rowView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                rowView = inflater.inflate(R.layout.grid_item, null);
                viewCache = new ViewCache(rowView);
                rowView.setTag(viewCache);
            } else {
                viewCache = (ViewCache) rowView.getTag();
            }
            BaseItem baseItem = getItem(position);

            // Load the image and set it on the ImageView
            String imageUrl = baseUrl + baseItem.getItem_image().get(0).getThumb();
            ImageView imageView = viewCache.getImageView();
            imageView.setTag(imageUrl);
            Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new ImageCallback() {
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewByTag = (ImageView) gridView.findViewWithTag(imageUrl);
                    if (imageViewByTag != null) {
                        imageViewByTag.setImageDrawable(imageDrawable);
                    }
                }
            });
            if (cachedImage == null) {
                imageView.setImageResource(R.drawable.loading);
            }else{
                imageView.setImageDrawable(cachedImage);
            }
            // Set the text on the TextView
            TextView textView = viewCache.getTextView();
            textView.setText(baseItem.getItem_name());
            
            //Set status
            TextView tvStatus = viewCache.getTvStatus();
            if(baseItem.getStatus() == 0) {
            	tvStatus.setVisibility(View.VISIBLE);
            }else {
            	tvStatus.setVisibility(View.GONE);
            }
            return rowView;
        }

}