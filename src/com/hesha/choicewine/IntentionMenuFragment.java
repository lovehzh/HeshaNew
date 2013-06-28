package com.hesha.choicewine;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.choice.Intention;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class IntentionMenuFragment extends ListFragment {
	private ArrayList<Intention> intentions;
	private Button btnDone;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		btnDone = (Button)getActivity().findViewById(R.id.btn_done);
		btnDone.setVisibility(View.INVISIBLE);
		
		SampleAdapter adapter = new SampleAdapter(getActivity());
		intentions = ChoiceResultActivity.intentions;
		for (int i = 0; i < intentions.size(); i++) {
			adapter.add(intentions.get(i));
		}
		setListAdapter(adapter);
	}


	public class SampleAdapter extends ArrayAdapter<Intention> {
		
		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(intentions.get(position).getIntention_name());

			return convertView;
		}

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Fragment newContent = new ChoiceAboveFragment(position);
		if (newContent != null)
			switchFragment(newContent);
	}
	
	// the meat of switching the above fragment
		private void switchFragment(Fragment fragment) {
			if (getActivity() == null)
				return;

			if (getActivity() instanceof ChoiceResultActivity) {
				ChoiceResultActivity ra = (ChoiceResultActivity) getActivity();
				ra.switchContent(fragment);
			}
		}
}
