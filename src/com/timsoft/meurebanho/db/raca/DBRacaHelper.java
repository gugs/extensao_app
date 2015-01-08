package com.timsoft.meurebanho.db.raca;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelperAbstract;

public class DBRacaHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBRacaHelper";
	
	public static final String TABLE_NAME = "raca";
	
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRICAO + " text not null);";

	public DBRacaHelper(Context context) {
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
