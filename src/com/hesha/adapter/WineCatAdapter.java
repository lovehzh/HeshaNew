package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.choice.WineCatBean;
import com.hesha.constants.Constants;
import com.hesha.utils.AsyncImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WineCatAdapter extends ArrayAdapter<WineCatBean>{
	private ArrayList<WineCatBean> objs;
	private Context context;
	private AsyncImageLoader asyncImageLoader;
	private ListView listView;
	public WineCatAdapter(Context context,
			int textViewResourceId, ArrayList<WineCatBean> objs,
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
		final WineCatBean comment = objs.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.row_choice_catogory, null);
			holder = new Holder();
			
			
			holder.ivAvatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
			
			
			holder.tvTypeName = (TextView)convertView.findViewById(R.id.tv_cat_name);
			holder.tvDes = (TextView)convertView.findViewById(R.id.tv_des);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		
		
		holder.tvTypeName.setText(comment.getType_name());
		holder.tvDes.setText(comment.getDes());
		
		
//		String imageUrl = Constants.IMAGE_BASE_URL + comment.getImage();
//		ImageView iv = holder.ivAvatar;
//		iv.setTag(imageUrl);
//		
//		Drawable cacheImagePictureContent = asyncImageLoader.loadDrawable(imageUrl, 
//				new AsyncImageLoader.ImageCallback() {
//					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//						ImageView imageViewByTag = (ImageView)listView.findViewWithTag(imageUrl);
//						if(null != imageViewByTag){
//							imageViewByTag.setImageDrawable(imageDrawable);
//						}
//					}
//				});
//		
//		iv.setImageDrawable(cacheImagePictureContent);
		
		
		return convertView;
	}
	
	
	class Holder {
		ImageView ivAvatar;
		TextView tvTypeName;
		TextView tvDes;
 	}
	
}
