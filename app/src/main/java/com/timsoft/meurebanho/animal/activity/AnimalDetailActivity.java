package com.timsoft.meurebanho.animal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.timsoft.meurebanho.MainActivity;
import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.category.db.DBCategoryAdapter;
import com.timsoft.meurebanho.category.model.Category;
import com.timsoft.meurebanho.death.activity.DeathDetailActivity;
import com.timsoft.meurebanho.death.activity.DeathMaintainActivity;
import com.timsoft.meurebanho.event.model.Event;
import com.timsoft.meurebanho.milking.activity.MilkingDetailActivity;
import com.timsoft.meurebanho.milking.activity.MilkingMaintainActivity;
import com.timsoft.meurebanho.milking.db.DBMilkingAdapter;
import com.timsoft.meurebanho.race.db.DBRaceAdapter;
import com.timsoft.meurebanho.race.model.Race;
import com.timsoft.meurebanho.sale.activity.SaleDetailActivity;
import com.timsoft.meurebanho.sale.activity.SaleMaintainActivity;
import com.timsoft.meurebanho.treatment.activity.TreatmentDetailActivity;
import com.timsoft.meurebanho.treatment.activity.TreatmentMaintainActivity;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.weighting.activity.WeightingDetailActivity;
import com.timsoft.meurebanho.weighting.activity.WeightingMaintainActivity;
import com.timsoft.meurebanho.weighting.db.DBWeightingAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnimalDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AnimalDetailActivity";
    private Animal animal;
    private int animalId;
    private boolean famVisible = false;
    private DBAnimalAdapter animalDatasource;
    private List<Integer> famElementsIds;
    private MenuItem mnuRetire, mnuUndoRetire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.animal_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animalId = getIntent().getExtras().getInt(DBAnimalAdapter.ID);

        final LinearLayout llOverlay = (LinearLayout) findViewById(R.id.ad_ll_overlay);
        llOverlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famVisible = false;
                updateFAM();
            }
        });

        final FloatingActionButton btnAddTreatment = (FloatingActionButton) findViewById(R.id.ad_btn_register_treatment);
        btnAddTreatment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famVisible = false;
                Intent intent = new Intent(AnimalDetailActivity.this, TreatmentMaintainActivity.class);
                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);
                intent.putExtra(DBAnimalAdapter.ID, animal.getId());
                startActivity(intent);
            }
        });

        final FloatingActionButton btnRegisterSale = (FloatingActionButton) findViewById(R.id.ad_btn_register_sale);
        btnRegisterSale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famVisible = false;
                Intent intent = new Intent(AnimalDetailActivity.this, SaleMaintainActivity.class);
                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);
                intent.putExtra(DBAnimalAdapter.ID, animal.getId());
                startActivity(intent);
            }
        });

        final FloatingActionButton btnRegisterWeighting = (FloatingActionButton) findViewById(R.id.ad_btn_register_weighting);
        btnRegisterWeighting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famVisible = false;
                Intent intent = new Intent(AnimalDetailActivity.this, WeightingMaintainActivity.class);
                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);
                intent.putExtra(DBAnimalAdapter.ID, animal.getId());
                startActivity(intent);
            }
        });

        final FloatingActionButton btnRegisterMilking = (FloatingActionButton) findViewById(R.id.ad_btn_register_milking);
        btnRegisterMilking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famVisible = false;
                Intent intent = new Intent(AnimalDetailActivity.this, MilkingMaintainActivity.class);
                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);
                intent.putExtra(DBAnimalAdapter.ID, animal.getId());
                startActivity(intent);
            }
        });

        final FloatingActionButton btnRegisterDeath = (FloatingActionButton) findViewById(R.id.ad_btn_register_death);
        btnRegisterDeath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famVisible = false;
                Intent intent = new Intent(AnimalDetailActivity.this, DeathMaintainActivity.class);
                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_ADD);
                intent.putExtra(DBAnimalAdapter.ID, animal.getId());
                startActivity(intent);
            }
        });

        ((FloatingActionButton) findViewById(R.id.ad_toggle_fam))
                .setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            famVisible = !famVisible;
                                            updateFAM();
                                        }
                                    }

                );

        famElementsIds = new ArrayList<>();

        famElementsIds.add(R.id.ad_ll_register_treatment);
        famElementsIds.add(R.id.ad_ll_register_milking);
        famElementsIds.add(R.id.ad_ll_register_weighting);
        famElementsIds.add(R.id.ad_ll_register_sale);
        famElementsIds.add(R.id.ad_ll_register_death);
        famElementsIds.add(R.id.ad_ll_overlay);

        updateFAM();

    }

    public void retireAnimal() {
        new AlertDialog.Builder(AnimalDetailActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.retire)
                .setMessage(R.string.retire_register_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        animalDatasource.open();
                        animal.setRetireDate(new Date());
                        animalDatasource.update(animal);
                        animalDatasource.close();
                        updateAnimalData();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public void undoRetireAnimal() {
        new AlertDialog.Builder(AnimalDetailActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.retire)
                .setMessage(R.string.retire_delete_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        animalDatasource.open();
                        animal.setRetireDate(null);
                        animalDatasource.update(animal);
                        animalDatasource.close();
                        updateAnimalData();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void updateFAM() {
        for (int id : famElementsIds) {
            if (famVisible) {
                switch (id) {
                    case R.id.ad_ll_register_sale:
                        if (!animal.isSold()) {
                            findViewById(id).setVisibility(View.VISIBLE);
                        }
                        break;

                    case R.id.ad_ll_register_death:
                        if (!animal.isDead()) {
                            findViewById(id).setVisibility(View.VISIBLE);
                        }
                        break;

                    case R.id.ad_ll_register_milking:
                        if (animal.getSex().equalsIgnoreCase("F")) {
                            findViewById(id).setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        findViewById(id).setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                findViewById(id).setVisibility(View.GONE);
            }
        }
    }

    private void updateAnimalData() {
        //Animal data
        animalDatasource = DBAnimalAdapter.getInstance();
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

        //Category Data
        DBCategoryAdapter categoryDatasource = DBCategoryAdapter.getInstance();
        categoryDatasource.open();
        Category category = categoryDatasource.get(animal.getCategoryId());
        categoryDatasource.close();

        ((TextView) findViewById(R.id.ad_id))
                .setText(animal.getIdToDisplay());

        ((TextView) findViewById(R.id.ad_name))
                .setText(animal.getName());

        ((TextView) findViewById(R.id.ad_name))
                .setText(animal.getName());

        if(TextUtils.isEmpty(animal.getEarTag())) {
            findViewById(R.id.ad_ear_tag_icon).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.ad_ear_tag))
                .setText(animal.getEarTag());

        if (animal.getPictureFile().exists()) {
            ((ImageView) findViewById(R.id.ad_picture)).setImageBitmap(BitmapFactory.decodeFile(animal.getPictureFile().getPath()));
        } else {
            ((ImageView) findViewById(R.id.ad_picture)).setImageResource(R.drawable.cow_hl);
        }

        ((TextView) findViewById(R.id.ad_race))
                .setText(race.getDescription());

        ((TextView) findViewById(R.id.ad_race))
                .setText(category.getDescription());

        ((TextView) findViewById(R.id.ad_sex))
                .setText(animal.getSexToDisplay());

        ((TextView) findViewById(R.id.ad_age))
                .setText(animal.getAgeInMonthsToDisplay());

        TableLayout table = (TableLayout) findViewById(R.id.ad_events_table);
        table.removeAllViews();

        for (final Event e : animal.getEvents()) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.event_row, null);

            ((ImageView) row.findViewById(R.id.er_icon)).setImageResource(e.getType().getResourceIconHL());
            ((TextView) row.findViewById(R.id.er_date)).setText(MainActivity.getFormatedDate(e.getDate()));
            ((TextView) row.findViewById(R.id.er_description)).setText(e.getDescription());
            ((TextView) row.findViewById(R.id.er_plus)).setText(e.getType().isSelectable() ? "+" : "");

            if (e.getType().isSelectable()) {
                row.setOnClickListener(new View.OnClickListener() {
                    private Intent intent;

                    @Override
                    public void onClick(View v) {
                        switch (e.getType()) {
                            case TREATMENT:
                                intent = new Intent(AnimalDetailActivity.this, TreatmentDetailActivity.class);
                                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
                                intent.putExtra(DBTreatmentAdapter.ID, e.getEntityId());
                                startActivity(intent);
                                break;

                            case DEATH:
                                intent = new Intent(AnimalDetailActivity.this, DeathDetailActivity.class);
                                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
                                intent.putExtra(DBAnimalAdapter.ID, e.getEntityId());
                                startActivity(intent);
                                break;

                            case SALE:
                                intent = new Intent(AnimalDetailActivity.this, SaleDetailActivity.class);
                                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
                                intent.putExtra(DBAnimalAdapter.ID, e.getEntityId());
                                startActivity(intent);
                                break;

                            case MILKING:
                                intent = new Intent(AnimalDetailActivity.this, MilkingDetailActivity.class);
                                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
                                intent.putExtra(DBMilkingAdapter.ID, e.getEntityId());
                                startActivity(intent);
                                break;

                            case WEIGHING:
                                intent = new Intent(AnimalDetailActivity.this, WeightingDetailActivity.class);
                                intent.putExtra(MeuRebanhoApp.ACTION, MeuRebanhoApp.ACTION_EDIT);
                                intent.putExtra(DBWeightingAdapter.ID, e.getEntityId());
                                startActivity(intent);
                                break;

                            default:
                                Toast.makeText(AnimalDetailActivity.this, "No action defined for this event type: " + e.getType().toString(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            table.addView(row);
        }
        table.requestLayout();     // Not sure if this is needed.

        //onResume ocorre antes do menu estar inflado, assim evita null pointer exception
        if(mnuRetire != null) {
            updateVisibleMenus();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        updateAnimalData();
        updateFAM();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.animal_detail_actions, menu);

        mnuRetire = menu.findItem(R.id.action_retire);
        mnuUndoRetire = menu.findItem(R.id.action_undo_retire);

        updateVisibleMenus();

        return true;
    }

    private void updateVisibleMenus() {
        if (animal.isRetired()) {
            mnuRetire.setVisible(false);
            mnuUndoRetire.setVisible(true);
        } else {
            mnuRetire.setVisible(true);
            mnuUndoRetire.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                actionEditAnimal();
                break;

            case R.id.action_retire:
                retireAnimal();
                break;

            case R.id.action_undo_retire:
                undoRetireAnimal();
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