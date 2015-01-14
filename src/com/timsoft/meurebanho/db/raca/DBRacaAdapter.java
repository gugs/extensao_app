package com.timsoft.meurebanho.db.raca;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.db.DBAdapterAbstract;
import com.timsoft.meurebanho.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.model.Raca;

public class DBRacaAdapter extends DBAdapterAbstract<Raca> {
	
	private static final String LOG_TAG = "DBRacaAdapter";
	
	private static DBRacaHelper dbRacaHelper;
	private static DBRacaAdapter mInstance;
	
	private DBRacaAdapter(Context context) {
		dbRacaHelper = new DBRacaHelper(context);
	}
	
	public static DBRacaAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBRacaAdapter(context);
		}
		return mInstance;
	}

	public Raca create(Raca raca) {
		Log.d(LOG_TAG, "Incluindo Raca: " + raca.toString());
		ContentValues values = new ContentValues();
		values.put(DBRacaHelper.ID, raca.getId());
		values.put(DBRacaHelper.DESCRICAO, raca.getDescricao());
		values.put(DBRacaHelper.ID_ESPECIE, raca.getIdEspecie());
		database.insert(DBRacaHelper.TABLE_NAME, null, values);

		return get(raca.getId());
	}

	public void delete(int idRaca) {
		Log.d(LOG_TAG, "Excluindo Raca: " + idRaca);
		database.delete(DBRacaHelper.TABLE_NAME, DBRacaHelper.ID + " = " + idRaca, null);
	}
	
	public Raca cursorTo(Cursor cursor) {
		Raca raca = new Raca(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
		return raca;
	}
	
	public List<Raca> list() {
		Log.d(LOG_TAG, "Obtendo Racas");
		List<Raca> listaRaca = new ArrayList<Raca>();
		Cursor cursor = database.rawQuery("select * from " + DBRacaHelper.TABLE_NAME + " order by " + DBRacaHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaRaca.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaRaca;
	}

	public Raca get(long idRaca) {
		Log.d(LOG_TAG, "Obtendo Raca: " + idRaca);
		String query = "select " +
				DBRacaHelper.TABLE_NAME + "." + DBRacaHelper.ID + ", " +
				DBRacaHelper.TABLE_NAME + "." + DBRacaHelper.DESCRICAO + ", " +
				DBRacaHelper.TABLE_NAME + "." + DBRacaHelper.ID_ESPECIE + 
				
				" from " + DBRacaHelper.TABLE_NAME + 
				
				" where " +
				DBRacaHelper.TABLE_NAME + "." + DBRacaHelper.ID + " = " + idRaca;
				
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
