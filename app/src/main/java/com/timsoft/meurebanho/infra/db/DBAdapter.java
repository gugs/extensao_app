package com.timsoft.meurebanho.infra.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

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

	//TODO: Deve ser feita atualização da tabela e não exclusão e inclusão
	public void update(T t) {
		delete(t);
		create(t);
	}
	
	protected DBHandler getDBHandler() {
		return DBHandler.getInstance(context);
	}
	
	protected Date longToDate(Cursor cursor, int columnIndex) {
		if(cursor.isNull(columnIndex)) {
			return null;
		} else {
			return new Date(cursor.getLong(columnIndex));
		}
	}
	
	public abstract String getTableName();
}
