package com.timsoft.meurebanho.animal.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.timsoft.meurebanho.R;

import java.util.List;

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
		//FIXME: Warning abaixo
		View rowView = inflater.inflate(R.layout.animal_list, parent, false);
		Animal a = values.get(position);
		
		((TextView) rowView.findViewById(R.id.animal_id))
			.setText(a.getIdToDisplay());
		
		((TextView) rowView.findViewById(R.id.animal_name))
			.setText(a.getName());
		
		((TextView) rowView.findViewById(R.id.animal_sex))
			.setText(a.getSexToDisplay(context));
		
		((TextView) rowView.findViewById(R.id.animal_sex))
			.setText(a.getAgeInMonthsToDisplay(context));
		
		return rowView;
	}
}
