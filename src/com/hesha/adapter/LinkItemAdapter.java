package com.hesha.adapter;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.LinkItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LinkItemAdapter extends ArrayAdapter<LinkItem>{
	private ArrayList<LinkItem> objs;
	private Context context;
	public LinkItemAdapter(Context context,
			int textViewResourceId, ArrayList<LinkItem> objs) {
		super(context, textViewResourceId, objs);
		this.context = context;
		this.objs = objs;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("objs size = " + objs.size());
		final LinkItem linkItem = objs.get(position);
		Holder holder;
		if(null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.row_sails_info, null);
			holder = new Holder();
			
			holder.tvTypeName = (TextView)convertView.findViewById(R.id.tv_type_name);
			holder.tvPrice = (TextView)convertView.findViewById(R.id.tv_price);
			holder.tvSailerName = (TextView)convertView.findViewById(R.id.tv_sailer_name);
			holder.tvStockNum = (TextView)convertView.findViewById(R.id.tv_stock_num);
			
			convertView.setTag(holder);
		}else {
			holder = (Holder)convertView.getTag();
		}
		
		
		int typeId = linkItem.getFrom_type();
		holder.tvTypeName.setText(getTypeNameById(typeId));
		holder.tvPrice.setText("￥" + linkItem.getPrice());
		holder.tvSailerName.setText(linkItem.getShop_name());
		holder.tvStockNum.setText("库存:" + linkItem.getStock());
		
		return convertView;
	}
	
	
	class Holder {
		TextView tvTypeName;
		TextView tvPrice;
		TextView tvSailerName;
		TextView tvStockNum;
  	}
	
	private String getTypeNameById(int id) {
		String result = "未知";
		switch (id) {
		case 1:
			result = "淘宝客";
			break;

		default:
			break;
		}
		
		return result;
	}
	
}
