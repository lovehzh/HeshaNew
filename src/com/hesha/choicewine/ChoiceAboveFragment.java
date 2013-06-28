package com.hesha.choicewine;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.R;
import com.hesha.SubjectDetailsActivity;
import com.hesha.adapter.ImageAndTextListAdapter;
import com.hesha.bean.BaseItem;
import com.hesha.bean.SubjectItem;
import com.hesha.bean.choice.Filter;
import com.hesha.bean.choice.FilterResultData;
import com.hesha.bean.choice.FilterResultStruct;
import com.hesha.bean.choice.Intention;
import com.hesha.bean.choice.SearchResultData;
import com.hesha.bean.choice.SearchResultStruct;
import com.hesha.bean.choice.WineCatBean;
import com.hesha.constants.Constants;
import com.hesha.utils.HttpUrlConnectionUtils;
import com.hesha.utils.MyDialog;
import com.hesha.utils.TimeoutErrorDialog;
import com.hesha.utils.Utils;

public class ChoiceAboveFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private static final String TAG = "ChoiceAboveFragment";
	private static InputMethodManager imm;
	private static Activity activity;
	private Bundle b1;
	private WineCatBean wineCatBean;
	private Intention intention;
	private ArrayList<Intention> intentions;
	private ArrayList<Filter> filters;

	private Button btnIntention, btnFilter;
	private ArrayList<BaseItem> items;
	private int totalItems;
	private int typeId, intentionId, beginIndex, pageSize, sortType, orderType;
	private String parameter;
	
	private Button btnBack, btnSearch;
	private TextView tvTitle;

	private GridView gridView;
	private ImageAndTextListAdapter adapter;

	private RelativeLayout rlTips;
	private TextView tvTipText;
	private ProgressBar pdTips;
	private static final int WHAT_DID_LOAD_DATA = 0;
	// private static final int WHAT_DID_REFRESH = 1;
	private static final int WHAT_DID_MORE = 2;
	private static final int CONNECTION_TIME_OUT = 3;
	private static final int WHAT_DID_SEARCH = 4;
	private static final int SEARCH_ACTION = 5;
	private static final int SHOW_ERROR = 6;
	private RelativeLayout headView;
	private Button bt;
	private ProgressBar pg;
	// ListView底部View
	private View moreView;
	private Handler handler;
	// 设置一个最大的数据条数，超过即不再加载
	// private int MaxDateNum;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	
	private int mPos = -1;
	static EditText etSearch;
	private String keywords;
	private static boolean isSearch;
	private LinearLayout llButtons;
	public ChoiceAboveFragment(){
		
	}
	
	public ChoiceAboveFragment(int position) {
		this.mPos = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mPos == -1 && savedInstanceState != null)
			mPos = savedInstanceState.getInt("mPos");
		return inflater.inflate(R.layout.choice_above_fragment, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		b1 = getArguments();
		if(null == b1) b1=savedInstanceState;

		wineCatBean = (WineCatBean) b1.getSerializable("wine_cat_bean");
		//intention = (Intention) b1.getSerializable("intention");
		intentions = (ArrayList<Intention>) b1.getSerializable("intentions");
		if(mPos == -1){
			intention = intentions.get(b1.getInt("position"));
		}else {
			intention = intentions.get(mPos);
		}
		
		filters = (ArrayList<Filter>) b1.getSerializable("filters");

		typeId = wineCatBean.getType_id();
		intentionId = intention.getIntention_id();
		pageSize = 10;
		sortType = 1;
		orderType = Constants.ASC;
		parameter = b1.getString("parameter");

		items = new ArrayList<BaseItem>();
		loadData();
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// init component
		activity = getActivity();
		imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		btnBack = (Button)activity.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		
		btnSearch = (Button)activity.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);
		
		tvTitle = (TextView)activity.findViewById(R.id.tv_title);
		
		btnIntention = (Button) activity.findViewById(R.id.btn_intention);
		btnIntention.setText(intention.getIntention_name());
		btnIntention.setOnClickListener(this);

		btnFilter = (Button) activity.findViewById(R.id.btn_filter);
		btnFilter.setOnClickListener(this);
		
		llButtons = (LinearLayout) activity.findViewById(R.id.ll_buttons);

		rlTips = (RelativeLayout) activity.findViewById(R.id.rl_tips);
		tvTipText = (TextView) activity.findViewById(R.id.pulldown_header_text);
		pdTips = (ProgressBar) activity
				.findViewById(R.id.pulldown_header_loading);

		gridView = (GridView) activity.findViewById(R.id.grid);
		adapter = new ImageAndTextListAdapter(activity, items, gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_intention:
			((ChoiceResultActivity) activity).getMenu().showMenu();
			initMenu(0);
			break;

		case R.id.btn_filter:
			((ChoiceResultActivity) activity).getMenu().showMenu();
			initMenu(1);
			break;
			
		case R.id.btn_back:
			isSearch = false;
			activity.finish();
			break;
			
		case R.id.btn_search:
			getSearchDialog(activity);
			
			break;

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		switch (adapter.getId()) {
		case R.id.grid:
			BaseItem baseItem = items.get(position);
			if(baseItem instanceof SubjectItem) {
				Intent intent = new Intent(getActivity(), SubjectDetailsActivity.class);
				baseItem.setItem_type(2);
				intent.putExtra("base_item", baseItem);
				startActivity(intent);
			}
			Intent intent = new Intent(getActivity(), SubjectDetailsActivity.class);
			
			break;

		default:
			break;
		}

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("mPos", mPos);
	}

	private void initMenu(int mode) {
		if (mode == 0) {
			((ChoiceResultActivity) activity).getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.menu_frame, new IntentionMenuFragment()).commit();
		} else {
			((ChoiceResultActivity) activity).getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.menu_frame, new FilterMenuFragment()).commit();
		}

	}

	private void loadData() {
		Log.i(TAG, "on loadData");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = null;
				Object struct = null;
				if(isSearch) {
					struct = getWineByKeywords(beginIndex, pageSize, sortType, orderType, parameter);
					msg = mUIHandler.obtainMessage(WHAT_DID_SEARCH);
				}else {
					struct = getWineByFilters(typeId,
							intentionId, beginIndex, pageSize, sortType, orderType,
							parameter);// 取服务器端数据
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				}
				
				msg.obj = struct;
				msg.sendToTarget();
			}
		}).start();

	}

	private Handler mUIHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA:

				FilterResultStruct struct = (FilterResultStruct) msg.obj;
				items = struct.getSubjects();
				totalItems = struct.getCounts();
				tvTitle.setText(Utils.replayDigital(getString(R.string.find_these_goods), totalItems));
				if (null != items) {
					adapter.clear();
					for (BaseItem item : items) {
						adapter.add(item);
					}
					rlTips.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				} else {
					// 筛选时
					// {"success":true,"error_code":"GB0000000","error_des":"","data":{"counts":"0","subjects":null}}
					adapter.clear();
					adapter.notifyDataSetChanged();

					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE);
				}
				break;
				
			case WHAT_DID_SEARCH:
				SearchResultStruct struct2 = (SearchResultStruct) msg.obj;
				items = struct2.getItems();
				totalItems = struct2.getResult_nums();
				tvTitle.setText(Utils.replayDigital(getString(R.string.find_these_goods), totalItems));
				if (null != items) {
					adapter.clear();
					for (BaseItem item : items) {
						adapter.add(item);
					}
					rlTips.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				} else {
					// 筛选时
					// {"success":true,"error_code":"GB0000000","error_des":"","data":{"counts":"0","subjects":null}}
					adapter.clear();
					adapter.notifyDataSetChanged();

					tvTipText.setText(getString(R.string.no_record));
					pdTips.setVisibility(View.INVISIBLE);
				}
				break;
				
			case SEARCH_ACTION:
				loadData();
				break;

			case CONNECTION_TIME_OUT:
				TimeoutErrorDialog.showTimeoutError(activity);
				break;
				
			case SHOW_ERROR:
				if(isSearch) {
					SearchResultData struct3 = (SearchResultData)msg.obj;
					MyDialog.showInfoDialog(getActivity(), getString(R.string.tips), struct3.getError_des());
				}else {
					FilterResultData struct3 = (FilterResultData)msg.obj;
					MyDialog.showInfoDialog(getActivity(), getString(R.string.tips), struct3.getError_des());
				}
				break;

			default:
				break;
			}
		}
	};

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
			Log.i(TAG, "response:" + response);

		if (response.equals(Constants.CONNECTION_TIMED_OUT)) {
			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
			msg.sendToTarget();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				FilterResultData struct = mapper.readValue(response,
						FilterResultData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if (success) {
					result = struct.getData();
				} else {
					Message msg = mUIHandler.obtainMessage(SHOW_ERROR);
					msg.obj = struct;
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							activity);
//					builder.setTitle("获取数据失败");
//					builder.setMessage("错误：" + struct.getError_des());
//					builder.setPositiveButton("确认",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//								}
//							});
//
//					builder.create().show();
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
	
	public  Dialog getSearchDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(R.layout.search_dialog_view);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.TOP); // set dialog display position here
//		window.setWindowAnimations(R.style.rise_up);//set animation here
		dialog.show();
		
		etSearch = (EditText)dialog.findViewById(R.id.et_search);
		
		
		etSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				isSearch = true;
				((ChoiceResultActivity) activity).getMenu().setSlidingEnabled(false);
				keywords = etSearch.getText().toString().trim();
				Toast.makeText(activity, keywords, Toast.LENGTH_SHORT).show();
				if (imm.isActive()) {//如果开启
					imm.hideSoftInputFromWindow(v.getWindowToken(),0);
				}
				llButtons.setVisibility(View.GONE);
				dialog.dismiss();
				
				parameter = "{\"key\":\" "+ keywords +" \",\"item_type\":\"2\"}";
				
				Message m = mUIHandler.obtainMessage(SEARCH_ACTION);
				m.sendToTarget();
				return false;
			}
		});
		
		
		Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		return dialog;
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
			Log.i(TAG, "response:" + response);

		if (response.equals(Constants.CONNECTION_TIMED_OUT)) {
			Message msg = mUIHandler.obtainMessage(CONNECTION_TIME_OUT);
			msg.sendToTarget();
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				SearchResultData struct = mapper.readValue(response,
						SearchResultData.class);
				boolean success = Boolean.valueOf(struct.getSuccess());
				if (success) {
					result = struct.getData();
				} else {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							activity);
//					builder.setTitle("获取数据失败");
//					builder.setMessage("错误：" + struct.getError_des());
//					builder.setPositiveButton("确认",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//								}
//							});
//
//					builder.create().show();
					Message msg = mUIHandler.obtainMessage(SHOW_ERROR);
					msg.obj = struct;
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
