package com.timsoft.meurebanho.specie.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.infra.db.DBAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.util.ArrayList;
import java.util.List;

public class DBSpecieAdapter extends DBAdapter<Specie> {

    private static final String LOG_TAG = "DBEspecieAdapter";

    public static final String TABLE_NAME = "specie";

    public static final String ID = "id";
    public static final String DESCRIPTION = "description";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + DESCRIPTION + " text not null);";

    private static DBSpecieAdapter mInstance;

    private DBSpecieAdapter() {
        super();
    }

    public static DBSpecieAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBSpecieAdapter();
        }
        return mInstance;
    }

    @Override
    public Specie create(Specie Especie) {
        Log.d(LOG_TAG, "Incluindo Especie: " + Especie.toString());
        ContentValues values = new ContentValues();
        values.put(ID, Especie.getId());
        values.put(DESCRIPTION, Especie.getDescription());

        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return get(Especie.getId());
    }

    @Override
    public void delete(Specie e) {
        Log.d(LOG_TAG, "Excluindo Especie: " + e.getId());
        database.delete(TABLE_NAME, ID + " = " + e.getId(), null);
    }

    @Override
    public Specie cursorTo(Cursor cursor) {
        Specie Especie = new Specie(cursor.getInt(0), cursor.getString(1));
        return Especie;
    }

    @Override
    public List<Specie> list() {
        Log.d(LOG_TAG, "Obtendo Especies");
        List<Specie> listaEspecie = new ArrayList<Specie>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listaEspecie.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listaEspecie;
    }

    @Override
    public Specie get(int idEspecie) {
        Log.d(LOG_TAG, "Obtendo Especie: " + idEspecie);
        String query = "select " +
                TABLE_NAME + "." + ID + ", " +
                TABLE_NAME + "." + DESCRIPTION +

                " from " + TABLE_NAME +

                " where " +
                TABLE_NAME + "." + ID + " = " + idEspecie;

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
