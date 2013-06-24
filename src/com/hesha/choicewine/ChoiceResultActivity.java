package com.hesha.choicewine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hesha.R;
import com.hesha.bean.choice.Intention;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ChoiceResultActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = "ChoiceResultActivity";
	private ListView list;
	private Intention intention;
	private Button btnIntention, btnFilter;
	
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
		intention = (Intention)intent.getSerializableExtra("intention");
	}
	
	private void initComponent() {
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new FilterFragment())
		.commit();
		
		btnIntention = (Button)findViewById(R.id.btn_intention);
		btnIntention.setText(intention.getIntention_name());
		btnIntention.setOnClickListener(this);
		
		btnFilter = (Button)findViewById(R.id.btn_filter);
		btnFilter.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_intention:
			
			break;
			
		case R.id.btn_filter:
			break;

		default:
			break;
		}
		
	}


}
