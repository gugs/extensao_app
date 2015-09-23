package com.timsoft.meurebanho.specie.model;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.infra.model.IDDescription;

import java.util.ArrayList;
import java.util.List;

public class Specie extends IDDescription {
    public Specie(int id, String description) {
        super(id, description);
    }

    public static List<Specie> getDefaultSpecies() {
        List<Specie> e = new ArrayList<Specie>();
        e.add(new Specie(1, MeuRebanhoApp.getContext().getResources().getString(R.string.specie_bovine)));
        e.add(new Specie(2, MeuRebanhoApp.getContext().getResources().getString(R.string.specie_caprine)));
        e.add(new Specie(3, MeuRebanhoApp.getContext().getResources().getString(R.string.specie_equine)));
        e.add(new Specie(4, MeuRebanhoApp.getContext().getResources().getString(R.string.specie_ovine)));
        e.add(new Specie(5, MeuRebanhoApp.getContext().getResources().getString(R.string.specie_swine)));
        return e;
    }
}
