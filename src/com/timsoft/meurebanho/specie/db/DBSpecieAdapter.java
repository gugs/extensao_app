package com.timsoft.meurebanho.specie.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.specie.model.Specie;

public class DBSpecieAdapter extends DBAdapterAbstract<Specie> {
	
	private static final String LOG_TAG = "DBEspecieAdapter";
	
	private static DBEspecieHelper dbEspecieHelper;
	private static DBSpecieAdapter mInstance;
	
	private DBSpecieAdapter(Context context) {
		dbEspecieHelper = new DBEspecieHelper(context);
	}
	
	public static DBSpecieAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBSpecieAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Specie create(Specie Especie) {
		Log.d(LOG_TAG, "Incluindo Especie: " + Especie.toString());
		ContentValues values = new ContentValues();
		values.put(DBEspecieHelper.ID, Especie.getId());
		values.put(DBEspecieHelper.DESCRICAO, Especie.getDescription());
		database.insert(DBEspecieHelper.TABLE_NAME, null, values);

		return get(Especie.getId());
	}

	@Override
	public void delete(Specie e) {
		Log.d(LOG_TAG, "Excluindo Especie: " + e.getId());
		database.delete(DBEspecieHelper.TABLE_NAME, DBEspecieHelper.ID + " = " + e.getId(), null);
	}
	
	@Override
	public Specie cursorTo(Cursor cursor) {
		Specie Especie = new Specie(cursor.getInt(0), cursor.getString(1));
		return Especie;
	}
	
	@Override
	public List<Specie> list() {
		Log.d(LOG_TAG, "Obtendo Especies");
		List<Specie> listaEspecie = new ArrayList<Specie>();
		Cursor cursor = database.rawQuery("select * from " + DBEspecieHelper.TABLE_NAME + " order by " + DBEspecieHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaEspecie.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaEspecie;
	}

	@Override
	public Specie get(int idEspecie) {
		Log.d(LOG_TAG, "Obtendo Especie: " + idEspecie);
		String query = "select " +
				DBEspecieHelper.TABLE_NAME + "." + DBEspecieHelper.ID + ", " +
				DBEspecieHelper.TABLE_NAME + "." + DBEspecieHelper.DESCRICAO + 
				
				" from " + DBEspecieHelper.TABLE_NAME + 
				
				" where " +
				DBEspecieHelper.TABLE_NAME + "." + DBEspecieHelper.ID + " = " + idEspecie;
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}
	
	@Override
	protected DBMeuRebanhoHelperAbstract getHelper() {
		return dbEspecieHelper;
	}
}
