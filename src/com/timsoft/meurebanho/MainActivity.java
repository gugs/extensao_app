package com.timsoft.meurebanho;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.timsoft.meurebanho.animal.activity.AnimalAddActivity;
import com.timsoft.meurebanho.animal.activity.AnimalEditActivity;
import com.timsoft.meurebanho.animal.activity.AnimalListActivity;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.animal.model.AnimalArrayAdapter;
import com.timsoft.meurebanho.farm.activity.FarmsActivity;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;
import com.timsoft.meurebanho.specie.model.SpecieArrayAdapter;

public class MainActivity extends ActionBarActivity {

	private static final String LOG_TAG = "MainActivity";

	private static final String FIRST_RUN = "FIRST_RUN";

	private DBAnimalAdapter animalDatasource;
	private DBSpecieAdapter specieDatasource;
	private DBRaceAdapter raceDatasource;

	private List<Animal> animals;

	private List<Specie> species;
	private Spinner speciesSpinner;

	private SharedPreferences prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);

		Log.d(LOG_TAG, "onCreate");

		prefs = getSharedPreferences(getString(R.string.app_full_name),
				MODE_PRIVATE);

		// if(BuildConfig.DEBUG) {
		// deleteDatabase(DBHandler.DATABASE_NAME);
		// prefs.edit().putBoolean(FIRST_RUN, true).commit();
		// }

		animalDatasource = DBAnimalAdapter.getInstance(this);
		raceDatasource = DBRaceAdapter.getInstance(this);
		specieDatasource = DBSpecieAdapter.getInstance(this);

		if (prefs.getBoolean(FIRST_RUN, true)) {
			populateDefaultData();
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
		}
		;

		// Species
		DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance(this);
		specieDatasource.open();
		species = specieDatasource.list();
		specieDatasource.close();

		speciesSpinner = (Spinner) findViewById(R.id.spinner_main_specie);

		speciesSpinner.setAdapter(new SpecieArrayAdapter(this, species));

		speciesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				updateView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				updateView();
			}
		});
		//

	}

	private void actionNewAnimal() {
		Intent intent = new Intent(MainActivity.this, AnimalAddActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateData();
		updateView();
	}

	private void updateView() {
		ListView lv = (ListView) findViewById(R.id.list_animal);
		lv.setAdapter(new AnimalArrayAdapter(this, getFilteredAnimals()));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editAnimal(((Animal) parent.getItemAtPosition(position)));
			}
		});
	}

	private void updateData() {
		animalDatasource.open();
		animals = animalDatasource.list();
		animalDatasource.close();
	}

	private List<Animal> getFilteredAnimals() {
		Specie selectedSpecie = (Specie) speciesSpinner.getSelectedItem();
		List<Animal> filteredAnimals = new ArrayList<Animal>();

		if (selectedSpecie != null) {
			for (Animal a : animals) {
				if (a.getSpecieId() == selectedSpecie.getId()) {
					filteredAnimals.add(a);
				}
			}
		}

		return filteredAnimals;
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
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_list_animals:
			intent = new Intent(MainActivity.this, AnimalListActivity.class);
			startActivity(intent);
			break;
		case R.id.action_new_animal:
			actionNewAnimal();
			break;
		case R.id.action_events:
			Toast.makeText(this, "Event Selected", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_farms:
			intent = new Intent(this, FarmsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}

	private void populateDefaultData() {
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
}
