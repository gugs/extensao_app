package com.timsoft.meurebanho;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.timsoft.meurebanho.animal.activity.AnimalAddActivity;
import com.timsoft.meurebanho.animal.activity.AnimalEditActivity;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.animal.model.AnimalArrayAdapter;
import com.timsoft.meurebanho.farm.activity.FarmsActivity;
import com.timsoft.meurebanho.infra.db.DBHandler;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

public class MainActivity extends ActionBarActivity {

	private static final String LOG_TAG = "MainActivity";
	
	private static final String FIRST_RUN = "FIRST_RUN";

	private DBAnimalAdapter animalDatasource;
	private DBSpecieAdapter specieDatasource;
	private DBRaceAdapter raceDatasource;
	
	private List<Animal> animals;
	
	private SharedPreferences prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		prefs = getSharedPreferences(getString(R.string.app_full_name), MODE_PRIVATE);
		
//		if(BuildConfig.DEBUG) {
//			deleteDatabase(DBHandler.DATABASE_NAME);
//			prefs.edit().putBoolean(FIRST_RUN, true).commit();
//		}
		
		animalDatasource = DBAnimalAdapter.getInstance(this);
		raceDatasource = DBRaceAdapter.getInstance(this);
		specieDatasource = DBSpecieAdapter.getInstance(this);
		
		if(prefs.getBoolean(FIRST_RUN, true)) {
			populateDefaultData();
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
		};
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_add_animal);
	    button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(MainActivity.this, AnimalAddActivity.class);
	    		startActivity(intent);
	        }
	    });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        updateData();
	}
	
	private void updateData() {
        animalDatasource.open();
        animals = animalDatasource.list();
        animalDatasource.close();
        
		ListView lv = (ListView) findViewById(R.id.list_animal);
		lv.setAdapter(new AnimalArrayAdapter(this, animals));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editAnimal(((Animal) parent.getItemAtPosition(position)));
			}
		});
	}
	
	protected void editAnimal(Animal a) {
		Intent intent = new Intent(this, AnimalEditActivity.class);
		intent.putExtra("animal_id", a.getId());
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_events:
			Toast.makeText(this, "Event Selected", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_farms:
			Intent intent = new Intent(this, FarmsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}
	
	private void populateDefaultData() {
		specieDatasource.open();
		for(Specie e : Specie.getDefaultSpecies(this)) {
			specieDatasource.create(e);
		}
		specieDatasource.close();
		
		raceDatasource.open();
		for(Race r : Race.getDefaultRaces()) {
			raceDatasource.create(r);
		}
		raceDatasource.close();
	}
}
