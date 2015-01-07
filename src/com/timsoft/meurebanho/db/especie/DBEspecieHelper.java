package com.timsoft.meurebanho.db.especie;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelper;

public class DBEspecieHelper extends DBMeuRebanhoHelper{
	
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

}
