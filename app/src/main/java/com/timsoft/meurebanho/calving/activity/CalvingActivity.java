package com.timsoft.meurebanho.calving.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;


import java.util.List;

public class CalvingActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "CalvingListActivity";
    public static final String ID = "id";
    private List<Animal> animals;
    private int animalID = 0;
    private Animal animal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCalvingWindow");
        setContentView(R.layout.animal_parental_list);
        ListView listViews = (ListView) findViewById(R.id.chart_layout);

        Intent i = getIntent();
        animalID =  i.getExtras().getInt(DBAnimalAdapter.ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(animalID != 0)
        {
            //Animal
            DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance();
            animalDatasource.open();
            animal = animalDatasource.get(animalID);
            animals = animalDatasource.getParentsFrom(animal.getSex(), animal.getId());
            animalDatasource.close();
            CalvingArrayAdapter adapter = new CalvingArrayAdapter(this, animals);
            listViews.setAdapter(adapter);
        }

        if(animals.size() == 0)
        {
            Toast.makeText(getApplicationContext(),
                    "Animal sem registro de parição!", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }

}
