package com.hesha.choicewine;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hesha.R;
import com.hesha.bean.choice.Filter;
import com.hesha.bean.choice.ParameterFilter;
import com.hesha.bean.choice.Paramters;
import com.hesha.bean.choice.Value;
import com.hesha.utils.JsonUtils;

public class FilterMenuFragment extends ListFragment {
	private ArrayList<Filter> filters;
	private static ArrayList<Value> values;
	private Button btnDone;
	private static SampleAdapter adapter;
	private static Filter filter;
	private static ArrayList<ParameterFilter> parameterFilters;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnDone = (Button)getActivity().findViewById(R.id.btn_done);
		btnDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				for(Filter filter : filters) {
					filter.setCurValue("");
				}
				adapter.notifyDataSetChanged();
				
				Paramters paramters = new Paramters();
				paramters.setFilters(parameterFilters);
				String paramter = JsonUtils.genJson(paramters);
				
				Fragment newContent = new ChoiceAboveFragment();
				if(null != newContent)
					switchFragment(newContent, paramter);
			}
		});
		
		adapter = new SampleAdapter(getActivity());
		filters = ChoiceResultActivity.filters;
		for (int i = 0; i < filters.size(); i++) {
			adapter.add(filters.get(i));
		}
		setListAdapter(adapter);
		
		parameterFilters = new ArrayList<ParameterFilter>();
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
			
			TextView tvCurValue= (TextView)convertView.findViewById(R.id.tv_cur_value);
			tvCurValue.setText(filters.get(position).getCurValue());

			return convertView;
		}

	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		filter = filters.get(position);
		values = filter.getValues();
		
		String[] temps = new String[values.size()];
		for(int i=0; i<temps.length; i++) {
			temps[i] = values.get(i).getValue_name();
		}
		
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Create and show the dialog.
		int checkedItem = 0;
        SomeDialog newFragment = new SomeDialog (checkedItem, temps);
        newFragment.show(ft, "dialog");
		
	}
	
	// the meat of switching the above fragment
		private void switchFragment(Fragment fragment, String parameter) {
			if (getActivity() == null)
				return;

			if (getActivity() instanceof ChoiceResultActivity) {
				ChoiceResultActivity ra = (ChoiceResultActivity) getActivity();
				ra.switchContent(fragment, parameter);
			}
		}
		
		public static class SomeDialog extends DialogFragment {
			private int checkedItem;
			private String[] strs;
			public SomeDialog(int checkedItem, String[] strs) {
				this.checkedItem = checkedItem;
				this.strs = strs;
			}

		    @Override
		    public Dialog onCreateDialog(Bundle savedInstanceState) {
		        return new AlertDialog.Builder(getActivity())
		        .setIcon(null)
		        .setTitle(R.string.please_select_property)
		        .setSingleChoiceItems(strs, checkedItem, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		                /* User clicked on a radio button do some stuff */
		            		Value value = values.get(whichButton);
		            		filter.setCurValue(value.getValue_name());
		            		adapter.notifyDataSetChanged();
		            		
		            		ParameterFilter parameterFilter = new ParameterFilter();
		            		parameterFilter.setFliter_id(filter.getFliter_id());
		            		parameterFilter.setValue_id(value.getValue_id());
		            		if(parameterFilters.size() == 0) {
		            			parameterFilters.add(parameterFilter);
		            		}else {
		            			for(int i=0; i<parameterFilters.size(); i++) {
			            			if(parameterFilters.get(i).getFliter_id() == parameterFilter.getFliter_id()) {
			            				parameterFilters.remove(i);
			            				parameterFilters.add(i, parameterFilter);
			            			}else {
			            				parameterFilters.add(parameterFilter);
			            			}
			            		}
		            		}
		            		
		            		dismiss();
		            }
		        }).create();
		    }
		}
		
}
