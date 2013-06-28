package com.hesha.choicewine;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.hesha.R;
import com.hesha.bean.choice.Filter;
import com.hesha.bean.choice.Intention;
import com.hesha.bean.choice.WineCatBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ChoiceResultActivity extends FragmentActivity {
	private Fragment mContent;
	private SlidingMenu menu;
	private WineCatBean wineCatBean;
	private Intention intention;
	private int position;
	public static ArrayList<Intention> intentions;
	public static ArrayList<Filter> filters;
	Bundle b1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("life", "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		initData();

		b1 = new Bundle();
		b1.putSerializable("wine_cat_bean", wineCatBean);
		b1.putSerializable("intention", intention);
		b1.putSerializable("intentions", intentions);
		b1.putSerializable("filters", filters);
		b1.putString("parameter", "");

		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null) {
			ChoiceAboveFragment choiceAboveFragment = new ChoiceAboveFragment(
					position);
			choiceAboveFragment.setArguments(b1);
			mContent = choiceAboveFragment;
		}
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new IntentionMenuFragment()).commit();
	}

	public SlidingMenu getMenu() {
		return menu;
	}

	private void initData() {
		Intent intent = getIntent();
		wineCatBean = (WineCatBean) intent
				.getSerializableExtra("wine_cat_bean");
		intention = (Intention) intent.getSerializableExtra("intention");
		intentions = (ArrayList<Intention>) intent
				.getSerializableExtra("intentions");
		filters = (ArrayList<Filter>) intent.getSerializableExtra("filters");
		position = intent.getIntExtra("position", 0);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("life", "onSaveInstanceState");
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		fragment.setArguments(b1);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				menu.showContent();
			}
		}, 50);
	}

	public void switchContent(final Fragment fragment, String parameter) {
		
		mContent = fragment;
		b1.putString("parameter", parameter);
		b1.putInt("position", position);
		fragment.setArguments(b1);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				menu.showContent();
			}
		}, 50);
	}

}
