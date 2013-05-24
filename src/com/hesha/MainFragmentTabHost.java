package com.hesha;





import com.hesha.view.PageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainFragmentTabHost extends FragmentActivity {
	
	private FragmentTabHost mTabHost;
	
	private String[] TabTag 		= {"tab1", "tab2", "tab3", "tab4"};
	private RadioGroup radioGroup;
	
	private Intent intent;
	
	private PageIndicator pageIndicator;
	private int originalPostion, targetPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_tabhost);
		
		pageIndicator = (PageIndicator)findViewById(R.id.pi);
		pageIndicator.setIndex(originalPostion);
		
		intent = new Intent(this, MyInfoFragment.class);
		
		radioGroup = (RadioGroup)findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tab_0:
					mTabHost.setCurrentTabByTag(TabTag[0]);
					targetPosition = 0;
					break;
					
				case R.id.main_tab_1:
					mTabHost.setCurrentTabByTag(TabTag[1]);
					targetPosition = 1;
					break;
					
				case R.id.main_tab_2:
					mTabHost.setCurrentTabByTag(TabTag[2]);
					targetPosition = 2;
					break;
					
				case R.id.main_tab_3:
					mTabHost.setCurrentTabByTag(TabTag[3]);
					targetPosition = 3;
					break;
					

				default:
					break;
				}
				
				pageIndicator.changePosition(originalPostion, targetPosition - originalPostion);
				originalPostion = targetPosition;
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		makeTab();
		
	}
	
	public FragmentTabHost makeTab() {
		if(this.mTabHost == null) {
			mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
			mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
			mTabHost.bringToFront();
			
			TabSpec myInfoSpec = mTabHost.newTabSpec(TabTag[3]);
			myInfoSpec.setIndicator("",null).setContent(intent);
			
			
			mTabHost.addTab(
	        		mTabHost.newTabSpec(TabTag[0]).setIndicator(""),
	        		CollectionFragment.class, 
	        		null);
			
			mTabHost.addTab(
	        		mTabHost.newTabSpec(TabTag[1]).setIndicator(""),
	        		ChoiceFragment.class, 
	        		null);
			
			mTabHost.addTab(
	        		mTabHost.newTabSpec(TabTag[2]).setIndicator(""),
	        		FriendsFragment.class, 
	        		null);
			
			mTabHost.addTab(myInfoSpec);
			
		}else {
			return mTabHost;
		}
		
		return null;
	}
	
	
	/**
	 * 连续按两次返回键就退出
	 */
	private long firstTime;
	private Boolean ifExit;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(System.currentTimeMillis() - firstTime < 3000){
				ifExit = true;
			}else{
				firstTime = System.currentTimeMillis();
				ifExit = false;
				Toast.makeText(this,
						getResources().getString(R.string.press_again_exit),
						Toast.LENGTH_SHORT).show();
			}
			if(ifExit){
				//do something, just like animation
				finish();
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
