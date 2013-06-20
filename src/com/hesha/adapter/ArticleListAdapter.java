package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.knowledge.Article;
import com.hesha.utils.DateUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ArticleListAdapter extends ArrayAdapter<Article> {
	private ArrayList<Article> articles;
	private Context context;
	
	public ArticleListAdapter(Context context, int textViewResourceId, ArrayList<Article> articles) {
		super(context, textViewResourceId, articles);
		this.articles = articles;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Article article = articles.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.row_article_list, null);
			
			holder = new Holder();
			holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
			holder.tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
			holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		holder.tvName.setText(article.getTitle());
		holder.tvUsername.setText(article.getUser_info().getUser_name());
		holder.tvDate.setText(DateUtils.getStringFromTimeMillis(article.getCreation_date() * 1000 + ""));
		
		return convertView;
	}
	
	class Holder {
		TextView tvName;
		TextView tvUsername;
		TextView tvDate;
	}
}
