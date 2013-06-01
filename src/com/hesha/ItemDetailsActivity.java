package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.CommentAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.Collection;
import com.hesha.bean.Comment;
import com.hesha.bean.ItemDetailData;
import com.hesha.bean.ItemDetailStruct;
import com.hesha.constants.Constants;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.DateUtils;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.ResponseErrorDialog;
import com.hesha.utils.TimeoutErrorDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemDetailsActivity extends Activity implements OnClickListener, OnItemClickListener{
	private static final String TAG = "CollectionDetailsActivity";
	private Button btnBack;
	private TextView tvTitle;
	private TextView tvUsername, tvItemNum, tvCreationDate, tvItemDes;
	
	private ImageView ivItem;
	private TextView tvNumOfCollected, tvNumOfLiked;
	private LinearLayout llUserCollected, llUserLiked;
	
	private ListView listView;
	private CommentAdapter adapter;
	private ArrayList<Comment> comments;
	
	private Collection collection;
	private BaseItem baseItem;
	private int commentNum = 20;
	
	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
	private static final int WHAT_DID_LOAD_DATA = 0;
//	private static final int WHAT_DID_REFRESH = 1;
//	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	private static final int RESPONSE_ERROR = 9;
	private static final int WHAT_DOWNLOAD_IMAGE = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail_activity);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		collection = (Collection)intent.getSerializableExtra("collection");
		baseItem = (BaseItem)intent.getSerializableExtra("base_item");
		
		comments = new ArrayList<Comment>();
	}
	
	private void updateUI() {
		tvTitle.setText(collection.getCollection_name());
		tvUsername.setText(collection.getUser_info().getUser_name());
		tvItemNum.setText("" + collection.getImage_num());
		tvCreationDate.setText(DateUtils.getStringFromTimeSeconds( collection.getCreation_date()));
		tvItemDes.setText(baseItem.getItem_des());
		
		tvNumOfCollected.setText("" + baseItem.getCollect_num());
		tvNumOfLiked.setText("" + baseItem.getLike_num());
		
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		tvTitle = (TextView)findViewById(R.id.tv_title);
		
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout llHeader = (LinearLayout)inflater.inflate(R.layout.item_head_view, null);
	
		
		tvUsername = (TextView)llHeader.findViewById(R.id.tv_username);
		tvItemNum = (TextView)llHeader.findViewById(R.id.tv_item_num);
		tvCreationDate = (TextView)llHeader.findViewById(R.id.tv_creation_date);
		tvItemDes = (TextView)llHeader.findViewById(R.id.tv_collection_des);
		
		ivItem = (ImageView)llHeader.findViewById(R.id.iv_item);
		loadPicture();
		
		tvNumOfCollected = (TextView)llHeader.findViewById(R.id.tv_num_of_collected);
		tvNumOfLiked = (TextView)llHeader.findViewById(R.id.tv_num_of_liked);
		
		llUserCollected = (LinearLayout)llHeader.findViewById(R.id.ll_user_collected);
		//dynamic add user avatar
		llUserLiked = (LinearLayout)llHeader.findViewById(R.id.ll_user_liked);
		//dynamic add user avatar
		
		listView = (ListView)findViewById(R.id.list);
		adapter = new CommentAdapter(this, android.R.layout.simple_list_item_1, comments, listView);
		listView.addHeaderView(llHeader);
		listView.setAdapter(adapter);
//		listView.setOnScrollListener(this);
//		listView.setOnItemClickListener(this);
		
		updateUI();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
			

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		
	}
	
	private void loadPicture() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			 	Drawable drawable = AsyncImageLoader.loadImageFromUrl(Constants.IMAGE_BASE_URL + baseItem.getItem_image().get(0).getImage());
			 	Message msg = mUIHandler.obtainMessage(WHAT_DOWNLOAD_IMAGE);
			 	msg.obj = drawable;
			 	msg.sendToTarget();
			}
		}).start();
	}
	
	private void loadData(){
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ItemDetailData data = getItemDetailDataFromServer();//取服务器端数据
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = data;
				msg.sendToTarget();
				}
		}).start();
		
	}
	
	private void loadMoreData() {
		
	}
	
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DOWNLOAD_IMAGE:
				if(null != msg.obj) {
					Drawable drawable = (Drawable)msg.obj;
					ivItem.setBackgroundDrawable(drawable);
				}
				break;
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					ItemDetailData data = (ItemDetailData)msg.obj;
					if(null != data) {
						if(data.getPhoto() != null) {
							baseItem = data.getPhoto();
						}else if(data.getLink() != null) {
							baseItem = data.getLink();
						}else {
							baseItem = data.getSubject();
						}
						
						//updata ui
						updateUI();
						
						ArrayList<Comment> normalComments = new ArrayList<Comment>();
						normalComments = data.getOther_comment();
						
//						for(int i=0; i<10; i++) {
//							Comment comment = new Comment();
//							normalComments.add(comment);
//						}
						
						
						ArrayList<Comment> expertComments = new ArrayList<Comment>();
						expertComments = data.getExpert_comment();
						
						adapter.clear();
						for(Comment c : normalComments) {
							adapter.add(c);
						}
//						rlTips.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
					}
					
				}else {
//					tvTipText.setText(getString(R.string.no_record));
//					pdTips.setVisibility(View.INVISIBLE); 
				}
				break;
			case RESPONSE_ERROR:
				String content = msg.getData().getString("des");
				ResponseErrorDialog.show(ItemDetailsActivity.this, content);
				break;
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(ItemDetailsActivity.this);
				break;

			default:
				break;
			}
		}
	};
	
	private ItemDetailData getItemDetailDataFromServer() {
		String response = "";
		String url = Constants.SERVER_URL + "?ac=getItemPageInfoByItemId&item_id=" + baseItem.getItem_id() 
				+ "&item_type=" + baseItem.getItem_type() + "&comment_num=" + commentNum +
				"&like_users_num=6&recollect_users_num=6" ;
		Log.i(TAG, url);
		ItemDetailData data = null;
		
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
				ItemDetailStruct struct = mapper.readValue(response, ItemDetailStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					data = struct.getData();
				}else {
					Message msg = mUIHandler.obtainMessage(RESPONSE_ERROR);
					Bundle bundle = new Bundle();
					bundle.putString("des", struct.getError_des());
					msg.setData(bundle);
					msg.sendToTarget();
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		}
		
		return null;
	}
}
