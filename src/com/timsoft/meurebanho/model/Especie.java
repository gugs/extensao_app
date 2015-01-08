package com.timsoft.meurebanho.model;

import java.util.ArrayList;
import java.util.List;


public class Especie extends IDDescricao {
	public Especie(int id, String descricao) {
		super(id, descricao); 
	}
	
	public static List<Especie> getEspeciesDefault() {
		List<Especie> r = new ArrayList<Especie>();
		r.add(new Especie(1, "Bovino"));
		r.add(new Especie(2, "Caprino"));
		r.add(new Especie(3, "Equino"));
		r.add(new Especie(4, "Ovino"));
		r.add(new Especie(5, "Suíno"));
		return r;
	}
}
