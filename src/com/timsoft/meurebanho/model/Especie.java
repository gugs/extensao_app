package com.timsoft.meurebanho.model;


public class Especie extends IDDescricao {
//	public static List<Especie> listaDefault;
	public static String[] especiesDefault = {"BOVINO", "EQUINO", "CAPRINO", "OVINO", "SUÃ�NO"};
	
	public Especie(long id, String descricao) {
		super(id, descricao); 
	}
	
//	public static List<Especie> getListaDefault() {
//		if(listaDefault == null) {
//			listaDefault = new ArrayList<Especie>();
//			for(String e : especiesDefault) {
//				listaDefault.add(new Especie(e.hashCode(), e));
//			}
//		}
//		return listaDefault;
//	}
	
}
