package com.timsoft.meurebanho.model;


public class Especie extends IDDescricao {
	public static String[] especiesDefault = {"BOVINO", "CAPRINO", "EQUINO", "OVINO", "SUÍNO"};
	
	public Especie(long id, String descricao) {
		super(id, descricao); 
	}
}
