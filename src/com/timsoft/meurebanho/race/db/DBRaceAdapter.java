package com.timsoft.meurebanho.race.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.race.model.Race;

public class DBRaceAdapter extends DBAdapterAbstract<Race> {
	
	private static final String LOG_TAG = "DBRacaAdapter";
	
	private static DBRaceHelper dbRacaHelper;
	private static DBRaceAdapter mInstance;
	
	private DBRaceAdapter(Context context) {
		dbRacaHelper = new DBRaceHelper(context);
	}
	
	public static DBRaceAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBRaceAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Race create(Race race) {
		Log.d(LOG_TAG, "Including Race: " + race.toString());
		ContentValues values = new ContentValues();
		values.put(DBRaceHelper.ID, race.getId());
		values.put(DBRaceHelper.DESCRIPTION, race.getDescription());
		values.put(DBRaceHelper.ID_SPECIE, race.getIdSpecie());
		database.insert(DBRaceHelper.TABLE_NAME, null, values);

		return get(race.getId());
	}

	@Override
	public void delete(Race r) {
		Log.d(LOG_TAG, "Excluiding Race: " + r.getId());
		database.delete(DBRaceHelper.TABLE_NAME, DBRaceHelper.ID + " = " + r.getId(), null);
	}
	
	@Override
	public Race cursorTo(Cursor cursor) {
		Race raca = new Race(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
		return raca;
	}
	
	@Override
	public List<Race> list() {
		Log.d(LOG_TAG, "Listing Races");
		List<Race> listaRaca = new ArrayList<Race>();
		Cursor cursor = database.rawQuery("select * from " + DBRaceHelper.TABLE_NAME + " order by " + DBRaceHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaRaca.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaRaca;
	}

	@Override
	public Race get(int idRaca) {
		Log.d(LOG_TAG, "Getting Race: " + idRaca);
		String query = "select " +
				DBRaceHelper.TABLE_NAME + "." + DBRaceHelper.ID + ", " +
				DBRaceHelper.TABLE_NAME + "." + DBRaceHelper.DESCRIPTION + ", " +
				DBRaceHelper.TABLE_NAME + "." + DBRaceHelper.ID_SPECIE + 
				
				" from " + DBRaceHelper.TABLE_NAME + 
				
				" where " +
				DBRaceHelper.TABLE_NAME + "." + DBRaceHelper.ID + " = " + idRaca;
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}
	
	@Override
	protected DBMeuRebanhoHelperAbstract getHelper() {
		return dbRacaHelper;
	}
}
