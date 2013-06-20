package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.ArticleListAdapter;
import com.hesha.bean.Comment;
import com.hesha.bean.knowledge.Article;
import com.hesha.bean.knowledge.ArticleCat;
import com.hesha.bean.knowledge.ArticleCatData;
import com.hesha.bean.knowledge.ArticleData;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.NetState;
import com.hesha.utils.TimeoutErrorDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ActivityKnowledgeList extends Activity implements OnClickListener, OnItemClickListener, Constants, OnScrollListener{
	private static final String TAG = "ActivityKnowlegeList";
	private Button btnBack;
	private TextView tvTitle;
	private ListView list;
	private ArticleCat articleCat;
	private ArrayList<Article> articles;
	private ArticleListAdapter adapter;
	
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	private RelativeLayout headView;
	private Button bt;
    private ProgressBar pg;
    
 // ListView底部View
    private View moreView;
    // 设置一个最大的数据条数，超过即不再加载
//    private int MaxDateNum;
    // 最后可见条目的索引
    private int lastVisibleIndex;
    
    private Handler handler;
    private int index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowlege_list);
		
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		articleCat = (ArticleCat)intent.getSerializableExtra("cat");
		articles = new ArrayList<Article>();
		
		if(NetState.isConnect(this)) {
			loadData();
		}
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
		
		LayoutInflater inflater = getLayoutInflater();
		headView = (RelativeLayout)inflater.inflate(R.layout.pulldown_header, null);
		
		// 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);
        bt = (Button) moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();
		list = (ListView)findViewById(R.id.list);
		list.addHeaderView(headView);
		
		
		adapter = new ArticleListAdapter(this, android.R.layout.simple_list_item_1, articles);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		list.setOnScrollListener(this);
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
		if(articles.size() != 0) {
			Article article = articles.get(position);
			Intent intent = new Intent(this, ActivityKnowledgeDetails.class);
			intent.putExtra("article", article);
			startActivity(intent);
		}
	}
	
	private void loadData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Article> objs = getArticlesByCategoryIdFromServer(index, PAGE_SIZE, 1, DES, articleCat.getAc_id());
				if(null != objs) Log.i(TAG, "on loadData objs size : " + objs.size());
				if(objs.size() == 0) objs = null; 
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = objs;
				msg.sendToTarget();
			}
		}).start();
	}
	
	private void loadMoreData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				index = index + PAGE_SIZE;
				ArrayList<Article> objs = getArticlesByCategoryIdFromServer(index, PAGE_SIZE, 1, DES, articleCat.getAc_id());
				if(null != objs) Log.i(TAG, "on loadData objs size : " + objs.size());
				if(objs.size() == 0) objs = null; 
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = objs;
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
					ArrayList<Article> objs = (ArrayList<Article>)msg.obj;
					adapter.clear();
					for(Article f : objs) {
						adapter.add(f);
					}
					list.removeHeaderView(headView);
					
					if(objs.size() == PAGE_SIZE) {
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
				
			case WHAT_DID_MORE:
				if(null != msg.obj) {
					ArrayList<Article> comments = (ArrayList<Article>)msg.obj;
					for(Article f : comments) {
						adapter.add(f);
					}
					
					if(comments.size() < Constants.PAGE_SIZE) {
						list.removeFooterView(moreView);
					}
				}
				
				
				adapter.notifyDataSetChanged();
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(ActivityKnowledgeList.this);
				break;

			default:
				break;
			}
		};
	};
	
	private ArrayList<Article> getArticlesByCategoryIdFromServer(int beginIndex, int pageNum, int sortType, int order, int acId) {
		String response = "";
		ArrayList<Article> articles = new ArrayList<Article>();
		
		String url = Constants.SERVER_URL + "?ac=getArticlesByCategoryId&begin_index=" + beginIndex
				+ "&page_num=" + pageNum + "&sort_type=" + sortType + "&order_type=" + order + "&ac_id=" + acId;
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
				
				ArticleData struct = mapper.readValue(response, ArticleData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					//
					articles = struct.getArticles();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(ActivityKnowledgeList.this);
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
		
		return articles;
	}
	
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
