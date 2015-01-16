package com.timsoft.meurebanho.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;

public class AnimalAddActivity extends ActionBarActivity {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "AnimalAddActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_add_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_save_add_animal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	save();
            }
        });
	}
	
    private void save() {
    	DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance(this);
    	String animalName;
    	EditText input = (EditText) findViewById(R.id.input_add_animal_name);
    	
    	//Validating
    	animalName = input.getText().toString().trim();
    	if("".equals(animalName)) {
    		input.setError(getResources().getString(R.string.alert_fill_animal_name));
    		return;
    	}
    	
    	//Finding new id and saving
    	animalDatasource.open();
    	int id = 1;
    	while(animalDatasource.get(id) != null) {
    		id++;
    	}
    	Animal a = new Animal();
    	a.setId(id);
    	a.setName(animalName);
    	animalDatasource.create(a);
    	animalDatasource.close();
		
		//Going back to MainActivity
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }
}
