package com.timsoft.meurebanho.calving.activity;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.model.Animal;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalvingArrayAdapter extends BaseAdapter
{

    private final List<Animal> animais;
    private Activity act;

    public CalvingArrayAdapter(Activity act, List<Animal> animais) {
        this.animais = animais;
        this.act = act;
    }

    public Activity getAct() {
        return act;
    }

    @Override
    public int getCount() {
        return animais.size();
    }

    @Override
    public Object getItem(int position) {
        return animais.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.animal_list_adapter, parent, false);
        Animal a = animais.get(position);

        TextView txEar = (TextView) view.findViewById(R.id.ca_ear_tag);
        txEar.setText(a.getEarTag());

        CharSequence dataFormat = android.text.format.DateFormat.format("dd/MM/yyyy", a.getBirthDate());

        TextView txBirth = (TextView) view.findViewById(R.id.ca_birth_date);
        txBirth.setText(dataFormat);

        Long dateCurrent;
        dateCurrent = (Calendar.getInstance().getTime().getTime() - a.getBirthDate().getTime());

        TextView txAfterBirht = (TextView) view.findViewById(R.id.ca_days_after);
        txAfterBirht.setText("Parido a "+TimeUnit.DAYS.convert(dateCurrent, TimeUnit.MILLISECONDS)+" dias.");
        return view;
    }
}
