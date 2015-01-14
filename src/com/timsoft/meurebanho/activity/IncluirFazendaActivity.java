package com.timsoft.meurebanho.activity;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.model.Fazenda;

public class IncluirFazendaActivity extends ActionBarActivity {

	private static final String LOG_TAG = "IncluirFazendaActivity";
	
	private DBFazendaAdapter fazendaDatasource;
	
	private EditText input;
	private AlertDialog alerta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incluir_fazenda_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		
		final ImageButton button = (ImageButton) findViewById(R.id.button_save_farm);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	salvar();
            }
        });
	}
	
    private void salvar() {
    	input = (EditText) findViewById(R.id.inputFazenda);
    	
    	fazendaDatasource.open();
    	int id = 0;
    	while(fazendaDatasource.get(id) != null) {
    		id++;
    	}
    	
//    	if(fazendaDatasource.get(id) != null) {
//    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    		builder.setTitle("Erro");
//            builder.setMessage("Já existe fazenda com este nome!");
//			builder.setPositiveButton("Ok",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface arg0, int arg1) {
//							alerta.dismiss();
//						}
//					}
//			);
//			alerta = builder.create();
//	        alerta.show();
//    	} else {
//        	Fazenda f = new Fazenda(id, input.getText().toString());
//    		fazendaDatasource.create(f);
//    	}
    	
    	Fazenda f = new Fazenda(id, input.getText().toString());
		fazendaDatasource.create(f);
		fazendaDatasource.close();
		
		Intent intent = new Intent(this, FazendasActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }
}
