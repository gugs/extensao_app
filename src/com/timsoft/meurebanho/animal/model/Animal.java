package com.timsoft.meurebanho.animal.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.graphics.Bitmap;

import com.timsoft.meurebanho.event.model.Evento;

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
	private Date aquisitionDate;
	private Date sellDate;
	
	private Date deathDate;
	private String deathReason;
	
	private double aquisitionValue;
	private double sellValue;
	
	private int fatherId;
	private int motherId;
	
	private List<Bitmap> pictures;
	
	private List<Evento> events;
	
	public Animal() {
	}
	
	public Animal(	int id, int specieId, int raceId, 
					String sex, String name, String earTag, 
					Date birthDate, Date aquisitionDate, Date sellDate, 
					Date deathDate, String deathReason,
					double aquisitionValue, double sellValue, 
					int fatherId, int motherId) {
		
		this.id = id;
		this.specieId = specieId;
		this.raceId = raceId;
		this.sex = sex;
		this.name = name;
		this.earTag = earTag;
		
		this.birthDate = birthDate;
		this.aquisitionDate = aquisitionDate;
		this.sellDate = sellDate;
		
		this.deathDate = deathDate;
		this.deathReason = deathReason;
		
		this.aquisitionValue = aquisitionValue;
		this.sellValue = sellValue;
		
		this.fatherId = fatherId;
		this.motherId = motherId;
	}

	public int getId() {
		return id;
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

	public Date getAquisitionDate() {
		return aquisitionDate;
	}

	public void setAquisitionDate(Date aquisitionDate) {
		this.aquisitionDate = aquisitionDate;
	}

	public Date getSellDate() {
		return sellDate;
	}

	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

	public double getAquisitionValue() {
		return aquisitionValue;
	}

	public void setAquisitionValue(double aquisitionValue) {
		this.aquisitionValue = aquisitionValue;
	}

	public double getSellValue() {
		return sellValue;
	}

	public void setSellValue(double sellValue) {
		this.sellValue = sellValue;
	}

	public List<Bitmap> getPictures() {
		return pictures;
	}

	public void setPictures(List<Bitmap> pictures) {
		this.pictures = pictures;
	}

	public List<Evento> getEvents() {
		return events;
	}

	public void setEvents(List<Evento> events) {
		this.events = events;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getFatherId() {
		return fatherId;
	}

	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}

	public int getMotherId() {
		return motherId;
	}

	public void setMotherId(int motherId) {
		this.motherId = motherId;
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
				+ earTag + ", birthDate=" + birthDate + ", aquisitionDate="
				+ aquisitionDate + ", sellDate=" + sellDate + ", deathDate="
				+ deathDate + ", deathReason=" + deathReason
				+ ", aquisitionValue=" + aquisitionValue + ", sellValue="
				+ sellValue + ", fatherId=" + fatherId + ", motherId="
				+ motherId + ", pictures=" + pictures + ", events=" + events
				+ "]";
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
			int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
			
			return diffMonth;
		}
		
	}
}
