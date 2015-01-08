package com.timsoft.meurebanho.db;

import java.util.List;

import android.database.Cursor;

public interface DBAdapterInterface<T> {
	public abstract void open();
	public abstract void close();
	public abstract T create(T t);
	public abstract void delete(int id);
	public abstract void clear();
	public abstract T cursorTo(Cursor cursor);
	public abstract List<T> list();
	public abstract T get(long id);
	public abstract void forceDrop();
}
