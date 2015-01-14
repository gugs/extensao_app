package com.timsoft.meurebanho.db.lote;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.db.DBAdapterAbstract;
import com.timsoft.meurebanho.db.DBMeuRebanhoHelperAbstract;
import com.timsoft.meurebanho.model.Lote;

public class DBLoteAdapter extends DBAdapterAbstract<Lote> {
	
	private static final String LOG_TAG = "DBLoteAdapter";
	
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

	@Override
	public Lote create(Lote lote) {
		Log.d(LOG_TAG, "Incluindo Lote: " + lote.toString());
		ContentValues values = new ContentValues();
		values.put(DBLoteHelper.ID, lote.getId());
		values.put(DBLoteHelper.DESCRICAO, lote.getDescricao());
		database.insert(DBLoteHelper.TABLE_NAME, null, values);

		return get(lote.getId());
	}

	@Override
	public void delete(Lote l) {
		Log.d(LOG_TAG, "Excluindo Lote: " + l.getId());
		database.delete(DBLoteHelper.TABLE_NAME, DBLoteHelper.ID + " = " + l.getId(), null);
	}
	
	@Override
	public Lote cursorTo(Cursor cursor) {
		Lote Lote = new Lote(cursor.getInt(0), cursor.getString(1));
		return Lote;
	}
	
	@Override
	public List<Lote> list() {
		Log.d(LOG_TAG, "Obtendo Lotes");
		List<Lote> listaLote = new ArrayList<Lote>();
		Cursor cursor = database.rawQuery("select * from " + DBLoteHelper.TABLE_NAME + " order by " + DBLoteHelper.ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listaLote.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listaLote;
	}

	@Override
	public Lote get(int idLote) {
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
