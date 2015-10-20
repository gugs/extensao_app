package com.timsoft.meurebanho.weighting.activity;

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
import com.timsoft.meurebanho.infra.DecimalTextWatcher;
import com.timsoft.meurebanho.weighting.db.DBWeightingAdapter;
import com.timsoft.meurebanho.weighting.model.Weighting;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class WeightingMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "WeightingMActivity";

    private Weighting editingWeighting;
    private String action;

    private TextView tvDate;
    private ImageButton btnClearDate;
    private EditText etWeight;
    private DBWeightingAdapter weightingDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weightingDatasource = weightingDatasource.getInstance();

        Log.d(LOG_TAG, "onCreate");

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_check_hd);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.weighting_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            //Caso seja uma inclusão de tratamento
            editingWeighting = new Weighting();
            editingWeighting.setAnimalId(getIntent().getExtras().getInt(DBAnimalAdapter.ID));

        } else {
            //Caso seja uma edição de tratamento
            weightingDatasource.open();
            editingWeighting = weightingDatasource.get(getIntent().getExtras().getInt(DBWeightingAdapter.ID));
            weightingDatasource.close();
        }

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.weighting_add));
        } else {
            setTitle(getResources().getString(R.string.weighting_edit));
        }
        //

        //Date
        tvDate = (TextView) findViewById(R.id.wm_date);
        tvDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnSetDate(this, tvDate));
        btnClearDate = (ImageButton) findViewById(R.id.wm_clear_date);
        btnClearDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnClearDate(tvDate, R.string.sale_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            MeuRebanhoApp.updateDate(tvDate, new Date());
        } else {
            MeuRebanhoApp.updateDate(tvDate, editingWeighting.getDate());
        }
        //

        //Weight
        etWeight = (EditText) findViewById(R.id.wm_weight);

        etWeight.addTextChangedListener(new DecimalTextWatcher(etWeight));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etWeight.setText((new DecimalFormat("#,###.00").format(editingWeighting.getWeight())));
        }
        //

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintain_actions, menu);
        return true;
    }

    private void save() {
        Weighting w = new Weighting();

        w.setId(editingWeighting.getId());
        w.setAnimalId(editingWeighting.getAnimalId());

        //Date
        try {
            w.setDate(MainActivity.getDateFormat().parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            Toast.makeText(this, R.string.weighting_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Weight
        try {
            double weight = NumberFormat.getNumberInstance().parse(etWeight.getText().toString()).doubleValue();
            if (weight <= 0) {
                Toast.makeText(this, R.string.weighting_weight_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            w.setWeight(weight);
        } catch (ParseException e) {
            Toast.makeText(this, R.string.weighting_weight_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        weightingDatasource.open();

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            weightingDatasource.create(w);
        } else {
            weightingDatasource.update(w);
        }

        weightingDatasource.close();

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