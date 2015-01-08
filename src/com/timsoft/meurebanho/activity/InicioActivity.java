package com.timsoft.meurebanho.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.timsoft.meurebanho.BuildConfig;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.DBAdapterInterface;
import com.timsoft.meurebanho.db.especie.DBEspecieAdapter;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.db.lote.DBLoteAdapter;
import com.timsoft.meurebanho.db.pasto.DBPastoAdapter;
import com.timsoft.meurebanho.db.raca.DBRacaAdapter;
import com.timsoft.meurebanho.model.Especie;

public class InicioActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "InicioActivity";
	
	private static final String FIRST_RUN = "firstRun";

	@SuppressWarnings("rawtypes")
	private List<DBAdapterInterface> dbAdapters;
	
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
		setContentView(R.layout.activity_inicio);
		
		especieDatasource = DBEspecieAdapter.getInstance(this);
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		loteDatasource = DBLoteAdapter.getInstance(this);
		pastoDatasource = DBPastoAdapter.getInstance(this);
		racaDatasource = DBRacaAdapter.getInstance(this);
		
		dbAdapters = new ArrayList<DBAdapterInterface>();
		dbAdapters.add(especieDatasource);
		dbAdapters.add(fazendaDatasource);
		dbAdapters.add(loteDatasource);
		dbAdapters.add(pastoDatasource);
		dbAdapters.add(racaDatasource);
		
		//Limpa as bases se estiver em modo DEBUG
		if(BuildConfig.DEBUG) {
			for(DBAdapterInterface dbAdapter : dbAdapters) {
				dbAdapter.open();
				dbAdapter.clear();
				dbAdapter.close();
			}
		}
		
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
		
		fazendaDatasource.open();
		int qtdFazendas = fazendaDatasource.list().size();
		fazendaDatasource.close();
		
		prefs = getSharedPreferences(getString(R.string.app_full_name), MODE_PRIVATE);
		
		//popular com dados básicos
		
		//Direciona para a tela de cadastro de fazendas caso não haja uma fazenda
		if(qtdFazendas == 0) {
			navegaFazendas();
		} else {
			final Button button = (Button) findViewById(R.id.button_fazendas);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	navegaFazendas();
	            }
	        });
		}
	}
	
    public void navegaFazendas() {
    	Intent intent = new Intent(this, FazendasActivity.class);
		startActivity(intent);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		
		if(prefs.getBoolean(FIRST_RUN, true) || BuildConfig.DEBUG) {
			especieDatasource.open();
			for(String e : Especie.especiesDefault) {
				especieDatasource.create(new Especie(e.hashCode(), e));
			}
			especieDatasource.close();
			
			//Carregar valores default em tabelas
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
		};
	}
}
