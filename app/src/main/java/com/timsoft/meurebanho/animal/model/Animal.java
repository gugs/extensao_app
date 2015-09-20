package com.timsoft.meurebanho.animal.model;

import com.timsoft.meurebanho.MeuRebanhoApp;
import com.timsoft.meurebanho.R;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	
	private Date birthDate;
	private Date acquisitionDate;
	private Date sellDate;
	
	private Date deathDate;
	private String deathReason;
	
	private double acquisitionValue;
	private double sellValue;
	
	public Animal() {
	}
	
	public Animal(	int id, int specieId, int raceId, 
					String sex, String name, String earTag, 
					Date birthDate, Date acquisitionDate, Date sellDate,
					Date deathDate, String deathReason,
					double acquisitionValue, double sellValue) {
		
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
		
		this.acquisitionValue = acquisitionValue;
		this.sellValue = sellValue;
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
		return "F".equalsIgnoreCase(getSex()) ?  MeuRebanhoApp.getContext().getResources().getString(R.string.female) : MeuRebanhoApp.getContext().getResources().getString(R.string.male);
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

	@Override
	public String toString() {
		return "Animal [id=" + id + ", specieId=" + specieId + ", raceId="
				+ raceId + ", sex=" + sex + ", name=" + name + ", earTag="
				+ earTag + ", birthDate=" + birthDate + ", acquisitionDate="
				+ acquisitionDate + ", sellDate=" + sellDate + ", deathDate="
				+ deathDate + ", deathReason=" + deathReason
				+ ", acquisitionValue=" + acquisitionValue + ", sellValue="
				+ sellValue + "]";
	}
	
	public boolean isAvailable() {
		return !isSold() && !isDead();
	}
	
	public boolean isSold() {
		return getSellDate() != null;
	}
	
	public boolean isDead() {
		return getDeathDate() != null;
	}

	public int getAgeInMonths() {
		if(getBirthDate() == null) {
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
}
