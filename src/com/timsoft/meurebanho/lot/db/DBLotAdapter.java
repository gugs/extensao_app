package com.timsoft.meurebanho.lot.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.lot.model.Lot;

public class DBLotAdapter extends DBAdapterAbstract<Lot> {
	
	private static final String LOG_TAG = "DBLoteAdapter";
	
	private static DBLotHelper dbLoteHelper;
	private static DBLotAdapter mInstance;
	
	private DBLotAdapter(Context context) {
		dbLoteHelper = new DBLotHelper(context);
	}
	
	public static DBLotAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBLotAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Lot create(Lot lote) {
		Log.d(LOG_TAG, "Incluindo Lote: " + lote.toString());
		ContentValues values = new ContentValues();
		values.put(DBLotHelper.ID, lote.getId());
		values.put(DBLotHelper.DESCRIPTION, lote.getDescription());
		database.insert(DBLotHelper.TABLE_NAME, null, values);

		return get(lote.getId());
	}

	@Override
	public void delete(Lot l) {
		Log.d(LOG_TAG, "Excluindo Lote: " + l.getId());
		database.delete(DBLotHelper.TABLE_NAME, DBLotHelper.ID + " = " + l.getId(), null);
	}
	
	@Override
	public Lot cursorTo(Cursor cursor) {
		Lot Lote = new Lot(cursor.getInt(0), cursor.getString(1));
		return Lote;
	}
	
	@Override
	public List<Lot> list() {
		Log.d(LOG_TAG, "Obtendo Lotes");
		List<Lot> listaLote = new ArrayList<Lot>();
		Cursor cursor = database.rawQuery("select * from " + DBLotHelper.TABLE_NAME + " order by " + DBLotHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaLote.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaLote;
	}

	@Override
	public Lot get(int idLote) {
		Log.d(LOG_TAG, "Obtendo Lote: " + idLote);
		String query = "select " +
				DBLotHelper.TABLE_NAME + "." + DBLotHelper.ID + ", " +
				DBLotHelper.TABLE_NAME + "." + DBLotHelper.DESCRIPTION + 
				
				" from " + DBLotHelper.TABLE_NAME + 
				
				" where " +
				DBLotHelper.TABLE_NAME + "." + DBLotHelper.ID + " = " + idLote;
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}
	
	@Override
	protected DBMeuRebanhoHelperAbstract getHelper() {
		return dbLoteHelper;
	}
}
