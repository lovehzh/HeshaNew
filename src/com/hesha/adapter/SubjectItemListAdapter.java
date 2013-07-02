package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.BaseItem;
import com.hesha.bean.SubjectItem;
import com.hesha.constants.Constants;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class SubjectItemListAdapter extends ArrayAdapter<BaseItem> {
	private ArrayList<BaseItem> baseitems;
	private Context context;
	private AsyncImageLoader asyncImageLoader;
	private ListView listView;
	
	public SubjectItemListAdapter(Context context, int textViewResourceId, ArrayList<BaseItem> baseitems, ListView listView) {
		super(context, textViewResourceId, baseitems);
		this.baseitems = baseitems;
		this.context = context;
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader(context);		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SubjectItem baseItem = (SubjectItem)baseitems.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.row_subject, null);
			
			holder = new Holder();
			holder.tvItemName = (TextView)convertView.findViewById(R.id.tv_item_name);
			holder.tvItemNameEn= (TextView)convertView.findViewById(R.id.tv_item_name_en);
			holder.tvUnitPrice = (TextView)convertView.findViewById(R.id.tv_unit_price);
			holder.tvSailersNum = (TextView)convertView.findViewById(R.id.tv_sailers_num);
			holder.icon = (ImageView)convertView.findViewById(R.id.iv_icon);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		
		holder.tvItemName.setText(baseItem.getItem_name());
		holder.tvItemNameEn.setText(baseItem.getEn_name());
		holder.tvUnitPrice.setText("￥" + baseItem.getPrice());
		holder.tvSailersNum.setText(Utils.replayDigital(context.getString(R.string.there_are_sailers), baseItem.getShop_nums()));
		
		String imageUrl = Constants.IMAGE_BASE_URL + baseItem.getItem_image().get(0).getThumb();
		ImageView iv = holder.icon;
		iv.setTag(imageUrl);
		
		Drawable cacheImagePictureContent = asyncImageLoader.loadDrawable(imageUrl, 
				new AsyncImageLoader.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
						ImageView imageViewByTag = (ImageView)listView.findViewWithTag(imageUrl);
						if(null != imageViewByTag){
							imageViewByTag.setImageDrawable(imageDrawable);
						}
					}
				});
		/**
		if (null == cacheImagePictureContent) {
			try {
			Thread.sleep(500);  //保证相同地址的图片可以加载上
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		**/
		if(null == cacheImagePictureContent) {
			iv.setImageResource(R.drawable.default_avatar);
		}else {
			iv.setImageDrawable(cacheImagePictureContent);
		}
		return convertView;
	}
	
	class Holder {
		TextView tvItemName;
		TextView tvItemNameEn;
		TextView tvUnitPrice;
		TextView tvSailersNum;
		ImageView icon;
	}
}
