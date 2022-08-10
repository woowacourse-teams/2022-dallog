package com.allog.dallog.infrastructure.log.dto;

public class Field {

    private String name;
    private String value;

    private Field() {
    }

    private Field(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public static Field from(final String steAsString) {
        String name = steAsString.substring(steAsString.indexOf("(") + 1, steAsString.indexOf(")"));
        String value = steAsString.substring(0, steAsString.indexOf("("));
        return new Field(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
