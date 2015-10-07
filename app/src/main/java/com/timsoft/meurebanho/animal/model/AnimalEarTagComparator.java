package com.timsoft.meurebanho.animal.model;

import java.util.Comparator;

public class AnimalEarTagComparator implements Comparator<Animal> {
    public int compare(Animal a1, Animal a2) {
        return (a1.getEarTag().compareTo(a2.getEarTag()));
    }
}
