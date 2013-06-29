package com.hesha;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.BaseStruct;
import com.hesha.bean.SubjectItem;
import com.hesha.bean.choice.UploadPhotoBean;
import com.hesha.constants.Constants;
import com.hesha.utils.ImageUpload;
import com.hesha.utils.Storage;
import com.hesha.widget.HorizontalListView;


public class ActivityUploadPhoto extends Activity implements OnClickListener{
	private static final String TAG = "ActivityUploadPhoto";
	private Button btnBack, btnSubmit;
	private ImageView ivImage;
	private Bitmap photo;
	private EditText etTitle;
	private ProgressDialog dialog;
	private SharedPreferences settings;
	private File photoFile;
	private SubjectItem subjectItem;
	
	private ImageAdapter adapter;
	private HorizontalListView list;
	private ArrayList<UploadPhotoBean> beans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		settings = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
		beans = new ArrayList<UploadPhotoBean>();
		for(int i=0; i<5; i++) {
			UploadPhotoBean bean = new UploadPhotoBean();
			bean.setFileName(i + "");
			bean.setTitle(i + "");
			beans.add(bean);
		}
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		photo = bundle.getParcelable("photo");
		subjectItem = (SubjectItem)intent.getSerializableExtra("base_item");//待修改
		
		photoFile = new File(Environment.getExternalStorageDirectory()
				+ "/product_photo.jpg");
		Storage.writeBitmapToExternalStorage(photoFile, photo);
		Log.i(TAG, "bitmap:" + photo);
		
		
	}
	
	private void initComponent() {
		dialog = new ProgressDialog(this);
		
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		
		ivImage = (ImageView)findViewById(R.id.ivImage);
		if(null != photo) ivImage.setImageBitmap(photo); 
		
		etTitle = (EditText)findViewById(R.id.et_title);
		
		btnSubmit = (Button)findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		
//		LayoutInflater inflater = getLayoutInflater();
//		LinearLayout footView = (LinearLayout)inflater.inflate(R.layout.item_of_upload_add, null);
		
		adapter = new ImageAdapter(this, android.R.layout.simple_list_item_1, beans);
		list = (HorizontalListView)findViewById(R.id.listView);
//		list.addView(footView);
		list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.btn_submit:
			String strTitle = etTitle.getText().toString().trim();
			String token = settings.getString(Constants.TOKEN, "");
			if(strTitle.length() < 1 || strTitle.length() > 20) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUploadPhoto.this);
				builder.setTitle("提示");
				builder.setMessage("必须输入标题，且标题长度不能大于20个字符");
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				builder.create().show();
			}else if(token.equals("")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUploadPhoto.this);
				builder.setTitle("提示");
				builder.setMessage("token不能为空，请先登录");
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				builder.create().show();
			}
			else {
				new UploadPhotoTask(token, 
						photoFile.getAbsolutePath()).execute((Void)null);
			}
			
			break;
			

		default:
			break;
		}
		
	}
	
	private class UploadPhotoTask extends AsyncTask<Void, Void, Void> {

		String response;
		String token;
		String image;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setMessage("正在上传产品图片，请稍后");
			dialog.show();
		}
		
		public UploadPhotoTask(String token, String image) {
			this.token = token;
			this.image = image;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Map<String, String> paramters = getUploadPhotoDataParameter(token);
			String url = Constants.SERVER_URL + "?ac=uploadPhotoByWineId";
					
			if (Constants.D)
				Log.i(TAG, url + " " + paramters.toString());
			response = new ImageUpload().postImg(url, paramters, image, "photo");
			Log.e(TAG, "url:" + url + "    image:" + image);
			if (Constants.D)
				Log.i(TAG, response);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog.isShowing()) {
				dialog.dismiss();
			}
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				BaseStruct struct = mapper.readValue(response, BaseStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					Toast.makeText(ActivityUploadPhoto.this, "上传图片成功!", Toast.LENGTH_SHORT).show();
					finish();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUploadPhoto.this);
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
					
//				}else {
//					String errorMessage = parseCode(strCode, "avatar");
//					AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUploadPhoto.this);
//					builder.setTitle("上传失败");
//					builder.setMessage(errorMessage);
//					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							
//						}
//					});
//					
//					builder.create().show();
//				}
				
				
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
	
	private Map<String, String> getUploadPhotoDataParameter(String token)
	{
		Map<String, String> paramters = new HashMap<String, String>();
		paramters.put(Constants.TOKEN, token);
		paramters.put("wine_id", subjectItem.getItem_id() + "");
		paramters.put("image_title", etTitle.getText().toString());
		Log.i(TAG, " pro id:" + subjectItem.getItem_id());
		return paramters;
	}
	
	private String parseCode(String code, String avatar) {
		String result = "";
		if(code.equals("GB2900513")) {
			result = "system error";
		}else if(code.equals("GB2900514")) {
			result = "API does not exist";
		}else if(code.equals("GB2900515")) {
			result = "Device id is empty";
		}else if(code.equals("GB2900516")) {
			result = "Device is not exists";
		}else if(code.equals("GB2900517")) {
			result = "File type is not allowed.";
		}else if(code.equals("GB2900518")) {
			result = "Can not move the temporay file.";
		}else if(code.equals("GB2900519")) {
			result = "The temporary file is not uploaded file.";
		}else if(code.equals("GB2900520")) {
			result = "Token has been expired.";
		}else if(code.equals("GB2900521")) {
			result = "Token is empty";
		}else if(code.equals("GB2900523")) {
			result = "Product id does not exist.";
		}
		else {
			result = "";
		}
		
		result += code;
		
		return result;
	}
	
	
	private class ImageAdapter extends ArrayAdapter<UploadPhotoBean>{
		private ArrayList<UploadPhotoBean> beans;
		private Context context;
		public ImageAdapter(Context context,
				int textViewResourceId, ArrayList<UploadPhotoBean> beans) {
			super(context, textViewResourceId, beans);
			this.context = context;
			this.beans = beans;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			if(position == beans.size() -1 ) {
				convertView = inflater.inflate(R.layout.item_of_upload_add, null);
			}else {
				convertView = inflater.inflate(R.layout.item_of_upload_list, null);
			}
			
			
			
//			ImageView iv = (ImageView)convertView.findViewById(R.id.image_mask);
//			iv.setBackgroundResource(R.drawable.image_upload_mask);
			Log.i(TAG, "xx" + position);
			return convertView;
		}
		
	}
	
	
}
