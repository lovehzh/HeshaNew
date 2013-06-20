package com.hesha.adapter;

import java.util.List;

import com.hesha.R;
import com.hesha.bean.choice.Intention;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class IntentionAdapter extends ArrayAdapter<Intention> {

        public IntentionAdapter(Activity activity, List<Intention> intentions) {
            super(activity, 0, intentions);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	    Activity activity = (Activity) getContext();
            // Inflate the views from XML
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                rowView = inflater.inflate(R.layout.grid_intention, null);
            }
            Intention intention = getItem(position);
            TextView tvIntention = (TextView)rowView.findViewById(R.id.tv_intention_name);
            tvIntention.setText(intention.getIntention_name());
            
            return rowView;
        }

}