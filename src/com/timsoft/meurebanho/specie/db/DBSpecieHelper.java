package com.timsoft.meurebanho.specie.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBSpecieHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBSpecieHelper";
	
	public static final String TABLE_NAME = "specie";
	
	public static final String ID = "id";
	public static final String DESCRIPTION = "description";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRIPTION + " text not null);";

	public DBSpecieHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Constructing DBSpecieHelper");
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
