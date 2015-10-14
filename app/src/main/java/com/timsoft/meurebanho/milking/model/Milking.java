package com.timsoft.meurebanho.milking.model;

import java.util.Date;

public class Milking {
    private int id;
    private int animalId;
    private Date date;
    private double amount;

    public Milking() {
    }

    public Milking(int id, int animalId, Date date, double amount) {
        this.id = id;
        this.animalId = animalId;
        this.date = date;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
