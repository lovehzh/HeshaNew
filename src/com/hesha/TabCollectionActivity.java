package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.MyListView.OnRefreshListener;
import com.hesha.adapter.CollectionTypeAndPreviewItemsAdapter;
import com.hesha.adapter.CollectionTypeAndPreviewItemsAdapter.OnMoreClickListener;
import com.hesha.bean.Collection;
import com.hesha.bean.CollectionStruct;
import com.hesha.bean.CollectionType;
import com.hesha.bean.CollectionTypeAndPreviewItems;
import com.hesha.constants.Constants;
import com.hesha.tasks.OnTaskFinishedListener;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.MyDialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class TabCollectionActivity extends Activity implements OnClickListener, OnMoreClickListener, OnItemClickListener, OnTaskFinishedListener {
	private static final String TAG = "TabCollectionActivity";
	private Context context;
	private Button btnCreateCollection;
	private LinearLayout ll0, ll1;
	private ProgressDialog dialog;
	
	private MyListView list;
	private CollectionTypeAndPreviewItemsAdapter adapter;
	private ArrayList<CollectionTypeAndPreviewItems> objs;
	private ArrayList<CollectionType> collectionTypes;
	
	
	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
	
	
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	
	private static final int PAGE_SIZE = 20;
	private int sortType =1, orderType =1;
	private int startIndex;
	
	private CollectionType currentColType;
	private SharedPreferences settings;
	
	private Dialog createDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_fragment);
		context = this;
		
		
		dialog = new ProgressDialog(this);
		
		
		initComponent();
		initData();
	}
	
	private void initData() {
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		collectionTypes = new ArrayList<CollectionType>();
		
		GetCollectionPageTask collectionPage = new GetCollectionPageTask(this, dialog);
		collectionPage.execute((Void)null);
	}

	private void initComponent() {
		
		btnCreateCollection = (Button) findViewById(R.id.add_collection);
		btnCreateCollection.setOnClickListener(this);
		

		ll0 = (LinearLayout) findViewById(R.id.ll_0);
		ll1 = (LinearLayout) findViewById(R.id.ll_1);

		
		rlTips = (RelativeLayout)findViewById(R.id.rl_tips);
		tvTipText = (TextView)findViewById(R.id.pulldown_header_text);
		pdTips = (ProgressBar)findViewById(R.id.pulldown_header_loading);
		
		list = (MyListView)findViewById(R.id.list);
		list.setonRefreshListener(refreshListener);
		list.setOnItemClickListener(this);
		
	}
	
	private void addCollectionTypesUI() {
		// add button dynamic
		final int itemSize = collectionTypes.size();
		int temp = itemSize;
		if(itemSize % 2 != 0) ++temp;
		for (int i = 0; i < temp; i++) {
			final Button btnTite = new Button(this);
			ColorStateList csl=(ColorStateList)getResources().getColorStateList(R.color.my_button); 
			btnTite.setTextColor(csl);
			if(i < itemSize){
				btnTite.setText(collectionTypes.get(i).getCollection_type_name());
			}
			btnTite.setTag(i);
			
			
			if (i < temp / 2) {
				ll0.addView(btnTite);
				if(i == 0) {
					btnTite.setBackgroundResource(R.drawable.b1);
				}else if(i == (temp/2 - 1)) {
					btnTite.setBackgroundResource(R.drawable.b3);
				}else {
					btnTite.setBackgroundResource(R.drawable.b2);
				}
				
			} else {
				ll1.addView(btnTite);
				if(i == temp/2) {
					btnTite.setBackgroundResource(R.drawable.b4);
				}else if(i == (temp - 1)) {
					btnTite.setBackgroundResource(R.drawable.b6);
				}else {
					btnTite.setBackgroundResource(R.drawable.b5);
				}
			}

			btnTite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					int i = Integer.valueOf(v.getTag().toString());
					if(i < itemSize){
						gotoCollections(collectionTypes.get(i).getCollection_type_id());
					}
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.add_collection:
			//先判断是否有登录，然后再进行后续操作
			String username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, Constants.INTENT_CODE_COLLECTION);
			} else {
				String token = settings.getString(Constants.TOKEN, "");
				createDialog = MyDialog.showCreateColDialog(context, token, this);
			}
			break;
			

		default:
			break;
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "code : " + requestCode + " " + resultCode);
		switch (resultCode) {
		case Constants.INTENT_CODE_COLLECTION:
			
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
					adapter.notifyDataSetChanged();
					list.onRefreshComplete();
				}

			}.execute((Void)null);
		}
	};
	
	public class GetCollectionPageTask extends AsyncTask<Void, Void, Void> implements Constants{
		private static final int CONNECTION_TIME_OUT = 3;
		private static final String TAG = "GetCollectionPage";
		private ProgressDialog dialog;
		private Context context;
		private String response;
		
		public GetCollectionPageTask(Context context, ProgressDialog dialog) {
			this.context = context;
			this.dialog = dialog;
		}
		
		private String getDataFromServer() {
			
			String url = SERVER_URL + "?ac=getCollectionHomePage&number=3";
			if(D) Log.i(TAG, "url:" + url);
			try {
				response = HttpUrlConnectionUtils.get(url, "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(D) Log.i(TAG, "response:" + response);
			
			return "";
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			dialog.setMessage("正在加载数据");
//			dialog.show();
			rlTips.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			getDataFromServer();
			return null;
		}
		
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
//			if(dialog.isShowing()) {
//				dialog.dismiss();
//			}
			rlTips.setVisibility(View.GONE);
			
			ObjectMapper mapper = new ObjectMapper();
			CollectionStruct struct = null;
			try {
				struct = mapper.readValue(response, CollectionStruct.class);
				
				objs = struct.getData().getCollections_data();
				
				for(int i=0; i<objs.size(); i++) {
					CollectionType temp = objs.get(i).getCollection_type();
					collectionTypes.add(temp);
				}
				addCollectionTypesUI();
				
				adapter = new CollectionTypeAndPreviewItemsAdapter(TabCollectionActivity.this, android.R.layout.simple_list_item_1, objs, list);
				adapter.setListener(TabCollectionActivity.this);
				list.setAdapter(adapter);
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
				MyDialog.showInfoDialog(TabCollectionActivity.this, R.string.tips, R.string.response_data_error);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
				MyDialog.showInfoDialog(TabCollectionActivity.this, R.string.tips, R.string.response_data_error);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
				MyDialog.showInfoDialog(TabCollectionActivity.this, R.string.tips, R.string.response_data_error);
			}
		}
	}

	@Override
	public void onMoreClick(int typeId) {
		//Toast.makeText(context, "" + typeId, Toast.LENGTH_SHORT).show();
		gotoCollections(typeId);
	}
	
	private void gotoCollections(int typeId) {
		for(CollectionType c : collectionTypes) {
			if(c.getCollection_type_id() == typeId) {
				currentColType = c;
			}
		}
		
		Intent intent = new Intent(this, CollectionsActivity.class);
		intent.putExtra("col_type", currentColType);
		intent.putExtra("types", collectionTypes);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		CollectionTypeAndPreviewItems obj = objs.get(position -1);
		Collection collection = obj.getCollections();
		CollectionType currentColType = obj.getCollection_type();
		Intent intent = new Intent(this, CollectionDetailsActivity.class);
		intent.putExtra("collection", collection);
		intent.putExtra("col_type", currentColType);
		startActivity(intent);
	}

	@Override
	public void updateActivityUI(Object obj) {
		// TODO Auto-generated method stub
		createDialog.dismiss();
		Toast.makeText(this, "创建专辑成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void jsonParseError() {
		// TODO Auto-generated method stub
		MyDialog.showInfoDialog(this, R.string.tips, R.string.response_data_error);
	}
	

}
