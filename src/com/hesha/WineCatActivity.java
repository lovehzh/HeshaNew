package com.hesha;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.adapter.IntentionAdapter;
import com.hesha.adapter.WineCatAdapter;
import com.hesha.bean.choice.Filter;
import com.hesha.bean.choice.Intention;
import com.hesha.bean.choice.WineCatBean;
import com.hesha.bean.choice.WineCatStruct;
import com.hesha.choicewine.ChoiceResultActivity;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.MyDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WineCatActivity extends Activity implements Constants, OnItemClickListener, OnClickListener{
	private static final String TAG = "WineCatActivity";
	private ListView list;
	private WineCatAdapter adapter;
	private ArrayList<WineCatBean> beans;
	
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int CONNECTION_TIME_OUT = 3;
	private static final int SERVER_RESPONSE_ERROR = 4;
	private RelativeLayout headView;
	private Button bt;
    private ProgressBar pg;
    // ListView底部View
    private View moreView;
    private Handler handler;
    
    private LinearLayout llChoiceCatDetail, llRowChoiceCat;
    private ImageView ivCatIcon;
    private TextView tvCatName, tvDes;
    private GridView grid;
    private IntentionAdapter intentionAdapter;
    private ArrayList<Intention> intentions;
    private WineCatBean wineCatBean;
    private ArrayList<Filter> filters;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		setContentView(R.layout.wine_cat_activity);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		beans = new ArrayList<WineCatBean>();
		intentions = new ArrayList<Intention>();
		loadData();
	}
	
	private void initComponent() {
		LayoutInflater inflater = getLayoutInflater();
		headView = (RelativeLayout)inflater.inflate(R.layout.pulldown_header, null);
		
		// 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);

        bt = (Button) moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();
		
		list = (ListView)findViewById(R.id.list);
		list.addHeaderView(headView);
		adapter = new WineCatAdapter(this, android.R.layout.simple_list_item_1, beans, list);
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		
		llChoiceCatDetail = (LinearLayout)findViewById(R.id.ll_choice_cat_detail);
		llChoiceCatDetail.setVisibility(View.INVISIBLE);
		
		llRowChoiceCat = (LinearLayout)findViewById(R.id.ll_row_choice_cat);
		llRowChoiceCat.setOnClickListener(this);
		
		ivCatIcon = (ImageView)llRowChoiceCat.findViewById(R.id.iv_avatar);
		tvCatName = (TextView)llRowChoiceCat.findViewById(R.id.tv_cat_name);
		tvDes = (TextView)llRowChoiceCat.findViewById(R.id.tv_des);
		
		grid = (GridView)findViewById(R.id.grid);
		grid.setSelector(R.drawable.hide_listview_yellow_selector);
		intentionAdapter = new IntentionAdapter(this, intentions);
		grid.setAdapter(intentionAdapter);
		grid.setOnItemClickListener(this);
	}
	
	private ArrayList<WineCatBean> getWineCatsFromServer() {
		String response = "";
		ArrayList<WineCatBean> beans = new ArrayList<WineCatBean>();
		
		String url = SERVER_URL + "?ac=getChoiceWinePage";
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
				
				WineCatStruct struct = mapper.readValue(response, WineCatStruct.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					//
					beans = struct.getData();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(WineCatActivity.this);
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
				Message msg = mUIHandler.obtainMessage(SERVER_RESPONSE_ERROR);
				msg.sendToTarget();
			} catch (JsonMappingException e) {
				e.printStackTrace();
				Message msg = mUIHandler.obtainMessage(SERVER_RESPONSE_ERROR);
				msg.sendToTarget();
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}		
		
		return beans;
	}
	
	private void loadData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<WineCatBean> comments = getWineCatsFromServer();
				if(null != comments) Log.i(TAG, "on loadData friend info size : " + comments.size());
				if(comments.size() == 0) comments = null; 
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = comments;
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
					ArrayList<WineCatBean> comments = (ArrayList<WineCatBean>)msg.obj;
					adapter.clear();
					for(WineCatBean f : comments) {
						adapter.add(f);
					}
					list.removeHeaderView(headView);
					
					adapter.notifyDataSetChanged();
				}else {
					TextView tvTipText = (TextView)headView.findViewById(R.id.pulldown_header_text);
					ProgressBar pdTips = (ProgressBar)headView.findViewById(R.id.pulldown_header_loading);
					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE);
				}
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(WineCatActivity.this);
				break;
				
			case SERVER_RESPONSE_ERROR:
				MyDialog.showInfoDialog(getParent(), R.string.tips, R.string.response_data_error);
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		switch (adapterView.getId()) {
		case R.id.list:
			wineCatBean = beans.get(position);
			filters = wineCatBean.getFilters();
			//ivCatIcon
			tvCatName.setText(wineCatBean.getType_name());
			tvDes.setText(wineCatBean.getDes());
			
			intentions = wineCatBean.getIntentions();
			intentionAdapter.clear();
			for(Intention i : intentions) {
				intentionAdapter.add(i);
			}
			intentionAdapter.notifyDataSetChanged();
			
			llChoiceCatDetail.setVisibility(View.VISIBLE);
			llChoiceCatDetail.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
			break;
			
		case R.id.grid:
			Intent intent = new Intent(this, ChoiceResultActivity.class);
			intent.putExtra("wine_cat_bean", wineCatBean);
			intent.putExtra("intention", intentions.get(position));
			intent.putExtra("intentions", intentions);
			intent.putExtra("filters", filters);
			intent.putExtra("position", position);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_row_choice_cat:
			llChoiceCatDetail.setVisibility(View.INVISIBLE);
			llChoiceCatDetail.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
			break;

		default:
			break;
		}
		
	}
	


}
