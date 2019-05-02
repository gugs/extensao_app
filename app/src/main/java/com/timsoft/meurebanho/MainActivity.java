package com.timsoft.meurebanho;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.timsoft.meurebanho.animal.activity.AnimalListActivity;
import com.timsoft.meurebanho.category.db.DBCategoryAdapter;
import com.timsoft.meurebanho.category.model.Category;
import com.timsoft.meurebanho.infra.FileUtils;
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



        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }

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

        FileUtils.deleteTemporaryImageFiles();

        Intent intent = new Intent(this, AnimalListActivity.class);
        startActivity(intent);
        finish();
    }

    private void populateDefaultData() {
        DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance();
        DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance();
        DBCategoryAdapter categoryDatasource = DBCategoryAdapter.getInstance();

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

        categoryDatasource.open();
        for (Category c : Category.getDefaultCategory()) {
            categoryDatasource.create(c);
        }
        categoryDatasource.close();
    }

    public static DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }

    public static String getFormatedDate(Date d) {
        return d == null ? "" : MainActivity.getDateFormat().format(d);
    }
    private boolean checkIfAlreadyhavePermission() {
        int Storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int aCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (Storage != PackageManager.PERMISSION_GRANTED
                || aCamera != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
