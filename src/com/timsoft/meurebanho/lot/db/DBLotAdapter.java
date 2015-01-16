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
	
	private static DBLoteHelper dbLoteHelper;
	private static DBLotAdapter mInstance;
	
	private DBLotAdapter(Context context) {
		dbLoteHelper = new DBLoteHelper(context);
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
		values.put(DBLoteHelper.ID, lote.getId());
		values.put(DBLoteHelper.DESCRICAO, lote.getDescription());
		database.insert(DBLoteHelper.TABLE_NAME, null, values);

		return get(lote.getId());
	}

	@Override
	public void delete(Lot l) {
		Log.d(LOG_TAG, "Excluindo Lote: " + l.getId());
		database.delete(DBLoteHelper.TABLE_NAME, DBLoteHelper.ID + " = " + l.getId(), null);
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
		Cursor cursor = database.rawQuery("select * from " + DBLoteHelper.TABLE_NAME + " order by " + DBLoteHelper.ID, null);
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
				DBLoteHelper.TABLE_NAME + "." + DBLoteHelper.ID + ", " +
				DBLoteHelper.TABLE_NAME + "." + DBLoteHelper.DESCRICAO + 
				
				" from " + DBLoteHelper.TABLE_NAME + 
				
				" where " +
				DBLoteHelper.TABLE_NAME + "." + DBLoteHelper.ID + " = " + idLote;
				
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
