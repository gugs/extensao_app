package com.timsoft.meurebanho.animal.db;

import android.content.Context;
import android.util.Log;

import com.timsoft.meurebanho.infra.db.DBMeuRebanhoHelperAbstract;

public class DBAnimalHelper extends DBMeuRebanhoHelperAbstract{
	
	private static final String LOG_TAG = "DBAnimalHelper";
	
	public static final String TABLE_NAME = "animal";
	
	public static final String ID = "id";
	public static final String SPECIE_ID = "specie_id";
	public static final String RACE_ID = "race_id";
	
	public static final String SEX = "sex";
	public static final String NAME = "name";
	public static final String EAR_TAG = "ear_tag";
	
	public static final String BIRTH_DATE = "birth_date";
	public static final String AQUISITION_DATE = "aquisition_date";
	public static final String SELL_DATE = "sell_date";
	
	public static final String DEATH_DATE = "death_date";
	public static final String DEATH_REASON = "death_reason";
	
	public static final String AQUISITION_VALUE = "aquisition_value";
	public static final String SELL_VALUE = "sell_value";
	
	public static final String FATHER_ID = "father_id";
	public static final String MOTHER_ID = "mother_id";
	
	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( " 
			+ ID + " integer primary key, "
			+ SPECIE_ID + " integer, "
			+ RACE_ID + " integer, "
			
			+ SEX + " text, "
			+ NAME + " text, "
			+ EAR_TAG + " text, "
			
			+ BIRTH_DATE + " integer, "
			+ AQUISITION_DATE + " integer, "
			+ SELL_DATE + " integer, "
			
			+ DEATH_DATE + " integer, "
			+ DEATH_REASON + " text, "
			
			+ AQUISITION_VALUE + " real, "
			+ SELL_VALUE + " real, "
			
			+ FATHER_ID + " integer, "
			+ MOTHER_ID + " integer "
			
			+ ");";

	public DBAnimalHelper(Context context) {
		super(context);
		Log.d(LOG_TAG, "Construindo DBAnimalHelper");
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
