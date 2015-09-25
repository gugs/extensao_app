package com.timsoft.meurebanho.animal.model;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.event.model.Event;
import com.timsoft.meurebanho.event.model.EventType;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.treatment.model.Treatment;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Animal {
    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_SOLD = 1;
    public static final int STATUS_DEAD = 2;

    private int id;
    private int specieId;
    private int raceId;

    private String sex;
    private String name;
    private String earTag;

    private String sellerName;

    private String buyerName;
    private String sellNotes;

    private Date birthDate;
    private Date acquisitionDate;
    private Date sellDate;

    private Date deathDate;
    private String deathReason;

    public Date getRetireDate() {
        return retireDate;
    }

    public void setRetireDate(Date retireDate) {
        this.retireDate = retireDate;
    }

    private Date retireDate;

    private double acquisitionValue;
    private double sellValue;

    public Animal() {
    }

    public Animal(int id, int specieId, int raceId,
                  String sex, String name, String earTag,
                  Date birthDate, Date acquisitionDate, Date sellDate,
                  Date deathDate, String deathReason, Date retireDate,
                  double acquisitionValue, double sellValue,
                  String sellerName, String buyerName, String sellNotes) {

        this.id = id;
        this.specieId = specieId;
        this.raceId = raceId;
        this.sex = sex;
        this.name = name;
        this.earTag = earTag;

        this.birthDate = birthDate;
        this.acquisitionDate = acquisitionDate;
        this.sellDate = sellDate;

        this.deathDate = deathDate;
        this.deathReason = deathReason;

        this.retireDate = retireDate;

        this.acquisitionValue = acquisitionValue;
        this.sellValue = sellValue;

        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.sellNotes = sellNotes;
    }

    public int getId() {
        return id;
    }

    public String getIdToDisplay() {
        return String.format(Locale.getDefault(), "%04d", getId());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public int getSpecieId() {
        return specieId;
    }

    public void setSpecieId(int specieId) {
        this.specieId = specieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEarTag() {
        return earTag;
    }

    public void setEarTag(String earTag) {
        this.earTag = earTag;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public double getAcquisitionValue() {
        return acquisitionValue;
    }

    public void setAcquisitionValue(double acquisitionValue) {
        this.acquisitionValue = acquisitionValue;
    }

    public double getSellValue() {
        return sellValue;
    }

    public void setSellValue(double sellValue) {
        this.sellValue = sellValue;
    }

    public String getSex() {
        return sex;
    }

    public String getSexToDisplay() {
        return "F".equalsIgnoreCase(getSex()) ? MeuRebanhoApp.getContext().getResources().getString(R.string.female) : MeuRebanhoApp.getContext().getResources().getString(R.string.male);
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public boolean isAvailable() {
        return !isSold() && !isDead() && !isRetired();
    }

    public boolean isSold() {
        return getSellDate() != null;
    }

    public boolean isDead() {
        return getDeathDate() != null;
    }

    public boolean isRetired() {
        return getRetireDate() != null;
    }

    public String getSellNotes() {
        return sellNotes;
    }

    public void setSellNotes(String sellNotes) {
        this.sellNotes = sellNotes;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getAgeInMonths() {
        if (getBirthDate() == null) {
            return 0;
        } else {
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(getBirthDate());
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(new Date());

            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        }
    }

    public String getAgeInMonthsToDisplay() {
        return getAgeInMonths() + " " + MeuRebanhoApp.getContext().getResources().getString(R.string.months);
    }

    public File getPictureFile() {
        return new File(MeuRebanhoApp.getMediaStorageDir().getPath() + File.separator + getIdToDisplay() + MeuRebanhoApp.DEFAULT_IMAGE_FILE_EXTENSION);
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        if (getBirthDate() != null) {
            events.add(new Event(0, EventType.BIRTH, getBirthDate(), MeuRebanhoApp.getContext().getResources().getString(R.string.birth)));
        }

        if (getAcquisitionDate() != null) {
            events.add(new Event(0, EventType.ACQUISITION, getAcquisitionDate(), NumberFormat.getCurrencyInstance().format(getAcquisitionValue())));
        }

        if (getDeathDate() != null) {
            events.add(new Event(0, EventType.DEATH, getDeathDate(), getDeathReason()));
        }

        DBTreatmentAdapter treatmentDatasource = DBTreatmentAdapter.getInstance();
        treatmentDatasource.open();
        for (Treatment t : treatmentDatasource.list(getId())) {
            events.add(new Event(t.getId(), EventType.TREATMENT, t.getDate(), t.getMedication()));
        }
        treatmentDatasource.close();

        Collections.sort(events, Collections.reverseOrder());

        return events;
    }
}