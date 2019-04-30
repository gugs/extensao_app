package com.timsoft.meurebanho.animal.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.infra.db.DBAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBAnimalAdapter extends DBAdapter<Animal> {

    private static final String LOG_TAG = "DBAnimalAdapter";

    public static final String TABLE_NAME = "animal";

    public static final String ID = "id";
    public static final String CATEGORY_ID = "category_id";
    public static final String SPECIE_ID = "specie_id";
    public static final String RACE_ID = "race_id";

    public static final String FATHER_ID = "father_id";
    public static final String MOTHER_ID = "mother_id";


    public static final String SEX = "sex";
    public static final String NAME = "name";
    public static final String EAR_TAG = "ear_tag";
    public static final String PATRIMONY_NUMBER = "patrimony";

    public static final String BIRTH_DATE = "birth_date";
    public static final String ACQUISITION_DATE = "acquisition_date";
    public static final String SALE_DATE = "sale_date";

    public static final String DEATH_DATE = "death_date";
    public static final String DEATH_REASON = "death_reason";

    public static final String RETIRE_DATE = "retire_date";

    public static final String ACQUISITION_VALUE = "acquisition_value";
    public static final String SALE_VALUE = "sale_value";

    public static final String SELLER_NAME = "seller_name";

    public static final String BUYER_NAME = "buyer_name";

    public static final String SALE_NOTES = "sale_notes";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
            + ID + " integer primary key, "
            + CATEGORY_ID + " integer not null, "
            + SPECIE_ID + " integer not null, "
            + RACE_ID + " integer not null, "

            + FATHER_ID + " integer, "
            + MOTHER_ID + " integer, "

            + SEX + " text not null, "
            + NAME + " text, "
            + EAR_TAG + " text, "
            + PATRIMONY_NUMBER + " text, "

            + BIRTH_DATE + " integer not null, "
            + ACQUISITION_DATE + " integer, "
            + SALE_DATE + " integer, "

            + DEATH_DATE + " integer, "
            + DEATH_REASON + " text, "

            + RETIRE_DATE + " integer, "

            + ACQUISITION_VALUE + " real, "
            + SALE_VALUE + " real, "

            + SELLER_NAME + " text, "
            + BUYER_NAME + " text, "
            + SALE_NOTES + " text"

            + ");";

    private static DBAnimalAdapter mInstance;

    private DBAnimalAdapter() {
        super();
    }

    public static DBAnimalAdapter getInstance() {
        if (mInstance == null) {
            mInstance = new DBAnimalAdapter();
        }
        return mInstance;
    }

    @Override
    public Animal create(Animal animal) {
        Log.d(LOG_TAG, "Including animal: " + animal.toString());
        ContentValues values = new ContentValues();
        values.put(ID, animal.getId());
        // Category shift
        values.put(CATEGORY_ID, animal.getCategoryId());
        values.put(SPECIE_ID, animal.getSpecieId());
        values.put(RACE_ID, animal.getRaceId());

        values.put(FATHER_ID, animal.getFather());
        values.put(MOTHER_ID, animal.getMother());

        values.put(SEX, animal.getSex());
        values.put(NAME, animal.getName());
        values.put(EAR_TAG, animal.getEarTag());
        values.put(PATRIMONY_NUMBER, animal.getPatrimonyNumber());

        if (animal.getBirthDate() != null) {
            values.put(BIRTH_DATE, animal.getBirthDate().getTime());
        }

        if (animal.getAcquisitionDate() != null) {
            values.put(ACQUISITION_DATE, animal.getAcquisitionDate().getTime());
        }

        if (animal.getSaleDate() != null) {
            values.put(SALE_DATE, animal.getSaleDate().getTime());
        }

        if (animal.getDeathDate() != null) {
            values.put(DEATH_DATE, animal.getDeathDate().getTime());
            values.put(DEATH_REASON, animal.getDeathReason());
        }

        if (animal.getRetireDate() != null) {
            values.put(RETIRE_DATE, animal.getRetireDate().getTime());
        }

        values.put(ACQUISITION_VALUE, animal.getAcquisitionValue());
        values.put(SALE_VALUE, animal.getSaleValue());

        values.put(SELLER_NAME, animal.getSellerName());
        values.put(BUYER_NAME, animal.getBuyerName());
        values.put(SALE_NOTES, animal.getSaleNotes());


        try {
            database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            Toast.makeText(MeuRebanhoApp.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return get(animal.getId());
    }

    @Override
    public void delete(Animal a) {
        Log.d(LOG_TAG, "Excluding animal: " + a.getId());
        if (get(a.getId()) == null) {
            throw new RuntimeException("Request to exclude inexistent animal: " + a.getId());
        }
        database.delete(TABLE_NAME, ID + " = " + a.getId(), null);
    }

    @Override
    public Animal cursorTo(Cursor cursor) {
        return new Animal(
                //ID
                cursor.getInt(0),

                //CATEGORY_ID
                cursor.getInt(1),

                //SPECIE_ID
                cursor.getInt(2),

                //RACE_ID
                cursor.getInt(3),

                //FATHER_ID
                cursor.getInt(4),

                //MOTHER_ID
                cursor.getInt(5),

                //SEX
                cursor.getString(6),

                //NAME
                cursor.getString(7),

                //EAR_TAG
                cursor.getString(8),

                //PATRIMONY_NUMBER
                cursor.getString(9),

                //BIRTH_DATE
                longToDate(cursor, 10),

                //ACQUISITION_DATE
                longToDate(cursor, 11),

                //SALE_DATE
                longToDate(cursor, 12),

                //DEATH_DATE
                longToDate(cursor, 13),

                //DEATH_REASON
                cursor.getString(14),

                //RETIRE_DATE
                longToDate(cursor, 15),

                //ACQUISITION_VALUE
                cursor.getDouble(16),

                //SALE_VALUE
                cursor.getDouble(17),

                //SELLER_NAME
                cursor.getString(18),

                //BUYER_NAME
                cursor.getString(19),

                //SALE_NOTES
                cursor.getString(20)
        );
    }

    @Override
    public List<Animal> list() {
        Log.d(LOG_TAG, "Obtendo Animals");
        List<Animal> listaAnimal = new ArrayList<Animal>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listaAnimal.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listaAnimal;
    }

    public List<Animal> list(int specieID) {
        Log.d(LOG_TAG, "Obtendo Animals");
        List<Animal> listaAnimal = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + SPECIE_ID + " = " + specieID + " order by " + ID, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listaAnimal.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listaAnimal;
    }

    @Override
    public Animal get(int idAnimal) {
        Log.d(LOG_TAG, "Obtendo Animal: " + idAnimal);
        String query = "select * from "
                + TABLE_NAME
                + " where "
                + TABLE_NAME + "." + ID + " = " + idAnimal;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursorTo(cursor);
        }
        return null;
    }

    public int getNextId() {
        String query = "select * from "
                + TABLE_NAME
                + " order by " + ID + " desc";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursorTo(cursor).getId() + 1;
        } else {
            return 1;
        }
    }

    /**
     * Sex letter for query filter. M for male and F for female
     * @param sexType M for male, F for Female
     * @return animalListBySex
     */

    public List<Animal> getAnimalsBySex(String sexType, int specie)
    {
        Log.d(LOG_TAG, "Obtendo Animal Male List");
        List<Animal> listaAnimal = new ArrayList<>();
        String query = "select * from "
                + TABLE_NAME
                + " where "
                + TABLE_NAME + "." + SEX +"= '"+sexType+
                "' AND "+ TABLE_NAME + "." +SPECIE_ID+"="+String.valueOf(specie)+";";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listaAnimal.add(cursorTo(cursor));
            } while (cursor.moveToNext());
        }
        return listaAnimal;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
