package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.CollectionTypeAndPreviewItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionTypeAndPreviewItemsAdapter extends ArrayAdapter<CollectionTypeAndPreviewItems>{
	private ArrayList<CollectionTypeAndPreviewItems> objs;
	private Context context;
	private ListView listView;
	public CollectionTypeAndPreviewItemsAdapter(Context context,
			int textViewResourceId, ArrayList<CollectionTypeAndPreviewItems> objs,
			ListView listView) {
		super(context, textViewResourceId, objs);
		this.context = context;
		this.objs = objs;
		this.listView = listView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("objs size = " + objs.size());
		final CollectionTypeAndPreviewItems obj = objs.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.colltion_item, null);
			holder = new Holder();
			
			holder.tvCategoryName = (TextView)convertView.findViewById(R.id.tv_category_name);
			holder.tvMore = (TextView)convertView.findViewById(R.id.tv_more);
			
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
		
		
		return convertView;
	}
	
	
	class Holder {
		TextView tvCategoryName;
		TextView tvMore;
		
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
