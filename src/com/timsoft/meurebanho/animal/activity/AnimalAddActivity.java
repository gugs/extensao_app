package com.timsoft.meurebanho.animal.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;
import com.timsoft.meurebanho.specie.model.SpecieArrayAdapter;

public class AnimalAddActivity extends ActionBarActivity {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "AnimalAddActivity";
	
	private List<Specie> species;
	private List<Race> races;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_add_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance(this);
		specieDatasource.open();
		species = specieDatasource.list();
    	specieDatasource.close();

    	List<String> speciesStringList = new ArrayList<String>();
    	for(Specie s : species) {
    		speciesStringList.add(s.getDescription());
    	}
    	
    	Spinner speciesSpinner = (Spinner) findViewById(R.id.spinner_add_animal_specie);
    	
//    	ArrayAdapter<String> speciesDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, speciesStringList);
//    	speciesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    	speciesSpinner.setAdapter(speciesDataAdapter);
    	
    	speciesSpinner.setAdapter(new SpecieArrayAdapter(this, species));
    	
		DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance(this);
		raceDatasource.open();
		races = raceDatasource.list();
		raceDatasource.close();
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_save_add_animal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	save();
            }
        });
	}
	
    private void save() {
    	DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance(this);
    	Animal a = new Animal();
    	
    	a.setId(Integer.parseInt(((EditText) findViewById(R.id.input_add_animal_id)).getText().toString()));
    	a.setName(((EditText) findViewById(R.id.input_add_animal_name)).getText().toString());
    	
    	//Validating
//    	if("".equals(animalName)) {
//    		input.setError(getResources().getString(R.string.alert_fill_animal_name));
//    		return;
//    	}
    	
    	//Finding new id and saving
//    	int id = 1;
//    	while(animalDatasource.get(id) != null) {
//    		id++;
//    	}
//    	Animal a = new Animal();
//    	a.setId(id);
//    	a.setName(animalName);
    	
    	animalDatasource.open();
    	animalDatasource.create(a);
    	animalDatasource.close();
		
		//Going back to MainActivity
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }
}
