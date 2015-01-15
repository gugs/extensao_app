package com.timsoft.meurebanho.farm.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBFarmHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBFarmHelper";
	
	public static final String TABLE_NAME = "farm";
	
	public static final String ID = "id";
	public static final String NAME = "name";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ NAME + " text not null);";

	public DBFarmHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Constructing DBFarmHelper");
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
