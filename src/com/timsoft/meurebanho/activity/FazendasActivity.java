package com.timsoft.meurebanho.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.db.fazenda.DBFazendaAdapter;
import com.timsoft.meurebanho.model.Fazenda;

public class FazendasActivity extends ActionBarActivity {

	private static final String LOG_TAG = "FazendasActivity";
	private DBFazendaAdapter fazendaDatasource;
	List<Fazenda> fazendas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fazendas_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		 
		Log.d(LOG_TAG, "onCreate");
		
		final ImageButton button = (ImageButton) findViewById(R.id.btn_adicionar_fazenda);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	incluirFazenda();
            }
        });
        
        fazendaDatasource = DBFazendaAdapter.getInstance(this);
        
//        fazendaDatasource.open();
//        fazendas = fazendaDatasource.list();
//        fazendaDatasource.close();
//        
//		ListView lv = (ListView) findViewById(R.id.list_fazendas);
//		lv.setAdapter(new FazendaArrayAdapter(this, fazendas));
//		lv.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				editarFazenda(((Fazenda) parent.getItemAtPosition(position)));
//			}
//		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        updateData();
	}
	
	private void updateData() {
        fazendaDatasource.open();
        fazendas = fazendaDatasource.list();
        fazendaDatasource.close();
        
		ListView lv = (ListView) findViewById(R.id.list_fazendas);
		lv.setAdapter(new FazendaArrayAdapter(this, fazendas));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editarFazenda(((Fazenda) parent.getItemAtPosition(position)));
			}
		});
	}
	
	protected void editarFazenda(Fazenda f) {
		Intent intent = new Intent(this, EditarFazendaActivity.class);
		intent.putExtra("fazenda", f);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	private void incluirFazenda() {
		Intent intent = new Intent(this, IncluirFazendaActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
}
