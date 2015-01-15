package com.timsoft.meurebanho.animal.model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.timsoft.meurebanho.R;

public class AnimalArrayAdapter extends ArrayAdapter<Animal> {
	private final Context context;
	private final List<Animal> values;

	public AnimalArrayAdapter(Context context, List<Animal> values) {
		super(context, R.layout.animal_list, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.animal_list, parent, false);
		
		TextView textViewId = (TextView) rowView.findViewById(R.id.animal_id);
		textViewId.setText(values.get(position).getId());
		
		TextView textViewName = (TextView) rowView.findViewById(R.id.animal_name);
		textViewName.setText(values.get(position).getName());
		
		return rowView;
	}
}
