package com.timsoft.meurebanho.treatment.activity;

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
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.treatment.model.Treatment;

import java.text.NumberFormat;

public class TreatmentDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "TreatmentDetailActivity";
    private Treatment treatment;
    private int treatmentId;
    private DBTreatmentAdapter treatmentDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.treatment_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        treatmentId = getIntent().getExtras().getInt(DBTreatmentAdapter.ID);
        treatmentDatasource = DBTreatmentAdapter.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Treatment data
        treatmentDatasource.open();
        treatment = treatmentDatasource.get(treatmentId);
        treatmentDatasource.close();
        //

        ((TextView) findViewById(R.id.td_date))
                .setText(MainActivity.getFormatedDate(treatment.getDate()));

        ((TextView) findViewById(R.id.td_reason))
                .setText(treatment.getReason());

        ((TextView) findViewById(R.id.td_medication))
                .setText(treatment.getMedication());

        ((TextView) findViewById(R.id.td_withdrawal_period))
                .setText(treatment.getWithdrawalPeriod());

        ((TextView) findViewById(R.id.td_cost))
                .setText(NumberFormat.getCurrencyInstance().format(treatment.getCost()));

        ((TextView) findViewById(R.id.td_notes))
                .setText(treatment.getNotes());

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

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void actionEdit() {
        Intent intent = new Intent(this, TreatmentMaintainActivity.class);

        intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
        intent.putExtra(DBTreatmentAdapter.ID, treatment.getId());

        startActivity(intent);
    }

    private void actionDelete() {
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.confirm_delete)
            .setMessage(R.string.confirm_delete_treatment)
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    treatmentDatasource.open();
                    treatmentDatasource.delete(treatment);
                    treatmentDatasource.close();
                }
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }
}