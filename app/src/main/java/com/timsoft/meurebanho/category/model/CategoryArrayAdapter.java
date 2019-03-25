package com.timsoft.meurebanho.category.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.specie.model.Specie;

import java.util.List;

public class CategoryArrayAdapter extends ArrayAdapter<Category> {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CategoryArrayAdapter";

    public CategoryArrayAdapter(Context context, List<Category> values) {
        super(context, R.layout.category_list, values);
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
        if (convertView == null)
            //convertView = View.inflate(getContext(),
            //        R.layout.category_list,
            //        null);
        ((TextView) convertView.findViewById(R.id.specie_list_name)).setText(getItem(position).getDescription());

        return convertView;
    }
}
