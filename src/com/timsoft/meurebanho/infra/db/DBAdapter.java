package com.timsoft.meurebanho.infra.db;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBAdapter<T> {
	
	protected static SQLiteDatabase database;
	private Context context;
	
	public abstract T create(T t);
	public abstract void delete(T t);
	public abstract T cursorTo(Cursor cursor);
	public abstract List<T> list();
	public abstract T get(int id);
	
	protected DBAdapter(Context context) {
		this.context = context;
	}
	
	public final void open() throws SQLException {
		database = getDBHandler().getWritableDatabase();
	}
	
	public final void close() {
		getDBHandler().close();
	}
	
	public final void clear() {
		database.delete(getTableName(), null, null);
	}
	
	public void update(T t) {
		delete(t);
		create(t);
	}
	
	protected DBHandler getDBHandler() {
		return DBHandler.getInstance(context);
	}
	
	public abstract String getTableName();
}
