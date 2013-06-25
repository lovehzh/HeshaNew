package com.hesha.choicewine;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.R;
import com.hesha.adapter.ImageAndTextListAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.choice.Filter;
import com.hesha.bean.choice.FilterResultData;
import com.hesha.bean.choice.FilterResultStruct;
import com.hesha.bean.choice.Intention;
import com.hesha.bean.choice.WineCatBean;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.TimeoutErrorDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ChoiceResultActivity extends BaseActivity implements OnClickListener, OnItemClickListener{
	private static final String TAG = "ChoiceResultActivity";
	private ListView list;
	private WineCatBean wineCatBean;
	private Intention intention;
	public static ArrayList<Intention> intentions;
	public static ArrayList<Filter> filters;
	private Button btnIntention, btnFilter;
	private ArrayList<BaseItem> items;
	private int totalItems;
	private int typeId, intentionId, beginIndex, pageSize, sortType, orderType;
	
	private GridView gridView;
	private ImageAndTextListAdapter adapter;
	
	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
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
    
    private Fragment mContent;
    
	public ChoiceResultActivity() {
		super(R.string.title);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_result_activity);
		
		initData();
		initComponent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		wineCatBean = (WineCatBean)intent.getSerializableExtra("wine_cat_bean");
		intention = (Intention)intent.getSerializableExtra("intention");
		intentions = (ArrayList<Intention>)intent.getSerializableExtra("intentions");
		filters = (ArrayList<Filter>)intent.getSerializableExtra("filters");
		
		typeId = wineCatBean.getType_id();
		intentionId = intention.getIntention_id();
		pageSize = 10;
		sortType = 1;
		orderType = Constants.ASC;
		
		items = new ArrayList<BaseItem>();
		loadData();
	}
	
	private void initMenu(int mode) {
		if(mode == 0){
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.menu_frame, new IntentionFragment())
			.commit();
		}else{
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.menu_frame, new FilterFragment())
			.commit();		
		}
		
	}
	
	private void initComponent() {
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		getSlidingMenu().setMenu(R.layout.menu_frame);
//		getSlidingMenu().setShadowDrawable(R.drawable.shadowright);
//		getSlidingMenu().attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		
		btnIntention = (Button)findViewById(R.id.btn_intention);
		btnIntention.setText(intention.getIntention_name());
		btnIntention.setOnClickListener(this);
		
		btnFilter = (Button)findViewById(R.id.btn_filter);
		btnFilter.setOnClickListener(this);
		
		rlTips = (RelativeLayout)findViewById(R.id.rl_tips);
		tvTipText = (TextView)findViewById(R.id.pulldown_header_text);
		pdTips = (ProgressBar)findViewById(R.id.pulldown_header_loading);
		
		gridView = (GridView)findViewById(R.id.grid);
		adapter = new ImageAndTextListAdapter(this, items, gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_intention:
			getSlidingMenu().showMenu();
			initMenu(0);
			break;
			
		case R.id.btn_filter:
			getSlidingMenu().showMenu();
			initMenu(1);
			break;

		default:
			break;
		}
		
	}
	
	private void loadData(){
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				FilterResultStruct struct = getWineByFilters(typeId, intentionId, beginIndex, pageSize,  sortType, orderType);//取服务器端数据
//			    if(null != collectionInfoAndItems) Log.i(TAG, "on loadData photoInfos size : " + photoInfos.size());
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.obj = struct;
				msg.sendToTarget();
				}
		}).start();
		
	}
	
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:
				
				if(null != msg.obj) {
					FilterResultStruct struct = (FilterResultStruct)msg.obj;
					items = struct.getSubjects();
					totalItems = struct.getCount();
					
					//updata ui
					//updateUI();
					
					adapter.clear();
					for(BaseItem item : items) {
						adapter.add(item);
					}
					rlTips.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				}else {
					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE); 
				}
				break;
				
			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(ChoiceResultActivity.this);
				break;

			default:
				break;
			}
		}
	};
	
	private FilterResultStruct getWineByFilters(int typeId, int intentionId, int beginIndex, int pageSize, int sortType, int orderType) {
		String response = "";
		String url = Constants.SERVER_URL + "?ac=getWinesByFilter&type_id=" + typeId +"&intention_id="+ intentionId +"&begin_index="+ beginIndex +"&page_num="+ pageSize +"&sort_type="+ sortType +"&order_type=" + orderType;
		FilterResultStruct result = new FilterResultStruct();
		
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
				FilterResultData struct = mapper.readValue(response, FilterResultData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if(success) {
					result = struct.getData();
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
			return result;
		}
		
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void switchContent(final Fragment fragment, int index) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		
		intention = intentions.get(index);
		intentionId = intention.getIntention_id();
		btnIntention.setText(intention.getIntention_name());
		loadData();
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
	
	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
}
