package com.timsoft.meurebanho.event.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.timsoft.meurebanho.event.model.Event;
import com.timsoft.meurebanho.infra.db.DBAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBEventAdapter extends DBAdapter<Event> {

	private static final String LOG_TAG = "DBEventAdapter";

	public static final String TABLE_NAME = "event";

	public static final String ID = "id";
	public static final String EVENT_TYPE_ID = "event_type_id";
	public static final String ANIMAL_ID = "animal_id";
	public static final String DESCRIPTION = "description";
	public static final String DATE = "date";

	public static final String TABLE_CREATE = "create table " + TABLE_NAME + "( "
			+ ID + " integer primary key, "

			+ EVENT_TYPE_ID + " integer, "
			+ ANIMAL_ID + " integer, "

            + DATE + " integer, "

			+ DESCRIPTION + " text"

			+ ");";

	private static DBEventAdapter mInstance;

	private DBEventAdapter(Context context) {
		super(context);
	}
	
	public static DBEventAdapter getInstance(Context context){
		if(mInstance == null){
			mInstance = new DBEventAdapter(context);
		}
		return mInstance;
	}

	@Override
	public Event create(Event event) {
		Log.d(LOG_TAG, "Including event: " + event.toString());
		ContentValues values = new ContentValues();
		values.put(ID, event.getId());
		values.put(EVENT_TYPE_ID, event.getEventTypeId());
		values.put(ANIMAL_ID, event.getAnimalId());
		
		values.put(DESCRIPTION, event.getDescription());
		values.put(DATE, event.getDate().getTime());

        database.insert(TABLE_NAME, null, values);
		return get(event.getId());
	}

	@Override
	public void delete(Event e) {
		Log.d(LOG_TAG, "Excluindo event: " + e.getId());
		if(get(e.getId()) == null) {
			throw new RuntimeException("Solicitação de exclusão de evento inexistente: " + e.getId());
		}
		database.delete(TABLE_NAME, ID + " = " + e.getId(), null);
	}
	
	@Override
	public Event cursorTo(Cursor cursor) {
		Event event = new Event(
				cursor.getInt(0),
				cursor.getInt(1),
                cursor.getInt(3),
                longToDate(cursor, 4),
				cursor.getString(5)
				);
		return event;
	}
	
	@Override
	public List<Event> list() {
		Log.d(LOG_TAG, "Obtendo events");
		List<Event> listEvents = new ArrayList<Event>();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " order by " + ID, null);
		if (cursor != null && cursor.moveToFirst()) {
	        do {
	        	listEvents.add(cursorTo(cursor));
	        } while (cursor.moveToNext());
		}
		return listEvents;
	}

	@Override
	public Event get(int idEvent) {
		Log.d(LOG_TAG, "Obtendo Event: " + idEvent);
		String query = 	"select * from " 
						+ TABLE_NAME
						+ " where " 
						+ TABLE_NAME + "." + ID + " = " + idEvent;
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor);
		}
		return null;
	}

	public int getNextId() {
		String query = 	"select * from " 
						+ TABLE_NAME
						+ " order by " + ID + " desc";
				
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			return cursorTo(cursor).getId() + 1;
		} else {
			return 1;
		}
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
}
