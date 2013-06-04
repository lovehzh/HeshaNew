package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.CommentAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.ColStruct;
import com.hesha.bean.Collection;
import com.hesha.bean.Comment;
import com.hesha.bean.CommentStruct;
import com.hesha.bean.gen.GetCommentsByCollectionIdPar;
import com.hesha.bean.gen.GetCommentsByItemIdPar;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;
import com.hesha.utils.TimeoutErrorDialog;
import com.hesha.utils.Utils;

public class CommentsActivity extends Activity implements OnClickListener, OnItemClickListener, OnScrollListener, Constants{
	private static final String TAG = "CommentsActivity";
	private Button btnBack;
	private ListView list;
	private CommentAdapter adapter;
	private ArrayList<Comment> comments;
	private SharedPreferences settings;
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	private RelativeLayout headView;
	private Button bt;
    private ProgressBar pg;
    // ListView底部View
    private View moreView;
    private Handler handler;
    // 设置一个最大的数据条数，超过即不再加载
//    private int MaxDateNum;
    // 最后可见条目的索引
    private int lastVisibleIndex;
    
    //请求的类型 是根据collection_id还是item_id
    private int requestType; 
    private Collection collection;
    private BaseItem baseItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments_activity);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		
		Intent intent = getIntent();
		requestType = intent.getIntExtra("request_type", FROM_COLLECTION_ID);
		if(requestType == FROM_COLLECTION_ID) {
			collection = (Collection)intent.getSerializableExtra("collection");
		}else {
			baseItem = (BaseItem)intent.getSerializableExtra("base_item");
			baseItem = Utils.getRealBaseItem(baseItem);
		}
		
		comments = new ArrayList<Comment>();
		
		loadData();
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		LayoutInflater inflater = getLayoutInflater();
		headView = (RelativeLayout)inflater.inflate(R.layout.pulldown_header, null);
		
		// 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);

        bt = (Button) moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();
		
		list = (ListView)findViewById(R.id.list);
		list.addHeaderView(headView);
		adapter = new CommentAdapter(this, android.R.layout.simple_list_item_1, comments, list);
		
		list.setAdapter(adapter);
		list.setOnScrollListener(this);
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
		if(comments.size() != 0) {
		}
	}
	
	private ArrayList<Comment> getCommentsFromServer(int beginIndex, int pageNum, int sortType, int id, int collectionId, int expertFlag) {
		String response = "";
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		GetCommentsByCollectionIdPar parameter = null;
		GetCommentsByItemIdPar parameter2 =null;
		
		
		String data = "";
		String url = "";
		if(requestType == FROM_COLLECTION_ID) {
			parameter = new GetCommentsByCollectionIdPar();
			parameter.setBegin_index(beginIndex);
			parameter.setCollection_id(collection.getCollection_id());
			parameter.setExpert_flag(expertFlag);
			parameter.setOrder_type(id);
			parameter.setPage_num(pageNum);
			parameter.setSort_type(sortType);
			url = SERVER_URL + "?ac=getCommentsFromCollectionId";
			data = JsonUtils.genJson(parameter);
		}else {
			parameter2 = new GetCommentsByItemIdPar();
			parameter2.setBegin_index(beginIndex);
			parameter2.setItem_id(baseItem.getItem_id());
			parameter2.setItem_type(baseItem.getItem_type());
			parameter2.setExpert_flag(expertFlag);
			parameter2.setOrder_type(id);
			parameter2.setPage_num(pageNum);
			parameter2.setSort_type(sortType);
			url = SERVER_URL + "?ac=getCommentsFromItemId";
			data = JsonUtils.genJson(parameter2);
		}
		
		if(Constants.D) Log.i(TAG, data); 
		
		if(Constants.D) Log.i(TAG, "url:" + url);
		
		try {
			response = HttpUrlConnectionUtils.post(url, data, Constants.CONTENT_TYPE_JSON);
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
				
				CommentStruct struct = mapper.readValue(response, CommentStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					//
					ArrayList<Comment> commentsExp = struct.getData().getExpert_comment();
					ArrayList<Comment> commentsNor = struct.getData().getOther_comment();
					
					if(null != commentsExp) 
					comments.addAll(commentsExp);
					
					if(null != commentsNor)
					comments.addAll(commentsNor);
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
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
		
		return comments;
	}
	
	private void loadData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Comment> comments = null;
				if(requestType == FROM_COLLECTION_ID){
					comments = getCommentsFromServer(0, PAGE_SIZE, 1, 1, collection.getCollection_id(), 3);//取服务器端数据
				}else {
					comments = getCommentsFromServer(0, PAGE_SIZE, 1, 1, baseItem.getItem_id(), 3);
				}
				if(null != comments) Log.i(TAG, "on loadData friend info size : " + comments.size());
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = comments;
				msg.sendToTarget();
			}
		}).start();
	}
	
	private void loadMoreData() {
		Log.i(TAG, "on loadMoreData");
		new Thread(new Runnable() {
			@Override
			public void run() {
//				int lastFid = comments.get(comments.size() -1).getFid();
//				ArrayList<Comment> friendInfos = getFriendsFromServer(lastFid);//取服务器端数据
//				if(null != friendInfos) Log.i(TAG, "on loadData friend info size : " + friendInfos.size());
//				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
//				msg.obj = friendInfos;
//				msg.sendToTarget();
			}
		}).start();
	}
	
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					ArrayList<Comment> fs = (ArrayList<Comment>)msg.obj;
					adapter.clear();
					for(Comment f : fs) {
						adapter.add(f);
					}
					list.removeHeaderView(headView);
					
					if(fs.size() == PAGE_SIZE) {
						list.addFooterView(moreView);
					}
					adapter.notifyDataSetChanged();
				}else {
					TextView tvTipText = (TextView)headView.findViewById(R.id.pulldown_header_text);
					ProgressBar pdTips = (ProgressBar)headView.findViewById(R.id.pulldown_header_loading);
					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE);
				}
				break;
//			case WHAT_DID_REFRESH:
//				
//				// 告诉它更新完毕
//				mPullDownView.notifyDidRefresh();
//				break;
//				
			case WHAT_DID_MORE:
				if(null != msg.obj) {
					ArrayList<Comment> fs = (ArrayList<Comment>)msg.obj;
					for(Comment f : fs) {
						adapter.add(f);
					}
					
					if(fs.size() < Constants.PAGE_SIZE) {
						list.removeFooterView(moreView);
					}
				}
				
				
				adapter.notifyDataSetChanged();
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(CommentsActivity.this);
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

        // 所有的条目已经和最大条数相等，则移除底部的View
//        if (totalItemCount == MaxDateNum + 1) {
//            listView.removeFooterView(moreView);
//            Toast.makeText(this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_LONG).show();
//        }
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == adapter.getCount() && lastVisibleIndex != 0) {
            // 当滑到底部时自动加载
             pg.setVisibility(View.VISIBLE);
             bt.setVisibility(View.GONE);
             loadMoreData();

        }
	}
}
