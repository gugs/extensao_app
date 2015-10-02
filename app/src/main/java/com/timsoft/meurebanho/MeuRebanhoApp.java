package com.timsoft.meurebanho;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MeuRebanhoApp extends Application {

    private static final String LOG_TAG = "MeuRebanhoApp";
    public static final String TEMPORARY_FILE_PREFIX = "TMP_";

    private static Context mContext;

    public static final String DEFAULT_IMAGE_FILE_EXTENSION = ".jpg";

    public static final String ACTION = "ACTION";

    public static final String ACTION_ADD = "ACTION_ADD";
    public static final String ACTION_EDIT = "ACTION_EDIT";

    private static final int BUFFER = 2048;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static View.OnClickListener getOnClickListenerForBtnSetDate(final Context context, final TextView tvDate) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        updateDate(tvDate, year, monthOfYear, dayOfMonth);
                    }
                };

                //TODO: Se já houver uma data digitada, exibir esta data no calendário, default está como data de hoje
                DatePickerDialog d = new DatePickerDialog(context, listener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        };
    }

    public static void updateDate(TextView tv, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int monthOfYear = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        updateDate(tv, year, monthOfYear, dayOfMonth);
    }

    @SuppressWarnings("deprecation")
    public static void updateDate(TextView tv, int year, int monthOfYear, int dayOfMonth) {
        DateFormat f = MainActivity.getDateFormat();
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        tv.setTextColor(getContext().getResources().getColor(android.R.color.primary_text_light));
        tv.setText(f.format(c.getTime()));
    }

    public static View.OnClickListener getOnClickListenerForBtnClearDate(final TextView tvDate, final int hint_id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setTextColor(getContext().getResources().getColor(R.color.hintTextAppearance));
                tvDate.setText(getContext().getResources().getString(hint_id));
            }
        };
    }
}
