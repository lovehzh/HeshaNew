package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.ImageAndTextListAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.Collection;
import com.hesha.bean.CollectionDetailStruct;
import com.hesha.bean.CollectionInfoAndItems;
import com.hesha.constants.Constants;
import com.hesha.utils.DateUtils;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.TimeoutErrorDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollectionDetailsActivity extends Activity implements OnClickListener, OnItemClickListener{
	private static final String TAG = "CollectionDetailsActivity";
	private Button btnBack, btnBackToCat;
	private TextView tvTitle;
	private TextView tvUsername, tvItemNum, tvCreationDate;
	private Collection collection;
	private GridView gridView;
	private ImageAndTextListAdapter adapter;
	private ArrayList<BaseItem> baseItems;
	private int collectionId;
	private CollectionInfoAndItems collectionInfoAndItems;
	
	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
//	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_detail_activity);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		baseItems = new ArrayList<BaseItem>();
		//collectionId = intent.getIntExtra("collectiion_id", -1);
		loadData();
	}
	
	private void updateUI() {
		tvTitle.setText(collection.getCollection_name());
		tvUsername.setText(collection.getUser_info().getUser_name());
		tvItemNum.setText("" + collection.getImage_num());
		tvCreationDate.setText(DateUtils.getStringFromTimeMillis( collection.getCreation_date() * 1000 + ""));
		
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		tvTitle = (TextView)findViewById(R.id.tv_title);
//		tvTitle.setText(collection.getCollection_name());
		
		tvUsername = (TextView)findViewById(R.id.tv_username);
//		tvUsername.setText(collection.getUser_info().getUser_name());
		
		tvItemNum = (TextView)findViewById(R.id.tv_item_num);
//		tvItemNum.setText("" + collection.getImage_num());
		
		tvCreationDate = (TextView)findViewById(R.id.tv_creation_date);
//		tvCreationDate.setText(collection.getCreation_date() + "");// need update
		
		btnBackToCat = (Button)findViewById(R.id.btn_back_to_cat);
		btnBackToCat.setText("");// need update
		btnBackToCat.setOnClickListener(this);
		
		rlTips = (RelativeLayout)findViewById(R.id.rl_tips);
		tvTipText = (TextView)findViewById(R.id.pulldown_header_text);
		pdTips = (ProgressBar)findViewById(R.id.pulldown_header_loading);
		
		gridView = (GridView)findViewById(R.id.grid);
		adapter = new ImageAndTextListAdapter(this, baseItems, gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		
	}
	
	private void loadData(){
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				CollectionInfoAndItems collectionInfoAndItems = getCollectionFromServer();//取服务器端数据
//			    if(null != collectionInfoAndItems) Log.i(TAG, "on loadData photoInfos size : " + photoInfos.size());
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = collectionInfoAndItems;
				msg.sendToTarget();
				}
		}).start();
		
	}
	
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					CollectionInfoAndItems collectionInfoAndItems = (CollectionInfoAndItems)msg.obj;
					collection = collectionInfoAndItems.getCollection_info();
					
					//updata ui
					updateUI();
					
					ArrayList<BaseItem> baseItems = collectionInfoAndItems.getItems();
					adapter.clear();
					for(BaseItem item : baseItems) {
						adapter.add(item);
					}
					rlTips.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				}else {
					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE); 
				}
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(CollectionDetailsActivity.this);
				break;

			default:
				break;
			}
		}
	};
	
	private CollectionInfoAndItems getCollectionFromServer() {
		String response = "";
		String url = Constants.SERVER_URL + "?ac=getItemsByCollectionId&collection_id=16&begin_index=0&page_num=10&sort_type=1&order_type=1";
		CollectionInfoAndItems collectionInfoAndItems = new CollectionInfoAndItems();
		
		try {
			response = HttpUrlConnectionUtils.get(url, "");
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		if(Constants.D) Log.i(TAG, "response:" + response);
		
		if(response.equals(Constants.CONNECTION_TIMED_OUT)) {
			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
			msg.sendToTarget();
		}else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				CollectionDetailStruct struct = mapper.readValue(response, CollectionDetailStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					collectionInfoAndItems = struct.getData().get(0);
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
			return collectionInfoAndItems;
		}
		
		return null;
	}
}
