package com.timsoft.meurebanho.db.pasto;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.db.DBMeuRebanhoHelper;

public class DBPastoHelper extends DBMeuRebanhoHelper{
	
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

}
