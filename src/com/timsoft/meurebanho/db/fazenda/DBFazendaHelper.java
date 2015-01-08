package com.timsoft.meurebanho.db.fazenda;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelperAbstract;

public class DBFazendaHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBFazendaHelper";
	
	public static final String TABLE_NAME = "fazenda";
	
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRICAO + " text not null);";

	public DBFazendaHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Construindo DBFazendaHelper");
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
