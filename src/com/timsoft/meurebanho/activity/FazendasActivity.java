package com.timsoft.meurebanho.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.model.Fazenda;

public class FazendasActivity extends Activity {

	private static final String LOG_TAG = "FazendasActivity";
	private DBFazendaAdapter fazendaDatasource;
	private EditText input;
	private AlertDialog alerta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG, "onCreate");
		
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		
		setContentView(R.layout.activity_fazendas);
		
		final Button button = (Button) findViewById(R.id.button_salvar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	salvar();
            }
        });
	}
	
    private void salvar() {
    	input = (EditText) findViewById(R.id.inputFazenda);
    	
    	fazendaDatasource.open();
    	int id = input.getText().toString().toUpperCase(Locale.US).hashCode();
    	if(fazendaDatasource.getFazenda(id) != null) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Erro");
            builder.setMessage("JÃ¡ existe fazenda com este nome!");
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
    		fazendaDatasource.createFazenda(f);
    	}
		fazendaDatasource.close();
    }
}
