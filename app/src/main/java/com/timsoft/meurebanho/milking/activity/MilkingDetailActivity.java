package com.timsoft.meurebanho.milking.activity;

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
import com.timsoft.meurebanho.milking.db.DBMilkingAdapter;
import com.timsoft.meurebanho.milking.model.Milking;

import java.text.NumberFormat;

public class MilkingDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MilkingDetailActivity";
    private Milking milking;
    private int milkingId;
    private DBMilkingAdapter milkingDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.milking_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        milkingId = getIntent().getExtras().getInt(DBMilkingAdapter.ID);
        milkingDatasource = DBMilkingAdapter.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Milking data
        milkingDatasource.open();
        milking = milkingDatasource.get(milkingId);
        milkingDatasource.close();
        //

        ((TextView) findViewById(R.id.wd_date))
                .setText(MainActivity.getFormatedDate(milking.getDate()));

        ((TextView) findViewById(R.id.wd_weight))
                .setText(NumberFormat.getCurrencyInstance().format(milking.getWeight()));

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
        Intent intent = new Intent(this, MilkingMaintainActivity.class);

        intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
        intent.putExtra(DBAnimalAdapter.ID, milking.getId());

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
                        milkingDatasource.open();
                        milkingDatasource.delete(milking);
                        milkingDatasource.close();
                        MilkingDetailActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}