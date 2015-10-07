package com.timsoft.meurebanho.animal.model;

import java.util.Comparator;

public class AnimalIDComparator implements Comparator<Animal> {
    public int compare(Animal a1, Animal a2) {
        return (new Integer(a1.getId()).compareTo(a2.getId()));
    }
}
