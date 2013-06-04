package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.ImageAndTextListAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.ColStruct;
import com.hesha.bean.Collection;
import com.hesha.bean.CollectionDetailStruct;
import com.hesha.bean.CollectionInfoAndItems;
import com.hesha.bean.CollectionType;
import com.hesha.bean.LinkItem;
import com.hesha.bean.PhotoItem;
import com.hesha.bean.SubjectItem;
import com.hesha.bean.gen.DoActionForCollectionPar;
import com.hesha.bean.gen.DoActionForItemPar;
import com.hesha.constants.Constants;
import com.hesha.tasks.DownloadImageTask;
import com.hesha.utils.DateUtils;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;
import com.hesha.utils.TimeoutErrorDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionDetailsActivity extends Activity implements OnClickListener, OnItemClickListener, Constants{
	private static final String TAG = "CollectionDetailsActivity";
	private Button btnBack, btnBackToCat;
	private ImageView ivAvatar;
	private TextView tvTitle;
	private TextView tvUsername, tvItemNum, tvCreationDate, tvColDes;
	private Collection collection;
	private GridView gridView;
	private ImageAndTextListAdapter adapter;
	private ArrayList<BaseItem> baseItems;
	private int collectionId;
	private CollectionInfoAndItems collectionInfoAndItems;
	private CollectionType currentColType;
	
	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
//	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	
	private SharedPreferences settings;
	private boolean isCollected;
	private RelativeLayout rlCollect, rlDiscuss, rlShare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_detail_activity);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		Intent intent = getIntent();
		collection = (Collection)intent.getSerializableExtra("collection");
		currentColType = (CollectionType)intent.getSerializableExtra("col_type");
		
		baseItems = new ArrayList<BaseItem>();
		collectionId = collection.getCollection_id();
		loadData();
	}
	
	private void updateUI() {
		tvTitle.setText(collection.getCollection_name());
		tvUsername.setText(collection.getUser_info().getUser_name());
		tvItemNum.setText("" + collection.getItem_nums());
		tvCreationDate.setText(DateUtils.getStringFromTimeSeconds( collection.getCreation_date()));
		tvColDes.setText(collection.getCollection_des());
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		
		tvTitle = (TextView)findViewById(R.id.tv_title);
		ivAvatar = (ImageView)findViewById(R.id.iv_avatar);
		new DownloadImageTask(ivAvatar).execute(Constants.IMAGE_BASE_URL + collection.getUser_info().getUser_avatar());
		tvUsername = (TextView)findViewById(R.id.tv_username);
		tvItemNum = (TextView)findViewById(R.id.tv_item_num);
		tvCreationDate = (TextView)findViewById(R.id.tv_creation_date);
		tvColDes = (TextView)findViewById(R.id.tv_collection_des);
		updateUI();
		
		btnBackToCat = (Button)findViewById(R.id.btn_back_to_cat);
		btnBackToCat.setText(currentColType.getCollection_type_name());
		btnBackToCat.setOnClickListener(this);
		
		rlTips = (RelativeLayout)findViewById(R.id.rl_tips);
		tvTipText = (TextView)findViewById(R.id.pulldown_header_text);
		pdTips = (ProgressBar)findViewById(R.id.pulldown_header_loading);
		
		gridView = (GridView)findViewById(R.id.grid);
		adapter = new ImageAndTextListAdapter(this, baseItems, gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
		rlCollect = (RelativeLayout)findViewById(R.id.rlCollect);
		rlCollect.setOnClickListener(this);
		
		rlDiscuss = (RelativeLayout)findViewById(R.id.rlDiscuss);
		rlDiscuss.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		String username;
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.btn_back_to_cat:
			finish();
			break;
			
		case R.id.rlCollect:
			//先判断是否有登录，然后再进行后续操作
			username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, INTENT_CODE_COLLECTION_DETAIL);
			} else {
				DoActionForCollectionPar parameter = new DoActionForCollectionPar();
				parameter.setAction_type(FAVORITE);
				if(isCollected) {
					parameter.setAdd_flag(REMOVE);
				}else {
					parameter.setAdd_flag(ADD);
				}
				parameter.setCollection_id(collectionId);
				parameter.setToken(settings.getString(TOKEN, ""));
				
				new DoActionForCollectionTask(this, new ProgressDialog(this), parameter).execute((Void)null);
			}
			break;
			
		case R.id.rlDiscuss:
			username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, INTENT_CODE_COLLECTION_DETAIL);
			} else {
				intent = new Intent(this, CommentsActivity.class);
				intent.putExtra("collection", collection);
				startActivity(intent);
			}
			break;
			
		case R.id.rlShare:
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		Intent intent = new Intent(this, ItemDetailsActivity.class);
		intent.putExtra("collection", collection);
		intent.putExtra("col_type", currentColType);
		
		BaseItem baseItem = baseItems.get(position);
		if(baseItem instanceof PhotoItem) {//bug ?
			baseItem.setItem_type(1);
		}else if(baseItem instanceof SubjectItem) {
			baseItem.setItem_type(2);
		}else {
			baseItem.setItem_type(3);
		}
		intent.putExtra("base_item", baseItem);
		startActivity(intent);
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
					
					baseItems = collectionInfoAndItems.getItems();
					adapter.clear();
					for(BaseItem item : baseItems) {
						if(item instanceof LinkItem){
							Log.i(TAG, item.getItem_name() + "is link item" +  " item_type=" + item.getItem_type());
						}else if(item instanceof PhotoItem) {
							Log.i(TAG, item.getItem_name() + "is is photo" +  " item_type=" + item.getItem_type());
						}else {
							Log.i(TAG, item.getItem_name() + "is subject" +  " item_type=" + item.getItem_type());
						}
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
		String url = Constants.SERVER_URL + "?ac=getItemsByCollectionId&collection_id=" + collectionId +"&begin_index=0&page_num=30&sort_type=1&order_type=1";
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
	
	public class DoActionForCollectionTask extends AsyncTask<Void, Void, Void> {
		private static final String TAG = "DoActionForItemTask";
		private Context context;
		private ProgressDialog dialog;
		String response;
		private DoActionForCollectionPar parameter;
		public DoActionForCollectionTask(Context context, ProgressDialog dialog, DoActionForCollectionPar parameter) {
			this.context = context;
			this.dialog = dialog;
			
			this.parameter = parameter;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("正在提交数据");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			
			String data = JsonUtils.genJson(parameter);
			
			if(Constants.D) Log.i(TAG, "data:" + data);
			
			String url = SERVER_URL + "?ac=doActionForCollection";
			if(Constants.D) Log.i(TAG, "url:" + url);
			
			try {
				response = HttpUrlConnectionUtils.post(url, data, Constants.CONTENT_TYPE_JSON);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(Constants.D) Log.i(TAG, "response:" + response);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
			ObjectMapper mapper = new ObjectMapper();
			try {
				ColStruct struct = mapper.readValue(response, ColStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					Collection collection = struct.getData();
					if(Constants.D) Log.i(TAG, "collectons size:" + collection);
					if(isCollected) {
						Toast.makeText(CollectionDetailsActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(CollectionDetailsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
						
					}
					
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
