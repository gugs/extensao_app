package com.timsoft.meurebanho.db.lote;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelper;

public class DBLoteHelper extends DBMeuRebanhoHelper{
	
	private static final String LOG_TAG = "DBLoteHelper";
	
	public static final String TABLE_NAME = "lote";
	
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRICAO + " text not null);";

	public DBLoteHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Construindo DBLoteHelper");
	}

}
