package com.hesha;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
public class CollectionFragment extends Fragment implements OnClickListener{
	private Button btnCreateCollection;
	private LinearLayout ll0, ll1;
	private Activity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.collection_fragment, null);		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		initComponent();
	}
	
	private void initComponent() {
		
		activity = getActivity();
		btnCreateCollection = (Button)activity.findViewById(R.id.add_collection);
		btnCreateCollection.setOnClickListener(CollectionFragment.this);
		
		ll0 = (LinearLayout)activity.findViewById(R.id.ll_0);
		ll1 = (LinearLayout)activity.findViewById(R.id.ll_1);
		
		//add button dynamic
		int itemSize = 10;
		for(int i=0; i<itemSize; i++) {
			Button btnTite = new Button(activity);
			btnTite.setText("some" + i);
			btnTite.setTag(i);
			if(i < itemSize / 2) {
				ll0.addView(btnTite);
			}else {
				ll1.addView(btnTite);
			}
			
			
			btnTite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int i = Integer.valueOf(v.getTag().toString());
					Toast.makeText(activity, "some" + i, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(activity, CollectionListActivity.class);
					startActivity(intent);
				}
			});
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_collection:
			boolean isLogin = false;
			if(isLogin) {
				
			}else {
				Intent intent = new Intent(activity, LoginActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

}
