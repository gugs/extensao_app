package com.timsoft.meurebanho.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.timsoft.meurebanho.BuildConfig;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.especie.DBEspecieAdapter;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.model.Especie;

public class InicioActivity extends Activity {

	private static final String LOG_TAG = "InicioActivity";
	
	private static final String FIRST_RUN = "firstRun";
	
	private DBFazendaAdapter fazendaDatasource;
	private DBEspecieAdapter especieDatasource;
	
	SharedPreferences prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG, "onCreate");
		
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		especieDatasource = DBEspecieAdapter.getInstance(this);
		
		fazendaDatasource.open();
		int qtdFazendas = fazendaDatasource.getFazendas().size();
		fazendaDatasource.close();
		
		//Limpa a base se estiver em modo DEBUG
		if(BuildConfig.DEBUG) {
			Log.d(LOG_TAG, "Qtd de fazendas: " + qtdFazendas);
			
			Log.d(LOG_TAG, "MODO DEBUG: Apagando fazendas");
			fazendaDatasource.open();
			fazendaDatasource.deleteFazendas();
			fazendaDatasource.close();
		}
		
		prefs = getSharedPreferences(getString(R.string.app_full_name), MODE_PRIVATE);
		
		//popular com dados bÃ¡sicos
		
		//Direciona para a tela de cadastro de fazendas caso nÃ£o haja uma fazenda
		if(qtdFazendas == 0) {
			navegaFazendas();
		}
		
		setContentView(R.layout.activity_inicio);
		
		final Button button = (Button) findViewById(R.id.button_fazendas);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	navegaFazendas();
            }
        });
	}
	
    public void navegaFazendas() {
    	Intent intent = new Intent(this, FazendasActivity.class);
		startActivity(intent);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, "onResume");
		
		if(prefs.getBoolean(FIRST_RUN, true) || BuildConfig.DEBUG) {
			especieDatasource.open();
			for(String e : Especie.especiesDefault) {
				especieDatasource.createEspecie(new Especie(e.hashCode(), e));
			}
			especieDatasource.close();
			
			//Carregar valores default em tabelas
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
		};
	}
}
