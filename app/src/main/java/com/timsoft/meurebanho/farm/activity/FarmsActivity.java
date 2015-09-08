package com.timsoft.meurebanho.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.farm.db.DBFarmAdapter;
import com.timsoft.meurebanho.farm.model.Farm;
import com.timsoft.meurebanho.farm.model.FarmArrayAdapter;

import java.util.List;

public class FarmsActivity extends AppCompatActivity {

	private static final String LOG_TAG = "FarmsActivity";
	private DBFarmAdapter farmsDatasource;
	private List<Farm> farms;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farms_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		 
		Log.d(LOG_TAG, "onCreate");
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_add_farm);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	addFarm();
            }
        });
        
        farmsDatasource = DBFarmAdapter.getInstance(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        updateData();
	}
	
	private void updateData() {
        farmsDatasource.open();
        farms = farmsDatasource.list();
        farmsDatasource.close();
        
		ListView lv = (ListView) findViewById(R.id.list_farms);
		lv.setAdapter(new FarmArrayAdapter(this, farms));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editFarm(((Farm) parent.getItemAtPosition(position)));
			}
		});
	}
	
	protected void editFarm(Farm f) {
		Intent intent = new Intent(this, FarmEditActivity.class);
		intent.putExtra("farm", f);
		startActivity(intent);
	}

	private void addFarm() {
		Intent intent = new Intent(this, FarmAddActivity.class);
		startActivity(intent);
	}
}
