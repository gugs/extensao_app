package com.timsoft.meurebanho.death.activity;

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

import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class DeathMaintainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DeathMaintainActivity";

    private Animal editingAnimal;
    private String action;

    private TextView tvDate;
    private ImageButton btnClearDate;
    private EditText etReason;
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

        setContentView(R.layout.death_mantain_activity);

        action = getIntent().getStringExtra(MeuRebanhoApp.ACTION);

        animalDatasource.open();
        editingAnimal = animalDatasource.get(getIntent().getExtras().getInt(DBAnimalAdapter.ID));
        animalDatasource.close();

        //Activity Title
        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            setTitle(getResources().getString(R.string.death_add));
        } else {
            setTitle(getResources().getString(R.string.death_edit));
        }
        //

        //Date
        tvDate = (TextView) findViewById(R.id.dm_date);
        tvDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnSetDate(this, tvDate));
        btnClearDate = (ImageButton) findViewById(R.id.dm_clear_date);
        btnClearDate.setOnClickListener(MeuRebanhoApp.getOnClickListenerForBtnClearDate(tvDate, R.string.death_date_hint));

        if (action.equals(MeuRebanhoApp.ACTION_ADD)) {
            MeuRebanhoApp.updateDate(tvDate, new Date());
        } else {
            MeuRebanhoApp.updateDate(tvDate, editingAnimal.getDeathDate());
        }
        //

        //Reason
        etReason = (EditText) findViewById(R.id.dm_notes);
        if (action.equals(MeuRebanhoApp.ACTION_EDIT)) {
            etReason.setText(editingAnimal.getDeathReason());
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
            editingAnimal.setDeathDate(MainActivity.getDateFormat().parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            Toast.makeText(this, R.string.death_date_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        //

        //Reason
        if (!TextUtils.isEmpty(etReason.getText().toString())) {
            editingAnimal.setDeathReason(etReason.getText().toString());
        } else {
            editingAnimal.setDeathReason("");
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