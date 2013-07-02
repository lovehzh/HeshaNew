package com.hesha.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hesha.R;
import com.hesha.bean.BaseItem;
import com.hesha.constants.Constants;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.AsyncImageLoader.ImageCallback;

public class ItemAdapter extends BaseAdapter {

	private List<BaseItem> mList = new ArrayList<BaseItem>();
	private Context mContext;

	private String footerviewItem;

	private FooterView footerView;

	private boolean footerViewEnable = false;
	private OnClickListener ml;

	private GridView gridView;
	private AsyncImageLoader asyncImageLoader;
	private String baseUrl;
	public ItemAdapter(Context context, List<BaseItem> list, GridView gridView) {
		if (list != null) {
			this.mList = list;
		}
		this.mContext = context;
		
		asyncImageLoader = new AsyncImageLoader(mContext);
        baseUrl = Constants.IMAGE_BASE_URL;
        this.gridView = gridView;
	}

	public boolean isFooterViewEnable() {
		return footerViewEnable;
	}

	/**
	 * 存放列表项控件句柄
	 */
	public static class ViewHolder {

		public TextView itemview;
		public ImageView iv;

	}

	public void setFootreViewEnable(boolean enable) {
		footerViewEnable = enable;
	}

	public void setOnFooterViewClickListener(OnClickListener l) {
		ml = l;
	}

	private int getDisplayWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return width;
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup parent) {
		// 伪造的空项可以根据楼盘id来确定。
		if (footerViewEnable && i == mList.size() - 1) {
			if (footerView == null) {
				footerView = new FooterView(parent.getContext());

				GridView.LayoutParams pl = new GridView.LayoutParams(
						getDisplayWidth((Activity) mContext),
						LayoutParams.WRAP_CONTENT);
				footerView.setLayoutParams(pl);
				footerView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (ml != null) {
							ml.onClick(v);
						}

					}
				});
			}
			setFooterViewStatus(FooterView.MORE);
			return footerView;
		}
		final ViewHolder holder;

		if (convertView == null
				|| (convertView != null && convertView == footerView)) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
			holder = new ViewHolder();
			holder.iv =  (ImageView)convertView.findViewById(R.id.image);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		int  index = (Integer)getItem(i);
		BaseItem baseItem = mList.get(index);

        // Load the image and set it on the ImageView
        String imageUrl = baseUrl + baseItem.getItem_image().get(0).getThumb();
        ImageView imageView = holder.iv;
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
            imageView.setImageResource(R.drawable.collection_loading_default);
        }else{
            imageView.setImageDrawable(cachedImage);
        }
		
		
		return convertView;
	}

	public FooterView getFooterView() {
		return footerView;
	}

	public void setFooterViewStatus(int status) {
		if (footerView != null) {
			footerView.setStatus(status);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
