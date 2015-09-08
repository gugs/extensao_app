package com.timsoft.meurebanho.farm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.farm.model.Farm;
import com.timsoft.meurebanho.infra.db.DBAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBFarmAdapter extends DBAdapter<Farm>{
	
	private static final String LOG_TAG = "DBFarmAdapter";
	
	public static final String TABLE_NAME = "farm";
	
	public static final String ID = "id";
	public static final String NAME = "name";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ NAME + " text not null);";
	
	private static DBFarmAdapter mInstance;
	
	private DBFarmAdapter(Context context) {
		super(context);
	}
	
	public static DBFarmAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBFarmAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Farm create(Farm farm) {
		Log.d(LOG_TAG, "Adding farm: " + farm.toString());
		ContentValues values = new ContentValues();
		values.put(ID, farm.getId());
		values.put(NAME, farm.getDescription());
		database.insert(TABLE_NAME, null, values);

		return get(farm.getId());
	}

	@Override
	public void delete(Farm f) {
		Log.d(LOG_TAG, "Deleting farm: " + f.getId());
		database.delete(TABLE_NAME, ID + " = " + f.getId(), null);
	}
	
	@Override
	public Farm cursorTo(Cursor cursor) {
		Farm farm = new Farm(cursor.getInt(0), cursor.getString(1));
		return farm;
	}
	
	@Override
	public List<Farm> list() {
		Log.d(LOG_TAG, "Getting farms");
		List<Farm> farmList = new ArrayList<Farm>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + NAME, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	farmList.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return farmList;
	}

	@Override
	public Farm get(int idFarm) {
		Log.d(LOG_TAG, "Getting farm: " + idFarm);
		String query = "select " +
				TABLE_NAME + "." + ID + ", " +
				TABLE_NAME + "." + NAME + 
				
				" from " + TABLE_NAME + 
				
				" where " +
				TABLE_NAME + "." + ID + " = " + idFarm;
				
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
