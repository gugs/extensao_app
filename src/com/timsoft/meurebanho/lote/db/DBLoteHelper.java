package com.timsoft.meurebanho.lote.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBLoteHelper extends DBMeuRebanhoHelperAbstract{
	
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
	
	@Override
	public String getTableCreateSQL() {
		return TABLE_CREATE;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

}
