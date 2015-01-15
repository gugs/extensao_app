package com.timsoft.meurebanho.farm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.farm.model.Farm;
import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBFarmAdapter extends DBAdapterAbstract<Farm>{
	
	private static final String LOG_TAG = "DBFarmAdapter";
	
	private static DBFarmHelper dbFarmHelper;
	private static DBFarmAdapter mInstance;
	
	private DBFarmAdapter(Context context) {
		dbFarmHelper = new DBFarmHelper(context);
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
		values.put(DBFarmHelper.ID, farm.getId());
		values.put(DBFarmHelper.NAME, farm.getDescricao());
		database.insert(DBFarmHelper.TABLE_NAME, null, values);

		return get(farm.getId());
	}

	@Override
	public void delete(Farm f) {
		Log.d(LOG_TAG, "Deleting farm: " + f.getId());
		database.delete(DBFarmHelper.TABLE_NAME, DBFarmHelper.ID + " = " + f.getId(), null);
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
		Cursor cursor = database.rawQuery("select * from " + DBFarmHelper.TABLE_NAME + " order by " + DBFarmHelper.NAME, null);
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
				DBFarmHelper.TABLE_NAME + "." + DBFarmHelper.ID + ", " +
				DBFarmHelper.TABLE_NAME + "." + DBFarmHelper.NAME + 
				
				" from " + DBFarmHelper.TABLE_NAME + 
				
				" where " +
				DBFarmHelper.TABLE_NAME + "." + DBFarmHelper.ID + " = " + idFarm;
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}

	@Override
	protected DBMeuRebanhoHelperAbstract getHelper() {
		return dbFarmHelper;
	}
}
