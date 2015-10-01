package com.timsoft.meurebanho.treatment.model;

import java.util.Date;

public class Treatment {
    private int id;
    private int animalId;
    private Date date;
    private String reason;
    private String medication;
    private int withdrawalPeriod;
    private double cost;
    private String notes;

    public Treatment() {
    }

    public Treatment(int id, int animalId, Date date, String reason, String medication, int withdrawPeriod,
                     double cost, String notes) {
        this.id = id;
        this.animalId = animalId;
        this.date = date;
        this.reason = reason;
        this.medication = medication;
        this.withdrawalPeriod = withdrawPeriod;
        this.cost = cost;
        this.notes = notes;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public int getWithdrawalPeriod() {
        return withdrawalPeriod;
    }

    public void setWithdrawalPeriod(int withdrawalPeriod) {
        this.withdrawalPeriod = withdrawalPeriod;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
