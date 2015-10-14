package com.timsoft.meurebanho.milking.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.infra.db.DBAdapter;
import com.timsoft.meurebanho.milking.model.Milking;

import java.util.ArrayList;
import java.util.List;

public class DBMilkingAdapter extends DBAdapter<Milking> {

    private static final String LOG_TAG = "DBMilkingAdapter";

    public static final String TABLE_NAME = "milking";

    public static final String ID = "id";
    public static final String ANIMAL_ID = "animal_id";
    public static final String DATE = "date";
    public static final String AMOUNT = "amount";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + ANIMAL_ID + " integer not null, "
            + DATE + " integer not null, "
            + AMOUNT + " real not null"
            + ");";

    private static DBMilkingAdapter mInstance;

    private DBMilkingAdapter() {
        super();
    }

    public static DBMilkingAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBMilkingAdapter();
        }
        return mInstance;
    }

    @Override
    public Milking create(Milking milking) {
        Log.d(LOG_TAG, "Including milking: " + milking.toString());
        ContentValues values = new ContentValues();

        values.put(ANIMAL_ID, milking.getAnimalId());
        values.put(DATE, milking.getDate().getTime());
        values.put(AMOUNT, milking.getAmount());

        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return get(milking.getId());
    }

    @Override
    public void delete(Milking t) {
        Log.d(LOG_TAG, "Excluding milking: " + t.getId());
        if (get(t.getId()) == null) {
            throw new RuntimeException("Solicitação de exclusão de milkingo inexistente: " + t.getId());
        }
        database.delete(TABLE_NAME, ID + " = " + t.getId(), null);
    }

    @Override
    public Milking cursorTo(Cursor cursor) {
        return new Milking(
                cursor.getInt(0),
                cursor.getInt(1),
                longToDate(cursor, 2),
                cursor.getDouble(3)
        );
    }

    @Override
    public List<Milking> list() {
        Log.d(LOG_TAG, "Obtendo milkings");
        List<Milking> listMilkings = new ArrayList<Milking>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listMilkings.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listMilkings;
    }

    public List<Milking> list(int animalId) {
        Log.d(LOG_TAG, "Getting milkings");
        List<Milking> listMilkings = new ArrayList<Milking>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + ANIMAL_ID + "=" + animalId + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listMilkings.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listMilkings;
    }

    @Override
    public Milking get(int idMilking) {
        Log.d(LOG_TAG, "Getting Milking: " + idMilking);
        String query = "select * from "
                + TABLE_NAME
                + " where "
                + TABLE_NAME + "." + ID + " = " + idMilking;

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