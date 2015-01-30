package com.timsoft.meurebanho.animal.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.farm.activity.FarmsActivity;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

@SuppressWarnings("deprecation")
public class AnimalListActivity extends ActionBarActivity {
	private int selectedSpecieId;
	
	private DBAnimalAdapter animalDatasource;
	private DBSpecieAdapter specieDatasource;

	private List<Animal> animals;

	private List<Specie> species;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		animalDatasource = DBAnimalAdapter.getInstance(this);
		specieDatasource = DBSpecieAdapter.getInstance(this);
		
		// Animals
		animalDatasource.open();
		animals = animalDatasource.list();
		animalDatasource.close();
		//
		
		// Species
		DBSpecieAdapter specieDatasource = DBSpecieAdapter.getInstance(this);
		specieDatasource.open();
		species = specieDatasource.list();
		specieDatasource.close();
		//
		
		for(Specie s : species){
			Tab tab = actionBar.newTab();   
		    tab = actionBar.newTab();
		    tab.setText(s.getDescription());
		    AnimalListActivityTabListener<AnimalListFragment> tl = new AnimalListActivityTabListener<AnimalListFragment>(this, Integer.toString(s.getId()), AnimalListFragment.class);
		    tab.setTabListener(tl);
		    actionBar.addTab(tab);
		}
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.show();
	}
	
	private class AnimalListActivityTabListener<T extends Fragment> implements
	    ActionBar.TabListener {
	    private Fragment mFragment;
	    private final ActionBarActivity mActivity;
	    private final String mTag;
	    private final Class<T> mClass;
	
	    /**
	     * Constructor used each time a new tab is created.
	     * 
	     * @param activity
	     *            The host Activity, used to instantiate the fragment
	     * @param tag
	     *            The identifier tag for the fragment
	     * @param clz
	     *            The fragment's Class, used to instantiate the fragment
	     */
	    public AnimalListActivityTabListener(ActionBarActivity activity, String tag, Class<T> clz) {
	        mActivity = activity;
	        mTag = tag;
	        mClass = clz;
	    }
	
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    	
	    	((AnimalListActivity)mActivity).setSelectedSpecieId(Integer.parseInt(mTag));
	    	
	        mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
	        // Check if the fragment is already initialized
	        if (mFragment == null) {
	            // If not, instantiate and add it to the activity
	            mFragment = Fragment.instantiate(mActivity, mClass.getName());
	            ft.add(android.R.id.content, mFragment, mTag);
	        } else {
	            // If it exists, simply attach it in order to show it
	            ft.attach(mFragment);
	        }
	    }
	
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	    }
	
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    }
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
	
	public int getSelectedSpecieId() {
		return selectedSpecieId;
	}

	public void setSelectedSpecieId(int selectedSpecieId) {
		this.selectedSpecieId = selectedSpecieId;
	}

	public List<Animal> getAnimals() {
		return animals;
	}
	
	public List<Animal> getAnimals(int specieId, int status) {
		List<Animal> filteredAnimals = new ArrayList<Animal>();
		boolean correctStatus = true;
		
		for (Animal a : animals) {
			if (a.getSpecieId() == getSelectedSpecieId()) {
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
	
	private void actionNewAnimal() {
		Intent intent = new Intent(this, AnimalAddActivity.class);
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
		case R.id.action_new_animal:
			actionNewAnimal();
			break;
			
		case R.id.action_events:
			Toast.makeText(this, "Event Selected", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.action_farms:
			Intent intent = new Intent(this, FarmsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

		return true;
	}
}
