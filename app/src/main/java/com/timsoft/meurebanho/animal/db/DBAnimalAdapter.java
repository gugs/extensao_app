package com.timsoft.meurebanho.animal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.infra.db.DBAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBAnimalAdapter extends DBAdapter<Animal> {
	
	private static final String LOG_TAG = "DBAnimalAdapter";
	
	public static final String TABLE_NAME = "animal";
	
	public static final String ID = "id";
	public static final String SPECIE_ID = "specie_id";
	public static final String RACE_ID = "race_id";
	
	public static final String SEX = "sex";
	public static final String NAME = "name";
	public static final String EAR_TAG = "ear_tag";
	
	public static final String BIRTH_DATE = "birth_date";
	public static final String AQUISITION_DATE = "aquisition_date";
	public static final String SELL_DATE = "sell_date";
	
	public static final String DEATH_DATE = "death_date";
	public static final String DEATH_REASON = "death_reason";
	
	public static final String AQUISITION_VALUE = "aquisition_value";
	public static final String SELL_VALUE = "sell_value";
	
	public static final String FATHER_ID = "father_id";
	public static final String MOTHER_ID = "mother_id";
	
	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ SPECIE_ID + " integer, "
			+ RACE_ID + " integer, "
			
			+ SEX + " text, "
			+ NAME + " text, "
			+ EAR_TAG + " text, "
			
			+ BIRTH_DATE + " integer, "
			+ AQUISITION_DATE + " integer, "
			+ SELL_DATE + " integer, "
			
			+ DEATH_DATE + " integer, "
			+ DEATH_REASON + " text, "
			
			+ AQUISITION_VALUE + " real, "
			+ SELL_VALUE + " real, "
			
			+ FATHER_ID + " integer, "
			+ MOTHER_ID + " integer "
			
			+ ");";
	
	private static DBAnimalAdapter mInstance;
	
	private DBAnimalAdapter(Context context) {
		super(context);
	}
	
	public static DBAnimalAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBAnimalAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Animal create(Animal animal) {
		Log.d(LOG_TAG, "Including animal: " + animal.toString());
		ContentValues values = new ContentValues();
		values.put(ID, animal.getId());
		values.put(SPECIE_ID, animal.getSpecieId());
		values.put(RACE_ID, animal.getRaceId());
		
		values.put(SEX, animal.getSex());
		values.put(NAME, animal.getName());
		values.put(EAR_TAG, animal.getEarTag());
		
		if(animal.getBirthDate() != null) {
			values.put(BIRTH_DATE, animal.getBirthDate().getTime());
		}
		
		if(animal.getAcquisitionDate() != null) {
			values.put(AQUISITION_DATE, animal.getAcquisitionDate().getTime());
		}
		
		if(animal.getSellDate() != null) {
			values.put(SELL_DATE, animal.getSellDate().getTime());
		}
		
		if(animal.getDeathDate() != null) {
			values.put(DEATH_DATE, animal.getDeathDate().getTime());
		}
		values.put(DEATH_REASON, animal.getDeathReason());
		
		values.put(AQUISITION_VALUE, animal.getAcquisitionValue());
		values.put(SELL_VALUE, animal.getSellValue());
		database.insert(TABLE_NAME, null, values);
		return get(animal.getId());
	}

	@Override
	public void delete(Animal a) {
		Log.d(LOG_TAG, "Excluindo animal: " + a.getId());
		if(get(a.getId()) == null) {
			throw new RuntimeException("Solicitação de exclusão de animal inexistente: " + a.getId());
		}
		database.delete(TABLE_NAME, ID + " = " + a.getId(), null);
	}
	
	@Override
	public Animal cursorTo(Cursor cursor) {
		Animal Animal = new Animal(
				cursor.getInt(0),
				cursor.getInt(1),
				cursor.getInt(2),
				
				cursor.getString(3),
				cursor.getString(4),
				cursor.getString(5),
				
				longToDate(cursor, 6),
				longToDate(cursor, 7),
				longToDate(cursor, 8),
				
				longToDate(cursor, 9),
				cursor.getString(10),
				
				cursor.getDouble(11),
				cursor.getDouble(12));
		return Animal;
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

	@Override
	public Animal get(int idAnimal) {
		Log.d(LOG_TAG, "Obtendo Animal: " + idAnimal);
		String query = 	"select * from " 
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
		String query = 	"select * from " 
						+ TABLE_NAME
						+ " order by " + ID + " desc";
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor).getId() + 1;
		} else {
			return 1;
		}
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
}
