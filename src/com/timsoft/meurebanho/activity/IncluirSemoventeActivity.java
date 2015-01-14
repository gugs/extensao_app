package com.timsoft.meurebanho.activity;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.model.Fazenda;

public class IncluirSemoventeActivity extends ActionBarActivity {

	private static final String LOG_TAG = "IncluirSemoventeActivity";
	
	private DBFazendaAdapter fazendaDatasource;
	
	private EditText input;
	private AlertDialog alerta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.incluir_fazenda_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		
		final Button button = (Button) findViewById(R.id.btn_salvar_inclusao_fazenda);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	salvar();
            }
        });
	}
	
    private void salvar() {
    	input = (EditText) findViewById(R.id.input_incluir_fazenda);
    	
    	fazendaDatasource.open();
    	int id = input.getText().toString().toUpperCase(Locale.US).hashCode();
    	if(fazendaDatasource.get(id) != null) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Erro");
            builder.setMessage("Já existe fazenda com este nome!");
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							alerta.dismiss();
						}
					}
			);
			alerta = builder.create();
	        alerta.show();
    	} else {
        	Fazenda f = new Fazenda(id, input.getText().toString());
    		fazendaDatasource.create(f);
    	}
		fazendaDatasource.close();
    }
}
