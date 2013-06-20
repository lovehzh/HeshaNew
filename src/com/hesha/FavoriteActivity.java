package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.BaseItem;
import com.hesha.bean.ColStruct;
import com.hesha.bean.Collection;
import com.hesha.bean.ColsStruct;
import com.hesha.bean.PhotoItem;
import com.hesha.bean.SubjectItem;
import com.hesha.bean.gen.AddItemToColPar;
import com.hesha.bean.gen.GetMyCollectionsParameter;
import com.hesha.constants.Constants;
import com.hesha.tasks.CreateCollectionTask;
import com.hesha.tasks.OnTaskFinishedListener;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;
import com.hesha.utils.MyDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteActivity extends Activity implements OnClickListener, OnItemClickListener, OnTaskFinishedListener{
	private Button btnCancel, btnCreate;
	private EditText etColName;
	private TextView tvColName;
	private ListView list;
	private MyApplication app;
	private ArrayList<Collection> myCollections;
	private SharedPreferences settings;
	private ArrayAdapter<Collection> adapter;
	private ArrayList<Collection> cols, colsTemp;
	private LinearLayout headView;
	
	private Collection collection;
	private BaseItem baseItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_activity);
		app = (MyApplication)getApplication();
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		collection = (Collection)intent.getSerializableExtra("collection");
		baseItem = (BaseItem)intent.getSerializableExtra("base_item");
		
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		String token = settings.getString(Constants.TOKEN, "");
		cols = new ArrayList<Collection>();
		colsTemp = new ArrayList<Collection>();
		
		GetMyCollectionsParameter parameter = new GetMyCollectionsParameter();
		parameter.setBegin_index(0);
		parameter.setOrder_type(1);
		parameter.setPage_num(20);
		parameter.setSort_type(1);
		parameter.setToken(token);
		parameter.setType(1);
		
		myCollections = app.getMyCollections();
		if(null == myCollections) {
			new GetMyCollectionsTask(this, new ProgressDialog(this), parameter).execute((Void)null);
		}else {
			for(Collection c : myCollections) {
				cols.add(c);
			}
		}
	}
	
	private void initComponent() {
		etColName = (EditText)findViewById(R.id.et_col_name);
		etColName.addTextChangedListener(watcher);
		
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		LayoutInflater inflater = getLayoutInflater();
		headView = (LinearLayout)inflater.inflate(R.layout.favorite_head_view, null);
		
		list = (ListView)findViewById(R.id.list);
		list.addHeaderView(headView);
		
		adapter = new ArrayAdapter<Collection>(this, android.R.layout.simple_expandable_list_item_1, colsTemp);
		list.setAdapter(adapter);
//		list.removeHeaderView(headView);
		list.setOnItemClickListener(this);
		
		
		
		tvColName = (TextView)headView.findViewById(R.id.tv_col_name);
		btnCreate = (Button)headView.findViewById(R.id.btn_create);
		btnCreate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
			
		case R.id.btn_create:
			String token = settings.getString(Constants.TOKEN, "");
			String collectionName = etColName.getText().toString().trim();
			String collectionDes = "";
			CreateCollectionTask task = new CreateCollectionTask(this, new ProgressDialog(this), token, collectionName, collectionDes);
			task.setListener(this);
			task.execute((Void)null);
			break;

		default:
			break;
		}
		
	}
	
	public class GetMyCollectionsTask extends AsyncTask<Void, Void, Void> implements Constants{
		private static final String TAG = "GetMyCollectionsTask";
		private Context context;
		private ProgressDialog dialog;
		String response;
		private GetMyCollectionsParameter parameter;
		public GetMyCollectionsTask(Context context, ProgressDialog dialog, GetMyCollectionsParameter parameter) {
			this.context = context;
			this.dialog = dialog;
			
			this.parameter = parameter;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("正在加载数据");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			
			String data = JsonUtils.genJson(parameter);
			
			if(Constants.D) Log.i(TAG, "data:" + data);
			
			String url = SERVER_URL + "?ac=getMyCollections";
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
				ColsStruct struct = mapper.readValue(response, ColsStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					ArrayList<Collection> collections = struct.getData();
					if(Constants.D) Log.i(TAG, "collectons size:" + collections.size());
					for(Collection c: collections) {
						cols.add(c);
					}
					colsTemp.addAll(cols);
					adapter.notifyDataSetChanged();
					app.setMyCollections(myCollections);
					
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
	
	TextWatcher watcher = new TextWatcher() {
		private CharSequence temp;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			temp = s;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length() == 0) {
				headView.setVisibility(View.GONE);
				adapter.clear();
				colsTemp = new ArrayList<Collection>();
				colsTemp.addAll(cols);
				for(Collection name : colsTemp) {
					adapter.add(name);
				}
				adapter.notifyDataSetChanged();
			}else {
				headView.setVisibility(View.VISIBLE);
				tvColName.setText("\""+ temp + "\"");
				adapter.clear();
				ArrayList<Collection> resuts = localSearch(temp.toString());
				for(Collection name : resuts) {
					adapter.add(name);
				}
				adapter.notifyDataSetChanged();
			}
			
		}
	};
	
	private ArrayList<Collection> localSearch(String keyWords) {
		ArrayList<Collection> resuts = new ArrayList<Collection>();
		for(Collection name : cols) {
			if(name.getCollection_name().contains(keyWords)) {
				resuts.add(name);
			}
		}
		return resuts;
	}
	
	public class AddItemToColTask extends AsyncTask<Void, Void, Void> implements Constants{
		private static final String TAG = "GetMyCollectionsTask";
		private Context context;
		private ProgressDialog dialog;
		String response;
		private AddItemToColPar parameter;
		public AddItemToColTask(Context context, ProgressDialog dialog, AddItemToColPar parameter) {
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
			
			String url = SERVER_URL + "?ac=addItemToCollection";
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
					Toast.makeText(FavoriteActivity.this, "收集成功", Toast.LENGTH_SHORT).show();
					finish();
					
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

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		AddItemToColPar par = new AddItemToColPar();
		String token = settings.getString(Constants.TOKEN, "");
		int collection_id = cols.get(position).getCollection_id();
		int item_id = baseItem.getItem_id();
		
		if(baseItem instanceof PhotoItem) {//bug ?
			baseItem.setItem_type(1);
		}else if(baseItem instanceof SubjectItem) {
			baseItem.setItem_type(2);
		}else {
			baseItem.setItem_type(3);
		}
		int item_type = baseItem.getItem_type();
		
		par.setCollection_id(collection_id);
		par.setItem_id(item_id);
		par.setItem_type(item_type);
		par.setToken(token);
		
		new AddItemToColTask(this, new ProgressDialog(this), par).execute((Void)null);
	}

	@Override
	public void updateActivityUI(Object obj) {
		// TODO Auto-generated method stub
		etColName.setText("");
		
		Collection collection = (Collection)obj;
		
		adapter.clear();
		colsTemp = new ArrayList<Collection>();
		colsTemp.addAll(cols);
		colsTemp.add(0, collection);
		for(Collection c : colsTemp) {
			adapter.add(c);
		}
		
		adapter.notifyDataSetChanged();
		
		
	}

	@Override
	public void jsonParseError() {
		// TODO Auto-generated method stub
		MyDialog.showInfoDialog(this, R.string.tips, R.string.response_data_error);
	}
	
	
}
