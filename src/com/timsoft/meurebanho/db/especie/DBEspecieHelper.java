package com.timsoft.meurebanho.db.especie;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelperAbstract;

public class DBEspecieHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBEspecieHelper";
	
	public static final String TABLE_NAME = "especie";
	
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRICAO + " text not null);";

	public DBEspecieHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Construindo DBEspecieHelper");
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
