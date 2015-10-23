package com.timsoft.meurebanho.evolution.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;
import com.timsoft.meurebanho.specie.model.SpecieArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EvolutionActivity extends AppCompatActivity {

    private static final String LOG_TAG = "EvolutionActivity";

    private LineChart mChart;
    private Spinner specieSpinner;
    private List<Animal> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.evolution_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mChart = (LineChart) findViewById(R.id.evolution_chart);

        //Species
        DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance();
        specieDatasource.open();
        List<Specie> species = specieDatasource.list();
        specieDatasource.close();

        specieSpinner = (Spinner) findViewById(R.id.e_specie);
        specieSpinner.setAdapter(new SpecieArrayAdapter(this, species));

        specieSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateChart();
            }
        });
    }

    public void updateChart() {
        mChart.setDescription("Evolução do Rebanho");

        Specie specie = (Specie) specieSpinner.getSelectedItem();

        // Animals
        DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance();
        animalDatasource.open();
        animals = animalDatasource.list(specie.getId());
        animalDatasource.close();
        //

        setData();
    }

    private void setData() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -1);
        startDate.add(Calendar.MONTH, 1);
        startDate.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat monthDate = new SimpleDateFormat("MMM/yy");

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(startDate.getTime());

        for (int i = 0; i < 12; i++) {
            xVals.add(monthDate.format(currentDate.getTime()));
            int count = 0;
            for(Animal a : animals) {
                if(a.isAvailable(currentDate.getTime())) {
                    count++;
                }
            }

            Log.d(LOG_TAG, String.format("count=%s;i=%s;", count, i));
            yVals.add(new Entry(count, i));

            currentDate.add(Calendar.MONTH, 1);
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Animais disponíveis");

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.clear();
        mChart.setData(data);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateChart();
    }
    //http://stackoverflow.com/questions/21321789/android-datepicker-change-to-only-month-and-year
}