package com.hesha.tasks;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.Collection;
import com.hesha.bean.CreateColResStruct;
import com.hesha.bean.gen.CreateCollectionParameter;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.JsonUtils;

public class CreateCollectionTask extends AsyncTask<Void, Void, Void> implements Constants{
	private static final String TAG = "CreateCollectionTask";
	private Context context;
	private ProgressDialog dialog;
	private String token;
	String response;
	private String colName, colDes;
	
	public CreateCollectionTask(Context context, ProgressDialog dialog, String token, 
			String colName, String colDes) {
		this.context = context;
		this.dialog = dialog;
		this.token = token;
		this.colName = colName;
		this.colDes = colDes;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("正在提交数据");
		dialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		CreateCollectionParameter gen = new CreateCollectionParameter();
		gen.setToken(token);
		gen.setCollection_name(colName);
		gen.setCollection_des(colDes);
		
		String data = JsonUtils.genJson(gen);
		
		String url = SERVER_URL + "?ac=createCollection";
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
			CreateColResStruct struct = mapper.readValue(response, CreateColResStruct.class);
			boolean success = Boolean.valueOf(struct.getSuccess());
			if(success) {
				Collection collection = struct.getData();
				if(Constants.D) Log.i(TAG, "collecton:" + collection.getCollection_name());
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
