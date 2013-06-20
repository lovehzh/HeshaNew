package com.hesha;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hesha.utils.MoveBg;

public class TabChoiceActivity extends ActivityGroup implements OnClickListener{
	RelativeLayout layout;
	RelativeLayout layout_news_main;
	LayoutInflater inflater;
	private TextView tv_front;//需要移动的View
	int avg_width = 0;// 用于记录平均每个标签的宽度，移动的时候需要
	int startX;//移动的起始位置
	LayoutParams params;
	TextView tv_bar_news;
	TextView tv_bar_sport;
	
	Intent intent;
	View page;//用来存放顶部具体分类的view
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_activity);
		
		initComponent();
	}
	
	private void initComponent() {
		layout = (RelativeLayout) findViewById(R.id.layout_title_bar);
		layout_news_main = (RelativeLayout) findViewById(R.id.layout_news_main);
		inflater = getLayoutInflater();
		
		tv_bar_news = (TextView) findViewById(R.id.tv_title_bar_news);
		tv_bar_news.setOnClickListener(this);
		tv_bar_sport = (TextView) findViewById(R.id.tv_title_bar_sport);
		tv_bar_sport.setOnClickListener(this);
		
		tv_front = new TextView(this);
		tv_front.setBackgroundResource(R.drawable.choice_over);
		tv_front.setTextColor(Color.WHITE);
		tv_front.setText("选酒");
		tv_front.setPadding(40, 0, 40, 0);
		tv_front.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layout.addView(tv_front, param);
		
		
		//默认显示选酒
		intent = new Intent(this, WineCatActivity.class);
		page = getLocalActivityManager().startActivity("activity1", intent).getDecorView();
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout_news_main.addView(page, params);
		
	}

	@Override
	public void onClick(View v) {
		
		avg_width = findViewById(R.id.layout).getWidth();
		switch (v.getId()) {
		case R.id.tv_title_bar_news:
			MoveBg.moveFrontBg(tv_front, startX, 0, 0, 0);
			startX = 0;
			tv_front.setText(R.string.choice);
			//
			page  =  inflater.inflate(R.layout.wine_cat_activity, null);
			intent.setClass(TabChoiceActivity.this,  WineCatActivity.class);
			page = getLocalActivityManager().startActivity("activity1", intent).getDecorView();
			break;
			
		case R.id.tv_title_bar_sport:
			MoveBg.moveFrontBg(tv_front, startX, avg_width, 0, 0);
			startX = avg_width;
			tv_front.setText(R.string.knowledge);
			
			//
			intent.setClass(TabChoiceActivity.this, ActivityKnowledge.class);
			page = getLocalActivityManager().startActivity("activity2", intent).getDecorView();
			break;

		default:
			break;
		}
		//切换
		layout_news_main.removeAllViews();
		layout_news_main.addView(page, params);
	}
}
