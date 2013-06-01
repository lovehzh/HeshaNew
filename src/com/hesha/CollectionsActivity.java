package com.hesha;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.MyListView.OnRefreshListener;
import com.hesha.adapter.ColAndPreviewItemsAdapter;
import com.hesha.adapter.CollectionTypeAndPreviewItemsAdapter;
import com.hesha.bean.Collection;
import com.hesha.bean.CollectionType;
import com.hesha.bean.ColsStruct;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.TimeoutErrorDialog;

public class CollectionsActivity extends Activity implements OnClickListener, OnItemClickListener {
	private static final String TAG = "CollectionsActivity";
	private Context context;
	private Button btnCreateCollection, btnGoMain;
	private LinearLayout ll0, ll1;
	private ProgressDialog dialog;
	
	private MyListView list;
	private ArrayList<CollectionType> collectionTypes;
	
	
	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
	
	private ColAndPreviewItemsAdapter adapter2;
	private ArrayList<Collection> collections;
	
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	
	private static final int PAGE_SIZE = 20;
	private int sortType =1, orderType =1;
	private int startIndex;
	
	private CollectionType currentColType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collections);
		context = this;
		
		collectionTypes = new ArrayList<CollectionType>();
		dialog = new ProgressDialog(this);
		
		collections = new ArrayList<Collection>();
		
		initData();
		initComponent();
		addCollectionTypesUI();
	}
	
	private void initData() {
		Intent intent = getIntent();
		currentColType = (CollectionType)intent.getSerializableExtra("col_type");
		collectionTypes = (ArrayList<CollectionType>)intent.getSerializableExtra("types");
		loadData(currentColType.getCollection_type_id());
	}

	private void initComponent() {
		
		btnCreateCollection = (Button) findViewById(R.id.add_collection);
		btnCreateCollection.setOnClickListener(this);
		
		btnGoMain = (Button)findViewById(R.id.btn_go_main);
		btnGoMain.setOnClickListener(this);

		ll0 = (LinearLayout) findViewById(R.id.ll_0);
		ll1 = (LinearLayout) findViewById(R.id.ll_1);

		
		rlTips = (RelativeLayout)findViewById(R.id.rl_tips);
		tvTipText = (TextView)findViewById(R.id.pulldown_header_text);
		pdTips = (ProgressBar)findViewById(R.id.pulldown_header_loading);
		
		list = (MyListView)findViewById(R.id.list);
		list.setonRefreshListener(refreshListener);
		
		
		adapter2 = new ColAndPreviewItemsAdapter(context, android.R.layout.simple_list_item_1, collections, list);
		list.setAdapter(adapter2);
		list.setOnItemClickListener(this);
	}
	
	private void addCollectionTypesUI() {
		// add button dynamic
		int itemSize = collectionTypes.size();
		for (int i = 0; i < itemSize; i++) {
			final Button btnTite = new Button(this);
			btnTite.setText(collectionTypes.get(i).getCollection_type_name());
			btnTite.setTag(i);
			if (i < itemSize / 2) {
				ll0.addView(btnTite);
			} else {
				ll1.addView(btnTite);
			}

			btnTite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int i = Integer.valueOf(v.getTag().toString());
					Toast.makeText(CollectionsActivity.this, "some" + i,
							Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(CollectionsActivity.this, CollectionDetailsActivity.class);
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_collection:
			boolean isLogin = false;
			if (isLogin) {

			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
			break;
			
		case R.id.btn_go_main:
			finish();
			break;

		default:
			break;
		}

	}
	
	private OnRefreshListener refreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			new AsyncTask<Void, Void, Void>() {
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
//					adapter.notifyDataSetChanged();
					list.onRefreshComplete();
				}

			}.execute((Void)null);
		}
	};
	

	private void loadData(final int typeId){
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Collection> collections = getColByTypeIdFromServer(typeId, startIndex, PAGE_SIZE, sortType, orderType);//取服务器端数据
//			    if(null != collectionInfoAndItems) Log.i(TAG, "on loadData photoInfos size : " + photoInfos.size());
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = collections;
				msg.sendToTarget();
				}
		}).start();
		
	}
	
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					ArrayList<Collection> collections = (ArrayList<Collection>)msg.obj;
					
					adapter2.clear();
					for(Collection c : collections) {
						adapter2.add(c);
					}
					
					rlTips.setVisibility(View.GONE);
					adapter2.notifyDataSetChanged();
				}else {
					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE); 
				}
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(context);
				break;

			default:
				break;
			}
		}
	};
	
	private ArrayList<Collection> getColByTypeIdFromServer(int typeId, int beginIndex, int pageSize, int sortType, int orderType) {
		
		String response = "";
		String url = Constants.SERVER_URL + "?ac=getCollectionsbyType&collect_type_id=" + typeId +"&begin_index=" + beginIndex+ "&page_num="+ pageSize +"&sort_type="+sortType+"&order_type="+orderType;
		ArrayList<Collection> collections = new ArrayList<Collection>();
		
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
				ColsStruct struct = mapper.readValue(response, ColsStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					collections = struct.getData();
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
			return collections;
		}
		return null;
	}

	

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		Collection collection = collections.get(position - 1);
		Intent intent = new Intent(this, CollectionDetailsActivity.class);
		intent.putExtra("collection", collection);
		intent.putExtra("col_type", currentColType);
		startActivity(intent);
	}
}
