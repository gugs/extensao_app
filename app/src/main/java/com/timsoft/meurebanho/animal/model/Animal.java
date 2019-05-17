package com.timsoft.meurebanho.animal.model;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;
import com.timsoft.meurebanho.event.model.EnumEventType;
import com.timsoft.meurebanho.event.model.Event;
import com.timsoft.meurebanho.infra.FileUtils;
import com.timsoft.meurebanho.milking.db.DBMilkingAdapter;
import com.timsoft.meurebanho.milking.model.Milking;
import com.timsoft.meurebanho.observations.db.DBObservationAdapter;
import com.timsoft.meurebanho.observations.model.Observation;
import com.timsoft.meurebanho.treatment.db.DBTreatmentAdapter;
import com.timsoft.meurebanho.treatment.model.Treatment;
import com.timsoft.meurebanho.weighting.db.DBWeightingAdapter;
import com.timsoft.meurebanho.weighting.model.Weighting;

import java.io.File;
import java.text.DecimalFormat;
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
    public static final int STATUS_RETIRED = 3;

    private int id;
    private int categoryId;
    private int specieId;
    private int raceId;

    private int father;
    private int mother;

    private String sex;
    private String name;
    private String earTag;
    private String patrimonyNumber;

    private String sellerName;

    private String buyerName;
    private String saleNotes;

    private Date birthDate;
    private Date acquisitionDate;
    private Date saleDate;

    private Date deathDate;
    private String deathReason;

    private Date retireDate;

    private double acquisitionValue;
    private double saleValue;

    public Animal() {
    }

    public Animal(int id, int categoryId, int specieId, int raceId, int father, int mother,
                  String sex, String name, String earTag, String patrimonyNumber,
                  Date birthDate, Date acquisitionDate, Date saleDate,
                  Date deathDate, String deathReason, Date retireDate,
                  double acquisitionValue, double saleValue,
                  String sellerName, String buyerName, String saleNotes) {

        this.id = id;
        this.categoryId = categoryId;
        this.specieId = specieId;
        this.raceId = raceId;
        this.father = father;
        this.mother = mother;
        this.sex = sex;
        this.name = name;
        this.earTag = earTag;
        this.patrimonyNumber = patrimonyNumber;
        this.birthDate = birthDate;
        this.acquisitionDate = acquisitionDate;
        this.saleDate = saleDate;

        this.deathDate = deathDate;
        this.deathReason = deathReason;

        this.retireDate = retireDate;

        this.acquisitionValue = acquisitionValue;
        this.saleValue = saleValue;

        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.saleNotes = saleNotes;
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

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getPatrimonyNumber() { return patrimonyNumber; }

    public void setPatrimonyNumber(String patrimonyNumber) {this.patrimonyNumber = patrimonyNumber;}

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public int getFather() {
        return father;
    }

    public void setFather(int father) {
        this.father = father;
    }

    public int getMother() {
        return mother;
    }

    public void setMother(int mother) {
        this.mother = mother;
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

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public double getAcquisitionValue() {
        return acquisitionValue;
    }

    public void setAcquisitionValue(double acquisitionValue) {
        this.acquisitionValue = acquisitionValue;
    }

    public double getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(double saleValue) {
        this.saleValue = saleValue;
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

    public boolean isAvailable(Date date) {
        if(isRetired()) {
            return false;
        }

        Date startDate = getAcquisitionDate() == null ? getBirthDate() : getAcquisitionDate();
        Date endDate = null;

        if(getDeathDate() != null) {
            endDate = getDeathDate();
        }

        if(getSaleDate() != null && (getDeathDate() == null || getSaleDate().getTime() < getDeathDate().getTime())) {
            endDate = getSaleDate();
        }

        if(date.getTime() >= startDate.getTime()) {
            if(endDate == null) {
                return true;
            } else {
                if(date.getTime() <= endDate.getTime()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public boolean isAvailable() {
        return !isSold() && !isDead() && !isRetired();
    }

    public boolean isSold() {
        return getSaleDate() != null && !isRetired();
    }

    public boolean isDead() {
        return getDeathDate() != null && !isRetired();
    }

    public boolean isRetired() {
        return getRetireDate() != null;
    }

    public String getSaleNotes() {
        return saleNotes;
    }

    public void setSaleNotes(String saleNotes) {
        this.saleNotes = saleNotes;
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

    public Date getRetireDate() {
        return retireDate;
    }

    public void setRetireDate(Date retireDate) {
        this.retireDate = retireDate;
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
        return new File(FileUtils.getMediaStorageDir().getPath() + File.separator + getIdToDisplay() + MeuRebanhoApp.DEFAULT_IMAGE_FILE_EXTENSION);
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        if (getBirthDate() != null) {
            events.add(new Event(0, EnumEventType.BIRTH, getBirthDate(), MeuRebanhoApp.getContext().getResources().getString(R.string.birth)));
        }

        if (getAcquisitionDate() != null) {
            events.add(new Event(0, EnumEventType.ACQUISITION, getAcquisitionDate(), NumberFormat.getCurrencyInstance().format(getAcquisitionValue())));
        }

        if (getSaleDate() != null) {
            events.add(new Event(getId(), EnumEventType.SALE, getSaleDate(), NumberFormat.getCurrencyInstance().format(getSaleValue())));
        }

        if (getDeathDate() != null) {
            events.add(new Event(getId(), EnumEventType.DEATH, getDeathDate(), getDeathReason()));
        }

        if (getRetireDate() != null) {
            events.add(new Event(0, EnumEventType.RETIRE, getRetireDate(),  MeuRebanhoApp.getContext().getString(R.string.retire)));
        }

        DBTreatmentAdapter treatmentDatasource = DBTreatmentAdapter.getInstance();
        treatmentDatasource.open();
        for (Treatment t : treatmentDatasource.list(getId())) {
            events.add(new Event(t.getId(), EnumEventType.TREATMENT, t.getDate(),
                    t.getMedication()));
        }
        treatmentDatasource.close();

        DBWeightingAdapter weightingDatasource = DBWeightingAdapter.getInstance();
        weightingDatasource.open();
        for (Weighting w : weightingDatasource.list(getId())) {
            events.add(new Event(w.getId(), EnumEventType.WEIGHING, w.getDate(),
                    (new DecimalFormat("#,###.00 Kg")).format(w.getWeight())));
        }
        weightingDatasource.close();

        DBMilkingAdapter milkingDatasource = DBMilkingAdapter.getInstance();
        milkingDatasource.open();
        for (Milking m : milkingDatasource.list(getId())) {
            events.add(new Event(m.getId(), EnumEventType.MILKING, m.getDate(),
                    (new DecimalFormat("#,###.00 Kg")).format(m.getWeight())));
        }
        milkingDatasource.close();

        DBObservationAdapter observationDbObservationAdapter = DBObservationAdapter.getInstance();
        observationDbObservationAdapter.open();
        for (Observation m : observationDbObservationAdapter.list(getId())) {
            events.add(new Event(m.getIdObservation(), EnumEventType.OBSERVATION,
                    m.getDateOccurrence(), m.getObsDescription()));
        }
        milkingDatasource.close();

        Collections.sort(events, Collections.reverseOrder());

        return events;
    }
}