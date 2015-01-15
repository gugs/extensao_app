package com.timsoft.meurebanho;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.timsoft.meurebanho.animal.activity.AnimalAddActivity;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.animal.model.AnimalArrayAdapter;
import com.timsoft.meurebanho.farm.activity.FarmEditActivity;
import com.timsoft.meurebanho.farm.activity.FarmsActivity;
import com.timsoft.meurebanho.farm.db.DBFarmAdapter;
import com.timsoft.meurebanho.infra.db.DBAdapterAbstract;
import com.timsoft.meurebanho.lote.db.DBLoteAdapter;
import com.timsoft.meurebanho.lote.model.Lote;
import com.timsoft.meurebanho.pasto.db.DBPastoAdapter;
import com.timsoft.meurebanho.pasto.model.Pasto;
import com.timsoft.meurebanho.raca.db.DBRacaAdapter;
import com.timsoft.meurebanho.raca.model.Raca;
import com.timsoft.meurebanho.specie.db.DBEspecieAdapter;
import com.timsoft.meurebanho.specie.model.Especie;

public class MainActivity extends ActionBarActivity {

	private static final String LOG_TAG = "MainActivity";
	
	private static final String FIRST_RUN = "firstRun";

	@SuppressWarnings("rawtypes")
	private List<DBAdapterAbstract> dbAdapters;
	
	private DBAnimalAdapter animalDatasource;
	private DBEspecieAdapter especieDatasource;
	private DBFarmAdapter farmDatasource;
	private DBLoteAdapter loteDatasource;
	private DBPastoAdapter pastoDatasource;
	private DBRacaAdapter racaDatasource;
	
	private List<Animal> animals;
	
	SharedPreferences prefs = null;

	@SuppressWarnings("rawtypes")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		animalDatasource = DBAnimalAdapter.getInstance(this);
		especieDatasource = DBEspecieAdapter.getInstance(this);
		farmDatasource = DBFarmAdapter.getInstance(this);
		loteDatasource = DBLoteAdapter.getInstance(this);
		pastoDatasource = DBPastoAdapter.getInstance(this);
		racaDatasource = DBRacaAdapter.getInstance(this);
		
		dbAdapters = new ArrayList<DBAdapterAbstract>();
		dbAdapters.add(animalDatasource);
		dbAdapters.add(especieDatasource);
		dbAdapters.add(farmDatasource);
		dbAdapters.add(loteDatasource);
		dbAdapters.add(pastoDatasource);
		dbAdapters.add(racaDatasource);
		
		prefs = getSharedPreferences(getString(R.string.app_full_name), MODE_PRIVATE);
		
		//Carrega dados default
		if(prefs.getBoolean(FIRST_RUN, true)) {
			especieDatasource.open();
			for(Especie e : Especie.getEspeciesDefault()) {
				especieDatasource.create(e);
			}
			especieDatasource.close();
			
			loteDatasource.open();
			loteDatasource.create(new Lote(1, "Lote1"));
			loteDatasource.close();
			
			pastoDatasource.open();
			pastoDatasource.create(new Pasto(1, "Pasto1"));
			pastoDatasource.close();
			
			racaDatasource.open();
			for(Raca r : Raca.getRacasDefault()) {
				racaDatasource.create(r);
			}
			racaDatasource.close();
			
			//Carregar valores default em tabelas
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
		};
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_adicionar_semovente);
	    button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(MainActivity.this, AnimalAddActivity.class);
	    		startActivity(intent);
	        }
	    });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        updateData();
	}
	
	private void updateData() {
        animalDatasource.open();
        animals = animalDatasource.list();
        animalDatasource.close();
        
		ListView lv = (ListView) findViewById(R.id.list_animal);
		lv.setAdapter(new AnimalArrayAdapter(this, animals));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editAnimal(((Animal) parent.getItemAtPosition(position)));
			}
		});
	}
	
	protected void editAnimal(Animal a) {
		Intent intent = new Intent(this, FarmEditActivity.class);
		intent.putExtra("animal_id", a.getId());
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_eventos:
			Toast.makeText(this, "Eventos Selecionado", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_farms:
			Intent intent = new Intent(this, FarmsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}
}
