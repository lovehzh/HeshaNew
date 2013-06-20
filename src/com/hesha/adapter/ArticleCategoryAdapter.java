package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.knowledge.ArticleCat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ArticleCategoryAdapter extends ArrayAdapter<ArticleCat> {
	private ArrayList<ArticleCat> cats;
	private Context context;
	
	public ArticleCategoryAdapter(Context context, int textViewResourceId, ArrayList<ArticleCat> cats) {
		super(context, textViewResourceId, cats);
		this.cats = cats;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ArticleCat cat = cats.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.row_article_category, null);
			
			holder = new Holder();
			holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		holder.tvName.setText(cat.getAc_name());
		
		return convertView;
	}
	
	class Holder {
		TextView tvName;
	}
}
