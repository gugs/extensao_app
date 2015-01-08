package com.timsoft.meurebanho.db.raca;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timsoft.meurebanho.db.DBAdapterInterface;
import com.timsoft.meurebanho.model.Raca;

public class DBRacaAdapter implements DBAdapterInterface<Raca> {
	
	private static final String LOG_TAG = "DBRacaAdapter";
	
	protected static SQLiteDatabase database;
	
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

	public void open() throws SQLException {
		database = dbRacaHelper.getWritableDatabase();
	}

	public void close() {
		dbRacaHelper.close();
	}

	public Raca create(Raca raca) {
		Log.d(LOG_TAG, "Incluindo Raca: " + raca.toString());
		ContentValues values = new ContentValues();
		values.put(DBRacaHelper.ID, raca.getId());
		values.put(DBRacaHelper.DESCRICAO, raca.getDescricao());
		database.insert(DBRacaHelper.TABLE_NAME, null, values);

		return get(raca.getId());
	}

	public void delete(int idRaca) {
		Log.d(LOG_TAG, "Excluindo Raca: " + idRaca);
		database.delete(DBRacaHelper.TABLE_NAME, DBRacaHelper.ID + " = " + idRaca, null);
	}
	
	public void clear() {
		Log.d(LOG_TAG, "Excluindo todas Racas");
		database.delete(DBRacaHelper.TABLE_NAME, null, null);
	}

	public Raca cursorTo(Cursor cursor) {
		Raca raca = new Raca(cursor.getLong(0), cursor.getString(1));
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
				DBRacaHelper.TABLE_NAME + "." + DBRacaHelper.DESCRICAO + 
				
				" from " + DBRacaHelper.TABLE_NAME + 
				
				" where " +
				DBRacaHelper.TABLE_NAME + "." + DBRacaHelper.ID + " = " + idRaca;
				
//		Log.d(LOG_TAG, "Query: " + query);
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}
	
	public void forceDrop(){
		Log.d(LOG_TAG, "ForÃ§ando Drop");
		database.execSQL("DROP TABLE IF EXISTS " + DBRacaHelper.TABLE_NAME);
	}
}
