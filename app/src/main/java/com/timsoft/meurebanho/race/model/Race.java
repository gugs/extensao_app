package com.timsoft.meurebanho.race.model;

import com.timsoft.meurebanho.infra.model.IDDescription;

import java.util.ArrayList;
import java.util.List;

public class Race extends IDDescription {

    private int idSpecie;

    public Race(int id, String descricao, int idSpecie) {
        super(id, descricao);
        this.idSpecie = idSpecie;
    }

    public int getIdSpecie() {
        return idSpecie;
    }

    public void setIdSpecie(int idSpecie) {
        this.idSpecie = idSpecie;
    }

    public static List<Race> getDefaultRaces() {
        List<Race> r = new ArrayList<Race>();

//        //Bovinos - http://cnpc.org.br/pecuaria.php
//        r.add(new Race(101, "Aberdeen Angus", 1));
//        r.add(new Race(102, "Belgian Blue", 1));
//        r.add(new Race(103, "Blonde Daquitaine", 1));
//        r.add(new Race(104, "Bonsmara", 1));
//        r.add(new Race(105, "Braford", 1));
//        r.add(new Race(106, "Brahman", 1));
//        r.add(new Race(107, "Brangus", 1));
//        r.add(new Race(108, "Bravon", 1));
//        r.add(new Race(109, "Canchim", 1));
//        r.add(new Race(110, "Caracu", 1));
//        r.add(new Race(111, "Charolês", 1));
//        r.add(new Race(112, "Chianina", 1));
//        r.add(new Race(113, "Devon", 1));
//        r.add(new Race(114, "Gir", 1));
//        r.add(new Race(115, "Guzerá", 1));
//        r.add(new Race(116, "Hereford", 1));
//        r.add(new Race(117, "Indubrasil", 1));
//        r.add(new Race(118, "Limousin", 1));
//        r.add(new Race(119, "Marchigiana", 1));
//        r.add(new Race(120, "Mertolengo", 1));
//        r.add(new Race(121, "Nelore", 1));
//        r.add(new Race(122, "Piemontês", 1));
//        r.add(new Race(123, "Romagnolo", 1));
//        r.add(new Race(124, "Santa Gertrudis", 1));
//        r.add(new Race(125, "Shorthorn", 1));
//        r.add(new Race(126, "Simental", 1));
//        r.add(new Race(127, "Tabapuã", 1));
//        r.add(new Race(128, "Wagyu", 1));
//        r.add(new Race(129, "Comum", 1));

        //Caprinos - http://www.caprileite.com.br/racas.php?tipo=Caprinos
        r.add(new Race(101, "Alpino", 1));
        r.add(new Race(102, "Anglonubiano", 1));
        r.add(new Race(103, "Boer ", 1));
        r.add(new Race(104, "Saanen ", 1));
        r.add(new Race(105, "Savana ", 1));
        r.add(new Race(106, "Toggenburg", 1));
        r.add(new Race(107, "Mestiço", 1));

//        //Equinos - http://www.guiaderacas.com.br/cavalos/racas/
//        r.add(new Race(301, "American Saddlebred", 3));
//        r.add(new Race(302, "Andaluz", 3));
//        r.add(new Race(303, "Anglo-Árabe", 3));
//        r.add(new Race(304, "Árabe", 3));
//        r.add(new Race(305, "Appaloosa", 3));
//        r.add(new Race(306, "Berbere", 3));
//        r.add(new Race(307, "Brasileiro de Hipismo", 3));
//        r.add(new Race(308, "Bretão", 3));
//        r.add(new Race(309, "Campolina", 3));
//        r.add(new Race(310, "Crioulo", 3));
//        r.add(new Race(311, "Danish Warmblood", 3));
//        r.add(new Race(312, "Falabella", 3));
//        r.add(new Race(313, "Finlandês", 3));
//        r.add(new Race(314, "Friesian", 3));
//        r.add(new Race(315, "Mangalarga", 3));
//        r.add(new Race(316, "Mangalarga Marchador", 3));
//        r.add(new Race(317, "Mustang", 3));
//        r.add(new Race(318, "Oldenburg", 3));
//        r.add(new Race(319, "Paint Horse", 3));
//        r.add(new Race(320, "Percheron", 3));
//        r.add(new Race(321, "Pônei", 3));
//        r.add(new Race(322, "Puro Sangue Inglês", 3));
//        r.add(new Race(323, "Puro Sangue Lusitano", 3));
//        r.add(new Race(324, "Quarto de Milha", 3));
//        r.add(new Race(325, "Shire", 3));
//        r.add(new Race(326, "Sela Francesa", 3));

        //Ovinos - http://www.caprileite.com.br/racas.php?tipo=Ovinos
        r.add(new Race(201, "Bergamácia Brasileira", 2));
        r.add(new Race(202, "Dâmara", 2));
        r.add(new Race(203, "Dorper", 2));
        r.add(new Race(204, "Hampshire Down", 2));
        r.add(new Race(205, "Ile de France", 2));
        r.add(new Race(206, "Lacaune", 2));
        r.add(new Race(207, "Morada Nova", 2));
        r.add(new Race(208, "Santa Inês", 2));
        r.add(new Race(209, "Somalis Brasileira", 2));
        r.add(new Race(210, "Suffolk", 2));
        r.add(new Race(211, "Texel", 2));
        r.add(new Race(212, "Mestiço", 2));

        //TODO: Suínos - http://tcpermaculture.com/site/2014/01/15/domestic-pigs-breeds-and-terminology/
        //TODO: Asininos - http://www.oocities.org/asininos/raca.html

        return r;
    }
}
