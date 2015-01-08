package com.timsoft.meurebanho.model;

import java.util.ArrayList;
import java.util.List;

public class Raca extends IDDescricao {

	private int idEspecie;
	
	public Raca(int id, String descricao, int idEspecie) {
		super(id, descricao);
		this.idEspecie = idEspecie;
	}
	
	public int getIdEspecie() {
		return idEspecie;
	}

	public void setIdEspecie(int idEspecie) {
		this.idEspecie = idEspecie;
	}

	public static List<Raca> getRacasDefault() {
		List<Raca> r = new ArrayList<Raca>();
		
		//Bovinos - http://cnpc.org.br/pecuaria.php
		r.add(new Raca(101, "Aberdeen Angus", 1));
		r.add(new Raca(102, "Belgian Blue", 1));
		r.add(new Raca(103, "Blonde Daquitaine", 1));
		r.add(new Raca(104, "Bonsmara", 1));
		r.add(new Raca(105, "Braford", 1));
		r.add(new Raca(106, "Brahman", 1));
		r.add(new Raca(107, "Brangus", 1));
		r.add(new Raca(108, "Bravon", 1));
		r.add(new Raca(109, "Canchim", 1));
		r.add(new Raca(110, "Caracu", 1));
		r.add(new Raca(111, "Charolês", 1));
		r.add(new Raca(112, "Chianina", 1));
		r.add(new Raca(113, "Devon", 1));
		r.add(new Raca(114, "Gir", 1));
		r.add(new Raca(115, "Guzerá", 1));
		r.add(new Raca(116, "Hereford", 1));
		r.add(new Raca(117, "Indubrasil", 1));
		r.add(new Raca(118, "Limousin", 1));
		r.add(new Raca(119, "Marchigiana", 1));
		r.add(new Raca(120, "Mertolengo", 1));
		r.add(new Raca(121, "Nelore", 1));
		r.add(new Raca(122, "Piemontês", 1));
		r.add(new Raca(123, "Romagnolo", 1));
		r.add(new Raca(124, "Santa Gertrudis", 1));
		r.add(new Raca(125, "Shorthorn", 1));
		r.add(new Raca(126, "Simental", 1));
		r.add(new Raca(127, "Tabapuã", 1));
		r.add(new Raca(128, "Wagyu", 1));
		
		//Caprinos - http://www.caprileite.com.br/racas.php?tipo=Caprinos
		r.add(new Raca(201, "Alpino", 2));
		r.add(new Raca(202, "Anglonubiano", 2));	
		r.add(new Raca(203, "Boer ", 2));
		r.add(new Raca(204, "Saanen ", 2));
		r.add(new Raca(205, "Savana ", 2));
		r.add(new Raca(206, "Toggenburg", 2));
		
		//Equinos - http://www.guiaderacas.com.br/cavalos/racas/
		r.add(new Raca(301, "American Saddlebred", 3));
		r.add(new Raca(302, "Andaluz", 3));
		r.add(new Raca(303, "Anglo-Árabe", 3));
		r.add(new Raca(304, "Árabe", 3));
		r.add(new Raca(305, "Appaloosa", 3));
		r.add(new Raca(306, "Berbere", 3));
		r.add(new Raca(307, "Brasileiro de Hipismo", 3));
		r.add(new Raca(308, "Bretão", 3));
		r.add(new Raca(309, "Campolina", 3));
		r.add(new Raca(310, "Crioulo", 3));
		r.add(new Raca(311, "Danish Warmblood", 3));
		r.add(new Raca(312, "Falabella", 3));
		r.add(new Raca(313, "Finlandês", 3));
		r.add(new Raca(314, "Friesian", 3));
		r.add(new Raca(315, "Mangalarga", 3));
		r.add(new Raca(316, "Mangalarga Marchador", 3));
		r.add(new Raca(317, "Mustang", 3));
		r.add(new Raca(318, "Oldenburg", 3));
		r.add(new Raca(319, "Paint Horse", 3));
		r.add(new Raca(320, "Percheron", 3));
		r.add(new Raca(321, "Pônei", 3));
		r.add(new Raca(322, "Puro Sangue Inglês", 3));
		r.add(new Raca(323, "Puro Sangue Lusitano", 3));
		r.add(new Raca(324, "Quarto de Milha", 3));
		r.add(new Raca(325, "Shire", 3));
		r.add(new Raca(326, "Sela Francesa", 3));
		
		//Ovinos - http://www.caprileite.com.br/racas.php?tipo=Ovinos
		r.add(new Raca(401, "Bergamácia Brasileira", 4));
		r.add(new Raca(402, "Dâmara", 4));
		r.add(new Raca(403, "Dorper", 4));
		r.add(new Raca(404, "Hampshire Down", 4));
		r.add(new Raca(405, "Ile de France", 4));
		r.add(new Raca(406, "Lacaune", 4));
		r.add(new Raca(407, "Morada Nova", 4));
		r.add(new Raca(408, "Santa Inês", 4));
		r.add(new Raca(409, "Somalis Brasileira", 4));
		r.add(new Raca(410, "Suffolk", 4));
		r.add(new Raca(411, "Texel", 4));
		
		//TODO: Suínos - http://tcpermaculture.com/site/2014/01/15/domestic-pigs-breeds-and-terminology/
		//TODO: Asininos - http://www.oocities.org/asininos/raca.html
		
		return r;
	}
}
