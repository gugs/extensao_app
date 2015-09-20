package com.timsoft.meurebanho.event.model;

import com.timsoft.meurebanho.infra.model.IDDescription;

import java.util.Date;

public class Event extends IDDescription {
	private int animalId;
	private Date date;
	private int eventTypeId;

	public Event(){
	}

	public Event(int id, int eventTypeId, int animalId,
				 Date date, String description) {
		super(id, description);
		this.eventTypeId = eventTypeId;
		this.animalId = animalId;
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date data) {
		this.date = data;
	}

	public int getAnimalId() {
		return animalId;
	}

	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}

	public int getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
}
