package com.timsoft.meurebanho.animal.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBAnimalAdapter extends DBAdapterAbstract<Animal>{
	
	private static final String LOG_TAG = "DBAnimalAdapter";
	
	private static DBAnimalHelper dbAnimalHelper;
	private static DBAnimalAdapter mInstance;
	
	private DBAnimalAdapter(Context context) {
		dbAnimalHelper = new DBAnimalHelper(context);
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
		values.put(DBAnimalHelper.ID, animal.getId());
		values.put(DBAnimalHelper.SPECIE_ID, animal.getSpecieId());
		values.put(DBAnimalHelper.RACE_ID, animal.getRaceId());
		
		values.put(DBAnimalHelper.SEX, animal.getSex());
		values.put(DBAnimalHelper.NAME, animal.getName());
		values.put(DBAnimalHelper.EAR_TAG, animal.getEarTag());
		
		if(animal.getBirthDate() != null) {
			values.put(DBAnimalHelper.BIRTH_DATE, animal.getBirthDate().getTime());
		}
		
		if(animal.getAquisitionDate() != null) {
			values.put(DBAnimalHelper.AQUISITION_DATE, animal.getAquisitionDate().getTime());
		}
		
		if(animal.getSellDate() != null) {
			values.put(DBAnimalHelper.SELL_DATE, animal.getSellDate().getTime());
		}
		
		values.put(DBAnimalHelper.AQUISITION_VALUE, animal.getAquisitionValue());
		values.put(DBAnimalHelper.SELL_VALUE, animal.getSellValue());
		database.insert(DBAnimalHelper.TABLE_NAME, null, values);
		return get(animal.getId());
	}

	@Override
	public void delete(Animal a) {
		Log.d(LOG_TAG, "Excluindo animal: " + a.getId());
		database.delete(DBAnimalHelper.TABLE_NAME, DBAnimalHelper.ID + " = " + a.getId(), null);
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
				
				new Date(cursor.getLong(6)),
				new Date(cursor.getLong(7)),
				new Date(cursor.getLong(8)),
				
				cursor.getDouble(9),
				cursor.getDouble(10),
				
				cursor.getInt(11),
				cursor.getInt(12));
		return Animal;
	}
	
	@Override
	public List<Animal> list() {
		Log.d(LOG_TAG, "Obtendo Animals");
		List<Animal> listaAnimal = new ArrayList<Animal>();
		Cursor cursor = database.rawQuery("select * from " + DBAnimalHelper.TABLE_NAME + " order by " + DBAnimalHelper.ID, null);
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
						+ DBAnimalHelper.TABLE_NAME
						+ " where " 
						+ DBAnimalHelper.TABLE_NAME + "." + DBAnimalHelper.ID + " = " + idAnimal;
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}

	@Override
	protected DBMeuRebanhoHelperAbstract getHelper() {
		return dbAnimalHelper;
	}
}
