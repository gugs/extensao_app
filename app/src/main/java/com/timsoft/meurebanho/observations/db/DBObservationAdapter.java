package com.timsoft.meurebanho.observations.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.infra.db.DBAdapter;
import com.timsoft.meurebanho.observations.model.Observation;

import java.util.ArrayList;
import java.util.List;

public class DBObservationAdapter extends DBAdapter<Observation>
{
    private static final String LOG_TAG = "DBObservationAdapter";

    public static final String TABLE_NAME = "Observation";

    public static final String ID = "id";
    public static final String ANIMAL_ID = "animal_id";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "obsDescription";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + ANIMAL_ID + " integer not null, "
            + DATE + " integer not null, "
            + DESCRIPTION + " text not null"
            + ");";

    private static DBObservationAdapter mInstance;

    private DBObservationAdapter() {
        super();
    }

    public static DBObservationAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBObservationAdapter();
        }
        return mInstance;
    }

    @Override
    public Observation create(Observation obs) {
        Log.d(LOG_TAG, "Including Observation: " + obs.toString());
        ContentValues values = new ContentValues();

        values.put(ANIMAL_ID, obs.getAnimalID());
        values.put(DATE, obs.getDateOccurrence().getTime());
        values.put(DESCRIPTION, obs.getObsDescription());

        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return get(obs.getIdObservation());
    }

    @Override
    public void delete(Observation t) {
        Log.d(LOG_TAG, "Excluding observation: " + t.getIdObservation());
        if (get(t.getIdObservation()) == null) {
            throw new RuntimeException("Solicitação de exclusão de milkingo inexistente: "
                    + t.getIdObservation());
        }
        database.delete(TABLE_NAME, ID + " = " + t.getIdObservation(), null);
    }

    @Override
    public Observation cursorTo(Cursor cursor) {
        return new Observation(
                cursor.getInt(0),
                cursor.getInt(1),
                longToDate(cursor, 2),
                cursor.getString(3)
        );
    }

    @Override
    public List<Observation> list() {
        Log.d(LOG_TAG, "Obtendo observation");
        List<Observation> listObservation = new ArrayList<Observation>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listObservation.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listObservation;
    }

    public List<Observation> list(int observationId) {
        Log.d(LOG_TAG, "Getting observation");
        List<Observation> listObservation = new ArrayList<Observation>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " +
                ANIMAL_ID + "=" + observationId + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listObservation.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listObservation;
    }

    @Override
    public Observation get(int idObservation) {
        Log.d(LOG_TAG, "Getting Observation: " + idObservation);
        String query = "select * from "
                + TABLE_NAME
                + " where "
                + TABLE_NAME + "." + ID + " = " + idObservation;

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
