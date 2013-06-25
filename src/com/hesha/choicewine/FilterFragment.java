package com.hesha.choicewine;

import java.util.ArrayList;

import com.hesha.R;
import com.hesha.bean.choice.Filter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FilterFragment extends ListFragment {
	private ArrayList<Filter> filters;
	private Button btnDone;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnDone = (Button)getActivity().findViewById(R.id.btn_done);
		btnDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Fragment newContent = new FilterFragment();
				switchFragment(newContent);
			}
		});
		
		SampleAdapter adapter = new SampleAdapter(getActivity());
		filters = ChoiceResultActivity.filters;
		for (int i = 0; i < filters.size(); i++) {
			adapter.add(filters.get(i));
		}
		setListAdapter(adapter);
	}


	public class SampleAdapter extends ArrayAdapter<Filter> {
		
		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_filter, null);
			}
			Log.i("ff", filters.get(position).getName());
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(filters.get(position).getName());
			

			return convertView;
		}

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Context context = getActivity().getApplicationContext();
		new  AlertDialog.Builder(context)
        .setIcon(null)
        .setTitle(R.string.filter)
        .setSingleChoiceItems(R.array.select_dialog_items2, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked on a radio button do some stuff */
            }
        }).create().show();
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
