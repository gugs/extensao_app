package com.timsoft.meurebanho.pasture.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.pasture.model.Pasture;

public class DBPastureAdapter extends DBAdapterAbstract<Pasture> {
	
	private static final String LOG_TAG = "DBPastoAdapter";
	
	private static DBPastoHelper dbPastoHelper;
	private static DBPastureAdapter mInstance;
	
	private DBPastureAdapter(Context context) {
		dbPastoHelper = new DBPastoHelper(context);
	}
	
	public static DBPastureAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBPastureAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Pasture create(Pasture pasto) {
		Log.d(LOG_TAG, "Incluindo Pasto: " + pasto.toString());
		ContentValues values = new ContentValues();
		values.put(DBPastoHelper.ID, pasto.getId());
		values.put(DBPastoHelper.DESCRICAO, pasto.getDescription());
		database.insert(DBPastoHelper.TABLE_NAME, null, values);

		return get(pasto.getId());
	}

	@Override
	public void delete(Pasture p) {
		Log.d(LOG_TAG, "Excluindo Pasto: " + p.getId());
		database.delete(DBPastoHelper.TABLE_NAME, DBPastoHelper.ID + " = " + p.getId(), null);
	}
	
	@Override
	public Pasture cursorTo(Cursor cursor) {
		Pasture pasto = new Pasture(cursor.getInt(0), cursor.getString(1));
		return pasto;
	}
	
	@Override
	public List<Pasture> list() {
		Log.d(LOG_TAG, "Obtendo Pastos");
		List<Pasture> listaPasto = new ArrayList<Pasture>();
		Cursor cursor = database.rawQuery("select * from " + DBPastoHelper.TABLE_NAME + " order by " + DBPastoHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaPasto.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaPasto;
	}

	@Override
	public Pasture get(int idPasto) {
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
