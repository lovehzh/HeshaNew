package com.hesha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CollectionListActivity extends Activity implements OnClickListener{
	private Button btnBack, btnCreateCollection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_list_activity);
		
		initComponents();
	}
	
	private void initComponents() {
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		btnCreateCollection = (Button)findViewById(R.id.btnCreateCollection);
		btnCreateCollection.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
			
		case R.id.btnCreateCollection:
			boolean isSignin = false;
			if(isSignin) {
				//show dialog
			}else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
		
	}
}
