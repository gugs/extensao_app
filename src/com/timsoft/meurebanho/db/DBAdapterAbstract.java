package com.timsoft.meurebanho.db;

import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBAdapterAbstract<T> {
	
	protected static SQLiteDatabase database;
	
	public abstract T create(T t);
	public abstract void delete(T t);
	public abstract T cursorTo(Cursor cursor);
	public abstract List<T> list();
	public abstract T get(int id);
	
	protected abstract DBMeuRebanhoHelperAbstract getHelper();
	
	public final void forceDrop(){
		getHelper().forceDrop(database);
	}
	
	public final void open() throws SQLException {
		database = getHelper().getWritableDatabase();
	}
	
	public final void close() {
		getHelper().close();
	}
	
	public final void clear() {
		database.delete(getHelper().getTableName(), null, null);
	}
	
	public void update(T t) {
		delete(t);
		create(t);
	}
}
