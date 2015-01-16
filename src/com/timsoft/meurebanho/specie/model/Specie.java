package com.timsoft.meurebanho.specie.model;

import java.util.ArrayList;
import java.util.List;

import com.timsoft.meurebanho.infra.model.IDDescription;

public class Specie extends IDDescription {
	public Specie(int id, String descricao) {
		super(id, descricao); 
	}
	
	public static List<Specie> getDefaultSpecies() {
		List<Specie> r = new ArrayList<Specie>();
		r.add(new Specie(1, "Bovino"));
		r.add(new Specie(2, "Caprino"));
		r.add(new Specie(3, "Equino"));
		r.add(new Specie(4, "Ovino"));
		r.add(new Specie(5, "Suíno"));
		return r;
	}
}
