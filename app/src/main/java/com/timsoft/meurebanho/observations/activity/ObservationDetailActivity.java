package com.timsoft.meurebanho.observations.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.observations.db.DBObservationAdapter;
import com.timsoft.meurebanho.observations.model.Observation;

import java.text.DecimalFormat;

public class ObservationDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ObsDetailActivity";
    private Observation observation;
    private int observationId;
    private DBObservationAdapter observationDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.observation_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        observationId = getIntent().getExtras().getInt(DBObservationAdapter.ID);
        observationDatasource = DBObservationAdapter.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Observation data
        observationDatasource.open();
        observation = observationDatasource.get(observationId);
        observationDatasource.close();
        //

        ((TextView) findViewById(R.id.ocd_date))
                .setText(MainActivity.getFormatedDate(observation.getDateOccurrence()));

        ((TextView) findViewById(R.id.ocd_observation))
                .setText(observation.getObsDescription());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                actionEdit();
                break;

            case R.id.action_delete:
                actionDelete();
                break;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void actionEdit() {
        Intent intent = new Intent(this, ObservationMaintainActivity.class);

        intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
        intent.putExtra(DBAnimalAdapter.ID, observation.getIdObservation());

        startActivity(intent);
    }

    private void actionDelete() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.milking_confirm_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        observationDatasource.open();
                        observationDatasource.delete(observation);
                        observationDatasource.close();
                        ObservationDetailActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}