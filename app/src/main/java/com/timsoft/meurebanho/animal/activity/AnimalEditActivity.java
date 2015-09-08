package com.timsoft.meurebanho.animal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;

public class AnimalEditActivity extends AppCompatActivity {

	private static final String LOG_TAG = "AnimalEditActivity";
	
	private DBAnimalAdapter animalDatasource;
	
	private EditText input;
	private Animal animal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_edit_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		animalDatasource = DBAnimalAdapter.getInstance(this);
		
		animalDatasource.open();
		animal = animalDatasource.get(getIntent().getExtras().getInt("animal_id"));
		animalDatasource.close();
		
		input = (EditText) findViewById(R.id.input_edit_animal_name);
		input.setText(animal.getName());
		
		final ImageButton btnSalvar = (ImageButton) findViewById(R.id.btn_save_edit_animal);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	save();
            }
        });
        
        final ImageButton btnExcluir = (ImageButton) findViewById(R.id.btn_delete_animal);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	delete();
            }
        });
	}
	
    private void save() {
    	String animalName;
    	animalName = input.getText().toString().trim();
    	if("".equals(animalName)) {
    		input.setError(getResources().getString(R.string.alert_fill_animal_name));
    		return;
    	}
    	
    	animalDatasource.open();
    	animal.setName(animalName);
    	animalDatasource.update(animal);
    	animalDatasource.close();
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }
    
    private void delete() {
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.exclusion_confirmation))
        .setMessage(R.string.confirm_animal_exclusion)
        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	animalDatasource.open();
		        	animalDatasource.delete(animal);
		    		animalDatasource.close();
		    		
		    		Intent intent = new Intent(AnimalEditActivity.this, MainActivity.class);
		    		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    		startActivity(intent);
		    		finish();    
		        }
		
		    })
	    .setNegativeButton(getResources().getString(R.string.no), null)
	    .show();
    }
}
