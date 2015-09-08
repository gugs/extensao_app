package com.timsoft.meurebanho.race.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.timsoft.meurebanho.R;

import java.util.List;

public class RaceArrayAdapter extends ArrayAdapter<Race> {
	
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "RaceArrayAdapter";
	
	public RaceArrayAdapter(Context context, List<Race> values) {
		super(context, R.layout.race_list, values);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return initView(position, convertView);
	}
	
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }
    
    private View initView(int position, View convertView) {
        if(convertView == null)
            convertView = View.inflate(getContext(),
                                       R.layout.race_list,
                                       null);
        ((TextView)convertView.findViewById(R.id.race_list_name)).setText(getItem(position).getDescription());
        
        return convertView;
    }
}
