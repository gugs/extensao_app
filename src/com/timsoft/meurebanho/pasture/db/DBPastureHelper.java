package com.timsoft.meurebanho.pasture.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBPastureHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBPastureHelper";
	
	public static final String TABLE_NAME = "pasture";
	
	public static final String ID = "id";
	public static final String DESCRIPTION = "description";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRIPTION + " text not null);";

	public DBPastureHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Constructing DBPastureHelper");
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
