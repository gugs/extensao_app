package com.timsoft.meurebanho.weighting.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.infra.db.DBAdapter;
import com.timsoft.meurebanho.weighting.model.Weighting;

import java.util.ArrayList;
import java.util.List;

public class DBWeightingAdapter extends DBAdapter<Weighting> {

    private static final String LOG_TAG = "DBWeightingAdapter";

    public static final String TABLE_NAME = "weighting";

    public static final String ID = "id";
    public static final String ANIMAL_ID = "animal_id";
    public static final String DATE = "date";
    public static final String WEIGHT = "weight";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + ANIMAL_ID + " integer not null, "
            + DATE + " integer not null, "
            + WEIGHT + " real not null"
            + ");";

    private static DBWeightingAdapter mInstance;

    private DBWeightingAdapter() {
        super();
    }

    public static DBWeightingAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBWeightingAdapter();
        }
        return mInstance;
    }

    @Override
    public Weighting create(Weighting weighting) {
        Log.d(LOG_TAG, "Including weighting: " + weighting.toString());
        ContentValues values = new ContentValues();

        values.put(ANIMAL_ID, weighting.getAnimalId());
        values.put(DATE, weighting.getDate().getTime());
        values.put(WEIGHT, weighting.getWeight());

        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return get(weighting.getId());
    }

    @Override
    public void delete(Weighting t) {
        Log.d(LOG_TAG, "Excluding weighting: " + t.getId());
        if (get(t.getId()) == null) {
            throw new RuntimeException("Solicitação de exclusão de weightingo inexistente: " + t.getId());
        }
        database.delete(TABLE_NAME, ID + " = " + t.getId(), null);
    }

    @Override
    public Weighting cursorTo(Cursor cursor) {
        return new Weighting(
                cursor.getInt(0),
                cursor.getInt(1),
                longToDate(cursor, 2),
                cursor.getDouble(3)
        );
    }

    @Override
    public List<Weighting> list() {
        Log.d(LOG_TAG, "Obtendo weightings");
        List<Weighting> listWeightings = new ArrayList<Weighting>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listWeightings.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listWeightings;
    }

    public List<Weighting> list(int animalId) {
        Log.d(LOG_TAG, "Getting weightings");
        List<Weighting> listWeightings = new ArrayList<Weighting>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + ANIMAL_ID + "=" + animalId + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listWeightings.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listWeightings;
    }

    @Override
    public Weighting get(int idWeighting) {
        Log.d(LOG_TAG, "Getting Weighting: " + idWeighting);
        String query = "select * from "
                + TABLE_NAME
                + " where "
                + TABLE_NAME + "." + ID + " = " + idWeighting;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursorTo(cursor);
        }
        return null;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}