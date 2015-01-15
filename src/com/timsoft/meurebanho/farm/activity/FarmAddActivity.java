package com.timsoft.meurebanho.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.farm.db.DBFarmAdapter;
import com.timsoft.meurebanho.farm.model.Farm;

public class FarmAddActivity extends ActionBarActivity {

	private static final String LOG_TAG = "FarmAddActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_add_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_save_add_farm);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	salvar();
            }
        });
	}
	
    private void salvar() {
    	DBFarmAdapter farmDatasource = DBFarmAdapter.getInstance(this);
    	String farmName;
    	EditText input = (EditText) findViewById(R.id.input_add_farm);
    	
    	//Validating
    	farmName = input.getText().toString().trim();
    	if("".equals(farmName)) {
    		input.setError(getResources().getString(R.string.alert_fill_farm_name));
    		return;
    	}
    	
    	//Finding new id and saving
    	farmDatasource.open();
    	int id = 0;
    	while(farmDatasource.get(id) != null) {
    		id++;
    	}
    	Farm f = new Farm(id, farmName);
		farmDatasource.create(f);
		farmDatasource.close();
		
		//Going back to FarmsActivity
		Intent intent = new Intent(this, FarmsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }
}
