package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.BaseItem;
import com.hesha.bean.Collection;
import com.hesha.bean.ImageBean;
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

public class ColAndPreviewItemsAdapter extends ArrayAdapter<Collection>{
	private ArrayList<Collection> objs;
	private Context context;
	private AsyncImageLoader asyncImageLoader;
	private ListView listView;
	public ColAndPreviewItemsAdapter(Context context,
			int textViewResourceId, ArrayList<Collection> objs,
			ListView listView) {
		super(context, textViewResourceId, objs);
		this.context = context;
		this.objs = objs;
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("objs size = " + objs.size());
		final Collection collection = objs.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.colltion_item_without_title, null);
			holder = new Holder();
			
			
			holder.iv0 = (ImageView)convertView.findViewById(R.id.iv_0);
			holder.iv1 = (ImageView)convertView.findViewById(R.id.iv_1);
			holder.iv2 = (ImageView)convertView.findViewById(R.id.iv_2);
			
			holder.tvItemTitle = (TextView)convertView.findViewById(R.id.tv_item_title);
			holder.tvItemDes = (TextView)convertView.findViewById(R.id.tv_item_des);
			
			holder.tvPictursNum = (TextView)convertView.findViewById(R.id.tv_picturs_num);
			holder.tvGoodsNum = (TextView)convertView.findViewById(R.id.tv_goods_num);
			holder.tvCreator = (TextView)convertView.findViewById(R.id.tv_creator);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		
		String name = collection.getCollection_name();
		name = Utils.decodeSpecial(name);
		holder.tvItemTitle.setText(name);
		holder.tvItemDes.setText(collection.getCollection_des());
		holder.tvPictursNum.setText(collection.getImage_num() + "");
		holder.tvGoodsNum.setText(collection.getProduct_num() + "");
		holder.tvCreator.setText(collection.getUser_info().getUser_name());
		
		ArrayList<BaseItem> items = collection.getItems();
		
		ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
		imageViews.add(holder.iv0);
		imageViews.add(holder.iv1);
		imageViews.add(holder.iv2);
		
		for(int i=0; i<items.size(); i++) {
			ImageBean imageBean = items.get(i).getItem_image().get(0);
			String imageUrl = Constants.IMAGE_BASE_URL + imageBean.getThumb();
			ImageView iv = imageViews.get(i);
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
			
			if (cacheImagePictureContent == null) {
                iv.setImageResource(R.drawable.collection_loading_default);
            }else{
            		iv.setImageDrawable(cacheImagePictureContent);
            }
		}
		
		
		return convertView;
	}
	
	
	class Holder {
		ImageView iv0;
		ImageView iv1;
		ImageView iv2;
		
		TextView tvItemTitle;
		TextView tvItemDes;
		
		TextView tvPictursNum;
		TextView tvGoodsNum;
		TextView tvCreator;
 	}
	
}
