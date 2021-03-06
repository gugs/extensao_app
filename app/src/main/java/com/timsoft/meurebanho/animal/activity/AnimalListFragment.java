package com.timsoft.meurebanho.animal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.animal.db.DBAnimalAdapter;
import com.timsoft.meurebanho.animal.model.Animal;
import com.timsoft.meurebanho.animal.model.AnimalArrayAdapter;

public class AnimalListFragment extends Fragment {

    private static final String LOG_TAG = "AnimalListFragment";

    private AnimalListActivity activity;
    private int selectedStatus;
    private int index;

    static AnimalListFragment newInstance(int index) {
        AnimalListFragment f = new AnimalListFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AnimalListActivity) getActivity();
        this.index = getArguments() != null ? getArguments().getInt("index") : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.animal_list_fragment, container, false);

        //Default status radio
        selectedStatus = Animal.STATUS_AVAILABLE;
        RadioButton r = (RadioButton) v.findViewById(R.id.radio_animal_list_available);
        r.setChecked(true);
        //

        //Radio group listener
        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group_animal_list_status);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_animal_list_available:
                        selectedStatus = Animal.STATUS_AVAILABLE;
                        break;

                    case R.id.radio_animal_list_sold:
                        selectedStatus = Animal.STATUS_SOLD;
                        break;

                    case R.id.radio_animal_list_dead:
                        selectedStatus = Animal.STATUS_DEAD;
                        break;

                    case R.id.radio_animal_list_retired:
                        selectedStatus = Animal.STATUS_RETIRED;
                        break;

                    default:
                        throw new RuntimeException("Status not defined for id: " + checkedId);
                }

                updateView();
            }
        });
        //

        //buttons
        final View addButton = v.findViewById(R.id.button_add_animal);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.actionNewAnimal();
            }
        });
        //

        return v;
    }

    public void updateView() {
        ListView lv = (ListView) getView().findViewById(R.id.list_animal_list);
        lv.setAdapter(new AnimalArrayAdapter(activity, activity.getAnimals(activity.getSpecies().get(index).getId(), selectedStatus)));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                detailAnimal(((Animal) parent.getItemAtPosition(position)));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void detailAnimal(Animal a) {
        Intent intent = new Intent(activity, AnimalDetailActivity.class);
        intent.putExtra(DBAnimalAdapter.ID, a.getId());
        startActivity(intent);
    }
}