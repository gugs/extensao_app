package com.timsoft.meurebanho.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.model.Fazenda;

public class FazendasActivity extends ActionBarActivity {

	private static final String LOG_TAG = "FazendasActivity";
	private DBFazendaAdapter fazendaDatasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fazendas_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		 
		Log.d(LOG_TAG, "onCreate");
		
		final ImageButton button = (ImageButton) findViewById(R.id.button_add_farm);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	incluirFazenda();
            }
        });
        
        fazendaDatasource = DBFazendaAdapter.getInstance(this);
        
        fazendaDatasource.open();
        List<Fazenda> fazendas = fazendaDatasource.list();
        fazendaDatasource.close();
        
        final ListView listview = (ListView) findViewById(R.id.list_view_farms);
        
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, fazendas);
		
		listview.setAdapter(adapter);

	}
	
	private void incluirFazenda() {
		Intent intent = new Intent(this, IncluirFazendaActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	
	private class StableArrayAdapter extends ArrayAdapter<Fazenda> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<Fazenda> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i).getDescricao(), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position).getDescricao();
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}
}
