package com.timsoft.meurebanho.db.fazenda;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timsoft.meurebanho.model.Fazenda;

public class DBFazendaAdapter {
	
	private static final String LOG_TAG = "DBFazendaAdapter";
	
	private static SQLiteDatabase database;
	private static DBFazendaHelper dbFazendaHelper;
	private static DBFazendaAdapter mInstance;
	
//	private String[] allColumns = { 
//			DBNoticiaHelper.ID, 
//			DBNoticiaHelper.ID_CATEGORIA,
//			DBNoticiaHelper.TITULO, 
//			DBNoticiaHelper.LINK, 
//			DBNoticiaHelper.DESCRICAO, 
//			DBNoticiaHelper.ID_ORIGEM,
//			DBNoticiaHelper.DATA_PUBLICACAO };

	private DBFazendaAdapter(Context context) {
		dbFazendaHelper = new DBFazendaHelper(context);
	}
	
	public static DBFazendaAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBFazendaAdapter(context);
		}
		return mInstance;
	}

	public void open() throws SQLException {
		database = dbFazendaHelper.getWritableDatabase();
	}

	public void close() {
		dbFazendaHelper.close();
	}

	public Fazenda createFazenda(Fazenda fazenda) {
		Log.d(LOG_TAG, "Incluindo fazenda: " + fazenda.toString());
		ContentValues values = new ContentValues();
		values.put(DBFazendaHelper.ID, fazenda.getId());
		values.put(DBFazendaHelper.DESCRICAO, fazenda.getDescricao());
		database.insert(DBFazendaHelper.TABLE_NAME, null, values);

		return getFazenda(fazenda.getId());
	}

	public void deleteFazenda(int idFazenda) {
		Log.d(LOG_TAG, "Excluindo fazenda: " + idFazenda);
		database.delete(DBFazendaHelper.TABLE_NAME, DBFazendaHelper.ID + " = " + idFazenda, null);
	}
	
	public void deleteFazendas() {
		Log.d(LOG_TAG, "Excluindo todas fazendas");
		database.delete(DBFazendaHelper.TABLE_NAME, null, null);
	}

	private Fazenda cursorToFazenda(Cursor cursor) {
		Fazenda fazenda = new Fazenda(cursor.getLong(0), cursor.getString(1));
		return fazenda;
	}
	
	public List<Fazenda> getFazendas() {
		Log.d(LOG_TAG, "Obtendo fazendas");
		List<Fazenda> listaFazenda = new ArrayList<Fazenda>();
		Cursor cursor = database.rawQuery("select * from " + DBFazendaHelper.TABLE_NAME + " order by " + DBFazendaHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaFazenda.add(cursorToFazenda(cursor));
	        } while (cursor.moveToNext());
		}
		return listaFazenda;
	}

	public Fazenda getFazenda(long idFazenda) {
		Log.d(LOG_TAG, "Obtendo fazenda: " + idFazenda);
		String query = "select " +
				DBFazendaHelper.TABLE_NAME + "." + DBFazendaHelper.ID + ", " +
				DBFazendaHelper.TABLE_NAME + "." + DBFazendaHelper.DESCRICAO + 
				
				" from " + DBFazendaHelper.TABLE_NAME + 
				
				" where " +
				DBFazendaHelper.TABLE_NAME + "." + DBFazendaHelper.ID + " = " + idFazenda;
				
//		Log.d(LOG_TAG, "Query: " + query);
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorToFazenda(cursor);
		}
		return null;
	}
	
	public void forceDrop(){
		Log.d(LOG_TAG, "ForÃ§ando Drop");
		database.execSQL("DROP TABLE IF EXISTS " + DBFazendaHelper.TABLE_NAME);
	}
}
