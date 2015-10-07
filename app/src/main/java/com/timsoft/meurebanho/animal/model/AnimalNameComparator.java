package com.timsoft.meurebanho.animal.model;

import java.util.Comparator;

public class AnimalNameComparator implements Comparator<Animal> {
    public int compare(Animal a1, Animal a2) {
        return (a1.getName().compareTo(a2.getName()));
    }
}
