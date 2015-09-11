package com.timsoft.meurebanho.farm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.farm.db.DBFarmAdapter;
import com.timsoft.meurebanho.farm.model.Farm;

public class FarmEditActivity extends AppCompatActivity {

	private static final String LOG_TAG = "FarmEditActivity";
	
	private DBFarmAdapter farmDatasource;
	
	private EditText input;
	private Farm farm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farm_edit_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		farmDatasource = DBFarmAdapter.getInstance(this);
		
		Bundle data = getIntent().getExtras();
		farm = data.getParcelable("farm");
		
		input = (EditText) findViewById(R.id.input_edit_farm);
		input.setText(farm.getDescription());
		
		final ImageButton btnSalvar = (ImageButton) findViewById(R.id.btn_save_edit_farm);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	save();
            }
        });
        
        final ImageButton btnExcluir = (ImageButton) findViewById(R.id.btn_delete_farm);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	delete();
            }
        });
	}
	
    private void save() {
    	String farmName;
    	farmName = input.getText().toString().trim();
    	if("".equals(farmName)) {
    		input.setError(getResources().getString(R.string.alert_fill_farm_name));
    		return;
    	}
    	
    	farmDatasource.open();
    	farm.setDescription(farmName);
		farmDatasource.update(farm);
		farmDatasource.close();
		
		Intent intent = new Intent(this, FarmsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }
    
    private void delete() {
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.exclusion_confirmation))
        .setMessage(R.string.confirm_farm_exclusion)
        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	farmDatasource.open();
		    		farmDatasource.delete(farm);
		    		farmDatasource.close();
		    		
		    		Intent intent = new Intent(FarmEditActivity.this, FarmsActivity.class);
		    		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    		startActivity(intent);
		    		finish();    
		        }
		
		    })
	    .setNegativeButton(getResources().getString(R.string.no), null)
	    .show();
    }
}
