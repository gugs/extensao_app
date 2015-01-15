package com.timsoft.meurebanho.pasto.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.pasto.model.Pasto;

public class DBPastoAdapter extends DBAdapterAbstract<Pasto> {
	
	private static final String LOG_TAG = "DBPastoAdapter";
	
	private static DBPastoHelper dbPastoHelper;
	private static DBPastoAdapter mInstance;
	
	private DBPastoAdapter(Context context) {
		dbPastoHelper = new DBPastoHelper(context);
	}
	
	public static DBPastoAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBPastoAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Pasto create(Pasto pasto) {
		Log.d(LOG_TAG, "Incluindo Pasto: " + pasto.toString());
		ContentValues values = new ContentValues();
		values.put(DBPastoHelper.ID, pasto.getId());
		values.put(DBPastoHelper.DESCRICAO, pasto.getDescricao());
		database.insert(DBPastoHelper.TABLE_NAME, null, values);

		return get(pasto.getId());
	}

	@Override
	public void delete(Pasto p) {
		Log.d(LOG_TAG, "Excluindo Pasto: " + p.getId());
		database.delete(DBPastoHelper.TABLE_NAME, DBPastoHelper.ID + " = " + p.getId(), null);
	}
	
	@Override
	public Pasto cursorTo(Cursor cursor) {
		Pasto pasto = new Pasto(cursor.getInt(0), cursor.getString(1));
		return pasto;
	}
	
	@Override
	public List<Pasto> list() {
		Log.d(LOG_TAG, "Obtendo Pastos");
		List<Pasto> listaPasto = new ArrayList<Pasto>();
		Cursor cursor = database.rawQuery("select * from " + DBPastoHelper.TABLE_NAME + " order by " + DBPastoHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaPasto.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaPasto;
	}

	@Override
	public Pasto get(int idPasto) {
		Log.d(LOG_TAG, "Obtendo Pasto: " + idPasto);
		String query = "select " +
				DBPastoHelper.TABLE_NAME + "." + DBPastoHelper.ID + ", " +
				DBPastoHelper.TABLE_NAME + "." + DBPastoHelper.DESCRICAO + 
				
				" from " + DBPastoHelper.TABLE_NAME + 
				
				" where " +
				DBPastoHelper.TABLE_NAME + "." + DBPastoHelper.ID + " = " + idPasto;
				
//		Log.d(LOG_TAG, "Query: " + query);
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}
	
	@Override
	protected DBMeuRebanhoHelperAbstract getHelper() {
		return dbPastoHelper;
	}
}
