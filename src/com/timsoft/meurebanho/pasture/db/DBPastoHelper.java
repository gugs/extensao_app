package com.timsoft.meurebanho.pasture.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBPastoHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBPastoHelper";
	
	public static final String TABLE_NAME = "pasto";
	
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRICAO + " text not null);";

	public DBPastoHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Construindo DBRacaHelper");
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
