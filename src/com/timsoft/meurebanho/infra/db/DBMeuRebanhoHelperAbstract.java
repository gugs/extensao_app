package com.timsoft.meurebanho.infra.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timsoft.meurebanho.BuildConfig;

public abstract class DBMeuRebanhoHelperAbstract extends SQLiteOpenHelper {

	private static final String LOG_TAG = "DBMeuRebanhoHelper";
	
	private static final String DATABASE_NAME = "meurebanho.db";
	private static final int DATABASE_VERSION = 1;
	public static final String ID = "_id";
	
	public DBMeuRebanhoHelperAbstract(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		//Força um update para regerar as tabelas
//		if(BuildConfig.DEBUG){
//			onUpgrade(db, 0, 0);
//		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "Criando tabelas.");
		db.execSQL(this.getTableCreateSQL());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading " + getTableName() + " from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		forceDrop(db);
		onCreate(db);
	}
	
	public void forceDrop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + getTableName());
	}
	
	public abstract String getTableCreateSQL();
	
	public abstract String getTableName();
}
