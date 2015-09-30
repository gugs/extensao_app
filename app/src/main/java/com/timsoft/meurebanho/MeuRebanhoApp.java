package com.timsoft.meurebanho;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MeuRebanhoApp extends Application {

    private static final String LOG_TAG = "MeuRebanhoApp";

    private static Context mContext;

    public static final String DEFAULT_IMAGE_FILE_EXTENSION = ".jpg";

    public static final String ACTION = "ACTION";

    public static final String ACTION_ADD = "ACTION_ADD";
    public static final String ACTION_EDIT = "ACTION_EDIT";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static File getMediaStorageDir() {
//        return new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), getContext().getResources().getString(R.string.app_full_name));
        File f = new File(getApplicationStorageDir(), "pictures");

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para armazenamento de mídia em: " + f.toString();
                Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
    }

    public static File getBackupStorageDir() {
        File f = new File(getApplicationStorageDir(), "backup");

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para cópia de segurança da aplicação: " + f.toString();
                Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
    }

    public static File getApplicationStorageDir() {
        File f = new File(Environment.getExternalStorageDirectory(), getContext().getResources().getString(R.string.app_short_name));

        if (!f.exists()) {
            if (!f.mkdir()) {
                String m = "Não foi possível criar o diretório para armazenamento de dados da aplicação em: " + f.toString();
                Toast.makeText(getContext(), m, Toast.LENGTH_LONG).show();
                throw new RuntimeException(m);
            }
        }

        return f;
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

    public static void copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    public static void rescanApplicationStorageDir() {
        rescanApplicationStorageDir(getApplicationStorageDir().toString());
    }

    private static void rescanApplicationStorageDir(String dest) {
        // Scan files only (not folders);
        File[] files = new File(dest).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        String[] paths = new String[files.length];
        for (int co = 0; co < files.length; co++) {
            paths[co] = files[co].getAbsolutePath();
        }

        MediaScannerConnection.scanFile(getContext(), paths, null, null);

        // and now recursively scan subfolders
        files = new File(dest).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (int co = 0; co < files.length; co++) {
            rescanApplicationStorageDir(files[co].getAbsolutePath());
        }
    }
}
