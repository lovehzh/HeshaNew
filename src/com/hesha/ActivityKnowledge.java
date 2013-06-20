package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.ArticleCategoryAdapter;
import com.hesha.bean.knowledge.ArticleCat;
import com.hesha.bean.knowledge.ArticleCatData;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.NetState;
import com.hesha.utils.TimeoutErrorDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ActivityKnowledge extends Activity implements OnClickListener, OnItemClickListener{
	private static final String TAG = "ActivityKnowledge";
	public static final int INTENT_CODE_FROM_ADD_KNOWLEAGE = 30;
	
	private ListView list;
	private ArticleCategoryAdapter adapter;
	private ArrayList<ArticleCat> cats, firstCats, secondCats;
	private SharedPreferences settings;
	
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	private RelativeLayout headView;
	private Button bt;
    private ProgressBar pg;
    
    private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge);
		
        
        initData();
        initComponent();
	}
	
	private void initData() {
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		
		cats = new ArrayList<ArticleCat>();
		firstCats = new ArrayList<ArticleCat>();
		secondCats = new ArrayList<ArticleCat>();
		
		if(NetState.isConnect(this)) {
			loadData();
		}
	}

	private void initComponent() {
		LayoutInflater inflater = getLayoutInflater();
		headView = (RelativeLayout)inflater.inflate(R.layout.pulldown_header, null);
		handler = new Handler();
		
		adapter = new ArticleCategoryAdapter(this, android.R.layout.simple_list_item_1, cats);
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		ArticleCat articleCat = firstCats.get(position);
		ArrayList<ArticleCat> results = new ArrayList<ArticleCat>();
		for(ArticleCat ac : secondCats) {
			if(ac.getP_id() == articleCat.getAc_id()) {
				results.add(ac);
			}
		}
		Intent intent = new Intent(this, ActivityKnowlegeSecond.class);
		intent.putExtra("cat", articleCat);
		intent.putExtra("cats", results);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			

		default:
			break;
		}
		
	}
	
	private void loadData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<ArticleCat> cats = getArticleCategoryFromServer();
				if(null != cats) Log.i(TAG, "on loadData friend info size : " + cats.size());
				if(cats.size() == 0) cats = null; 
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = cats;
				msg.sendToTarget();
			}
		}).start();
	}
	
	
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					ArrayList<ArticleCat> cats = (ArrayList<ArticleCat>)msg.obj;
					for(ArticleCat ac : cats) {
						if(ac.getP_id() == 0) {
							firstCats.add(ac);
						}else {
							secondCats.add(ac);
						}
					}
					adapter.clear();
					for(ArticleCat f : firstCats) {
						adapter.add(f);
					}
					list.removeHeaderView(headView);
					
					adapter.notifyDataSetChanged();
				}else {
					TextView tvTipText = (TextView)headView.findViewById(R.id.pulldown_header_text);
					ProgressBar pdTips = (ProgressBar)headView.findViewById(R.id.pulldown_header_loading);
					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE);
				}
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(ActivityKnowledge.this);
				break;

			default:
				break;
			}
		};
	};
	
	private ArrayList<ArticleCat> getArticleCategoryFromServer() {
		String response = "";
		ArrayList<ArticleCat> cats = new ArrayList<ArticleCat>();
		
		String url = Constants.SERVER_URL + "?ac=getArticleCategory";
		if(Constants.D) Log.i(TAG, "url:" + url);
		
		try {
			response = HttpUrlConnectionUtils.get(url, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Constants.D) Log.i(TAG, "response:" + response);
		
		if(response.equals(Constants.CONNECTION_TIMED_OUT)) {
			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
			msg.sendToTarget();
		}else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				
				ArticleCatData struct = mapper.readValue(response, ArticleCatData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					//
					cats = struct.getAcs();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKnowledge.this);
					builder.setTitle("获取数据失败");
					builder.setMessage("错误：" + struct.getError_des());
					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
					
					builder.create().show();
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		return cats;
	}
}
