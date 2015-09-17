package com.timsoft.meurebanho.animal.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;

import java.text.NumberFormat;

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
    }

	@Override
	public void onResume() {
		super.onResume();  // Always call the superclass method first

		//Animal data
		DBAnimalAdapter animalDatasource = DBAnimalAdapter.getInstance(this);
		animalDatasource.open();
		animal = animalDatasource.get(animalId);
		animalDatasource.close();
		//

		//Race data
		DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance(this);
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

		if(!TextUtils.isEmpty(animal.getEarTag())){
			((TextView) findViewById(R.id.ad_ear_tag))
					.setText(animal.getEarTag());
		} else {
			findViewById(R.id.ad_ear_tag_label).setVisibility(View.GONE);
			findViewById(R.id.ad_ear_tag).setVisibility(View.GONE);
		}

		if(animal.getPictureFile().exists()) {
			((ImageView) findViewById(R.id.ad_picture)).setImageBitmap(BitmapFactory.decodeFile(animal.getPictureFile().getPath()));
		}

		((TextView) findViewById(R.id.ad_race))
				.setText(race.getDescription());

		((TextView) findViewById(R.id.ad_sex))
				.setText(animal.getSexToDisplay());

		((TextView) findViewById(R.id.ad_birth_date))
				.setText(MainActivity.getFormatedDate(animal.getBirthDate()));

		((TextView) findViewById(R.id.ad_age))
				.setText(animal.getAgeInMonthsToDisplay());

		if(animal.getAquisitionDate() != null){
			((TextView) findViewById(R.id.ad_aquisition_date))
					.setText(MainActivity.getFormatedDate(animal.getAquisitionDate()));

			NumberFormat nf = NumberFormat.getCurrencyInstance();
			((TextView) findViewById(R.id.ad_aquisition_value))
					.setText(nf.format(animal.getAquisitionValue()));
		} else {
			findViewById(R.id.ad_aquisition_date_label).setVisibility(View.GONE);
			findViewById(R.id.ad_aquisition_date).setVisibility(View.GONE);
			findViewById(R.id.ad_aquisition_value).setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.animal_detail_activity_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit_animal:
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