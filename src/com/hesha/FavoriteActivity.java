package com.hesha;

import java.util.ArrayList;

import com.hesha.bean.Collection;
import com.hesha.bean.gen.GetMyCollectionsParameter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FavoriteActivity extends Activity implements OnClickListener{
	private Button btnCancel;
	private EditText etColName;
	private ListView list;
	private MyApplication app;
	private ArrayList<Collection> myCollections;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_activity);
		app = (MyApplication)getApplication();
		
		initData();
		initComponent();
	}
	
	private void initData() {
		myCollections = app.getMyCollections();
		if(null == myCollections) {
			
		}
	}
	
	private void initComponent() {
		etColName = (EditText)findViewById(R.id.et_col_name);
		
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		list = (ListView)findViewById(R.id.list);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
