package com.timsoft.meurebanho.db.raca;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelper;

public class DBRacaHelper extends DBMeuRebanhoHelper{
	
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

}
