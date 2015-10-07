package com.timsoft.meurebanho.animal.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.animal.model.AnimalBirthDateComparator;
import com.timsoft.meurebanho.animal.model.AnimalEarTagComparator;
import com.timsoft.meurebanho.animal.model.AnimalIDComparator;
import com.timsoft.meurebanho.animal.model.AnimalNameComparator;
import com.timsoft.meurebanho.animal.model.EnumFilterType;
import com.timsoft.meurebanho.animal.model.EnumSortType;
import com.timsoft.meurebanho.backup.activity.BackupActivity;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.specie.db.DBSpecieAdapter;
import com.timsoft.meurebanho.specie.model.Specie;

import java.util.ArrayList;
import java.util.Collections;
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

    private MenuItem menuItemFilter;
    private MenuItem menuItemRemoveFilter;

    private EnumFilterType filterType;
    private String filterParameter;

    private EnumSortType sortType = EnumSortType.ID;
    private boolean sortAscending = true;

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
                clearFilter();
                updateView();
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

    private void clearFilter() {
        filterType = null;
        filterParameter = null;
        menuItemFilter.setVisible(true);
        menuItemRemoveFilter.setVisible(false);
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
        boolean correctStatus = true, matchQueryString = true, matchFilter = true;

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

                if (filterType != null && !TextUtils.isEmpty(filterParameter)) {
                    matchFilter = false;
                    switch (filterType) {
                        case RACE:
                            if (a.getRaceId() == Integer.parseInt(filterParameter)) {
                                matchFilter = true;
                            }
                            break;

                        case SEX:
                            if (a.getSex().equalsIgnoreCase(filterParameter)) {
                                matchFilter = true;
                            }
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), "Tipo de filtro inválido: " + filterType, Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                if (correctStatus && matchQueryString && matchFilter) {
                    filteredAnimals.add(a);
                }
            }
        }

        switch (sortType) {
            case BIRTH_DATE:
                Collections.sort(filteredAnimals, new AnimalBirthDateComparator());
                break;

            case EAR_TAG:
                Collections.sort(filteredAnimals, new AnimalEarTagComparator());
                break;

            case ID:
                Collections.sort(filteredAnimals, new AnimalIDComparator());
                break;

            case NAME:
                Collections.sort(filteredAnimals, new AnimalNameComparator());
                break;

            default:
                Toast.makeText(getApplicationContext(), "Tipo de ordenação não definido: " + sortType, Toast.LENGTH_LONG).show();
                break;
        }

        if(!sortAscending){
            Collections.reverse(filteredAnimals);
        }

        return filteredAnimals;
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
                updateView();
                MenuItemCompat.collapseActionView(searchItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryString = s;
                updateView();
                return false;
            }
        });

        menuItemFilter = menu.findItem(R.id.action_filter);
        menuItemRemoveFilter = menu.findItem(R.id.action_remove_filter);

        menuItemRemoveFilter.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_backup:
                Intent intent = new Intent(this, BackupActivity.class);
                startActivity(intent);
                break;

            case R.id.action_filter:
                filterAnimal();
                break;

            case R.id.action_sort:
                sortAnimal();
                break;

            case R.id.action_remove_filter:
                clearFilter();
                updateView();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void sortAnimal() {
        final Dialog sortDialog = new Dialog(this);

        sortDialog.setContentView(R.layout.order_dialog);
        sortDialog.setTitle(R.string.select_sort_type);

        ((RadioButton) sortDialog.findViewById(R.id.od_ascending)).setChecked(true);
        ((RadioButton) sortDialog.findViewById(R.id.od_descending)).setChecked(false);

        ((TextView) sortDialog.findViewById(R.id.od_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = EnumSortType.ID;
                sortAscending = ((RadioButton) sortDialog.findViewById(R.id.od_ascending)).isChecked();
                sortDialog.dismiss();
                updateView();
            }
        });

        ((TextView) sortDialog.findViewById(R.id.od_ear_tag)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = EnumSortType.EAR_TAG;
                sortAscending = ((RadioButton) sortDialog.findViewById(R.id.od_ascending)).isChecked();
                sortDialog.dismiss();
                updateView();
            }
        });

        ((TextView) sortDialog.findViewById(R.id.od_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = EnumSortType.NAME;
                sortAscending = ((RadioButton) sortDialog.findViewById(R.id.od_ascending)).isChecked();
                sortDialog.dismiss();
                updateView();
            }
        });

        ((TextView) sortDialog.findViewById(R.id.od_birth_date)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = EnumSortType.BIRTH_DATE;
                sortAscending = ((RadioButton) sortDialog.findViewById(R.id.od_ascending)).isChecked();
                sortDialog.dismiss();
                updateView();
            }
        });

        sortDialog.show();
    }

    private void filterAnimal() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_filter_type)
                .setItems(R.array.filter_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                filterType = EnumFilterType.RACE;
                                showOptionsForFilterType();
                                break;
                            case 1:
                                filterType = EnumFilterType.SEX;
                                showOptionsForFilterType();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Tipo de filtro não definido: " + which, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .show();
    }

    private void showOptionsForFilterType() {
        switch (filterType) {
            case RACE:

                DBRaceAdapter raceDatasource = DBRaceAdapter.getInstance();
                raceDatasource.open();
                final List<Race> races = raceDatasource.listBySpecieId(getSpecies().get(pager.getCurrentItem()).getId());
                raceDatasource.close();

                List<CharSequence> raceNames = new ArrayList<>();
                for (Race r : races) {
                    raceNames.add(r.getDescription());
                }

                new AlertDialog.Builder(this)
                        .setTitle(R.string.select_filter_race)
                        .setItems(raceNames.toArray(new CharSequence[raceNames.size()]), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                menuItemFilter.setVisible(false);
                                menuItemRemoveFilter.setVisible(true);

                                filterParameter = Integer.toString(races.get(which).getId());
                                updateView();
                            }
                        })
                        .show();

                break;

            case SEX:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.select_filter_sex)
                        .setItems(R.array.sex_options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                menuItemFilter.setVisible(false);
                                menuItemRemoveFilter.setVisible(true);

                                switch (which) {
                                    case 0:
                                        filterParameter = "M";
                                        updateView();
                                        break;
                                    case 1:
                                        filterParameter = "F";
                                        updateView();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "Tipo de filtro não definido: " + which, Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .show();
                break;

            default:
                Toast.makeText(getApplicationContext(), "Tipo de filtro não esperado: " + filterType, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void updateView() {
        ((AnimalListFragment) findFragmentByPosition(pager.getCurrentItem())).updateView();
    }

    public List<Specie> getSpecies() {
        return species;
    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + pager.getId() + ":"
                        + fragmentPagerAdapter.getItemId(position));
    }
}
