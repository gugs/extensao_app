package com.timsoft.meurebanho.activity;

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
import android.widget.ImageButton;
import android.widget.Toast;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.DBAdapterAbstract;
import com.timsoft.meurebanho.db.especie.DBEspecieAdapter;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.db.lote.DBLoteAdapter;
import com.timsoft.meurebanho.db.pasto.DBPastoAdapter;
import com.timsoft.meurebanho.db.raca.DBRacaAdapter;
import com.timsoft.meurebanho.model.Especie;
import com.timsoft.meurebanho.model.Lote;
import com.timsoft.meurebanho.model.Pasto;
import com.timsoft.meurebanho.model.Raca;

public class MainActivity extends ActionBarActivity {

	private static final String LOG_TAG = "InicioActivity";
	
	private static final String FIRST_RUN = "firstRun";

	@SuppressWarnings("rawtypes")
	private List<DBAdapterAbstract> dbAdapters;
	
	private DBEspecieAdapter especieDatasource;
	private DBFazendaAdapter fazendaDatasource;
	private DBLoteAdapter loteDatasource;
	private DBPastoAdapter pastoDatasource;
	private DBRacaAdapter racaDatasource;
	
	SharedPreferences prefs = null;

	@SuppressWarnings("rawtypes")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		especieDatasource = DBEspecieAdapter.getInstance(this);
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		loteDatasource = DBLoteAdapter.getInstance(this);
		pastoDatasource = DBPastoAdapter.getInstance(this);
		racaDatasource = DBRacaAdapter.getInstance(this);
		
		dbAdapters = new ArrayList<DBAdapterAbstract>();
		dbAdapters.add(especieDatasource);
		dbAdapters.add(fazendaDatasource);
		dbAdapters.add(loteDatasource);
		dbAdapters.add(pastoDatasource);
		dbAdapters.add(racaDatasource);
		
		//Limpa as bases se estiver em modo DEBUG
//		if(BuildConfig.DEBUG) {
//			
//			for(DBAdapterAbstract dbAdapter : dbAdapters) {
//				dbAdapter.open();
//				dbAdapter.clear();
//				dbAdapter.close();
//			}
//		}
		
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("Mensagem");
//	        builder.setMessage("Primeira utilização do Sistema!");
//			builder.setPositiveButton("Ok",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface arg0, int arg1) {
//							alerta.dismiss();
//						}
//					}
//			);
//			alerta = builder.create();
//	        alerta.show();
		
		prefs = getSharedPreferences(getString(R.string.app_full_name), MODE_PRIVATE);
		
		//Dados default
//		if(prefs.getBoolean(FIRST_RUN, true) || BuildConfig.DEBUG) {
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
		
//		final Button button = (Button) findViewById(R.id.button_fazendas);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	navegaFazendas();
//            }
//        });
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_adicionar_semovente);
	    button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	incluirFazenda();
	        }

	    });
	}
	
    public void incluirFazenda() {
    	Intent intent = new Intent(this, IncluirFazendaActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		
		//Direciona para a tela de cadastro de fazendas caso não haja uma fazenda
//		fazendaDatasource.open();
//		int qtdFazendas = fazendaDatasource.list().size();
//		fazendaDatasource.close();
//		
//		if(qtdFazendas == 0) {
//			incluirFazenda();
//		}
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
		// action with ID action_refresh was selected
		case R.id.action_eventos:
			Toast.makeText(this, "Eventos Selecionado", Toast.LENGTH_SHORT).show();
			break;
		// action with ID action_settings was selected
		case R.id.action_fazendas:
//			Toast.makeText(this, "Fazendas Selecionado", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, FazendasActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}
	
	private void incluirSemovente() {
		Intent intent = new Intent(this, IncluirSemoventeActivity.class);
		startActivity(intent);
	}

}
