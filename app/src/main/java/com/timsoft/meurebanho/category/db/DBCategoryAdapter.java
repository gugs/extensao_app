package com.timsoft.meurebanho.category.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.category.model.Category;
import com.timsoft.meurebanho.infra.db.DBAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBCategoryAdapter extends DBAdapter<Category> {

    private static final String LOG_TAG = "DBCategoryAdapter";

    public static final String TABLE_NAME = "category";

    public static final String ID = "id";
    public static final String DESCRIPTION = "description";

    public static final String ANIMAL_ID = "id_animal";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + DESCRIPTION + " text not null);";

    private static DBCategoryAdapter mInstance;

    private DBCategoryAdapter() {
        super();
    }

    public static DBCategoryAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBCategoryAdapter();
        }
        return mInstance;
    }

    @Override
    public Category create(Category category) {
        Log.d(LOG_TAG, "Incluindo Categoria: " + category.toString());
        ContentValues values = new ContentValues();
        values.put(ID, category.getId());
        values.put(DESCRIPTION, category.getDescription());

        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return get(category.getId());
    }

    @Override
    public void delete(Category e) {
        Log.d(LOG_TAG, "Excluindo Categoria: " + e.getId());
        database.delete(TABLE_NAME, ID + " = " + e.getId(), null);
    }

    @Override
    public Category cursorTo(Cursor cursor) {
        Category category = new Category(cursor.getInt(0), cursor.getString(1));
        return category;
    }

    @Override
    public List<Category> list() {
        Log.d(LOG_TAG, "Obtendo Categoria");
        List<Category> listaCategory = new ArrayList<Category>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listaCategory.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listaCategory;
    }


    @Override
    public Category get(int idCategory) {
        Log.d(LOG_TAG, "Obtendo Categoria: " + idCategory);
        String query = "select " +
                TABLE_NAME + "." + ID + ", " +
                TABLE_NAME + "." + DESCRIPTION +

                " from " + TABLE_NAME +

                " where " +
                TABLE_NAME + "." + ID + " = " + idCategory;

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
