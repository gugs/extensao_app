package com.timsoft.meurebanho.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.model.Fazenda;

public class FazendaArrayAdapter extends ArrayAdapter<Fazenda> {
	private final Context context;
	private final List<Fazenda> values;

	public FazendaArrayAdapter(Context context, List<Fazenda> values) {
		super(context, R.layout.list_fazenda, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.list_fazenda, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.nome_fazenda);
		textView.setText(values.get(position).getDescricao());
		
		return rowView;
	}
}
