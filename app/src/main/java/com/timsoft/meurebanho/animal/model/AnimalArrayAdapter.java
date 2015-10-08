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

import java.util.List;

public class AnimalArrayAdapter extends ArrayAdapter<Animal> {
    private final Context context;
    private final List<Animal> values;

    public AnimalArrayAdapter(Context context, List<Animal> values) {
        super(context, R.layout.animal_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //FIXME: Warning abaixo
        View rowView = inflater.inflate(R.layout.animal_list_item, parent, false);
        Animal a = values.get(position);

        if (a.getPictureFile().exists()) {
            ((ImageView) rowView.findViewById(R.id.animal_image)).setImageBitmap(BitmapFactory.decodeFile(a.getPictureFile().getPath()));
        } else {
            ((ImageView) rowView.findViewById(R.id.animal_image)).setImageResource(R.drawable.cow_hl);
        }

        ((TextView) rowView.findViewById(R.id.animal_id))
                .setText(a.getIdToDisplay());

        TextView tvAnimalEarTag = (TextView) rowView.findViewById(R.id.animal_ear_tag);
        if(TextUtils.isEmpty(a.getEarTag())) {
            tvAnimalEarTag.setVisibility(View.GONE);
        } else {
            tvAnimalEarTag.setVisibility(View.VISIBLE);
            tvAnimalEarTag.setText(a.getEarTag());
        }

        TextView tvAnimalName = (TextView) rowView.findViewById(R.id.animal_name);
        if(TextUtils.isEmpty(a.getName())) {
            tvAnimalName.setVisibility(View.GONE);
        } else {
            tvAnimalName.setVisibility(View.VISIBLE);
            tvAnimalName.setText(a.getName());
        }

        ((TextView) rowView.findViewById(R.id.animal_sex))
                .setText(a.getSexToDisplay());

        ((TextView) rowView.findViewById(R.id.animal_age))
                .setText(a.getAgeInMonthsToDisplay());

        return rowView;
    }
}
