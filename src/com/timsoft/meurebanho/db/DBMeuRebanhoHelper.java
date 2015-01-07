package com.timsoft.meurebanho.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timsoft.meurebanho.db.especie.DBEspecieHelper;
import com.timsoft.meurebanho.db.fazenda.DBFazendaHelper;

public abstract class DBMeuRebanhoHelper extends SQLiteOpenHelper {

	private static final String LOG_TAG = "DBMeuRebanhoHelper";
	
	private static final String DATABASE_NAME = "meurebanho.db";
	private static final int DATABASE_VERSION = 1;
	public static final String ID = "_id";
	
	public DBMeuRebanhoHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "Criando tabelas.");
		db.execSQL(DBFazendaHelper.TABLE_CREATE);
		db.execSQL(DBEspecieHelper.TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DBFazendaHelper.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBEspecieHelper.TABLE_NAME);
		onCreate(db);
	}

}
