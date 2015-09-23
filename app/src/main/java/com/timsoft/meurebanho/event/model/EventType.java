package com.timsoft.meurebanho.event.model;

public enum EventType {
    ACQUISITION(false, "A"), BIRTH(false, "B"), DEATH(false, "D"), TREATMENT(true, "T");

    private boolean selectable;
    private String icon;

    EventType(boolean selectable, String icon) {
        this.selectable = selectable;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isSelectable() {
        return selectable;
    }

}
