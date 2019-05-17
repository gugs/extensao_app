package com.timsoft.meurebanho.observations.model;

import java.util.Date;

public class Observation
{
    private int idObservation;
    private int animalID;
    private Date dateOccurrence;
    private String obsDescription;


    public Observation()
    {

    }

    public Observation (int idObservation, int animalID, Date dateOccurrence, String obsDescription)
    {
        this.idObservation = idObservation;
        this.animalID = animalID;
        this.dateOccurrence = dateOccurrence;
        this.obsDescription = obsDescription;
    }

    public int getIdObservation() {
        return idObservation;
    }

    public void setIdObservation(int idObservation) {
        this.idObservation = idObservation;
    }

    public Date getDateOccurrence() {
        return dateOccurrence;
    }

    public void setDateOccurrence(Date dateOccurrence) {
        this.dateOccurrence = dateOccurrence;
    }

    public String getObsDescription() {
        return obsDescription;
    }

    public void setObsDescription(String obsDescription) {
        this.obsDescription = obsDescription;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }
}
