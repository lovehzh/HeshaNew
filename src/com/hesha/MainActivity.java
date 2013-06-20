package com.hesha;


import com.hesha.utils.MoveBg;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	RelativeLayout bottom_layout;
	ImageView img;
	int startLeft;
	
	private static final String COLLECTION = "collection";
	private static final String CHOICE = "choice";
	private static final String FRIENDS = "friends";
	private static final String MYINFO = "myinfo";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bottom_layout = (RelativeLayout) findViewById(R.id.layout_bottom);
        
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec(COLLECTION).setIndicator(COLLECTION).setContent(new Intent(this, TabCollectionActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(CHOICE).setIndicator(CHOICE).setContent(new Intent(this, TabChoiceActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(FRIENDS).setIndicator(FRIENDS).setContent(new Intent(this, TabFriendsActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(MYINFO).setIndicator(MYINFO).setContent(new Intent(this, TabMyInfoActivity.class)));
        
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        
        img = new ImageView(this);
        img.setImageResource(R.drawable.tab_front_bg);
        bottom_layout.addView(img);
    }
    
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.main_tab_0:
				tabHost.setCurrentTabByTag(COLLECTION);
//				moveFrontBg(img, startLeft, 0, 0, 0);
				MoveBg.moveFrontBg(img, startLeft, 0, 0, 0);
				startLeft = 0;
				break;
			case R.id.main_tab_1:
				tabHost.setCurrentTabByTag(CHOICE);
				MoveBg.moveFrontBg(img, startLeft, img.getWidth(), 0, 0);
				startLeft = img.getWidth();
				break;
			case R.id.main_tab_2:
				tabHost.setCurrentTabByTag(FRIENDS);
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 2, 0, 0);
				startLeft = img.getWidth() * 2;
				break;
			case R.id.main_tab_3:
				tabHost.setCurrentTabByTag(MYINFO);
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 3, 0, 0);
				startLeft = img.getWidth() * 3;
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("ttt");
	};
}