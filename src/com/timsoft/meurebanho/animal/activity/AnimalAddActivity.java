package com.timsoft.meurebanho.animal.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;

public class AnimalAddActivity extends ActionBarActivity {

	private static final String LOG_TAG = "AnimalAddActivity";
	
	private DBAnimalAdapter animalDatasource;
	
	private EditText input;
	private AlertDialog alerta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_add_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		animalDatasource = DBAnimalAdapter.getInstance(this);
		
		final Button button = (Button) findViewById(R.id.btn_save_add_animal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	save();
            }
        });
	}
	
    private void save() {
    }
}
