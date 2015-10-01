package com.timsoft.meurebanho.treatment.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.infra.db.DBAdapter;
import com.timsoft.meurebanho.treatment.model.Treatment;

import java.util.ArrayList;
import java.util.List;

public class DBTreatmentAdapter extends DBAdapter<Treatment> {

    private static final String LOG_TAG = "DBTreatmentAdapter";

    public static final String TABLE_NAME = "treatment";

    public static final String ID = "id";
    public static final String ANIMAL_ID = "animal_id";
    public static final String DATE = "date";
    public static final String REASON = "reason";
    public static final String MEDICATION = "medication";
    public static final String WITHDRAWAL_PERIOD = "withdrawal_period";
    public static final String COST = "cost";
    public static final String NOTES = "notes";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + ANIMAL_ID + " integer not null, "
            + DATE + " integer not null, "
            + REASON + " text not null,"
            + MEDICATION + " text not null,"
            + WITHDRAWAL_PERIOD + " integer, "
            + COST + " real, "
            + NOTES + " text"
            + ");";

    private static DBTreatmentAdapter mInstance;

    private DBTreatmentAdapter() {
        super();
    }

    public static DBTreatmentAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBTreatmentAdapter();
        }
        return mInstance;
    }

    @Override
    public Treatment create(Treatment treatment) {
        Log.d(LOG_TAG, "Including treatment: " + treatment.toString());
        ContentValues values = new ContentValues();

        values.put(ANIMAL_ID, treatment.getAnimalId());
        values.put(DATE, treatment.getDate().getTime());
        values.put(REASON, treatment.getReason());
        values.put(MEDICATION, treatment.getMedication());
        values.put(WITHDRAWAL_PERIOD, treatment.getWithdrawalPeriod());
        values.put(COST, treatment.getCost());
        values.put(NOTES, treatment.getNotes());

        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return get(treatment.getId());
    }

    @Override
    public void delete(Treatment t) {
        Log.d(LOG_TAG, "Excluding treatment: " + t.getId());
        if (get(t.getId()) == null) {
            throw new RuntimeException("Solicitação de exclusão de treatmento inexistente: " + t.getId());
        }
        database.delete(TABLE_NAME, ID + " = " + t.getId(), null);
    }

    @Override
    public Treatment cursorTo(Cursor cursor) {
        return new Treatment(
                cursor.getInt(0),
                cursor.getInt(1),
                longToDate(cursor, 2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5),
                cursor.getDouble(6),
                cursor.getString(7)
        );
    }

    @Override
    public List<Treatment> list() {
        Log.d(LOG_TAG, "Obtendo treatments");
        List<Treatment> listTreatments = new ArrayList<Treatment>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listTreatments.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listTreatments;
    }

    public List<Treatment> list(int animalId) {
        Log.d(LOG_TAG, "Getting treatments");
        List<Treatment> listTreatments = new ArrayList<Treatment>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + ANIMAL_ID + "=" + animalId + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listTreatments.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listTreatments;
    }

    @Override
    public Treatment get(int idTreatment) {
        Log.d(LOG_TAG, "Getting Treatment: " + idTreatment);
        String query = "select * from "
                + TABLE_NAME
                + " where "
                + TABLE_NAME + "." + ID + " = " + idTreatment;

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