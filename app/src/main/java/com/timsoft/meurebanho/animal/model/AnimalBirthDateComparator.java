package com.timsoft.meurebanho.animal.model;

import java.util.Comparator;

public class AnimalBirthDateComparator implements Comparator<Animal> {
    public int compare(Animal a1, Animal a2) {
        return (a1.getBirthDate().compareTo(a2.getBirthDate()));
    }
}
