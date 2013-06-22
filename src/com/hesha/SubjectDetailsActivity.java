package com.hesha;

import java.io.File;
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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
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
import com.hesha.bean.gen.DoActionForItemPar;
import com.hesha.constants.Constants;
import com.hesha.tasks.DownloadImageTask;
import com.hesha.tasks.OnTaskFinishedListener;
import com.hesha.utils.AsyncImageLoader;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;
import com.hesha.utils.ResponseErrorDialog;
import com.hesha.utils.SelectPhoto;
import com.hesha.utils.TimeoutErrorDialog;
import com.hesha.utils.Utils;

public class SubjectDetailsActivity extends Activity implements OnClickListener, OnItemClickListener, OnTaskFinishedListener{
	private static final String TAG = "SubjectDetailsActivity";
	public static final int INTENT_FROM_CAMERA = 11;
	public static final int INTENT_FROM_PHOTO_ALBUM = 12;
	public static final int INTENT_FROM_PHOTO_ZOOM = 13;
	private Button btnBack, btnUploadPhoto;
	private TextView tvName, tvEnName, tvCatName;
	private TextView tvPrice, tvLikeNum, tvCollectNum;
	private Button btnBuy;
	private WebView webSubjectDes;
	private TextView tvExpertCommentNum, tvNorCommentNum, tvSailersNum;
	
	private ImageView ivItem;
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
		setContentView(R.layout.subject_detail_activity);
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
//		tvCreationDate.setText(DateUtils.getStringFromTimeSeconds( collection.getCreation_date()));
		tvName.setText(baseItem.getItem_name());
		tvEnName.setText("");
		tvCatName.setText("");
		tvPrice.setText("");
		tvLikeNum.setText(baseItem.getLike_num() + "");
		tvCollectNum.setText(baseItem.getComment_num() + "");
		
		webSubjectDes.loadDataWithBaseURL(null, baseItem.getItem_des(), "text/html", "utf-8", null);
	}
	
	private void initComponent() {
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		btnUploadPhoto = (Button)findViewById(R.id.btn_upload_photo);
		btnUploadPhoto.setOnClickListener(this);
		
		
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout llHeader = (LinearLayout)inflater.inflate(R.layout.subject_head_view, null);
	
		tvName = (TextView)llHeader.findViewById(R.id.tv_name);
		tvEnName = (TextView)llHeader.findViewById(R.id.tv_en_name);
		tvCatName = (TextView)llHeader.findViewById(R.id.tv_cat_name);
		tvPrice = (TextView)llHeader.findViewById(R.id.tv_price);
		tvLikeNum = (TextView)llHeader.findViewById(R.id.tv_like_num);
		tvCollectNum = (TextView)llHeader.findViewById(R.id.tv_collect_num);
		
		webSubjectDes = (WebView)llHeader.findViewById(R.id.web_subject_des);
		webSubjectDes.setBackgroundColor(0);
		
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
			
		case R.id.btn_upload_photo:
			username = settings.getString(Constants.USERNAME, "");
			if (username.equals("")) {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, Constants.INTENT_CODE_SUBJECT_UPLOAD_PHOTO);
			} else {
				SelectPhoto.getRiseUpDialog(this);
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
			
		case INTENT_FROM_CAMERA:
			File mCurrentPhotoFile = SelectPhoto.getmCurrentPhotoFile();
			Log.i(TAG, "url:" + mCurrentPhotoFile.getAbsolutePath() + " file length:" + mCurrentPhotoFile.length());
			if(mCurrentPhotoFile.length() != 0) {
				startPhotoZoom(Uri.fromFile(mCurrentPhotoFile));
			}
			break;
			
		case INTENT_FROM_PHOTO_ALBUM:
			if(data != null) {
				startPhotoZoom(data.getData());
			}
			break;
			
		case INTENT_FROM_PHOTO_ZOOM:
			if(data != null) {
				setPicToView(data);
			}
			break;
			
		case Constants.INTENT_CODE_SUBJECT_UPLOAD_PHOTO:
			SelectPhoto.getRiseUpDialog(this);
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
	
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, INTENT_FROM_PHOTO_ZOOM);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata)
	{
		Bundle extras = picdata.getExtras();
		if (extras != null)
		{
			Bitmap photo = extras.getParcelable("data");
			
			Intent intent = new Intent(this, ActivityUploadPhoto.class);
			intent.putExtra("photo", photo);
			intent.putExtra("base_item", baseItem);
			startActivity(intent);
		}
	}
}
