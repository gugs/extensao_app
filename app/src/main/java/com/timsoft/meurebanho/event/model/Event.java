package com.timsoft.meurebanho.event.model;

import java.util.Date;

public class Event implements Comparable<Event> {
    private int entityId;
    private EventType type;
    private Date date;
    private String description;

    public Event() {
    }

    public Event(int entityId, EventType type, Date date, String description) {
        this.entityId = entityId;
        this.type = type;
        this.date = date;
        this.description = description;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Event another) {
        //order by date and after by type
        if (getDate().compareTo(another.getDate()) != 0) {
            return getDate().compareTo(another.getDate());
        } else {
            return type.compareTo(another.getType());
        }
    }
}
