package com.timsoft.meurebanho.specie.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.infra.model.IDDescription;

public class Specie extends IDDescription {
	public Specie(int id, String description) {
		super(id, description); 
	}
	
	public static List<Specie> getDefaultSpecies(Context context) {
		List<Specie> e = new ArrayList<Specie>();
		e.add(new Specie(1, context.getResources().getString(R.string.specie_bovine)));
		e.add(new Specie(2, context.getResources().getString(R.string.specie_caprine)));
		e.add(new Specie(3, context.getResources().getString(R.string.specie_equine)));
		e.add(new Specie(4, context.getResources().getString(R.string.specie_ovine)));
		e.add(new Specie(5, context.getResources().getString(R.string.specie_swine)));
		return e;
	}
}
