package com.allog.dallog.infrastructure.log.dto;

import java.util.List;

public class Embed {

    private String title;
    private String description;
    private String color;
    private List<Field> fields;

    private Embed() {
    }

    public Embed(final String title, final String color) {
        this(title, null, color, null);
    }

    public Embed(final String title, final String description, final String color, final List<Field> fields) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public List<Field> getFields() {
        return fields;
    }
}
