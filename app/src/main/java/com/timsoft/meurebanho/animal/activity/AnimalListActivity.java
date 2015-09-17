package com.timsoft.meurebanho.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class AnimalListActivity extends AppCompatActivity {
	private int selectedSpecieId;

	private DBAnimalAdapter animalDatasource;
	private List<Animal> animals;

	private DBSpecieAdapter specieDatasource;
	private List<Specie> species;
	
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animal_list_activity);

		// ActionBar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//
		
		// Animals
		animalDatasource = DBAnimalAdapter.getInstance(this);
		animalDatasource.open();
		animals = animalDatasource.list();
		animalDatasource.close();
		//
		
		// Species
		specieDatasource = DBSpecieAdapter.getInstance(this);
		specieDatasource.open();
		species = specieDatasource.list();
		specieDatasource.close();
		//
		
		// Pager
		pager = (ViewPager) findViewById(R.id.animal_list_pager);
        FragmentManager fm = getSupportFragmentManager();
 
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                actionBar.setSelectedNavigationItem(position);
            }
        };
 
        pager.setOnPageChangeListener(pageChangeListener);
        AnimalListFragmentPagerAdapter fragmentPagerAdapter = new AnimalListFragmentPagerAdapter(fm, species.size());
        pager.setAdapter(fragmentPagerAdapter);
        //
        
        // Tabs
        for(Specie s : species){
			Tab tab = actionBar.newTab();   
		    tab.setText(s.getDescription());

		    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	 
	            @Override
	            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	            }
	 
	            @Override
	            public void onTabSelected(Tab tab, FragmentTransaction ft) {
	                pager.setCurrentItem(tab.getPosition());
	            }
	 
	            @Override
	            public void onTabReselected(Tab tab, FragmentTransaction ft) {
	            }
	        };
	        
		    tab.setTabListener(tabListener);
		    actionBar.addTab(tab);
		}
        //
        
	    actionBar.show();
	    
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateData();
	}
	
	private void updateData() {
		animalDatasource.open();
		animals = animalDatasource.list();
		animalDatasource.close();
	}
	
	public List<Animal> getAnimals() {
		return animals;
	}
	
	public List<Animal> getAnimals(int specieId, int status) {
		List<Animal> filteredAnimals = new ArrayList<>();
		boolean correctStatus = true;
		
		for (Animal a : animals) {
			if (a.getSpecieId() == specieId) {
				switch (status) {
				case Animal.STATUS_AVAILABLE:
					correctStatus = a.isAvailable(); 
					break;
					
				case Animal.STATUS_SOLD:
					correctStatus = a.isSold(); 
					break;
					
				case Animal.STATUS_DEAD:
					correctStatus = a.isDead(); 
					break;
					
				default:
					throw new RuntimeException("Invalid status: " + status);
				}
				
				if(correctStatus) {
					filteredAnimals.add(a);
				}
			}
		}

		return filteredAnimals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}
	
	public void actionNewAnimal() {
		Intent intent = new Intent(this, AnimalMaintainActivity.class);

//		Bundle bundle = new Bundle();
//		bundle.putParcelable(Specie.class.toString(), species.get(pager.getCurrentItem()));
//		intent.putExtras(bundle);

        intent.putExtra(DBSpecieAdapter.ID, species.get(pager.getCurrentItem()).getId());
		intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);

		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.animal_list_activity_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast.makeText(this, "Busca Animal", Toast.LENGTH_SHORT).show();
			break;
			
//		case R.id.action_events:
//			Toast.makeText(this, "Event Selected", Toast.LENGTH_SHORT).show();
//			break;
			
//		case R.id.action_farms:
//			Intent intent = new Intent(this, FarmsActivity.class);
//			startActivity(intent);
//			break;
		default:
			break;
		}

		return true;
	}

	public List<Specie> getSpecies() {
		return species;
	}

	public void setSpecies(List<Specie> species) {
		this.species = species;
	}
}
