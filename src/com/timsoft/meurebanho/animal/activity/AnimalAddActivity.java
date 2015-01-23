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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
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
	private Spinner racesSpinner, speciesSpinner;
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
		animalDatasource = DBAnimalAdapter.getInstance(this);
		animalDatasource.open();
		tvId.setText(Integer.toString(animalDatasource.getNextId()));
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
		
		racesSpinner = (Spinner) findViewById(R.id.spinner_add_animal_race);
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
	
	private DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
	}
	
	private void updateDate(TextView tv, int year, int monthOfYear, int dayOfMonth) {
		DateFormat f = getDateFormat();
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
		
    	racesSpinner.setAdapter(new RaceArrayAdapter(this, filteredRaces));
	}
	
    private void save() {
    	DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance(this);
    	Animal a = new Animal();
    	
    	// id
    	a.setId(Integer.parseInt((tvId.getText().toString())));
    	animalDatasource.open();
    	if(a.getId() == 0) {
    		Toast.makeText(this, R.string.alert_invalid_id, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	if(animalDatasource.get(a.getId()) != null) {
    		Toast.makeText(this, R.string.alert_id_already_used, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//
    	
    	//specie
    	Specie selectedSpecie = (Specie) speciesSpinner.getSelectedItem();
    	if(selectedSpecie == null) {
    		Toast.makeText(this, R.string.specie_not_selected, Toast.LENGTH_SHORT).show();
    		return;
    	} else {
    		a.setSpecieId(selectedSpecie.getId());
    	}
    	//
    	
    	//race
    	Race selectedRace = (Race) racesSpinner.getSelectedItem();
    	if(selectedRace == null) {
    		Toast.makeText(this, R.string.race_not_selected, Toast.LENGTH_SHORT).show();
    		return;
    	} else {
    		a.setRaceId(selectedRace.getId());
    	}
    	//
    	
    	//sex
    	if(((RadioButton) findViewById(R.id.radio_add_animal_sex_male)).isChecked()) {
    		a.setSex("M");
    	} else if(((RadioButton) findViewById(R.id.radio_add_animal_sex_female)).isChecked()) {
    		a.setSex("F");
    	}
    	
    	if(a.getSex() == null){
    		Toast.makeText(this, R.string.sex_not_selected, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//
    	
    	//Name
    	a.setName(((EditText) findViewById(R.id.input_add_animal_name)).getText().toString().trim());
    	//
    	
    	//Ear tag
    	a.setEarTag(((EditText) findViewById(R.id.input_add_animal_ear_tag)).getText().toString().trim());
    	//
    	
    	//Birth date
    	try {
			a.setBirthDate(getDateFormat().parse(tvBirthDate.getText().toString()));
		} catch (java.text.ParseException e) {
			Toast.makeText(this, R.string.birth_date_invalid, Toast.LENGTH_SHORT).show();
			return;
		}
    	//
    	
    	//Aquisition date
    	if(!"".equals(tvAquisitionDate.getText().toString().trim())){
	    	try {
				a.setAquisitionDate(getDateFormat().parse(tvAquisitionDate.getText().toString()));
			} catch (java.text.ParseException e) {
				Toast.makeText(this, R.string.aquisition_date_invalid, Toast.LENGTH_SHORT).show();
				return;
			}
    	}
    	
    	//Aquisition value
    	a.setAquisitionValue(aquisitionValue);
    	//
    	
    	//Sell date
    	if(!"".equals(tvSellDate.getText().toString())) {
	    	try {
				a.setSellDate(getDateFormat().parse(tvSellDate.getText().toString()));
			} catch (java.text.ParseException e) {
				Toast.makeText(this, R.string.sell_date_invalid, Toast.LENGTH_SHORT).show();
				return;
			}
    	}
    	//
    	
    	//Sell value
    	a.setSellValue(sellValue);
    	//
    	
    	//Death date
    	if(!"".equals(tvDeathDate.getText().toString())) {
	    	try {
				a.setSellDate(getDateFormat().parse(tvDeathDate.getText().toString()));
			} catch (java.text.ParseException e) {
				Toast.makeText(this, R.string.death_date_invalid, Toast.LENGTH_SHORT).show();
				return;
			}
    	}
    	//
    	
    	//Death reason
    	a.setDeathReason(((EditText) findViewById(R.id.input_add_animal_death_reason)).getText().toString().trim());
    	
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
