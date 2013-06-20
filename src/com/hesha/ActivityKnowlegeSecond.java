package com.hesha;

import java.util.ArrayList;

import com.hesha.adapter.ArticleCategoryAdapter;
import com.hesha.bean.knowledge.ArticleCat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class ActivityKnowlegeSecond extends Activity implements OnClickListener, OnItemClickListener{
	private static final String TAG = "ActivityKnowlegeSecond";
	private Button btnBack;
	private TextView tvTitle;
	private ListView list;
	private ArticleCat articleCat;
	private ArrayList<ArticleCat> secondCats;
	private ArticleCategoryAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowlege_second);
		
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		articleCat = (ArticleCat)intent.getSerializableExtra("cat");
		secondCats = (ArrayList<ArticleCat>)intent.getSerializableExtra("cats");
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		if(null != articleCat) {
			tvTitle.setText(articleCat.getAc_name());
		}else {
			//tvTitle.setText(getString(R.string.my_article));
		}
		
		
		list = (ListView)findViewById(R.id.list);
		
		if(secondCats.size() == 0) {
//			RelativeLayout headView;
//			LayoutInflater inflater = getLayoutInflater();
//			headView = (RelativeLayout)inflater.inflate(R.layout.pulldown_header, null);
//			TextView tvTipText = (TextView)headView.findViewById(R.id.pulldown_header_text);
//			ProgressBar pdTips = (ProgressBar)headView.findViewById(R.id.pulldown_header_loading);
//			tvTipText.setText(getString(R.string.no_record));
//			pdTips.setVisibility(View.INVISIBLE);
			
			TextView tv = new TextView(this);
			tv.setPadding(20, 15, 0, 15);
			tv.setTextSize(17);
			tv.setTextColor(getResources().getColor(R.color.red));
			tv.setText(getString(R.string.no_record));
			list.addHeaderView(tv);		
		}
		
		adapter = new ArticleCategoryAdapter(this, android.R.layout.simple_list_item_1, secondCats);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		if(secondCats.size() != 0) {
			ArticleCat articleCat = secondCats.get(position);
			Intent intent = new Intent(this, ActivityKnowledgeList.class);
			intent.putExtra("cat", articleCat);
			startActivity(intent);
		}
	}
}
