package com.timsoft.meurebanho.race.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBRaceHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBRacaHelper";
	
	public static final String TABLE_NAME = "raca";
	
	public static final String ID = "id";
	public static final String DESCRICAO = "descricao";
	public static final String ID_ESPECIE = "id_especie";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ DESCRICAO + " text not null, " 
			+ ID_ESPECIE + " integer not null)";

	public DBRaceHelper(Context context) {
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
