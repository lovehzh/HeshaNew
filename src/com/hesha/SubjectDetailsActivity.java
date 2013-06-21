package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.CommentAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.ColStruct;
import com.hesha.bean.Collection;
import com.hesha.bean.CollectionType;
import com.hesha.bean.Comment;
import com.hesha.bean.ItemDetailData;
import com.hesha.bean.ItemDetailStruct;
import com.hesha.bean.User;
import com.hesha.bean.gen.AddCommentToItemPar;
import com.hesha.bean.gen.DoActionForItemPar;
import com.hesha.constants.Constants;
import com.hesha.tasks.DownloadImageTask;
import com.hesha.tasks.OnTaskFinishedListener;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.DateUtils;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;
import com.hesha.utils.MyDialog;
import com.hesha.utils.ResponseErrorDialog;
import com.hesha.utils.TimeoutErrorDialog;
import com.hesha.utils.Utils;

public class SubjectDetailsActivity extends Activity implements OnClickListener, OnItemClickListener, OnTaskFinishedListener{
	private static final String TAG = "CollectionDetailsActivity";
	private Button btnBack, btnBackToCat;
	private TextView tvTitle;
	private ImageView ivAvatar;
	private TextView tvUsername, tvItemNum, tvCreationDate, tvItemDes;
	
	private ImageView ivItem;
	private TextView tvNumOfCollected, tvNumOfLiked;
	private LinearLayout llUserCollected, llUserLiked;
	private LinearLayout llCollectedDes, llLikedDes;
	
	private ListView listView;
	private CommentAdapter adapter;
	private ArrayList<Comment> comments;
	
	private Collection collection;
	private CollectionType currentColType;
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
	
	private Context context;
	private RelativeLayout rlCollect, rlLike, rlDiscuss, rlShare;
	private ImageView iv;
	private SharedPreferences settings;
	
