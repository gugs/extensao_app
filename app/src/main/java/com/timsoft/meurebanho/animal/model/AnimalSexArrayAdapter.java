package com.timsoft.meurebanho.animal.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.category.model.Category;

import java.util.List;

public class AnimalSexArrayAdapter extends ArrayAdapter<Animal> {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = "CategoryArrayAdapter";

    public AnimalSexArrayAdapter(Context context, List<Animal> values) {
        super(context, R.layout.animal_sex_list, values);
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
            convertView = View.inflate(getContext(),
                    R.layout.animal_sex_list,
                    null);
        ((TextView) convertView.findViewById(R.id.animal_sex_list_id)).
                setText(String.valueOf(getItem(position).getEarTag()));
        ((TextView) convertView.findViewById(R.id.animal_sex_list_name)).
                setText(" - "+getItem(position).getName());

        return convertView;
    }
}
