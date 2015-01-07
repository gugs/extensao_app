package com.timsoft.meurebanho.db.lote;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timsoft.meurebanho.model.Lote;

public class DBLoteAdapter {
	
	private static final String LOG_TAG = "DBLoteAdapter";
	
	protected static SQLiteDatabase database;
	
	private static DBLoteHelper dbLoteHelper;
	private static DBLoteAdapter mInstance;
	
	private DBLoteAdapter(Context context) {
		dbLoteHelper = new DBLoteHelper(context);
	}
	
	public static DBLoteAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBLoteAdapter(context);
		}
		return mInstance;
	}

	public void open() throws SQLException {
		database = dbLoteHelper.getWritableDatabase();
	}

	public void close() {
		dbLoteHelper.close();
	}

	public Lote createLote(Lote lote) {
		Log.d(LOG_TAG, "Incluindo Lote: " + lote.toString());
		ContentValues values = new ContentValues();
		values.put(DBLoteHelper.ID, lote.getId());
		values.put(DBLoteHelper.DESCRICAO, lote.getDescricao());
		database.insert(DBLoteHelper.TABLE_NAME, null, values);

		return getLote(lote.getId());
	}

	public void deleteLote(int idLote) {
		Log.d(LOG_TAG, "Excluindo Lote: " + idLote);
		database.delete(DBLoteHelper.TABLE_NAME, DBLoteHelper.ID + " = " + idLote, null);
	}
	
	public void deleteLotes() {
		Log.d(LOG_TAG, "Excluindo todas Lotes");
		database.delete(DBLoteHelper.TABLE_NAME, null, null);
	}

	private Lote cursorToLote(Cursor cursor) {
		Lote Lote = new Lote(cursor.getLong(0), cursor.getString(1));
		return Lote;
	}
	
	public List<Lote> getLotes() {
		Log.d(LOG_TAG, "Obtendo Lotes");
		List<Lote> listaLote = new ArrayList<Lote>();
		Cursor cursor = database.rawQuery("select * from " + DBLoteHelper.TABLE_NAME + " order by " + DBLoteHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaLote.add(cursorToLote(cursor));
	        } while (cursor.moveToNext());
		}
		return listaLote;
	}

	public Lote getLote(long idLote) {
		Log.d(LOG_TAG, "Obtendo Lote: " + idLote);
		String query = "select " +
				DBLoteHelper.TABLE_NAME + "." + DBLoteHelper.ID + ", " +
				DBLoteHelper.TABLE_NAME + "." + DBLoteHelper.DESCRICAO + 
				
				" from " + DBLoteHelper.TABLE_NAME + 
				
				" where " +
				DBLoteHelper.TABLE_NAME + "." + DBLoteHelper.ID + " = " + idLote;
				
//		Log.d(LOG_TAG, "Query: " + query);
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorToLote(cursor);
		}
		return null;
	}
	
	public void forceDrop(){
		Log.d(LOG_TAG, "ForÃ§ando Drop");
		database.execSQL("DROP TABLE IF EXISTS " + DBLoteHelper.TABLE_NAME);
	}
}
