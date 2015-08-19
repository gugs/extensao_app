package com.timsoft.meurebanho.farm.model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.timsoft.meurebanho.R;

public class FarmArrayAdapter extends ArrayAdapter<Farm> {
	//FIXME: Utilizar a solução do SpecieArrayAdapter
	
	@SuppressWarnings("unused")
	private final Context context;
	private final List<Farm> values;

	public FarmArrayAdapter(Context context, List<Farm> values) {
		super(context, R.layout.farm_list, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.farm_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.farm_name);
		textView.setText(values.get(position).getDescription());
		
		return rowView;
	}
}
