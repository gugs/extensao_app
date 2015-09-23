package com.timsoft.meurebanho.infra.model;

public class IDDescription {
    private int id;
    private String description;

    public IDDescription() {
    }

    public IDDescription(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descricao) {
        this.description = descricao;
    }

    @Override
    public String toString() {
        return "IDDescription [id=" + id + ", description=" + description + "]";
    }
}
