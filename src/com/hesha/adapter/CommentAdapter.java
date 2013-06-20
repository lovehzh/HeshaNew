package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.Comment;
import com.hesha.constants.Constants;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.DateUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<Comment>{
	private ArrayList<Comment> objs;
	private Context context;
	private AsyncImageLoader asyncImageLoader;
	private ListView listView;
	public CommentAdapter(Context context,
			int textViewResourceId, ArrayList<Comment> objs,
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
		final Comment comment = objs.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.row_comment, null);
			holder = new Holder();
			
			
			holder.ivAvatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
			
			
			holder.tvUsername = (TextView)convertView.findViewById(R.id.tv_username);
			holder.tvContent = (TextView)convertView.findViewById(R.id.tv_content_xxx);
			holder.tvDate = (TextView)convertView.findViewById(R.id.tv_date);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		
		
		holder.tvUsername.setText(comment.getUser_info().getUser_name());
		holder.tvContent.setText(comment.getComment_content());
		holder.tvDate.setText(DateUtils.getStringFromTimeMillis(comment.getCreation_date() * 1000 + ""));
		
		
		String imageUrl = Constants.IMAGE_BASE_URL + comment.getUser_info().getUser_avatar();
		ImageView iv = holder.ivAvatar;
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
		
		if (null == cacheImagePictureContent) {
			try {
			Thread.sleep(500);  //保证相同地址的图片可以加载上
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
		if(null == cacheImagePictureContent) {
			iv.setImageResource(R.drawable.default_avatar);
		}else {
			iv.setImageDrawable(cacheImagePictureContent);
		}
		
		
		return convertView;
	}
	
	
	class Holder {
		ImageView ivAvatar;
		TextView tvUsername;
		TextView tvContent;
		TextView tvDate;
 	}
	
}
