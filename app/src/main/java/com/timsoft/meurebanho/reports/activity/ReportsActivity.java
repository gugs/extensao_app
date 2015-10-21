package com.timsoft.meurebanho.reports.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;
import com.timsoft.meurebanho.specie.model.SpecieArrayAdapter;

import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ReportsActivity";

    private Spinner specieSpinner;
    private List<Animal> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.reports_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Species
        DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance();
        specieDatasource.open();
        List<Specie> species = specieDatasource.list();
        specieDatasource.close();

        specieSpinner = (Spinner) findViewById(R.id.r_specie);
        specieSpinner.setAdapter(new SpecieArrayAdapter(this, species));

        specieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateReport();
            }
        });
    }

    private void updateReport() {
        Specie specie = (Specie) specieSpinner.getSelectedItem();

        // Animals
        DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance();
        animalDatasource.open();
        animals = animalDatasource.list(specie.getId());
        animalDatasource.close();
        //

        int available = 0, sold = 0, dead = 0;
        int males = 0, females = 0;
        int a0_6 = 0, a7_12 = 0, a13_18 = 0, a19_24 = 0, a25_36 = 0, ao_36 = 0;

        for(Animal a : animals) {
            if(a.isAvailable()) {
                available++;
            }

            if(a.isSold()) {
                sold++;
            }

            if(a.isDead()) {
                dead++;
            }

            if(a.isAvailable()) {
                if (a.getSex().equalsIgnoreCase("M")) {
                    males++;
                } else {
                    females++;
                }

                if(a.getAgeInMonths() <= 6) {
                    a0_6++;
                } else if(a.getAgeInMonths() > 6 && a.getAgeInMonths() <= 12) {
                    a7_12++;
                } else if(a.getAgeInMonths() > 12 && a.getAgeInMonths() <= 18) {
                    a13_18++;
                } else if(a.getAgeInMonths() > 18 && a.getAgeInMonths() <= 24) {
                    a19_24++;
                } else if(a.getAgeInMonths() > 24 && a.getAgeInMonths() <= 36) {
                    a25_36++;
                } else {
                    ao_36++;
                }
            }
        }

        ((TextView) findViewById(R.id.r_available)).setText(Integer.toString(available));
        ((TextView) findViewById(R.id.r_sold)).setText(Integer.toString(sold));
        ((TextView) findViewById(R.id.r_dead)).setText(Integer.toString(dead));

        ((TextView) findViewById(R.id.r_males)).setText(Integer.toString(males));
        ((TextView) findViewById(R.id.r_females)).setText(Integer.toString(females));

        ((TextView) findViewById(R.id.r_a0_6)).setText(Integer.toString(a0_6));
        ((TextView) findViewById(R.id.r_a7_12)).setText(Integer.toString(a7_12));
        ((TextView) findViewById(R.id.r_a13_18)).setText(Integer.toString(a13_18));
        ((TextView) findViewById(R.id.r_a19_24)).setText(Integer.toString(a19_24));
        ((TextView) findViewById(R.id.r_a25_36)).setText(Integer.toString(a25_36));
        ((TextView) findViewById(R.id.r_ao_36)).setText(Integer.toString(ao_36));

        //Races
        DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance();
        raceDatasource.open();
        List<Race> races = raceDatasource.listBySpecieId(specie.getId());
        raceDatasource.close();

        TableLayout table = (TableLayout) findViewById(R.id.r_table);
        table.removeAllViews();

        for(Race r : races) {
            int qtd = 0;
            for(Animal a : animals) {
                if(a.getRaceId() == r.getId()) {
                    qtd++;
                }
            }

            if(qtd > 0) {
                TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.reports_race_row, null);

                ((TextView) row.findViewById(R.id.r_row_race_name)).setText(r.getDescription());
                ((TextView) row.findViewById(R.id.r_row_race_quantity)).setText(Integer.toString(qtd));
                table.addView(row);
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        updateReport();
    }
}