package com.timsoft.meurebanho.db.especie;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.timsoft.meurebanho.db.DBAdapterInterface;
import com.timsoft.meurebanho.model.Especie;

public class DBEspecieAdapter implements DBAdapterInterface<Especie> {
	
	private static final String LOG_TAG = "DBEspecieAdapter";
	
	protected static SQLiteDatabase database;
	
	private static DBEspecieHelper dbEspecieHelper;
	private static DBEspecieAdapter mInstance;
	
	private DBEspecieAdapter(Context context) {
		dbEspecieHelper = new DBEspecieHelper(context);
	}
	
	public static DBEspecieAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBEspecieAdapter(context);
		}
		return mInstance;
	}

	public void open() throws SQLException {
		database = dbEspecieHelper.getWritableDatabase();
	}

	public void close() {
		dbEspecieHelper.close();
	}

	public Especie create(Especie Especie) {
		Log.d(LOG_TAG, "Incluindo Especie: " + Especie.toString());
		ContentValues values = new ContentValues();
		values.put(DBEspecieHelper.ID, Especie.getId());
		values.put(DBEspecieHelper.DESCRICAO, Especie.getDescricao());
		database.insert(DBEspecieHelper.TABLE_NAME, null, values);

		return get(Especie.getId());
	}

	public void delete(int idEspecie) {
		Log.d(LOG_TAG, "Excluindo Especie: " + idEspecie);
		database.delete(DBEspecieHelper.TABLE_NAME, DBEspecieHelper.ID + " = " + idEspecie, null);
	}
	
	public void clear() {
		Log.d(LOG_TAG, "Excluindo todas Especies");
		database.delete(DBEspecieHelper.TABLE_NAME, null, null);
	}

	public Especie cursorTo(Cursor cursor) {
		Especie Especie = new Especie(cursor.getLong(0), cursor.getString(1));
		return Especie;
	}
	
	public List<Especie> list() {
		Log.d(LOG_TAG, "Obtendo Especies");
		List<Especie> listaEspecie = new ArrayList<Especie>();
		Cursor cursor = database.rawQuery("select * from " + DBEspecieHelper.TABLE_NAME + " order by " + DBEspecieHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaEspecie.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaEspecie;
	}

	public Especie get(long idEspecie) {
		Log.d(LOG_TAG, "Obtendo Especie: " + idEspecie);
		String query = "select " +
				DBEspecieHelper.TABLE_NAME + "." + DBEspecieHelper.ID + ", " +
				DBEspecieHelper.TABLE_NAME + "." + DBEspecieHelper.DESCRICAO + 
				
				" from " + DBEspecieHelper.TABLE_NAME + 
				
				" where " +
				DBEspecieHelper.TABLE_NAME + "." + DBEspecieHelper.ID + " = " + idEspecie;
				
//		Log.d(LOG_TAG, "Query: " + query);
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}
	
	public void forceDrop(){
		Log.d(LOG_TAG, "ForÃ§ando Drop");
		database.execSQL("DROP TABLE IF EXISTS " + DBEspecieHelper.TABLE_NAME);
	}
}
