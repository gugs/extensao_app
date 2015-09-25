package com.timsoft.meurebanho.sale.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.infra.MoneyTextWatcher;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class SaleMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SaleMaintainActivity";

    private Animal editingAnimal;
    private String action;

    private TextView tvDate;
    private ImageButton btnClearDate;
    private EditText etValue, etBuyer, etNotes;
    private DBAnimalAdapter animalDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animalDatasource = animalDatasource.getInstance();

        Log.d(LOG_TAG, "onCreate");

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.check);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.sale_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        animalDatasource.open();
        editingAnimal = animalDatasource.get(getIntent().getExtras().getInt(DBAnimalAdapter.ID));
        animalDatasource.close();

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.sale_add));
        } else {
            setTitle(getResources().getString(R.string.sale_edit));
        }
        //

        //Date
        tvDate = (TextView) findViewById(R.id.sm_date);
        tvDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnSetDate(this, tvDate));
        btnClearDate = (ImageButton) findViewById(R.id.sm_clear_date);
        btnClearDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnClearDate(tvDate, R.string.sale_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            MeuRebanhoApp.updateDate(tvDate, new Date());
        } else {
            MeuRebanhoApp.updateDate(tvDate, editingAnimal.getSellDate());
        }
        //

        //Value
        etValue = (EditText) findViewById(R.id.sm_value);

        etValue.addTextChangedListener(new MoneyTextWatcher(etValue));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etValue.setText(NumberFormat.getCurrencyInstance().format(editingAnimal.getSellValue()));
        }
        //

        //Buyer
        etBuyer = (EditText) findViewById(R.id.sm_buyer);
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etBuyer.setText(editingAnimal.getBuyerName());
        }
        //

        //Notes
        etNotes = (EditText) findViewById(R.id.sm_notes);
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etNotes.setText(editingAnimal.getSellNotes());
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
        //Date
        try {
            editingAnimal.setSellDate(MainActivity.getDateFormat().parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            Toast.makeText(this, R.string.sale_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Value
        try {
            double value = NumberFormat.getCurrencyInstance().parse(etValue.getText().toString()).doubleValue();
            if (value <= 0) {
                Toast.makeText(this, R.string.sale_value_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            editingAnimal.setSellValue(value);
        } catch (ParseException e) {
            Toast.makeText(this, R.string.sale_value_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Buyer
        if (!TextUtils.isEmpty(etBuyer.getText())) {
            editingAnimal.setBuyerName(etBuyer.getText().toString());
        } else {
            Toast.makeText(this, R.string.sale_buyer_name_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Notes
        if (!TextUtils.isEmpty(etNotes.getText().toString())) {
            editingAnimal.setSellNotes(etNotes.getText().toString());
        } else {
            editingAnimal.setSellNotes("");
        }
        //

        animalDatasource.open();

        animalDatasource.update(editingAnimal);

        animalDatasource.close();

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