package com.timsoft.meurebanho.treatment.activity;

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
import com.timsoft.meurebanho.infra.MoneyTextWatcher;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.treatment.model.Treatment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class TreatmentMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "TreatMaintainActivity";

    private Treatment editingTreatment;
    private String action;

    private TextView tvDate;
    private ImageButton btnClearDate;
    private EditText etReason, etMedication, etWithdrawalPeriod, etCost, etNotes;
    private DBTreatmentAdapter treatmentDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageButton btnClearTreatmentDate;

        treatmentDatasource = DBTreatmentAdapter.getInstance();

        Log.d(LOG_TAG, "onCreate");

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.check);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.treatment_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            //Caso seja uma inclusão de tratamento
            editingTreatment = new Treatment();
            editingTreatment.setAnimalId(getIntent().getExtras().getInt(DBAnimalAdapter.ID));

        } else {
            //Caso seja uma edição de tratamento
            treatmentDatasource.open();
            editingTreatment = treatmentDatasource.get(getIntent().getExtras().getInt(DBTreatmentAdapter.ID));
            treatmentDatasource.close();
        }

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.add_treatment));
        } else {
            setTitle(getResources().getString(R.string.edit_treatment));
        }
        //

        //Date
        tvDate = (TextView) findViewById(R.id.tm_date);
        tvDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnSetDate(this, tvDate));
        btnClearDate = (ImageButton) findViewById(R.id.tm_clear_date);
        btnClearDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnClearDate(tvDate, R.string.treatment_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            MeuRebanhoApp.updateDate(tvDate, new Date());
        } else {
            MeuRebanhoApp.updateDate(tvDate, editingTreatment.getDate());
        }
        //

        //Reason
        etReason = (EditText) findViewById(R.id.tm_reason);
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etReason.setText(editingTreatment.getReason());
        }
        //

        //Medication
        etMedication = ((EditText) findViewById(R.id.tm_medication));
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etMedication.setText(editingTreatment.getMedication());
        }
        //

        //Withdrawal Period
        etWithdrawalPeriod = ((EditText) findViewById(R.id.tm_withdrawal_period));
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etWithdrawalPeriod.setText(Integer.toString(editingTreatment.getWithdrawalPeriod()));
        }
        //

        //Cost
        etCost = (EditText) findViewById(R.id.tm_cost);

        etCost.addTextChangedListener(new MoneyTextWatcher(etCost));

        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etCost.setText(NumberFormat.getCurrencyInstance().format(editingTreatment.getCost()));
        }
        //

        //Notes
        etNotes = (EditText) findViewById(R.id.tm_notes);
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etNotes.setText(editingTreatment.getNotes());
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
        Treatment t = new Treatment();

        t.setId(editingTreatment.getId());
        t.setAnimalId(editingTreatment.getAnimalId());

        //Date
        try {
            t.setDate(MainActivity.getDateFormat().parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            Toast.makeText(this, R.string.treatment_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Reason
        if (!TextUtils.isEmpty(etReason.getText())) {
            t.setReason(etReason.getText().toString());
        } else {
            Toast.makeText(this, R.string.treatment_reason_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Medication
        if (!TextUtils.isEmpty(etMedication.getText())) {
            t.setMedication(etMedication.getText().toString());
        } else {
            Toast.makeText(this, R.string.treatment_medication_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Withdrawal Perioc
        if (!TextUtils.isEmpty(etWithdrawalPeriod.getText())) {
            try {
                t.setWithdrawalPeriod(Integer.parseInt(etWithdrawalPeriod.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.withdrawal_period_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //

        //Cost
        if (!TextUtils.isEmpty(etCost.getText().toString())) {
            try {
                double cost = NumberFormat.getCurrencyInstance().parse(etCost.getText().toString()).doubleValue();
                if (cost < 0) {
                    Toast.makeText(this, R.string.treatment_cost_invalid, Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setCost(cost);
            } catch (ParseException e) {
                Toast.makeText(this, R.string.treatment_cost_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //

        //Notes
        if (!TextUtils.isEmpty(etNotes.getText())) {
            t.setNotes(etNotes.getText().toString());
        }
        //

        treatmentDatasource.open();

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            treatmentDatasource.create(t);
        } else {
            treatmentDatasource.update(t);
        }

        treatmentDatasource.close();

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