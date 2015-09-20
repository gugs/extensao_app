package com.timsoft.meurebanho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.timsoft.meurebanho.animal.activity.AnimalListActivity;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	private static final String LOG_TAG = "MainActivity";

	private static final String FIRST_RUN = "FIRST_RUN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(LOG_TAG, "onCreate");

		SharedPreferences prefs = getSharedPreferences(getString(R.string.app_full_name),
				MODE_PRIVATE);

//		 if(BuildConfig.DEBUG) {
//			 deleteDatabase(DBHandler.DATABASE_NAME);
//			 prefs.edit().putBoolean(FIRST_RUN, true).commit();
//		 }

		if (prefs.getBoolean(FIRST_RUN, true)) {
			populateDefaultData();
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
		}
		
		Intent intent = new Intent(this, AnimalListActivity.class);
		startActivity(intent);
		finish();
	}

	private void populateDefaultData() {
		DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance(this);
		DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance(this);
		
		specieDatasource.open();
		for (Specie e : Specie.getDefaultSpecies(this)) {
			specieDatasource.create(e);
		}
		specieDatasource.close();

		raceDatasource.open();
		for (Race r : Race.getDefaultRaces()) {
			raceDatasource.create(r);
		}
		raceDatasource.close();
	}
	
	public static DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
	}
	
	public static String getFormatedDate(Date d) {
		return d == null ? "" : MainActivity.getDateFormat().format(d);
	}

	public static String getFormatedValue(double value) {
		NumberFormat nf = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols (Locale.getDefault()));  
		return nf.format(value);
	}
}
