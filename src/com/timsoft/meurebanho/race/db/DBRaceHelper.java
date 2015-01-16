package com.timsoft.meurebanho.race.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBRaceHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBRaceHelper";
	
	public static final String TABLE_NAME = "race";
	
	public static final String ID = "id";
	public static final String DESCRIPTION = "description";
	public static final String ID_SPECIE = "id_specie";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRIPTION + " text not null, " 
			+ ID_SPECIE + " integer not null)";

	public DBRaceHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Constructing DBRaceHelper");
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
