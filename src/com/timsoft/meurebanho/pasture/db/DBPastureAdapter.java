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
	
	private static DBPastureHelper dbPastoHelper;
	private static DBPastureAdapter mInstance;
	
	private DBPastureAdapter(Context context) {
		dbPastoHelper = new DBPastureHelper(context);
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
		values.put(DBPastureHelper.ID, pasto.getId());
		values.put(DBPastureHelper.DESCRIPTION, pasto.getDescription());
		database.insert(DBPastureHelper.TABLE_NAME, null, values);

		return get(pasto.getId());
	}

	@Override
	public void delete(Pasture p) {
		Log.d(LOG_TAG, "Excluindo Pasto: " + p.getId());
		database.delete(DBPastureHelper.TABLE_NAME, DBPastureHelper.ID + " = " + p.getId(), null);
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
		Cursor cursor = database.rawQuery("select * from " + DBPastureHelper.TABLE_NAME + " order by " + DBPastureHelper.ID, null);
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
				DBPastureHelper.TABLE_NAME + "." + DBPastureHelper.ID + ", " +
				DBPastureHelper.TABLE_NAME + "." + DBPastureHelper.DESCRIPTION + 
				
				" from " + DBPastureHelper.TABLE_NAME + 
				
				" where " +
				DBPastureHelper.TABLE_NAME + "." + DBPastureHelper.ID + " = " + idPasto;
				
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
