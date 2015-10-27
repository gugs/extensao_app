package com.timsoft.meurebanho.infra.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.milking.db.DBMilkingAdapter;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.weighting.db.DBWeightingAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DatabaseHandler";

    public static final String DB_NAME = "meurebanho.db";
    public static final int DB_VERSION = 2;

    private static DBHandler mInstance;
    private List<String> listTableCreate;
    private List<String> listTableName;

    private DBHandler() {
        super(MeuRebanhoApp.getContext(), DB_NAME, null, DB_VERSION);

        listTableCreate = new ArrayList<>();
        listTableName = new ArrayList<>();

        listTableCreate.add(DBAnimalAdapter.TABLE_CREATE);
        listTableName.add(DBAnimalAdapter.TABLE_NAME);

        listTableCreate.add(DBRaceAdapter.TABLE_CREATE);
        listTableName.add(DBRaceAdapter.TABLE_NAME);

        listTableCreate.add(DBSpecieAdapter.TABLE_CREATE);
        listTableName.add(DBSpecieAdapter.TABLE_NAME);

        listTableCreate.add(DBTreatmentAdapter.TABLE_CREATE);
        listTableName.add(DBTreatmentAdapter.TABLE_NAME);

        listTableCreate.add(DBWeightingAdapter.TABLE_CREATE);
        listTableName.add(DBWeightingAdapter.TABLE_NAME);

        listTableCreate.add(DBMilkingAdapter.TABLE_CREATE);
        listTableName.add(DBMilkingAdapter.TABLE_NAME);
    }

    public static DBHandler getInstance() {
        if (mInstance == null) {
            mInstance = new DBHandler();
        }
        return mInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onOpen");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate");
        for (String tableCreate : listTableCreate) {
            Log.d(LOG_TAG, tableCreate);
            db.execSQL(tableCreate);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade");
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(DBWeightingAdapter.TABLE_CREATE);
            db.execSQL(DBMilkingAdapter.TABLE_CREATE);
        } else {
            throw new RuntimeException(String.format("Unexpected database updgrade, from %s to %s",
                    oldVersion, newVersion));
        }
    }

    public void forceDrop(SQLiteDatabase db) {
        for (String tableName : listTableName) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }
    }
}
