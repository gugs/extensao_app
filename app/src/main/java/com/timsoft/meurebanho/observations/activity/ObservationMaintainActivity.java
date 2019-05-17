package com.timsoft.meurebanho.observations.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.milking.db.DBMilkingAdapter;
import com.timsoft.meurebanho.observations.db.DBObservationAdapter;
import com.timsoft.meurebanho.observations.model.Observation;

import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class ObservationMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ObservationMActivity";

    private Observation editingObservation;
    private String action;

    private TextView tvDate;
    private ImageButton btnClearDate;
    private EditText etObservation;
    private DBObservationAdapter observationDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        observationDatasource = observationDatasource.getInstance();

        Log.d(LOG_TAG, "onCreate");

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_check_hd);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.observation_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            //Caso seja uma inclusão de observacao
            editingObservation = new Observation();
            editingObservation.setAnimalID(getIntent().getExtras().getInt(DBAnimalAdapter.ID));

        } else {
            //Caso seja uma edição de observacao
            observationDatasource.open();
            editingObservation= observationDatasource.get(getIntent().getExtras().getInt(DBObservationAdapter.ID));
            observationDatasource.close();
        }

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.observation_add));
        } else {
            setTitle(getResources().getString(R.string.observation_edit));
        }
        //

        //Date
        tvDate = (TextView) findViewById(R.id.oc_date);
        tvDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnSetDate(this,
                tvDate));
        btnClearDate = (ImageButton) findViewById(R.id.oc_clear_date);
        btnClearDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnClearDate(tvDate,
                R.string.observation_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            MeuRebanhoApp.updateDate(tvDate, new Date());
        } else {
            MeuRebanhoApp.updateDate(tvDate, editingObservation.getDateOccurrence());
        }
        //

        //Weight
        etObservation = (EditText) findViewById(R.id.oc_observation);
        etObservation.setText(editingObservation.getObsDescription());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintain_actions, menu);
        return true;
    }

    private void save() {
        Observation m = new Observation();

        m.setIdObservation(editingObservation.getIdObservation());
        m.setAnimalID(editingObservation.getAnimalID());

        //Date
        try {
            m.setDateOccurrence(MainActivity.getDateFormat().parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            Toast.makeText(this, R.string.observation_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Weight
        try {
            String value = etObservation.getText().toString();
            if (value.length() == 0) {
                Toast.makeText(this, R.string.milking_weight_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            m.setObsDescription(value);
        } catch (Exception e) {
            Toast.makeText(this, R.string.milking_weight_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        observationDatasource.open();

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            observationDatasource.create(m);
        } else {
            observationDatasource.update(m);
        }

        observationDatasource.close();

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Substitui a ação de voltar pela ação de salvar
            case android.R.id.home:
                save();
                break;

            //Cancela as alterações
            case R.id.action_discard_changes:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}