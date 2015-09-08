package com.timsoft.meurebanho.race.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBAdapter;
import com.timsoft.meurebanho.race.model.Race;

import java.util.ArrayList;
import java.util.List;

public class DBRaceAdapter extends DBAdapter<Race> {
	
	private static final String LOG_TAG = "DBRacaAdapter";
	
	public static final String TABLE_NAME = "race";
	
	public static final String ID = "id";
	public static final String DESCRIPTION = "description";
	//FIXME: Alterar string para "specie_id" ao recriar tabelas
	public static final String SPECIE_ID = "id_specie";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRIPTION + " text not null, " 
			+ SPECIE_ID + " integer not null)";
	
	private static DBRaceAdapter mInstance;
	
	private DBRaceAdapter(Context context) {
		super(context);
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
		values.put(ID, race.getId());
		values.put(DESCRIPTION, race.getDescription());
		values.put(SPECIE_ID, race.getIdSpecie());
		database.insert(TABLE_NAME, null, values);

		return get(race.getId());
	}

	@Override
	public void delete(Race r) {
		Log.d(LOG_TAG, "Excluiding Race: " + r.getId());
		database.delete(TABLE_NAME, ID + " = " + r.getId(), null);
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
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaRaca.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaRaca;
	}
	
	public List<Race> listBySpecieId(int specieId) {
		Log.d(LOG_TAG, "Listing Races");
		List<Race> listaRaca = new ArrayList<Race>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where " + SPECIE_ID + " = " + specieId + " order by " + ID, null);
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
				TABLE_NAME + "." + ID + ", " +
				TABLE_NAME + "." + DESCRIPTION + ", " +
				TABLE_NAME + "." + SPECIE_ID + 
				
				" from " + TABLE_NAME + 
				
				" where " +
				TABLE_NAME + "." + ID + " = " + idRaca;
				
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
