package com.timsoft.meurebanho.animal.activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.db.DBAnimalHelper;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.race.model.RaceArrayAdapter;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;
import com.timsoft.meurebanho.specie.model.SpecieArrayAdapter;

public class AnimalAddActivity extends ActionBarActivity {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "AnimalAddActivity";
	
	private List<Specie> species;
	private List<Race> races;
	private Spinner speciesSpinner;
	private TextView tvId, tvBirthDate, tvAquisitionDate, tvSellDate, tvDeathDate, tvAquisitionValue, tvSellValue;
	private double aquisitionValue, sellValue;
	private DBAnimalAdapter animalDatasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_add_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//id
		tvId = (TextView) findViewById(R.id.input_add_animal_id);
		animalDatasource.open();
		tvId.setText(animalDatasource.getNextId());
		animalDatasource.close();
		//
		
		//Species
		DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance(this);
		specieDatasource.open();
		species = specieDatasource.list();
    	specieDatasource.close();

    	speciesSpinner = (Spinner) findViewById(R.id.spinner_add_animal_specie);
    	
    	speciesSpinner.setAdapter(new SpecieArrayAdapter(this, species));
    	
    	speciesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	updateRaces();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            	updateRaces();
            }
        });
    	//
    	
    	//Race
		DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance(this);
		raceDatasource.open();
		races = raceDatasource.list();
		raceDatasource.close();
    	//
		
		//Birth Date
		tvBirthDate = (TextView) findViewById(R.id.input_add_animal_birth_date);
		tvBirthDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnDateSetListener listener = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						updateBirthDate(year, monthOfYear, dayOfMonth);
					}
				};

				DatePickerDialog d = new DatePickerDialog(AnimalAddActivity.this, listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				d.show();
			}
		});
		//
		
		//Aquisition Date
		tvAquisitionDate = (TextView) findViewById(R.id.input_add_animal_aquisition_date);
		tvAquisitionDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnDateSetListener listener = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						updateAquisitionDate(year, monthOfYear, dayOfMonth);
					}
				};

				DatePickerDialog d = new DatePickerDialog(AnimalAddActivity.this, listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				d.show();
			}
		});
		//
		
		//Sell Date
		tvSellDate = (TextView) findViewById(R.id.input_add_animal_sell_date);
		tvSellDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnDateSetListener listener = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						updateSellDate(year, monthOfYear, dayOfMonth);
					}
				};

				DatePickerDialog d = new DatePickerDialog(AnimalAddActivity.this, listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				d.show();
			}
		});
		//
		
		//Death Date
		tvDeathDate = (TextView) findViewById(R.id.input_add_animal_death_date);
		tvDeathDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnDateSetListener listener = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						updateDeathDate(year, monthOfYear, dayOfMonth);
					}
				};

				DatePickerDialog d = new DatePickerDialog(AnimalAddActivity.this, listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				d.show();
			}
		});
		//
		
		//Aquisition Value
		tvAquisitionValue = (TextView) findViewById(R.id.input_add_animal_aquisition_value);
		
		tvAquisitionValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				try {
					aquisitionValue = Double.parseDouble(editable.toString().replace(',', '.'));
				}catch (ParseException e){
					aquisitionValue = 0;
				}
			}
		});
		//
		
		//Aquisition Value
		tvSellValue = (TextView) findViewById(R.id.input_add_animal_sell_value);
		
		tvSellValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				try {
					sellValue = Double.parseDouble(editable.toString().replace(',', '.'));
				}catch (ParseException e){
					sellValue = 0;
				}
			}
		});
		
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_save_add_animal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	save();
            }
        });
	}
	
	private void updateBirthDate(int year, int monthOfYear, int dayOfMonth) {
		updateDate(tvBirthDate, year, monthOfYear, dayOfMonth);
	}
	
	private void updateAquisitionDate(int year, int monthOfYear, int dayOfMonth) {
		updateDate(tvAquisitionDate, year, monthOfYear, dayOfMonth);
	}
	
	private void updateSellDate(int year, int monthOfYear, int dayOfMonth) {
		updateDate(tvSellDate, year, monthOfYear, dayOfMonth);
	}
	
	private void updateDeathDate(int year, int monthOfYear, int dayOfMonth) {
		updateDate(tvDeathDate, year, monthOfYear, dayOfMonth);
	}
	
	private void updateDate(TextView tv, int year, int monthOfYear, int dayOfMonth) {
		DateFormat f = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		tv.setText(f.format(c.getTime()));
	}
	
	private void updateRaces() {
		Specie selectedSpecie = (Specie) speciesSpinner.getSelectedItem();
		List<Race> filteredRaces = new ArrayList<Race>();
		if(selectedSpecie != null) {
			for(Race r : races) {
				if(r.getIdSpecie() == selectedSpecie.getId()) {
					filteredRaces.add(r);
				}
			}
		}
		
		Spinner racesSpinner = (Spinner) findViewById(R.id.spinner_add_animal_race);
    	
    	racesSpinner.setAdapter(new RaceArrayAdapter(this, filteredRaces));
	}
	
    private void save() {
    	DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance(this);
    	int id = Integer.parseInt((tvId.getText().toString()));
    	animalDatasource.open();
    	if(id == 0) {
    		tvId.setError(getResources().getString(R.string.alert_invalid_id));
    		return;
    	}
    	
    	if(animalDatasource.get(id) != null) {
    		tvId.setError(getResources().getString(R.string.alert_id_already_used));
    		return;
    	}
    	
    	Animal a = new Animal();
    	a.setId(id);
    	a.setName(((EditText) findViewById(R.id.input_add_animal_name)).getText().toString());
    	
    	return;
    	
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
    	
//    	animalDatasource.open();
//    	animalDatasource.create(a);
//    	animalDatasource.close();
//		
//		//Going back to MainActivity
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//		startActivity(intent);
//		finish();
    }
    
}
