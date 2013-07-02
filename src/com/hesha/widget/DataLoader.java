package com.hesha.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.BaseItem;
import com.hesha.bean.choice.FilterResultData;
import com.hesha.bean.choice.FilterResultStruct;
import com.hesha.bean.choice.SearchResultData;
import com.hesha.bean.choice.SearchResultStruct;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class DataLoader {
	private Context context;
	private OnCompletedListener l;

	public DataLoader(Context mContext) {
		this.context = mContext;

	}

	public void setOnCompletedListerner(OnCompletedListener mL) {
		l = mL;
	}

	public void startLoading(HashMap<String, String> mParams) {
		if (mParams != null) {
			LoadTask task = new LoadTask();
			task.execute(mParams);
		}
	}

	private class LoadTask extends
			AsyncTask<HashMap<String, String>, Void, List<BaseItem>> {

		@Override
		protected List<BaseItem> doInBackground(HashMap<String, String>... params) {
			List<BaseItem> ret = null;
			try {
				String strIsSearch = params[0].get("isSearch");
				boolean isSearch;
				if(null == strIsSearch || !strIsSearch.equals("true")) {
					isSearch = false;
				}else {
					isSearch = true;
				}
				
				int beginIndex = Integer.parseInt(params[0].get("beginIndex"));
				int pageSize = Integer.parseInt(params[0].get("pageSize"));
				int sortType = Integer.parseInt(params[0].get("sortType"));
				int orderType = Integer.parseInt(params[0].get("orderType"));
				String parameter = params[0].get("parameter");
				int typeId = Integer.parseInt(params[0].get("typeId"));
				int intentionId = Integer.parseInt(params[0].get("intentionId"));
				
				ret = new ArrayList<BaseItem>();
				
				if(isSearch) {
					SearchResultStruct struct = getWineByKeywords(beginIndex, pageSize, sortType, orderType, parameter);
					ret = struct.getItems();
				}else {
					FilterResultStruct struct = getWineByFilters(typeId, intentionId, beginIndex, pageSize, sortType, orderType, parameter);
					ret =  struct.getSubjects();
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return ret;
		}

		@Override
		protected void onPostExecute(List<BaseItem> ret) {

			if (ret == null) {
				l.onCompletedFailed("------------------faild");
			} else {
				l.onCompletedSucceed(ret);
			}

		}
	}

	public interface OnCompletedListener {
		public void onCompletedSucceed(List<BaseItem> list);

		public void onCompletedFailed(String str);

		public void getCount(int count);
	}
	
	private FilterResultStruct getWineByFilters(int typeId, int intentionId,
			int beginIndex, int pageSize, int sortType, int orderType,
			String parameter) {
		String response = "";
		String url = Constants.SERVER_URL + "?ac=getWinesByFilter&type_id="
				+ typeId + "&intention_id=" + intentionId + "&begin_index="
				+ beginIndex + "&page_num=" + pageSize + "&sort_type="
				+ sortType + "&order_type=" + orderType;
		FilterResultStruct result = new FilterResultStruct();

		try {
			response = HttpUrlConnectionUtils.post(url, parameter,
					Constants.CONTENT_TYPE_JSON);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Constants.D)
			Log.i("load data", "response:" + response);

		if (response.equals(Constants.CONNECTION_TIMED_OUT)) {
//			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
//			msg.sendToTarget();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				FilterResultData struct = mapper.readValue(response,
						FilterResultData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if (success) {
					result = struct.getData();
				} else {
//					Message msg = mUIHandler.obtainMessage(SHOW_ERROR);
//					msg.obj = struct;
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		return null;
	}
	
	
	private SearchResultStruct getWineByKeywords(int beginIndex, int pageSize,
			int sortType, int orderType, String parameter) {
		String response = "";
		String url = Constants.SERVER_URL + "?ac=searchItemByKeyWords&begin_index="
				+ beginIndex + "&page_num=" + pageSize + "&sort_type="
				+ sortType + "&order_type=" + orderType;
		SearchResultStruct result = new SearchResultStruct();

		try {
			response = HttpUrlConnectionUtils.post(url, parameter, Constants.CONTENT_TYPE_JSON);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Constants.D)
			Log.i("load data search", "response:" + response);

		if (response.equals(Constants.CONNECTION_TIMED_OUT)) {
//			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
//			msg.sendToTarget();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				SearchResultData struct = mapper.readValue(response,
						SearchResultData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if (success) {
					result = struct.getData();
				} else {
//					Message msg = mUIHandler.obtainMessage(SHOW_ERROR);
//					msg.obj = struct;
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		return null;
	}
}
