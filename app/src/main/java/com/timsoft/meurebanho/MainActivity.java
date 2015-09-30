package com.timsoft.meurebanho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.timsoft.meurebanho.animal.activity.AnimalListActivity;
import com.timsoft.meurebanho.infra.db.DBHandler;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private static final String FIRST_RUN = "FIRST_RUN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_full_name),
                MODE_PRIVATE);

        boolean recreateDB = false;

        if (prefs.getBoolean(FIRST_RUN, true)) {
            recreateDB = true;
            prefs.edit().putBoolean(FIRST_RUN, false).commit();
        }

        if (recreateDB) {
            if (!deleteDatabase(DBHandler.DB_NAME)) {
                Log.w(LOG_TAG, "Could not delete DB: " + DBHandler.DB_NAME);
            }
            populateDefaultData();
        }

        Intent intent = new Intent(this, AnimalListActivity.class);
        startActivity(intent);
        finish();
    }

    private void populateDefaultData() {
        DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance();
        DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance();

        specieDatasource.open();
        for (Specie e : Specie.getDefaultSpecies()) {
            specieDatasource.create(e);
        }
        specieDatasource.close();

        raceDatasource.open();
        for (Race r : Race.getDefaultRaces()) {
            raceDatasource.create(r);
        }
        raceDatasource.close();
    }

    public static DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }

    public static String getFormatedDate(Date d) {
        return d == null ? "" : MainActivity.getDateFormat().format(d);
    }
}
