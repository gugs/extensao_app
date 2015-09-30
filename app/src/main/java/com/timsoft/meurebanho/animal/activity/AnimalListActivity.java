package com.timsoft.meurebanho.animal.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.backup.activity.BackupActivity;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class AnimalListActivity extends AppCompatActivity {
    private static final String LOG_TAG = "AnimalListActivity";

    private DBAnimalAdapter animalDatasource;
    private List<Animal> animals;

    private DBSpecieAdapter specieDatasource;
    private List<Specie> species;
    private AnimalListFragmentPagerAdapter fragmentPagerAdapter;

    private ViewPager pager;
    private SearchView searchView;

    private String queryString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_list_activity);

        // ActionBar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //

        // Animals
        animalDatasource = DBAnimalAdapter.getInstance();
        animalDatasource.open();
        animals = animalDatasource.list();
        animalDatasource.close();
        //

        // Species
        specieDatasource = DBSpecieAdapter.getInstance();
        specieDatasource.open();
        species = specieDatasource.list();
        specieDatasource.close();
        //

        // Pager
        pager = (ViewPager) findViewById(R.id.animal_list_pager);
        FragmentManager fm = getSupportFragmentManager();

        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(LOG_TAG, "onPageSelected: " + position);
                super.onPageSelected(position);
                actionBar.setSelectedNavigationItem(position);
                searchView.setIconified(true);
                searchView.setQuery("", false);
                queryString = "";
                ((AnimalListFragment) findFragmentByPosition(pager.getCurrentItem())).updateView();
            }
        };

        pager.setOnPageChangeListener(pageChangeListener);
        fragmentPagerAdapter = new AnimalListFragmentPagerAdapter(fm, species.size());
        pager.setAdapter(fragmentPagerAdapter);
        //

        // Tabs
        for (Specie s : species) {
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
        boolean correctStatus = true, matchQueryString = true;

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

                    case Animal.STATUS_RETIRED:
                        correctStatus = a.isRetired();
                        break;

                    default:
                        throw new RuntimeException("Invalid status: " + status);
                }

                if (!TextUtils.isEmpty(queryString)) {
                    matchQueryString = false;
                    if (a.getName().toUpperCase().contains(queryString.toUpperCase())) {
                        matchQueryString = true;
                    }

                    if (a.getIdToDisplay().toUpperCase().contains(queryString.toUpperCase())) {
                        matchQueryString = true;
                    }

                    if (a.getEarTag().toString().toUpperCase().contains(queryString.toUpperCase())) {
                        matchQueryString = true;
                    }
                }

                if (correctStatus && matchQueryString) {
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

        intent.putExtra(DBSpecieAdapter.ID, species.get(pager.getCurrentItem()).getId());
        intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.animal_list_actions, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryString = s;
                ((AnimalListFragment) findFragmentByPosition(pager.getCurrentItem())).updateView();
                MenuItemCompat.collapseActionView(searchItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryString = s;
                ((AnimalListFragment) findFragmentByPosition(pager.getCurrentItem())).updateView();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_backup:
                Intent intent = new Intent(this, BackupActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public List<Specie> getSpecies() {
        return species;
    }

    public void setSpecies(List<Specie> species) {
        this.species = species;
    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + pager.getId() + ":"
                        + fragmentPagerAdapter.getItemId(position));
    }
}