	private boolean isLike;
	private Dialog createCommentDialog;
	private ImageView imgLike;
	private boolean isCollect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail_activity);
		context = SubjectDetailsActivity.this;
		initData();
		initComponent();
		loadData();
	}
	
	private void initData() {
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		
		Intent intent = getIntent();
		collection = (Collection)intent.getSerializableExtra("collection");
		currentColType = (CollectionType)intent.getSerializableExtra("col_type");
		baseItem = (BaseItem)intent.getSerializableExtra("base_item");
		
		comments = new ArrayList<Comment>();
	}
	
	private void updateUI() {
		tvTitle.setText(collection.getCollection_name());
		tvUsername.setText(collection.getUser_info().getUser_name());
		tvCreationDate.setText(DateUtils.getStringFromTimeSeconds( collection.getCreation_date()));
		tvItemDes.setText(baseItem.getItem_des());
		
		tvNumOfCollected.setText("" + baseItem.getRecollect_num());
		tvNumOfLiked.setText("" + baseItem.getLike_num());
		
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		tvTitle = (TextView)findViewById(R.id.tv_title);
		
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout llHeader = (LinearLayout)inflater.inflate(R.layout.item_head_view, null);
	
		ivAvatar = (ImageView)llHeader.findViewById(R.id.iv_avatar);
		new DownloadImageTask(ivAvatar).execute(Constants.IMAGE_BASE_URL + collection.getUser_info().getUser_avatar());
		tvUsername = (TextView)llHeader.findViewById(R.id.tv_username);
		tvItemNum = (TextView)llHeader.findViewById(R.id.tv_item_num);
		tvItemNum.setVisibility(View.GONE);
		tvCreationDate = (TextView)llHeader.findViewById(R.id.tv_creation_date);
		tvItemDes = (TextView)llHeader.findViewById(R.id.tv_collection_des);
		btnBackToCat = (Button)llHeader.findViewById(R.id.btn_back_to_cat);
		btnBackToCat.setOnClickListener(this);
		btnBackToCat.setText(currentColType.getCollection_type_name());
		
		ivItem = (ImageView)llHeader.findViewById(R.id.iv_item);
//		Display mDisplay= getWindowManager().getDefaultDisplay();
//		int width= mDisplay.getWidth();
//		int Height= mDisplay.getHeight();
//		ivItem.setScaleType(ImageView.ScaleType.FIT_XY);
//		ivItem.setAdjustViewBounds(true);
//		ivItem.setMaxHeight(Height);//屏幕高度
//		ivItem.setMaxWidth(width);//屏幕宽度  
		loadPicture();
		
		tvNumOfCollected = (TextView)llHeader.findViewById(R.id.tv_num_of_collected);
		tvNumOfLiked = (TextView)llHeader.findViewById(R.id.tv_num_of_liked);
		
		llUserCollected = (LinearLayout)llHeader.findViewById(R.id.ll_user_collected);
		llUserLiked = (LinearLayout)llHeader.findViewById(R.id.ll_user_liked);
		llCollectedDes = (LinearLayout)llHeader.findViewById(R.id.ll_collect_des);
		llLikedDes = (LinearLayout)llHeader.findViewById(R.id.ll_like_des);
		
		
		listView = (ListView)findViewById(R.id.list);
		adapter = new CommentAdapter(this, android.R.layout.simple_list_item_1, comments, listView);
		listView.addHeaderView(llHeader);
		listView.setAdapter(adapter);
//		listView.setOnScrollListener(this);
//		listView.setOnItemClickListener(this);
		
		updateUI();
		
		rlCollect = (RelativeLayout)findViewById(R.id.rlCollect);
		rlCollect.setOnClickListener(this);
		//imgCollect = (ImageView)findViewById(R.id.imageview_details_collect);
		
		rlLike = (RelativeLayout)findViewById(R.id.rlLike);
		rlLike.setOnClickListener(this);
		imgLike = (ImageView)findViewById(R.id.imageview_details_like);
		
		rlDiscuss = (RelativeLayout)findViewById(R.id.rlDiscuss);
		rlDiscuss.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//loadData();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		String username;
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.rlCollect:
			//先判断是否有登录，然后再进行后续操作
			username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, Constants.INTENT_CODE_ITEM_DETAIL);
			} else {
				intent =new Intent(this, FavoriteActivity.class);
				intent.putExtra("collection", collection);
				intent.putExtra("base_item", baseItem);
				startActivity(intent);
			}
			break;
			
		case R.id.rlLike:
			username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, Constants.INTENT_CODE_ITEM_DETAIL_LIKE);
			} else {
				DoActionForItemPar parameter = new DoActionForItemPar();
				parameter.setAction_type(Constants.LIKE);
				if(isLike) {
					parameter.setAdd_flag(Constants.REMOVE);
				}else {
					parameter.setAdd_flag(Constants.ADD);
				}
				parameter.setItem_id(baseItem.getItem_id());
				parameter.setItem_type(Utils.getRealBaseItem(baseItem).getItem_type());
				parameter.setToken(settings.getString(Constants.TOKEN, ""));
				
				new DoActionForItemTask(this, new ProgressDialog(this), parameter).execute((Void)null);
			}
			break;
			
		case R.id.rlDiscuss:
			username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, Constants.INTENT_CODE_ITEM_DETAIL_LIKE);
			} else {
				//baseItem = Utils.getRealBaseItem(baseItem);//当元素为图片时直接弹出对话框
//				if(baseItem.getItem_type() == Constants.ITEM_PHOTO) {
//					AddCommentToItemPar parameter = new AddCommentToItemPar();
//					parameter.setCollection_id(collection.getCollection_id());
//					parameter.setItem_id(baseItem.getItem_id());
//					parameter.setItem_type(Utils.getRealBaseItem(baseItem).getItem_type());
//					parameter.setToken(settings.getString(Constants.TOKEN, ""));
//					
//					createCommentDialog = MyDialog.showCommentDialog(this, parameter, this);
//				}else {
					intent = new Intent(this, CommentsActivity.class);
					intent.putExtra("base_item", baseItem);
					intent.putExtra("collection", collection);
					intent.putExtra("request_type", Constants.FROM_ITEM_ID);
					startActivity(intent);
//				}
				
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
		switch (requestCode) {
		case Constants.INTENT_CODE_ITEM_DETAIL:
			Intent intent =new Intent(this, FavoriteActivity.class);
			intent.putExtra("collection", collection);
			intent.putExtra("base_item", baseItem);
			startActivity(intent);
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
	
	private void setUserIcon(ArrayList<User> users, LinearLayout ll) {
		for(int i=0; i< users.size(); i++) {
			ImageView iv = new ImageView(SubjectDetailsActivity.this);
			iv.setPadding(2, 2, 2, 2);
//			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			LayoutParams params = new LayoutParams(75, 75);
			iv.setLayoutParams(params);//(new LayoutParams(R.dimen.avatar_size, R.dimen.avatar_size));
			new DownloadImageTask(iv).execute(Constants.IMAGE_BASE_URL + users.get(i).getUser_avatar());
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "s", Toast.LENGTH_SHORT).show();
				}
			});
			
			ll.addView(iv);
		}
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
//					ivItem.setBackgroundDrawable(drawable);
					ivItem.setImageDrawable(drawable);
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
						
						ArrayList<User> collectedUser = data.getRecollect_users();
						ArrayList<User> likedUser = data.getLike_users();
						
						//用户为0时不显示对应UI
						if(collectedUser== null || collectedUser.size() == 0) {
							//not show ui
							llCollectedDes.setVisibility(View.GONE);
						}else {
							llCollectedDes.setVisibility(View.VISIBLE);
							setUserIcon(collectedUser, llUserCollected);
						}
						
						if(likedUser == null || likedUser.size() == 0) {
							//not show ui
							llLikedDes.setVisibility(View.GONE);
						}else {
							llLikedDes.setVisibility(View.VISIBLE);
							setUserIcon(likedUser, llUserLiked);
						}
						
						
						//普通评论和专家评论
						ArrayList<Comment> normalComments =  data.getOther_comment();
						
						ArrayList<Comment> expertComments =  data.getExpert_comment();
						if(null != normalComments) {
							adapter.clear();
							for(Comment c : normalComments) {
								adapter.add(c);
							}
//							rlTips.setVisibility(View.GONE);
							adapter.notifyDataSetChanged();
						}
					}
					
				}else {
//					tvTipText.setText(getString(R.string.no_record));
//					pdTips.setVisibility(View.INVISIBLE); 
				}
				break;
			case RESPONSE_ERROR:
				String content = msg.getData().getString("des");
				ResponseErrorDialog.show(SubjectDetailsActivity.this, content);
				break;
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(SubjectDetailsActivity.this);
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
	
	public class DoActionForItemTask extends AsyncTask<Void, Void, Void> implements Constants{
		private static final String TAG = "DoActionForItemTask";
		private Context context;
		private ProgressDialog dialog;
		String response;
		private DoActionForItemPar parameter;
		public DoActionForItemTask(Context context, ProgressDialog dialog, DoActionForItemPar parameter) {
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
			
			String url = SERVER_URL + "?ac=doActionForItem";
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
					if(isLike) {
						Toast.makeText(SubjectDetailsActivity.this, "已取消喜欢", Toast.LENGTH_SHORT).show();
						imgLike.setImageResource(R.drawable.button_details_like_default);
						isLike = false;
					}else {
						Toast.makeText(SubjectDetailsActivity.this, "已喜欢", Toast.LENGTH_SHORT).show();
						imgLike.setImageResource(R.drawable.button_details_like_selected);
						isLike = true;
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

	@Override
	public void updateActivityUI(Object obj) {
		// TODO Auto-generated method stub
		createCommentDialog.dismiss();
	}

	@Override
	public void jsonParseError() {
		// TODO Auto-generated method stub
		
	}
}
