package com.timsoft.meurebanho.activity;

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

public class EditarFazendaActivity extends ActionBarActivity {

	private static final String LOG_TAG = "IncluirFazendaActivity";
	
	private DBFazendaAdapter fazendaDatasource;
	
	private EditText input;
	private AlertDialog alerta;
	private Fazenda fazenda;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_fazenda_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		Log.d(LOG_TAG, "onCreate");
		
		fazendaDatasource = DBFazendaAdapter.getInstance(this);
		
		Bundle data = getIntent().getExtras();
		fazenda = (Fazenda) data.getParcelable("fazenda");
		
		input = (EditText) findViewById(R.id.input_editar_fazenda);
		input.setText(fazenda.getDescricao());
		
		final ImageButton btnSalvar = (ImageButton) findViewById(R.id.btn_salvar_edicao_fazenda);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	salvar();
            }
        });
        
        final ImageButton btnExcluir = (ImageButton) findViewById(R.id.btn_excluir_fazenda);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	excluir();
            }
        });
	}
	
    private void salvar() {
    	String nomeFazenda;
    	input = (EditText) findViewById(R.id.input_editar_fazenda);
    	nomeFazenda = input.getText().toString().trim();
    	if("".equals(nomeFazenda)) {
    		input.setError(getResources().getString(R.string.alerta_preencher_nome_fazenda));
    		return;
    	}
    	
    	fazendaDatasource.open();
    	fazenda.setDescricao(nomeFazenda);
		fazendaDatasource.update(fazenda);
		fazendaDatasource.close();
		
		Intent intent = new Intent(this, FazendasActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
    }
    
    private void excluir() {
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.confirmacao_de_exclusao))
        .setMessage(R.string.confirma_exclusao_fazenda)
        .setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	fazendaDatasource.open();
		    		fazendaDatasource.delete(fazenda);
		    		fazendaDatasource.close();
		    		
		    		Intent intent = new Intent(EditarFazendaActivity.this, FazendasActivity.class);
		    		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    		startActivity(intent);
		    		finish();    
		        }
		
		    })
	    .setNegativeButton(getResources().getString(R.string.nao), null)
	    .show();
    }
}
