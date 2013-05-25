package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.MyListView.OnRefreshListener;
import com.hesha.adapter.CollectionTypeAndPreviewItemsAdapter;
import com.hesha.bean.CollectionStruct;
import com.hesha.bean.CollectionType;
import com.hesha.bean.CollectionTypeAndPreviewItems;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TabCollectionActivity extends Activity implements OnClickListener {
	private Button btnCreateCollection;
	private LinearLayout ll0, ll1;
	private ProgressDialog dialog;
	
	private MyListView list;
	private CollectionTypeAndPreviewItemsAdapter adapter;
	private ArrayList<CollectionTypeAndPreviewItems> objs;
	private ArrayList<CollectionType> collectionTypes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_fragment);
		
		collectionTypes = new ArrayList<CollectionType>();
		dialog = new ProgressDialog(this);
		GetCollectionPageTask collectionPage = new GetCollectionPageTask(this, dialog);
		collectionPage.execute((Void)null);
		
		
		initComponent();
	}

	private void initComponent() {
		btnCreateCollection = (Button) findViewById(R.id.add_collection);
		btnCreateCollection.setOnClickListener(this);

		ll0 = (LinearLayout) findViewById(R.id.ll_0);
		ll1 = (LinearLayout) findViewById(R.id.ll_1);

		// add button dynamic
//		int itemSize = 10;
//		for (int i = 0; i < itemSize; i++) {
//			final Button btnTite = new Button(this);
//			btnTite.setText("some" + i);
//			btnTite.setTag(i);
//			if (i < itemSize / 2) {
//				ll0.addView(btnTite);
//			} else {
//				ll1.addView(btnTite);
//			}
//
//			btnTite.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					int i = Integer.valueOf(v.getTag().toString());
//					Toast.makeText(TabCollectionActivity.this, "some" + i,
//							Toast.LENGTH_SHORT).show();
//				}
//			});
//		}
		
		list = (MyListView)findViewById(R.id.list);
		list.setonRefreshListener(refreshListener);
		
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
					Toast.makeText(TabCollectionActivity.this, "some" + i,
							Toast.LENGTH_SHORT).show();
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
			dialog.setMessage("正在加载数据");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			getDataFromServer();
			return null;
		}
		
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
			
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
				list.setAdapter(adapter);
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
			}
		}
	}
}
