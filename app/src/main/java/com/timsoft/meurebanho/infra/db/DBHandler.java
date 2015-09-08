package com.timsoft.meurebanho.infra.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.farm.db.DBFarmAdapter;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

	private static final String LOG_TAG = "DatabaseHandler";
		
	public static final String DATABASE_NAME = "meurebanho.db";
	public static final int DATABASE_VERSION = 1;

	private static DBHandler mInstance;
	private List<String> listTableCreate;
	private List<String> listTableName;
	
	private DBHandler(Context context) {
		super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
		//FIXME: Voltar para linha de baixo para nao salvar na memória do aparelho e não no sdcard
		//super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		listTableCreate = new ArrayList<String>();
		listTableName = new ArrayList<String>();
		
		listTableCreate.add(DBAnimalAdapter.TABLE_CREATE);
		listTableName.add(DBAnimalAdapter.TABLE_NAME);
		
//		listTableCreate.add(DBEventAdapter.TABLE_CREATE);
//		listTableName.add(DBEventAdapter.TABLE_NAME);
		
		listTableCreate.add(DBFarmAdapter.TABLE_CREATE);
		listTableName.add(DBFarmAdapter.TABLE_NAME);
		
//		listTableCreate.add(DBLotAdapter.TABLE_CREATE);
//		listTableName.add(DBLotAdapter.TABLE_NAME);
		
//		listTableCreate.add(DBPastureAdapter.TABLE_CREATE);
//		listTableName.add(DBPastureAdapter.TABLE_NAME);
		
		listTableCreate.add(DBRaceAdapter.TABLE_CREATE);
		listTableName.add(DBRaceAdapter.TABLE_NAME);
		
		listTableCreate.add(DBSpecieAdapter.TABLE_CREATE);
		listTableName.add(DBSpecieAdapter.TABLE_NAME);
		
	}
	
	public static DBHandler getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBHandler(context);
		}
		return mInstance;
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.d(LOG_TAG, "onOpen");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "onCreate");
		for(String tableCreate : listTableCreate) {
			db.execSQL(tableCreate);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(LOG_TAG, "onUpgrade");
		forceDrop(db);
		onCreate(db);
	}
	
	public void forceDrop(SQLiteDatabase db) {
		for(String tableName : listTableName) {
			db.execSQL("DROP TABLE IF EXISTS " + tableName);
		}
	}
}
