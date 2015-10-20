package com.timsoft.meurebanho.milking.activity;

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
import com.timsoft.meurebanho.milking.db.DBMilkingAdapter;
import com.timsoft.meurebanho.milking.model.Milking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class MilkingMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MilkingMActivity";

    private Milking editingMilking;
    private String action;

    private TextView tvDate;
    private ImageButton btnClearDate;
    private EditText etWeight;
    private DBMilkingAdapter milkingDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        milkingDatasource = milkingDatasource.getInstance();

        Log.d(LOG_TAG, "onCreate");

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_check_hd);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.milking_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            //Caso seja uma inclusão de tratamento
            editingMilking = new Milking();
            editingMilking.setAnimalId(getIntent().getExtras().getInt(DBAnimalAdapter.ID));

        } else {
            //Caso seja uma edição de tratamento
            milkingDatasource.open();
            editingMilking = milkingDatasource.get(getIntent().getExtras().getInt(DBMilkingAdapter.ID));
            milkingDatasource.close();
        }

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.milking_add));
        } else {
            setTitle(getResources().getString(R.string.milking_edit));
        }
        //

        //Date
        tvDate = (TextView) findViewById(R.id.mm_date);
        tvDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnSetDate(this, tvDate));
        btnClearDate = (ImageButton) findViewById(R.id.mm_clear_date);
        btnClearDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnClearDate(tvDate, R.string.sale_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            MeuRebanhoApp.updateDate(tvDate, new Date());
        } else {
            MeuRebanhoApp.updateDate(tvDate, editingMilking.getDate());
        }
        //

        //Weight
        etWeight = (EditText) findViewById(R.id.mm_weight);

        etWeight.addTextChangedListener(new DecimalTextWatcher(etWeight));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etWeight.setText((new DecimalFormat("#,###.00").format(editingMilking.getWeight())));
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
        Milking m = new Milking();

        m.setId(editingMilking.getId());
        m.setAnimalId(editingMilking.getAnimalId());

        //Date
        try {
            m.setDate(MainActivity.getDateFormat().parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            Toast.makeText(this, R.string.milking_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Weight
        try {
            double value = NumberFormat.getNumberInstance().parse(etWeight.getText().toString()).doubleValue();
            if (value <= 0) {
                Toast.makeText(this, R.string.milking_weight_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            m.setWeight(value);
        } catch (ParseException e) {
            Toast.makeText(this, R.string.milking_weight_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        milkingDatasource.open();

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            milkingDatasource.create(m);
        } else {
            milkingDatasource.update(m);
        }

        milkingDatasource.close();

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