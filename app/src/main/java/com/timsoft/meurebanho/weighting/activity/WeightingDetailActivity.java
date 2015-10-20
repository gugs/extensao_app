package com.timsoft.meurebanho.weighting.activity;

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
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.sale.activity.SaleMaintainActivity;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.weighting.db.DBWeightingAdapter;
import com.timsoft.meurebanho.weighting.model.Weighting;

import java.text.NumberFormat;

public class WeightingDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "WeightingDActivity";
    private Weighting weighting;
    private int weightingId;
    private DBWeightingAdapter weightingDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.weighting_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weightingId = getIntent().getExtras().getInt(DBWeightingAdapter.ID);
        weightingDatasource = DBWeightingAdapter.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Weighting data
        weightingDatasource.open();
        weighting = weightingDatasource.get(weightingId);
        weightingDatasource.close();
        //

        ((TextView) findViewById(R.id.wd_date))
                .setText(MainActivity.getFormatedDate(weighting.getDate()));

        ((TextView) findViewById(R.id.wd_weight))
                .setText(NumberFormat.getCurrencyInstance().format(weighting.getWeight()));

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
        Intent intent = new Intent(this, WeightingMaintainActivity.class);

        intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
        intent.putExtra(DBAnimalAdapter.ID, weighting.getId());

        startActivity(intent);
    }

    private void actionDelete() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.weighting_confirm_delete)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        weightingDatasource.open();
                        weightingDatasource.delete(weighting);
                        weightingDatasource.close();
                        WeightingDetailActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}