package com.timsoft.meurebanho.animal.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.event.model.Event;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.treatment.activity.TreatmentDetailActivity;
import com.timsoft.meurebanho.treatment.activity.TreatmentMaintainActivity;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;

public class AnimalDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AnimalDetailActivity";
    private Animal animal;
    private int animalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.animal_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animalId = getIntent().getExtras().getInt(DBAnimalAdapter.ID);

        final Button btn_add_treatment = (Button) findViewById(R.id.ad_add_treatment);
        btn_add_treatment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AnimalDetailActivity.this, TreatmentMaintainActivity.class);
                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);
                intent.putExtra(DBAnimalAdapter.ID, animal.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        //Animal data
        DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance();
        animalDatasource.open();
        animal = animalDatasource.get(animalId);
        animalDatasource.close();
        //

        //Race data
        DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance();
        raceDatasource.open();
        Race race = raceDatasource.get(animal.getRaceId());
        raceDatasource.close();
        //

        ((TextView) findViewById(R.id.ad_id))
                .setText(animal.getIdToDisplay());

        ((TextView) findViewById(R.id.ad_name))
                .setText(animal.getName());

        ((TextView) findViewById(R.id.ad_name))
                .setText(animal.getName());

        ((TextView) findViewById(R.id.ad_ear_tag))
                .setText(animal.getEarTag());

        if (animal.getPictureFile().exists()) {
            ((ImageView) findViewById(R.id.ad_picture)).setImageBitmap(BitmapFactory.decodeFile(animal.getPictureFile().getPath()));
        }

        ((TextView) findViewById(R.id.ad_race))
                .setText(race.getDescription());

        ((TextView) findViewById(R.id.ad_sex))
                .setText(animal.getSexToDisplay());

        ((TextView) findViewById(R.id.ad_age))
                .setText(animal.getAgeInMonthsToDisplay());

        TableLayout table = (TableLayout) findViewById(R.id.ad_events_table);
        table.removeAllViews();

        for (final Event e : animal.getEvents()) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.event_row, null);
            ((TextView) row.findViewById(R.id.er_icon)).setText(e.getType().getIcon());
            ((TextView) row.findViewById(R.id.er_date)).setText(MainActivity.getFormatedDate(e.getDate()));
            ((TextView) row.findViewById(R.id.er_description)).setText(e.getDescription());
            ((TextView) row.findViewById(R.id.er_plus)).setText(e.getType().isSelectable() ? "+" : "");

            if(e.getType().isSelectable()) {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (e.getType()) {
                            case TREATMENT:
                                Intent intent = new Intent(AnimalDetailActivity.this, TreatmentDetailActivity.class);
                                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
                                intent.putExtra(DBTreatmentAdapter.ID, e.getEntityId());
                                startActivity(intent);
                                break;
                            case DEATH:
                                break;
                            default:
                                Toast.makeText(AnimalDetailActivity.this, "No action defined for this event type: " + e.getType().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            table.addView(row);
        }
        table.requestLayout();     // Not sure if this is needed.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.animal_detail_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                actionEditAnimal();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void actionEditAnimal() {
        Intent intent = new Intent(this, AnimalMaintainActivity.class);

        intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
        intent.putExtra(DBAnimalAdapter.ID, animal.getId());

        startActivity(intent);
    }
}