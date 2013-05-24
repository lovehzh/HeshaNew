package com.hesha.tasks;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.CollectionStruct;
import com.hesha.bean.CollectionTypeAndReviewItems;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
			
//			ArrayList<CollectionTypeAndReviewItems> list = struct.getData().ge
//			Log.i(TAG, "list.size = " + list.size());
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
